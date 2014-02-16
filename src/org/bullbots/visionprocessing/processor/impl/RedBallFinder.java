package org.bullbots.visionprocessing.processor.impl;

import java.util.ArrayList;

import org.bullbots.visionprocessing.Settings;
import org.bullbots.visionprocessing.processor.BallFinder;
import org.bullbots.visionprocessing.processor.ImgInfo;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class RedBallFinder implements BallFinder {

	static long imageNo = 0;

	private void saveImage(Mat image, String name) {

		if (Settings.saveImages() && (imageNo++ % 15) == 0)
			Highgui.imwrite("images/" + name + imageNo + ".png", image);
	}

	public ImgInfo processImage(Mat image) {
		double xDistance;
		double diameter;
		boolean ballFound = false;
		Mat image2 = new Mat(), image3 = new Mat();

		// Converts the color from BGR to HSV
		Imgproc.cvtColor(image, image2, Imgproc.COLOR_BGR2HSV_FULL); // Will
																		// this
																		// work?

		// Apply a blur filter
		Imgproc.GaussianBlur(image2, image2, new Size(7, 7), 1.1, 1.1);

		// Filters the image to look for red (This needs to be played with)
		int rotation = 128 - 255;
		Core.add(image2, new Scalar(rotation, 0, 0), image2);

		saveImage(image2, "ROT-");

		// Core.inRange(image2, new Scalar(114, 114, 114), new Scalar(142, 255,
		// 255), image3); // BEFORE
		// Hue, Saturation (Black to red), Value (Brightness)
		Core.inRange(image2, new Scalar(90, 120, 60),
				new Scalar(120, 255, 183), image3);

		// Finding contours
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(image3, contours, hierarchy,
				Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(
						0, 0));

		// Finding the ball
		ballFound = false;
		int i;
		for (i = 0; i < contours.size(); i++) {
			if (Imgproc.contourArea(contours.get(i)) > 100) {
				ballFound = true;
				break;
			}
		}

		if (ballFound) {
			Imgproc.drawContours(image, contours, i, new Scalar(0, 255, 0));
			Moments mu = Imgproc.moments(contours.get(i), false);
			Point mc = new Point(mu.get_m10() / mu.get_m00(), mu.get_m01()
					/ mu.get_m00());

			// Finding rect of circle
			Rect boundingRect = Imgproc.boundingRect(contours.get(i));

			diameter = Math.max(boundingRect.width, boundingRect.height);
			xDistance = mc.x - image.width() / 2;

			if (Settings.showImage()) {
				Core.circle(image, mc, 4, new Scalar(0, 255, 0), -1, 8, 0);
				Core.rectangle(image,
						new Point(boundingRect.x, boundingRect.y), new Point(
								boundingRect.x + boundingRect.width,
								boundingRect.y + boundingRect.height),
						new Scalar(255, 255, 100));
				Settings.getViewer().setImage(image);
				saveImage(image, "img-");
			}

			return new ImgInfoImpl(Math.round(xDistance), Math.round(diameter));
		} else {
			if (Settings.showImage()) {
				Settings.getViewer().setImage(image);
			}
		}

		return null;
	}

}
