package org.bullbots.visionprocessing.processor.impl;

import org.bullbots.visionprocessing.processor.AutoInfo;

public class AutoInfoImpl implements AutoInfo {
	
	boolean canSeeTape;
	double verticalHeight;

	@Override
	public boolean canSeeTape() {
		return canSeeTape;
	}

	@Override
	public double getVerticalHeight() {
		return verticalHeight;
	}

	public AutoInfoImpl(boolean canSeeTape, double verticalHeight) {
		this.canSeeTape = canSeeTape;
		this.verticalHeight = verticalHeight;
	}

	@Override
	public String toString() {
		return "AutoInfoImpl [canSeeTape=" + canSeeTape + ", verticalHeight="
				+ verticalHeight + "]";
	}
}
