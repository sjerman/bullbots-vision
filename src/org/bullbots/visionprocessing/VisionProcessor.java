package org.bullbots.visionprocessing;

import org.opencv.core.Mat;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class VisionProcessor extends AbstractVisionProcessor {

	// processor
	void run() {

//		NetworkTable.setIPAddress("localhost");
//		NetworkTable t = NetworkTable.getTable("test");
		// main loop
		init();

		int n = 0;
		while (true) {

			Mat img = camera.getImage();
			System.out.println(">>" + img.height() + " " + img.width());

			Mode m = getMode();
			switch (m) {
			case AUTO:
				break;
			case TELEOP:
				
				break;
			}
			sleep();
		}
	}

	// Main file
	public static void main(String[] args) {
		new VisionProcessor().run();
	}

}
