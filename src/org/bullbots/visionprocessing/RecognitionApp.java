package org.bullbots.visionprocessing;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bullbots.visionprocessing.camera.impl.AxisCamera;
import org.bullbots.visionprocessing.processor.AveragingQueue;
import org.bullbots.visionprocessing.processor.impl.RedBallFinder;
import org.bullbots.visionprocessing.processor.impl.ImgInfoImpl;
import org.opencv.core.Mat;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class RecognitionApp extends JFrame {

	private static final long serialVersionUID = 1L;

	private final AxisCamera camera = new AxisCamera();

	private final RedBallFinder processor = new RedBallFinder();

	private NetworkTable table;
	
	private ConnectionListener conListener= new ConnectionListener();

	private JLabel leftImage = new JLabel();
	private JLabel rightImage = new JLabel();

	public RecognitionApp() {
		init();
		initTable();
		beginProcessing();
	}

	public void init() {
		// Mat image = camera.getImage();
		int spacing = 0;
		int width = 320, height = 240;

		setSize(width * 2 + spacing, height);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);
		setVisible(true);

		leftImage.setVisible(true);
		leftImage.setBounds(0, 0, width, height);
		rightImage.setVisible(true);
		rightImage.setBounds(width + spacing, 0, width, height);
		add(leftImage);
		add(rightImage);

		System.out.println(">> GUI setup has successfully finished.");
	}

	public void initTable() {
		NetworkTable.setClientMode();
		NetworkTable.setTeam(1891);
//		NetworkTable.setIPAddress("10.18.91.2");
		table = NetworkTable.getTable("balltable");
		table.addConnectionListener(conListener, true);
		System.out.println(">> NetworkTable setup has successfully finished.");
	}

	private void beginProcessing() {

		AveragingQueue q = new AveragingQueue(4);

		boolean isFound = false;
		double xDistance = 0.0;
		double diameter = 0.0;
		long last=0;

		System.out.println("\n>> Processing has now begun:");
		while (true) {
			BufferedImage cameraImage = camera.getBIImage();
			Mat[] matImages = processor.processImage(processor
					.convBuff2Mat(cameraImage));

			// Converting so we can display on screen
			BufferedImage firstImage = processor.convMat2Buff(matImages[0]);
			BufferedImage afterImage = processor.convMat2Buff(matImages[2]);
			leftImage.setIcon(new ImageIcon(firstImage));
			rightImage.setIcon(new ImageIcon(afterImage));

			this.repaint();
			leftImage.repaint();
			rightImage.repaint();

			if (processor.isBallFound()) {
				q.add(new ImgInfoImpl(processor.getXDistance(), processor
						.getDiameter()));
				if (q.isFull()) {
					if (xDistance != q.getOffset() || diameter != q.getSize()) {
						table.putNumber("xdistance", q.getOffset());
						table.putNumber("diameter", q.getSize());
						isFound=true; 
						table.putBoolean("ballfound", isFound);
						long n = System.nanoTime();
//						System.out.println("New image: "+ (n-last)/1000+ "ms  :"+xDistance + "  "
//								+ diameter  );
						last = n;
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}

			} else {
				if (isFound) {
					isFound = false;
					table.putBoolean("ballfound", isFound);
					q.clear();
				}
			}
		}
	}
}
