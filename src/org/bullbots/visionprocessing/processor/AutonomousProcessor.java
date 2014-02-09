package org.bullbots.visionprocessing.processor;

import org.opencv.core.Mat;

public interface AutonomousProcessor {

	public AutoInfo processImage(Mat image);

}
