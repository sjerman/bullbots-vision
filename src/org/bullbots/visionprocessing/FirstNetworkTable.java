package org.bullbots.visionprocessing;

import org.bullbots.visionprocessing.AbstractVisionProcessor.Mode;
import org.bullbots.visionprocessing.processor.AutoInfo;
import org.bullbots.visionprocessing.processor.ImgInfo;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class FirstNetworkTable implements VisionNetworkTable {

	NetworkTable networkTable;

	public FirstNetworkTable() {
		NetworkTable.setTeam(1891);
		NetworkTable t = NetworkTable.getTable("test");
	}

	@Override
	public Mode getRobotMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setTeleopInfo(ImgInfo info) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAutoInfo(AutoInfo info) {
		// TODO Auto-generated method stub

	}

}
