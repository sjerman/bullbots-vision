package org.bullbots.visionprocessing;

import org.bullbots.visionprocessing.AbstractVisionProcessor.Mode;
import org.bullbots.visionprocessing.processor.AutoInfo;
import org.bullbots.visionprocessing.processor.ImgInfo;
import org.bullbots.visionprocessing.processor.impl.ImgInfoImpl;

import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.IRemote;
import edu.wpi.first.wpilibj.tables.IRemoteConnectionListener;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class FirstNetworkTable implements VisionNetworkTable, ITableListener, IRemoteConnectionListener {

	NetworkTable networkTable;
	
	Mode mode= Mode.UNKNOWN;

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
		if (info != null){
			System.out.println(">>"+info);
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
		System.out.println("ValueChanged:"+key);
		if (key.equals("robotMode")){
			System.out.println("Mode changed:"+value);
			mode = Mode.valueOf((String) value);
		}
	}

	@Override
	public void connected(IRemote remote) {
		System.out.println("Robot connected");
	}

	@Override
	public void disconnected(IRemote remote) {
		System.out.println("Robot disconnected");
	}
	
	public static void main(String[] args) {
		FirstNetworkTable t = new FirstNetworkTable();
		t.setTeleopInfo(new ImgInfoImpl(0.2,0.2));
	}

}
