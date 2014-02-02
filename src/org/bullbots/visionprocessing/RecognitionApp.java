package org.bullbots.visionprocessing;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.bullbots.visionprocessing.camera.AxisCamera;
import org.opencv.core.Mat;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class RecognitionApp extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private final AxisCamera camera = new AxisCamera("http://10.18.91.11/mjpg/video.mjpg");
	//private final Camera camera = new Camera("http://10.18.91.20/mjpg/video.mjpg");
	private final ImageProcessor processor = new ImageProcessor();
	
	private NetworkTable table;
	
	private JLabel leftImage = new JLabel();
	private JLabel rightImage = new JLabel();
	
	public RecognitionApp() {
		init();
		initTable();
		beginProcessing();
	}
	
	public void init() {
		//Mat image = camera.getImage();
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
		NetworkTable.setIPAddress("10.18.91.2");
		table = NetworkTable.getTable("balltable");
		System.out.println(">> NetworkTable setup has successfully finished.");
	}
	
	private void beginProcessing() {
		boolean isFound = false;
		double xDistance = 0.0;
		double diameter = 0.0;

		int n=0;
			
		System.out.println("\n>> Processing has now begun:");
		while(true) {
			BufferedImage cameraImage = camera.getImage();
			Mat[] matImages = processor.processImage(processor.convBuff2Mat(cameraImage));
			
			// Converting so we can display on screen
			BufferedImage firstImage = processor.convMat2Buff(matImages[0]);
			BufferedImage afterImage = processor.convMat2Buff(matImages[2]);
			leftImage.setIcon(new ImageIcon(firstImage));
			rightImage.setIcon(new ImageIcon(afterImage));
			
			this.repaint();
			leftImage.repaint();
			rightImage.repaint();
			
			if (isFound != processor.isBallFound() || xDistance != processor.getXDistance() ||
					diameter != processor.getDiameter()){
				isFound = processor.isBallFound();
				xDistance = processor.getXDistance();
				diameter = processor.getDiameter();
				table.putNumber("xdistance", xDistance);
				table.putNumber("diameter", diameter);
				table.putBoolean("ballfound", isFound);
				System.out.println("Bllep  "+xDistance+"  "+diameter+"   "+n++);
			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
