package org.bullbots.visionprocessing;

import org.bullbots.visionprocessing.AbstractVisionProcessor.Mode;
import org.bullbots.visionprocessing.processor.ImgInfo;

public interface VisionNetworkTable {
	
	Mode getRobotMode();
	
	void setTeleopInfo(ImgInfo info);
	
	void setAutoInfo();
	

}
