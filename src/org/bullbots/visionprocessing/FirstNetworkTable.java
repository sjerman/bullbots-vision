package org.bullbots.visionprocessing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bullbots.visionprocessing.AbstractVisionProcessor.Mode;
import org.bullbots.visionprocessing.processor.AutoInfo;
import org.bullbots.visionprocessing.processor.ImgInfo;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.IRemote;
import edu.wpi.first.wpilibj.tables.IRemoteConnectionListener;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class FirstNetworkTable implements VisionNetworkTable, ITableListener,
		IRemoteConnectionListener {

	NetworkTable networkTable;

	boolean tapefound = false;
	double distanceFromTable = 0.0;

	static Logger logger = LogManager.getLogger(FirstNetworkTable.class
			.getName());

	Mode mode = Mode.UNKNOWN;

	public FirstNetworkTable() {
		NetworkTable.setTeam(1891);
		NetworkTable.setClientMode();
		networkTable = NetworkTable.getTable("visionprocessing");
		networkTable.addTableListener("robotMode", this, true);
		networkTable.addConnectionListener(this, true);

	}

	@Override
	public Mode getRobotMode() {
		return mode;
	}

	@Override
	public void setTeleopInfo(ImgInfo info) {
		if (info != null) {
			networkTable.putBoolean("ballFound", true);
			networkTable.putNumber("xoffset", info.getOffset());
			networkTable.putNumber("size", info.getSize());
		} else {
			networkTable.putBoolean("ballFound", false);
		}

	}

	@Override
	public void setAutoInfo(AutoInfo info) {

		if (info.canSeeTape() != tapefound) {
			networkTable.putBoolean("tapefound", info.canSeeTape());
			tapefound = info.canSeeTape();
		}

		if (info.getDistanceFromTape() != distanceFromTable) {
			networkTable.putNumber("distancefromtape",
					info.getDistanceFromTape());
			distanceFromTable = info.getDistanceFromTape();
		}

	}

	@Override
	public void valueChanged(ITable source, String key, Object value,
			boolean isNew) {
		if (key.equals("robotMode")) {
			logger.trace("Mode changed:" + value);
			mode = Mode.valueOf((String) value);
		}
	}

	@Override
	public void connected(IRemote remote) {
		logger.info("Robot connected");
		networkTable.putString("robotMode", Mode.TELEOP.name());
	}

	@Override
	public void disconnected(IRemote remote) {
		logger.info("Robot disconnected");
	}

}
