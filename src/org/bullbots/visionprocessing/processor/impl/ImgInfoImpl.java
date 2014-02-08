package org.bullbots.visionprocessing.processor.impl;

import org.bullbots.visionprocessing.processor.ImgInfo;

public class ImgInfoImpl implements ImgInfo {
	float offset,size;

	public float getOffset() {
		return offset;
	}

	public float getSize() {
		return size;
	}

	public ImgInfoImpl(double offset, double size) {
		this.offset = (float) offset;
		this.size = (float) size;
	}

	@Override
	public String toString() {
		return "ImgInfo [offset=" + offset + ", size=" + size + "]";
	}

}
