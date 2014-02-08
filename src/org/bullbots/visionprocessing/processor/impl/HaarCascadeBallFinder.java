package org.bullbots.visionprocessing.processor.impl;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

import org.bullbots.visionprocessing.Settings;
import org.bullbots.visionprocessing.processor.BallFinder;
import org.bullbots.visionprocessing.processor.ImgInfo;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.objdetect.CascadeClassifier;

public class HaarCascadeBallFinder implements BallFinder {

	public ImgInfo processImage(Mat image) {
		
	    // Create a face detector from the cascade file in the resources
	    // directory.
	    CascadeClassifier ballFinder = new CascadeClassifier(getClass().getResource("lbpcascade_frontalface.xml").getPath());

	    // Detect faces in the image.
	    // MatOfRect is a special container class for Rect.
	    MatOfRect faceDetections = new MatOfRect();
	    ballFinder.detectMultiScale(image, faceDetections);

	    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

	    // Draw a bounding box around each face.
	    for (Rect rect : faceDetections.toArray()) {
	        Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
	    }


		// Finding the ball
		Rect bigRect=null;
	    for (Rect rect : faceDetections.toArray()) {
			if (rect.area()>100) {
				bigRect= rect;
				break;
			}
		}

		if (bigRect != null) {

			double diameter = Math.max(bigRect.width, bigRect.height);
			double xDistance = ((bigRect.width+bigRect.x)/2) - (image.width() / 2);

			if (Settings.showImage()) {
		        Core.rectangle(image, new Point(bigRect.x, bigRect.y), new Point(bigRect.x + bigRect.width, bigRect.y + bigRect.height), new Scalar(0, 255, 0));
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

	public Mat convBuff2Mat(BufferedImage image) {
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer())
				.getData();
		Mat matImage = new Mat(image.getHeight(), image.getWidth(),
				CvType.CV_8UC3);
		matImage.put(0, 0, pixels);
		return matImage;
	}

}
