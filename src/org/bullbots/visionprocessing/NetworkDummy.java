package org.bullbots.visionprocessing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bullbots.visionprocessing.AbstractVisionProcessor.Mode;
import org.bullbots.visionprocessing.processor.AutoInfo;
import org.bullbots.visionprocessing.processor.ImgInfo;

public class NetworkDummy implements VisionNetworkTable {
	
	static Logger logger = LogManager.getLogger(NetworkDummy.class.getName());


	boolean norepeat = false;
	long last = System.nanoTime();

	@Override
	public Mode getRobotMode() {
		String mStr = Settings.getTestMode();
		return Mode.valueOf(mStr);
	}

	@Override
	public void setTeleopInfo(ImgInfo info) {
		if (info == null) {
			if (!norepeat) {
				logger.info("No ball...");
				norepeat = true;
			}
		} else {
			long n = System.nanoTime();
			long diff = (n - last) / 1000 / 1000;
			logger.info(diff + "ms Setting:" + info);
			last = n;
		}

	}

	@Override
	public void setAutoInfo(AutoInfo info) {
		// TODO Auto-generated method stub

	}

}
