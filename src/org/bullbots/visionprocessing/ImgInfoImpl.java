package org.bullbots.visionprocessing;

public class ImgInfoImpl implements ImgInfo {
	float offset,size;

	public float getOffset() {
		return offset;
	}

	public float getSize() {
		return size;
	}

	public ImgInfoImpl(double d, double e) {
		this.offset = (float) d;
		this.size = (float) e;
	}

	@Override
	public String toString() {
		return "ImgInfo [offset=" + offset + ", size=" + size + "]";
	}

}
