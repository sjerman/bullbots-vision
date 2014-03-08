package org.bullbots.visionprocessing.processor.impl;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bullbots.visionprocessing.Settings;
import org.bullbots.visionprocessing.processor.AutoInfo;
import org.bullbots.visionprocessing.processor.AutonomousProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class AutonomousProcessorImpl implements AutonomousProcessor {

	// ratio = width / height
	private double horizontalRatio = 24 / 4;
	private double verticalRatio = 4 / 32;

	private double horizontalTolerance = 1;
	private double verticalTolerance = 1;

	static Logger logger = LogManager.getLogger(AutonomousProcessorImpl.class
			.getName());

	static long imageNo = 0;

	private void saveImage(Mat image, String name) {

		if (Settings.saveImages() && (imageNo++ % 15) == 0)
			Highgui.imwrite("images/" + name + imageNo + ".png", image);
	}

	@Override
	public AutoInfo processImage(Mat image) {
		Mat dirtyImage = new Mat();

		saveImage(image, "auto");

		// Blurring the image
		Imgproc.blur(image, dirtyImage, new Size(5, 5));

		// Converting to HSV
		Imgproc.cvtColor(dirtyImage, dirtyImage, Imgproc.COLOR_BGR2HSV_FULL);

		// Hue - Color, Saturation - Black to white, Value - brightness
		Core.inRange(dirtyImage, new Scalar(0, 0, 250), new Scalar(255, 255,
				255), dirtyImage);

		// Detecting edges
		Imgproc.Canny(dirtyImage, dirtyImage, 0, 0);

		// Finding contours
		ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(dirtyImage, contours, hierarchy,
				Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(
						0, 0));

		// Taking away very small objects
		contours = getObjectsLargerThan(contours, 100);

		boolean tapeFound = false;

		// Checking if at least 2 objects were found
		if (contours.size() > 1) {
			// Finding the two largest objects, in order to look for the two
			// pieces of tape
			MatOfPoint object1 = getLargest(contours);
			contours.remove(object1);
			MatOfPoint object2 = getLargest(contours);

			// Now checking if those objects are most likely the tape...

			// Finding rects of each object
			Rect rect1 = Imgproc.boundingRect(object1);
			Rect rect2 = Imgproc.boundingRect(object2);

			// 24in x 4in
			// 4in x 32in

			double rect1Ratio = rect1.width / (rect1.height + 0.0);
			double rect2Ratio = rect2.width / (rect2.height + 0.0);

			// Checking if both of the shapes match either the horizontal or
			// vertical ratios, within
			// a given tolerance
			if ((Math.abs(rect1Ratio - horizontalRatio) <= horizontalTolerance || Math
					.abs(rect2Ratio - horizontalRatio) <= horizontalTolerance)
					&& (Math.abs(rect1Ratio - verticalRatio) <= verticalTolerance || Math
							.abs(rect2Ratio - verticalRatio) <= verticalTolerance)) {
				tapeFound = true;
				// Drawing a rectangle around each object
				Scalar color = new Scalar(255, 0, 0);
				Core.rectangle(image, new Point(rect1.x, rect1.y), new Point(
						rect1.x + rect1.width, rect1.y + rect1.height), color);
				Core.rectangle(image, new Point(rect2.x, rect2.y), new Point(
						rect2.x + rect2.width, rect2.y + rect2.height), color);

				// Finding the distance from the tape
				double height = Math.max(rect1.height, rect2.height);
				logger.info("Found rectangles - Vertical Height = " + height);
				saveImage(image, "autor");
				if (Settings.showImage()) {
					Settings.getViewer().setImage(image);
				}
				if (tapeFound) {
					return new AutoInfoImpl(true, height);
				}

			}
		} else {
			if (Settings.showImage()) {
				Settings.getViewer().setImage(image);
			}
		}

		return null;
	}

	private ArrayList<MatOfPoint> getObjectsLargerThan(
			ArrayList<MatOfPoint> list, double size) {
		ArrayList<MatOfPoint> newList = list;
		for (int i = 0; i < newList.size(); i++) {
			if (Imgproc.contourArea(newList.get(i)) < size) {
				newList.remove(i);
			}
		}
		return newList;
	}

	private MatOfPoint getLargest(ArrayList<MatOfPoint> contours) {
		double max = -1; // area could turn out to be zero
		MatOfPoint object = null;
		for (int i = 0; i < contours.size(); i++) {
			double area = Imgproc.contourArea(contours.get(i));
			if (area > max) {
				max = area;
				object = contours.get(i);
			}
		}
		return object;
	}

}
