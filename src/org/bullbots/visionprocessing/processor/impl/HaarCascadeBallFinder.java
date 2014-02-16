package org.bullbots.visionprocessing.processor.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bullbots.visionprocessing.Settings;
import org.bullbots.visionprocessing.processor.BallFinder;
import org.bullbots.visionprocessing.processor.ImgInfo;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;

public class HaarCascadeBallFinder implements BallFinder {

	static Logger logger = LogManager.getLogger(HaarCascadeBallFinder.class
			.getName());

	private CascadeClassifier ballFinder;

	public HaarCascadeBallFinder() {
		String haarFile = Settings.getHaarCascade();
		logger.info("Loading haar classifier from: '" + haarFile + "'");

		// Create a face detector from the cascade file in the resources
		// directory.
		ballFinder = new CascadeClassifier(haarFile);
	}

	public ImgInfo processImage(Mat image) {

		// Detect faces in the image.
		// MatOfRect is a special container class for Rect.
		MatOfRect faceDetections = new MatOfRect();
		ballFinder.detectMultiScale(image, faceDetections);

		logger.trace(String.format("Detected %s objects",
				faceDetections.toArray().length));

		// Draw a bounding box around each face.
		for (Rect rect : faceDetections.toArray()) {
			Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x
					+ rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
		}

		// Finding the ball
		Rect bigRect = null;
		for (Rect rect : faceDetections.toArray()) {
			if (rect.area() > 100) {
				bigRect = rect;
				break;
			}
		}

		if (bigRect != null) {

			double diameter = Math.max(bigRect.width, bigRect.height);
			double xDistance = (bigRect.width + (bigRect.x / 2))
					- (image.width() / 2);

			if (Settings.showImage()) {
				Core.rectangle(image, new Point(bigRect.x, bigRect.y),
						new Point(bigRect.x + bigRect.width, bigRect.y
								+ bigRect.height), new Scalar(0, 255, 0));
				Settings.getViewer().setImage(image);
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
