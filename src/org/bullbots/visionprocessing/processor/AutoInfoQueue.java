package org.bullbots.visionprocessing.processor;

import java.util.ArrayDeque;

public class AutoInfoQueue extends ArrayDeque<AutoInfo> implements AutoInfo {
	int max = 0;

	@Override
	public boolean add(AutoInfo e) {
		boolean ret = super.add(e);
		if (this.size() > max) {
			this.remove();
		}
		return ret;
	}

	public AutoInfoQueue(int size) {
		super(size);
		max = size;
	}

	@Override
	public String toString() {
		String s = "AutoInfoQueue [";
		// for (ImgInfo i : this) {
		// s += i.toString() + " ";
		// }
		// s += "]";
		return s;
	}

	public boolean isFull() {
		return this.size() == this.max;
	}

	@Override
	public boolean canSeeTape() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getVerticalHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

}
