package org.bullbots.visionprocessing.processor.impl;

import org.bullbots.visionprocessing.processor.AutoInfo;

public class AutoInfoImpl implements AutoInfo {
	
	boolean canSeeTape;
	double disFromTape;

	@Override
	public boolean canSeeTape() {
		return canSeeTape;
	}

	@Override
	public double getDistanceFromTape() {
		return disFromTape;
	}

	public AutoInfoImpl(boolean canSeeTape, double verticalHeight) {
		this.canSeeTape = canSeeTape;
		this.disFromTape = verticalHeight;
	}

	@Override
	public String toString() {
		return "AutoInfoImpl [canSeeTape=" + canSeeTape + ", verticalHeight="
				+ disFromTape + "]";
	}
}
