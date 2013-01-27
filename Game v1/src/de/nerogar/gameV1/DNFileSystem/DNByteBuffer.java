package de.nerogar.gameV1.DNFileSystem;

import java.nio.ByteBuffer;

public class DNByteBuffer {
	public ByteBuffer byteBuffer;

	public DNByteBuffer(int length) {
		byteBuffer = ByteBuffer.allocate(length);
	}

	public int readInt() {
		return byteBuffer.getInt();
	}

	public float readFloat() {
		return byteBuffer.getFloat();
	}

	public double readDouble() {
		return byteBuffer.getDouble();
	}

	public long readLong() {
		return byteBuffer.getLong();
	}

	public byte readByte() {
		return byteBuffer.get();
	}

	public char readChar() {
		return byteBuffer.getChar();
	}

	public boolean readBool() {
		return byteBuffer.get() == 0x01;
	}

	public String readString() {
		int length = byteBuffer.getInt();
		String text = "";
		for (int i = 0; i < length; i++) {
			text += byteBuffer.getChar();
		}
		return text;
	}

	public void writeInt(int value) {
		byteBuffer.putInt(value);
	}

	public void writeFloat(float value) {
		byteBuffer.putFloat(value);
	}

	public void writeDouble(double value) {
		byteBuffer.putDouble(value);
	}

	public void writeLong(long value) {
		byteBuffer.putLong(value);
	}

	public void writeByte(byte value) {
		byteBuffer.put(value);
	}

	public void writeChar(char value) {
		byteBuffer.putChar(value);
	}

	public void writeBool(boolean value) {
		if (value) {
			byteBuffer.put((byte) 0x01);
		} else {
			byteBuffer.put((byte) 0x00);
		}
	}

	public void writeString(String value) {
		byteBuffer.putInt(value.length());
		for (int i = 0; i < value.length(); i++) {
			byteBuffer.putChar(value.charAt(i));
		}
	}

	public int size() {
		return byteBuffer.limit();
	}

	public int available() {
		return byteBuffer.limit() - byteBuffer.position();
	}
}
