package org.bullbots.visionprocessing;

import org.bullbots.visionprocessing.AbstractVisionProcessor.Mode;
import org.bullbots.visionprocessing.processor.ImgInfo;

public class NetworkDummy implements VisionNetworkTable {

	boolean norepeat = false;
	long last = System.nanoTime();

	@Override
	public Mode getRobotMode() {
		String mStr = Settings.getInstance().getProperty(Settings.TEST_MODE,
				"AUTO");
		return Mode.valueOf(mStr);
	}

	@Override
	public void setTeleopInfo(ImgInfo info) {
		if (info == null) {
			if (!norepeat) {
				System.out.println("No ball...");
				norepeat = true;
			}
		} else {
			long n = System.nanoTime();
			long diff = (n - last) / 1000 / 1000;
			System.out.println(diff + "ms Setting:" + info);
			last = n;
		}

	}

	@Override
	public void setAutoInfo() {
		// TODO Auto-generated method stub

	}

}
