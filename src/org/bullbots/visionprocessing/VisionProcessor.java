package org.bullbots.visionprocessing;

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
			Mode m = getMode();
			switch (m) {
			case AUTO:
				handleAuto();
				break;
			case TELEOP:
				handleTeleOp();
				break;
			}
		}
	}

	private void handleTeleOp() {
		Mat img = camera.getImage();
		System.out.println(">>" + img.height() + " " + img.width());
		ImgInfo info = ballfinder.processImage(img);
		if (info == null) {
			bfQueue.clear();
			networkTable.setTeleopInfo(info);
		} else {
			bfQueue.add(info);
			if (bfQueue.isFull()) {
				networkTable.setTeleopInfo(info);
			}
		}
	}

	private void handleAuto() {
		// TODO Auto-generated method stub

	}

	// Main file
	public static void main(String[] args) {
		new VisionProcessor().run();
	}

}
