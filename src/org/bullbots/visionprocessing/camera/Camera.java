package org.bullbots.visionprocessing.camera;

import org.opencv.core.Mat;

public interface Camera {

	Mat getImage();

	void setAddress(String addr);
	
	void start();

}
