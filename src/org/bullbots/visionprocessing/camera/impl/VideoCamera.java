package org.bullbots.visionprocessing.camera.impl;

import org.bullbots.visionprocessing.camera.Camera;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class VideoCamera implements Camera {

	private final VideoCapture VC;
	private Mat image;

	public VideoCamera() {
		VC = new VideoCapture(0);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public Mat getImage() {
		Mat image = new Mat(), image2 = new Mat();

		VC.read(image);

		Imgproc.resize(image, image, new Size(320, 240));

		Highgui.imwrite("IMAGE.JPG", image);
		return image;
	}

}
