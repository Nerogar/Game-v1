package de.nerogar.gameV1.DNFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DNFile {

	public String filename;
	DNNodePath nodePath;

	public DNFile(String filename) {
		this.filename = filename;
		nodePath = new DNNodePath(null);
	}

	public boolean exists() {
		File file = new File(filename);
		return file.exists();
	}

	public void load() {
		nodePath = new DNNodePath(null);
		try {
			File file = new File(filename);
			FileInputStream f = new FileInputStream(file);
			DNByteBuffer in = new DNByteBuffer((int) f.getChannel().size());
			f.getChannel().read(in.byteBuffer);
			in.byteBuffer.flip();

			readFile(in, nodePath, -1);

			f.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readFile(DNByteBuffer in, DNNodePath path, int length) throws IOException {

		int item = 0;
		while (in.available() > 0 && (item < length || length < 0)) {
			DNNode dnNode = readNextNode(in);
			if (dnNode.value != null) {
				path.addNode(dnNode.name, dnNode.typ, dnNode.length, dnNode.value);
			} else {
				path.addFolder(dnNode.name);
				readFile(in, path.getPath(dnNode.name), dnNode.length);
			}
			item++;
		}
	}

	private DNNode readNextNode(DNByteBuffer in) throws IOException {
		String name = in.readString();
		byte typ = in.readByte();
		int length = in.readInt();
		Object[] values = null;

		switch (typ) {
		case DNHelper.FOLDER:
			return new DNNode(name, typ, length, null);
		case DNHelper.INTEGER:
			values = new Integer[length];
			for (int i = 0; i < length; i++) {
				values[i] = (Integer) in.readInt();
			}
			break;
		case DNHelper.FLOAT:
			values = new Float[length];
			for (int i = 0; i < length; i++) {
				values[i] = (Float) in.readFloat();
			}
			break;
		case DNHelper.DOUBLE:
			values = new Double[length];
			for (int i = 0; i < length; i++) {
				values[i] = (Double) in.readDouble();
			}
			break;
		case DNHelper.LONG:
			values = new Long[length];
			for (int i = 0; i < length; i++) {
				values[i] = (Long) in.readLong();
			}
			break;
		case DNHelper.BYTE:
			values = new Byte[length];
			for (int i = 0; i < length; i++) {
				values[i] = (Byte) in.readByte();
			}
			break;
		case DNHelper.CHAR:
			values = new Character[length];
			for (int i = 0; i < length; i++) {
				values[i] = (Character) in.readChar();
			}
			break;
		case DNHelper.BOOLEAN:
			values = new Boolean[length];
			for (int i = 0; i < length; i++) {
				values[i] = (Boolean) in.readBool();
			}
			break;
		case DNHelper.STRING:
			values = new String[length];
			for (int i = 0; i < length; i++) {
				values[i] = (String) in.readString();
			}
			break;
		}

		if (length == 1) {
			return new DNNode(name, typ, length, values[0]);
		} else {
			return new DNNode(name, typ, length, values);
		}

	}

	public void save() {
		try {
			File file = new File(filename);
			FileOutputStream f = new FileOutputStream(file);
			DNByteBuffer out = new DNByteBuffer(nodePath.calcSize());

			writeFile(out, nodePath);

			out.byteBuffer.flip();
			f.getChannel().write(out.byteBuffer);

			f.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeFile(DNByteBuffer out, DNNodePath path) throws IOException {

		for (int i = 0; i < path.getNodes().size(); i++) {
			DNNode node = path.getNodes().get(i);
			writeNode(out, node);
		}

		for (int i = 0; i < path.getPaths().size(); i++) {
			DNNodePath node = path.getPaths().get(i);
			DNTagWriter.writeFolderTag(out, node.name, node.getNodes().size() + node.getPaths().size());
			writeFile(out, path.getPath(i));
		}
	}

	public void addNode(String name, Object value) {
		if (value instanceof Integer) {
			nodePath.addNodePath(name, DNHelper.INTEGER, 1, value);
		} else if (value instanceof Integer[]) {
			nodePath.addNodePath(name, DNHelper.INTEGER, ((Integer[]) value).length, value);
		} else if (value instanceof int[]) {
			Integer[] intValue = new Integer[((int[]) value).length];
			for (int i = 0; i < ((int[]) value).length; i++) {
				intValue[i] = ((int[]) value)[i];
			}
			nodePath.addNodePath(name, DNHelper.INTEGER, intValue.length, intValue);
		}
		else if (value instanceof Float) {
			nodePath.addNodePath(name, DNHelper.FLOAT, 1, value);
		} else if (value instanceof Float[]) {
			nodePath.addNodePath(name, DNHelper.FLOAT, ((Float[]) value).length, value);
		} else if (value instanceof float[]) {
			Float[] floatValue = new Float[((float[]) value).length];
			for (int i = 0; i < ((float[]) value).length; i++) {
				floatValue[i] = ((float[]) value)[i];
			}
			nodePath.addNodePath(name, DNHelper.FLOAT, floatValue.length, floatValue);
		}
		else if (value instanceof Double) {
			nodePath.addNodePath(name, DNHelper.DOUBLE, 1, value);
		} else if (value instanceof Double[]) {
			nodePath.addNodePath(name, DNHelper.DOUBLE, ((Double[]) value).length, value);
		} else if (value instanceof double[]) {
			Double[] doubleValue = new Double[((double[]) value).length];
			for (int i = 0; i < ((double[]) value).length; i++) {
				doubleValue[i] = ((double[]) value)[i];
			}
			nodePath.addNodePath(name, DNHelper.DOUBLE, doubleValue.length, doubleValue);
		}
		else if (value instanceof Long) {
			nodePath.addNodePath(name, DNHelper.LONG, 1, value);
		} else if (value instanceof Long[]) {
			nodePath.addNodePath(name, DNHelper.LONG, ((Long[]) value).length, value);
		} else if (value instanceof long[]) {
			Long[] longValue = new Long[((long[]) value).length];
			for (int i = 0; i < ((long[]) value).length; i++) {
				longValue[i] = ((long[]) value)[i];
			}
			nodePath.addNodePath(name, DNHelper.LONG, longValue.length, longValue);
		}
		else if (value instanceof Byte) {
			nodePath.addNodePath(name, DNHelper.BYTE, 1, value);
		} else if (value instanceof Byte[]) {
			nodePath.addNodePath(name, DNHelper.BYTE, ((Byte[]) value).length, value);
		} else if (value instanceof byte[]) {
			Byte[] byteValue = new Byte[((byte[]) value).length];
			for (int i = 0; i < ((byte[]) value).length; i++) {
				byteValue[i] = ((byte[]) value)[i];
			}
			nodePath.addNodePath(name, DNHelper.BYTE, byteValue.length, byteValue);
		}
		else if (value instanceof Character) {
			nodePath.addNodePath(name, DNHelper.CHAR, 1, value);
		} else if (value instanceof Character[]) {
			nodePath.addNodePath(name, DNHelper.CHAR, ((Character[]) value).length, value);
		} else if (value instanceof char[]) {
			Character[] charValue = new Character[((char[]) value).length];
			for (int i = 0; i < ((char[]) value).length; i++) {
				charValue[i] = ((char[]) value)[i];
			}
			nodePath.addNodePath(name, DNHelper.CHAR, charValue.length, charValue);
		}
		else if (value instanceof Boolean) {
			nodePath.addNodePath(name, DNHelper.BOOLEAN, 1, value);
		} else if (value instanceof Boolean[]) {
			nodePath.addNodePath(name, DNHelper.BOOLEAN, ((Boolean[]) value).length, value);
		} else if (value instanceof boolean[]) {
			Boolean[] boolValue = new Boolean[((boolean[]) value).length];
			for (int i = 0; i < ((boolean[]) value).length; i++) {
				boolValue[i] = ((boolean[]) value)[i];
			}
			nodePath.addNodePath(name, DNHelper.BOOLEAN, boolValue.length, boolValue);
		}
		else if (value instanceof String) {
			nodePath.addNodePath(name, DNHelper.STRING, 1, value);
		} else if (value instanceof String[]) {
			nodePath.addNodePath(name, DNHelper.STRING, ((String[]) value).length, value);
		}
	}

	private void writeNode(DNByteBuffer out, DNNode dnNode) throws IOException {
		Object value = dnNode.value;
		String name = dnNode.name;

		if (value instanceof Integer) {
			DNTagWriter.writeIntTag(out, name, (Integer) value);
		} else if (value instanceof Integer[]) {
			DNTagWriter.writeIntTag(out, name, (Integer[]) value);
		} else if (value instanceof Float) {
			DNTagWriter.writeFloatTag(out, name, (Float) value);
		} else if (value instanceof Float[]) {
			DNTagWriter.writeFloatTag(out, name, (Float[]) value);
		} else if (value instanceof Double) {
			DNTagWriter.writeDoubleTag(out, name, (Double) value);
		} else if (value instanceof Double[]) {
			DNTagWriter.writeDoubleTag(out, name, (Double[]) value);
		} else if (value instanceof Long) {
			DNTagWriter.writeLongTag(out, name, (Long) value);
		} else if (value instanceof Long[]) {
			DNTagWriter.writeLongTag(out, name, (Long[]) value);
		} else if (value instanceof Byte) {
			DNTagWriter.writeByteTag(out, name, (Byte) value);
		} else if (value instanceof Byte[]) {
			DNTagWriter.writeByteTag(out, name, (Byte[]) value);
		} else if (value instanceof Character) {
			DNTagWriter.writeCharTag(out, name, (Character) value);
		} else if (value instanceof Character[]) {
			DNTagWriter.writeCharTag(out, name, (Character[]) value);
		} else if (value instanceof Boolean) {
			DNTagWriter.writeBoolTag(out, name, (Boolean) value);
		} else if (value instanceof Boolean[]) {
			DNTagWriter.writeBoolTag(out, name, (Boolean[]) value);
		} else if (value instanceof String) {
			DNTagWriter.writeStringTag(out, name, (String) value);
		} else if (value instanceof String[]) {
			DNTagWriter.writeStringTag(out, name, (String[]) value);
		}
	}

	public void addFolder(String name) {
		nodePath.addPathPath(name);
	}

	//get funktionen:

	public int getInt(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return -1;
		if (node.length == 1) {
			if (node.typ == DNHelper.INTEGER && node.value instanceof Integer) return (Integer) node.value;
		}

		return 0;
	}

	public int[] getIntArray(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return null;
		if (node.length > 1) {
			if (node.typ == DNHelper.INTEGER && node.value instanceof Integer[]) {
				int[] tempIntAarray = new int[((Integer[]) node.value).length];
				for (int i = 0; i < tempIntAarray.length; i++) {
					tempIntAarray[i] = ((Integer[]) node.value)[i];
				}
				return tempIntAarray;
			}
		}

		return null;
	}

	public float getFloat(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return -1;
		if (node.length == 1) {
			if (node.typ == DNHelper.FLOAT && node.value instanceof Float) return (Float) node.value;
		}

		return 0;
	}

	public float[] getFloatArray(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return null;
		if (node.length > 1) {
			if (node.typ == DNHelper.FLOAT && node.value instanceof Float[]) {
				float[] tempFloatAarray = new float[((Float[]) node.value).length];
				for (int i = 0; i < tempFloatAarray.length; i++) {
					tempFloatAarray[i] = ((Float[]) node.value)[i];
				}
				return tempFloatAarray;
			}
		}

		return null;
	}

	public double getDouble(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return -1;
		if (node.length == 1) {
			if (node.typ == DNHelper.DOUBLE && node.value instanceof Double) return (Double) node.value;
		}

		return 0;
	}

	public double[] getDoubleArray(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return null;
		if (node.length > 1) {
			if (node.typ == DNHelper.DOUBLE && node.value instanceof Double[]) {
				double[] tempDoubleAarray = new double[((Double[]) node.value).length];
				for (int i = 0; i < tempDoubleAarray.length; i++) {
					tempDoubleAarray[i] = ((Double[]) node.value)[i];
				}
				return tempDoubleAarray;
			}
		}

		return null;
	}

	public long getLong(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return -1;
		if (node.length == 1) {
			if (node.typ == DNHelper.LONG && node.value instanceof Long) return (Long) node.value;
		}

		return 0;
	}

	public long[] getLongArray(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return null;
		if (node.length > 1) {
			if (node.typ == DNHelper.LONG && node.value instanceof Long[]) {
				long[] tempLongAarray = new long[((Long[]) node.value).length];
				for (int i = 0; i < tempLongAarray.length; i++) {
					tempLongAarray[i] = ((Long[]) node.value)[i];
				}
				return tempLongAarray;
			}
		}

		return null;
	}

	public byte getByte(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return -1;
		if (node.length == 1) {
			if (node.typ == DNHelper.BYTE && node.value instanceof Byte) return (Byte) node.value;
		}

		return 0;
	}

	public byte[] getByteArray(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return null;
		if (node.length > 1) {
			if (node.typ == DNHelper.BYTE && node.value instanceof Byte[]) {
				byte[] tempByteAarray = new byte[((Byte[]) node.value).length];
				for (int i = 0; i < tempByteAarray.length; i++) {
					tempByteAarray[i] = ((Byte[]) node.value)[i];
				}
				return tempByteAarray;
			}
		}

		return null;
	}

	public char getChar(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return 0;
		if (node.length == 1) {
			if (node.typ == DNHelper.CHAR && node.value instanceof Character) return (Character) node.value;
		}

		return 0;
	}

	public char[] getCharArray(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return null;
		if (node.length > 1) {
			if (node.typ == DNHelper.CHAR && node.value instanceof Character[]) {
				char[] tempCharacterAarray = new char[((Character[]) node.value).length];
				for (int i = 0; i < tempCharacterAarray.length; i++) {
					tempCharacterAarray[i] = ((Character[]) node.value)[i];
				}
				return tempCharacterAarray;
			}
		}

		return null;
	}

	public boolean getBoolean(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return false;
		if (node.length == 1) {
			if (node.typ == DNHelper.BOOLEAN && node.value instanceof Boolean) return (Boolean) node.value;
		}

		return false;
	}

	public boolean[] getBooleanArray(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return null;
		if (node.length > 1) {
			if (node.typ == DNHelper.BOOLEAN && node.value instanceof Boolean[]) {
				boolean[] tempBooleanAarray = new boolean[((Boolean[]) node.value).length];
				for (int i = 0; i < tempBooleanAarray.length; i++) {
					tempBooleanAarray[i] = ((Boolean[]) node.value)[i];
				}
				return tempBooleanAarray;
			}
		}

		return null;
	}

	public String getString(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return null;
		if (node.length == 1) {
			if (node.typ == DNHelper.STRING && node.value instanceof String) return (String) node.value;
		}

		return null;
	}

	public String[] getStringArray(String name) {
		DNNode node = nodePath.getNodePath(name);
		if (node == null) return null;
		if (node.length > 1) {
			if (node.typ == DNHelper.STRING && node.value instanceof String[]) return (String[]) node.value;
		}

		return null;
	}

	public int getNodesSize(String name) {
		return nodePath.getNodepath(name).getNodesSize();
	}

	public int getFoldersSize(String name) {
		DNNodePath test = nodePath.getNodepath(name);
		return test.getFoldersSize();
	}

	public int getSize(String name) {
		return nodePath.getNodepath(name).getSize();
	}
}
