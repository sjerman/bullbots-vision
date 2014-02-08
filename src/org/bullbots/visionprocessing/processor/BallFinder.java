package org.bullbots.visionprocessing.processor;

import org.opencv.core.Mat;

public interface BallFinder {

	ImgInfo processImage(Mat image);

}
