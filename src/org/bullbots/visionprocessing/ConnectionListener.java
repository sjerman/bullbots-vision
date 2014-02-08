package org.bullbots.visionprocessing;

import edu.wpi.first.wpilibj.tables.IRemote;
import edu.wpi.first.wpilibj.tables.IRemoteConnectionListener;

public class ConnectionListener implements IRemoteConnectionListener {
	
	

	@Override
	public void connected(IRemote arg0) {
		System.out.println("Robot connected");
	}

	@Override
	public void disconnected(IRemote arg0) {
		System.out.println("Robot disconnected");

	}
	

}
