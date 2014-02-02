package org.bullbots.visionprocessing.camera;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class Camera {

	private final VideoCapture VC;
	private Mat image;
	
	public Camera(String url) {
		// Loading the library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println(">> Successfully loaded native library [OpenCV " + Core.VERSION + "]");
		
        image = new Mat(640, 480,CvType.CV_8SC1 );
		VC = new VideoCapture(url);
	}
	
	public Mat getImage() {
		VC.read(image);
		Highgui.imwrite("IMAGE.JPG", image);
		return image;
	}
}
