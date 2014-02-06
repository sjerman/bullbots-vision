package org.bullbots.visionprocessing.processor.impl;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;

import org.bullbots.visionprocessing.processor.BallFinder;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

public class RedBallFinder implements BallFinder {
	
	private double xDistance;
	private double diameter;
	private boolean ballFound = false;
	
	public RedBallFinder() {
		// Loading the library
	}
    
    public Mat[] processImage(Mat image) {
    	Mat image2, image3;
    	image2 = new Mat(640,480,CvType.CV_8SC1);
    	image3 = new Mat(640,480,CvType.CV_8SC1);
    	
        // Apply a blur filter
        Imgproc.GaussianBlur(image, image, new Size(7, 7), 1.1, 1.1);
        
        // Converts the color from BGR to HSV
        Imgproc.cvtColor(image, image2, Imgproc.COLOR_BGR2HSV_FULL); // Will this work?
        
        // Filters the image to look for red (This needs to be played with)
        int rotation = 128 - 255;
        Core.add(image2, new Scalar(rotation, 0, 0), image2);
        //Core.inRange(image2, new Scalar(114, 114, 114), new Scalar(142, 255, 255), image3); // BEFORE
        // Hue, Saturation (Black to red), Value (Brightness)
        Core.inRange(image2, new Scalar(114, 64, 64), new Scalar(142, 255, 255), image3);
        
        // Finding contours
        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        Mat hierarchy=new Mat();
		Imgproc.findContours(image3, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0, 0));
		
		// Finding the ball
		ballFound = false;
		int i;
		for(i = 0; i < contours.size(); i++){
			if(Imgproc.contourArea(contours.get(i)) > 100){
				ballFound = true;
				break;
			}
		}
		
		if(ballFound){
			Imgproc.drawContours(image, contours, i, new Scalar(0,255,0));
			Moments mu = Imgproc.moments(contours.get(i),false);
			Point mc = new Point(mu.get_m10()/mu.get_m00(), mu.get_m01()/mu.get_m00());
			Core.circle(image, mc, 4, new Scalar(0,255,0),-1,8,0);
			
			// Finding rect of circle
			Rect boundingRect = Imgproc.boundingRect(contours.get(i));
			Core.rectangle(image, new Point(boundingRect.x, boundingRect.y), new Point(boundingRect.x + boundingRect.width, boundingRect.y + boundingRect.height), new Scalar(255, 255, 100));
			
			diameter = Math.max(boundingRect.width, boundingRect.height);
			xDistance = mc.x - image.width() / 2;
		}
		Mat[] mat = {image, image2, image3};
		return  mat;
    }
	
	public BufferedImage convMat2Buff(Mat mat) {
		// Code for converting Mat to BufferedImage
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if(mat.channels() > 1) {
			Mat mat2 = new Mat();
			Imgproc.cvtColor(mat,  mat2, Imgproc.COLOR_BGR2RGB);
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
		byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
    	Mat matImage = new Mat(image.getHeight(),image.getWidth(),CvType.CV_8UC3);
		matImage.put(0, 0, pixels);
		return matImage;
	}
	
	public double getXDistance() {
		return Math.round(xDistance);
	}
	
	public double getDiameter() {
		return Math.round(diameter);
	}
	
	public boolean isBallFound() {
		return  ballFound;
	}
}
