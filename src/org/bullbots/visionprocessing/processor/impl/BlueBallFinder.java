package org.bullbots.visionprocessing.processor.impl;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class BlueBallFinder extends ColorBallFinder {

	@Override
	public Mat filterColor(Mat img) {
		
		Mat out = new Mat();
		
		// Converts the color from BGR to HSV
		Imgproc.cvtColor(img, out, Imgproc.COLOR_BGR2HSV_FULL); // Will
																		// this
																		// work?
		
		// Apply a blur filter
		Imgproc.GaussianBlur(out, out, new Size(7, 7), 1.2, 1.2);


		// Core.inRange(image2, new Scalar(114, 114, 114), new Scalar(142, 255,
		// 255), image3); // BEFORE
		// Hue, Saturation (Black to red), Value (Brightness)
		Core.inRange(out, new Scalar(148, 120, 40),
				new Scalar(193, 255, 255), out);
		saveImage(out, "ROT-");
		
		return out;
	}

}
