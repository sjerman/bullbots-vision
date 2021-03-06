package org.bullbots.visionprocessing.processor;

import java.util.ArrayDeque;

public class BallFinderQueue extends ArrayDeque<ImgInfo> implements ImgInfo {
	int max = 0;

	@Override
	public boolean add(ImgInfo e) {
		boolean ret = super.add(e);
		if (this.size() > max) {
			this.remove();
		}
		return ret;
	}

	public BallFinderQueue(int size) {
		super(size);
		max = size;
	}

	@Override
	public String toString() {
		String s = "AveragingQueue [";
		for (ImgInfo i : this) {
			s += i.toString() + " ";
		}
		s += "]";
		return s;
	}

	@Override
	public float getOffset() {
		float offset = 0.0F;
		for (ImgInfo i : this) {
			offset += i.getOffset();
		}
		return offset / this.size();
	}

	@Override
	public float getSize() {
		float size = 0.0F;
		for (ImgInfo i : this) {
			size += i.getSize();
		}
		return size / this.size();
	}

	public boolean isFull() {
		return this.size() == this.max;
	}

}
