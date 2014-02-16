package org.bullbots.visionprocessing;

import org.bullbots.visionprocessing.processor.AutoInfo;
import org.bullbots.visionprocessing.processor.BallFinderQueue;
import org.bullbots.visionprocessing.processor.ImgInfo;
import org.opencv.core.Mat;

public class VisionProcessor extends AbstractVisionProcessor {

	BallFinderQueue bfQueue = new BallFinderQueue(5);

	// processor
	void run() {
		init();

		int n = 0;
		while (true) {
			Mode mode = networkTable.getRobotMode();
			switch (mode) {
			case AUTO:
				handleAuto();
				break;
			case TELEOP:
				handleTeleOp();
				break;
			default:
				sleep();
				sleep();
				logger.error("Mode is:" + mode + " - not doing anything....");
			}
		}
	}

	private void handleTeleOp() {
		Mat img = ballCamera.getImage();
		ImgInfo info = ballfinder.processImage(img);
		if (info == null) {
			bfQueue.clear();
			networkTable.setTeleopInfo(info);
		} else {
			bfQueue.add(info);
			if (bfQueue.isFull()) {
				networkTable.setTeleopInfo(bfQueue);
			}
		}
	}

	private void handleAuto() {
		Mat img = autoCamera.getImage();

		AutoInfo info = autonomousProcessor.processImage(img);
		networkTable.setAutoInfo(info);
	}

	// Main file
	public static void main(String[] args) {
		new VisionProcessor().run();
	}

}
