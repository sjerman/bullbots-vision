package org.bullbots.visionprocessing.processor.impl;

import org.bullbots.visionprocessing.Settings;
import org.bullbots.visionprocessing.processor.AutoInfo;
import org.bullbots.visionprocessing.processor.AutonomousProcessor;
import org.opencv.core.Mat;

public class AutonomousProcessorImpl implements AutonomousProcessor {

	@Override
	public AutoInfo processImage(Mat image) {
		if (Settings.showImage()) {
			Settings.getViewer().setImage(image);
		}
		return null;
	}

}
