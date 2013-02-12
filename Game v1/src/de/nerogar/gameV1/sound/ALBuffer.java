package de.nerogar.gameV1.sound;

public class ALBuffer {

	private int size;
	private int bufferID;
	
	public ALBuffer(int bufferID, int size) {
		this.bufferID = bufferID;
		this.size = size;
	}
	
	public void destroy() {
		ALHelper.destroyBuffer(this);
	}

	public int getSize() {
		return size;
	}
	
	public int getBufferID() {
		return bufferID;
	}
	
}
