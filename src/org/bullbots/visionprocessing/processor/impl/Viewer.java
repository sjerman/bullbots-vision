package org.bullbots.visionprocessing.processor.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Viewer extends JFrame {

	private JLabel imageContainer;
	private ImageIcon imageIcon;

	public Viewer() throws HeadlessException {
		super();
	    setLayout(new BorderLayout());
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		imageContainer = new JLabel();
		imageIcon= new ImageIcon();
		imageContainer.setIcon(imageIcon);
		imageContainer.setBounds(0, 0, 320, 240);
		this.setPreferredSize(new Dimension(320, 240));
		this.getContentPane().add(imageContainer);
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public void setImage(Mat image) {
		imageIcon.setImage(convMat2Buff(image));
		imageContainer.repaint();
	}

	public BufferedImage convMat2Buff(Mat mat) {
		// Code for converting Mat to BufferedImage
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if (mat.channels() > 1) {
			Mat mat2 = new Mat();
			Imgproc.cvtColor(mat, mat2, Imgproc.COLOR_BGR2RGB);
			type = BufferedImage.TYPE_3BYTE_BGR;
			mat = mat2;
		}
		byte[] b = new byte[mat.channels() * mat.cols() * mat.rows()];
		mat.get(0, 0, b); // Get all the pixels
		BufferedImage bImage = new BufferedImage(mat.cols(), mat.rows(), type);
		bImage.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), b);
		return bImage;
	}

}
