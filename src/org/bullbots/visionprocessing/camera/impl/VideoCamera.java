package org.bullbots.visionprocessing.camera.impl;

import org.bullbots.visionprocessing.Settings;
import org.bullbots.visionprocessing.camera.Camera;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class VideoCamera implements Camera {

	public static VideoCapture VC=null;
	
	private Mat image;

	public VideoCamera() {
	}

	public void setAddress(String addr) {
		int address = Integer.parseInt(addr);
		VC = new VideoCapture(address);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Runtime.getRuntime().addShutdownHook(new Shutdownhook());
	}

	public Mat getImage() {
		Mat image = new Mat(), image2 = new Mat();

		VC.read(image);

		Imgproc.resize(image, image, new Size(320, 240));

		return image;
	}
	
	private static class Shutdownhook extends Thread {
		public void run() {
			System.out.println("Shutting down");
			VC.release();
			
		}
	}

}
