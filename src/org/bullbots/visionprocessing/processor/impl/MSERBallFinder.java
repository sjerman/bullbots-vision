package org.bullbots.visionprocessing.processor.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bullbots.visionprocessing.Settings;
import org.bullbots.visionprocessing.processor.BallFinder;
import org.bullbots.visionprocessing.processor.ImgInfo;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.KeyPoint;
import org.opencv.objdetect.CascadeClassifier;

public class MSERBallFinder implements BallFinder {

	static Logger logger = LogManager.getLogger(MSERBallFinder.class
			.getName());

	private FeatureDetector ballFinder;

	public MSERBallFinder() {
		ballFinder =  FeatureDetector.create(FeatureDetector.MSER);
	}

	public ImgInfo processImage(Mat image) {
		
		FeatureDetector detector = FeatureDetector.create(FeatureDetector.GRID_ORB);

		// Detect faces in the image.
		// MatOfRect is a special container class for Rect.
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		detector.detect(image, keypoints);

		logger.trace(String.format("Detected %s objects",
				keypoints.toArray().length));

		// Draw a bounding box around each face.
		for (KeyPoint kp : keypoints.toArray()) {
			double xt = kp.pt.x - kp.size;
			double xb = kp.pt.x + kp.size;
			double yt = kp.pt.y - kp.size;
			double yb = kp.pt.y + kp.size;
			
			Core.rectangle(image, new Point(xt, yt), new Point(xb, yb), new Scalar(0, 255, 0));
		}
		Settings.getViewer().setImage(image);
		
//		// Finding the ball
//		Rect bigRect = null;
//		for (Rect rect : faceDetections.toArray()) {
//			if (rect.area() > 100) {
//				bigRect = rect;
//				break;
//			}
//		}
//
//		if (bigRect != null) {
//
//			double diameter = Math.max(bigRect.width, bigRect.height);
//			double xDistance = (bigRect.width + (bigRect.x / 2))
//					- (image.width() / 2);
//
//			if (Settings.showImage()) {
//				Core.rectangle(image, new Point(bigRect.x, bigRect.y),
//						new Point(bigRect.x + bigRect.width, bigRect.y
//								+ bigRect.height), new Scalar(0, 255, 0));
//				Settings.getViewer().setImage(image);
//			}
//
//			return new ImgInfoImpl(Math.round(xDistance), Math.round(diameter));
//		} else {
//			if (Settings.showImage()) {
//				Settings.getViewer().setImage(image);
//			}
//		}

		return null;
	}

}
