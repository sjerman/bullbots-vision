package org.bullbots.visionprocessing;

import org.bullbots.visionprocessing.AbstractVisionProcessor.Mode;
import org.bullbots.visionprocessing.processor.AutoInfo;
import org.bullbots.visionprocessing.processor.ImgInfo;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class FirstNetworkTable implements VisionNetworkTable, ITableListener {

	NetworkTable networkTable;
	
	Mode mode= Mode.UNKNOWN;

	public FirstNetworkTable() {
		NetworkTable.setTeam(1891);
		networkTable = NetworkTable.getTable("visionprocessing");
		networkTable.addTableListener("robotMode", this, true);
	}

	@Override
	public Mode getRobotMode() {
		return mode;
	}

	@Override
	public void setTeleopInfo(ImgInfo info) {
		if (info != null){
			networkTable.putBoolean("ballFound", true);
			networkTable.putNumber("xoffset", info.getOffset());
			networkTable.putNumber("size", info.getSize());			
		} else {
			networkTable.putBoolean("ballFound", false);
		}

	}

	@Override
	public void setAutoInfo(AutoInfo info) {
		// TODO Auto-generated method stub

	}

	@Override
	public void valueChanged(ITable source, String key, Object value,
			boolean isNew) {
		if (key.equals("robotMode")){
			mode = Mode.valueOf((String) value);
		}
	}

}
