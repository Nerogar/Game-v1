package de.nerogar.gameV1.DNFileSystem;

import java.io.IOException;

public class DNTagWriter {

	public static boolean writeFolderTag(DNByteBuffer out, String name, int length) throws IOException {
		out.writeString(name);
		out.writeByte(DNHelper.FOLDER);
		out.writeInt(length);
		return true;
	}

	public static boolean writeIntTag(DNByteBuffer out, String name, Integer value) throws IOException {
		return writeIntTag(out, name, new Integer[] { value });
	}

	public static boolean writeIntTag(DNByteBuffer out, String name, Integer[] values) throws IOException {
		out.writeString(name);
		out.writeByte(DNHelper.INTEGER);
		out.writeInt(values.length);
		for (int value : values) {
			out.writeInt(value);
		}
		return true;
	}

	public static boolean writeFloatTag(DNByteBuffer out, String name, Float value) throws IOException {
		return writeFloatTag(out, name, new Float[] { value });
	}

	public static boolean writeFloatTag(DNByteBuffer out, String name, Float[] values) throws IOException {
		out.writeString(name);
		out.writeByte(DNHelper.FLOAT);
		out.writeInt(values.length);
		for (float value : values) {
			out.writeFloat(value);
		}
		return true;
	}

	public static boolean writeDoubleTag(DNByteBuffer out, String name, Double value) throws IOException {
		return writeDoubleTag(out, name, new Double[] { value });
	}

	public static boolean writeDoubleTag(DNByteBuffer out, String name, Double[] values) throws IOException {
		out.writeString(name);
		out.writeByte(DNHelper.DOUBLE);
		out.writeInt(values.length);
		for (double value : values) {
			out.writeDouble(value);
		}
		return true;
	}

	public static boolean writeLongTag(DNByteBuffer out, String name, Long value) throws IOException {
		return writeLongTag(out, name, new Long[] { value });
	}

	public static boolean writeLongTag(DNByteBuffer out, String name, Long[] values) throws IOException {
		out.writeString(name);
		out.writeByte(DNHelper.LONG);
		out.writeInt(values.length);
		for (long value : values) {
			out.writeLong(value);
		}
		return true;
	}

	public static boolean writeByteTag(DNByteBuffer out, String name, Byte value) throws IOException {
		return writeByteTag(out, name, new Byte[] { value });
	}

	public static boolean writeByteTag(DNByteBuffer out, String name, Byte[] values) throws IOException {
		out.writeString(name);
		out.writeByte(DNHelper.BYTE);
		out.writeInt(values.length);
		for (byte value : values) {
			out.writeByte(value);
		}
		return true;
	}

	public static boolean writeCharTag(DNByteBuffer out, String name, Character value) throws IOException {
		return writeCharTag(out, name, new Character[] { value });
	}

	public static boolean writeCharTag(DNByteBuffer out, String name, Character[] values) throws IOException {
		out.writeString(name);
		out.writeByte(DNHelper.CHAR);
		out.writeInt(values.length);
		for (char value : values) {
			out.writeChar(value);
		}
		return true;
	}

	public static boolean writeBoolTag(DNByteBuffer out, String name, Boolean value) throws IOException {
		return writeBoolTag(out, name, new Boolean[] { value });
	}

	public static boolean writeBoolTag(DNByteBuffer out, String name, Boolean[] values) throws IOException {
		out.writeString(name);
		out.writeByte(DNHelper.BOOLEAN);
		out.writeInt(values.length);
		for (boolean value : values) {
			out.writeBool(value);
		}
		return true;
	}

	public static boolean writeStringTag(DNByteBuffer out, String name, String value) throws IOException {
		return writeStringTag(out, name, new String[] { value });
	}

	public static boolean writeStringTag(DNByteBuffer out, String name, String[] values) throws IOException {
		out.writeString(name);
		out.writeByte(DNHelper.STRING);
		out.writeInt(values.length);
		for (String value : values) {
			out.writeString(value);
		}
		return true;
	}
}
