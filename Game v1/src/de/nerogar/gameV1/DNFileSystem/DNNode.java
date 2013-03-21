package de.nerogar.gameV1.DNFileSystem;

public class DNNode {
	public String name;
	public byte typ;
	public int length;
	public Object value;

	public DNNode(String name, byte typ, int length, Object value) {
		this.name = name;
		this.typ = typ;
		this.length = length;
		this.value = value;
	}

	public int calcSize() {
		int size = 0;

		switch (typ) {
		case DNHelper.INTEGER:
			if (value instanceof Integer[]) {
				for (int i = 0; i < ((Integer[]) value).length; i++) {
					size += DNHelper.INTEGERSIZE;
				}
			} else {
				size += DNHelper.INTEGERSIZE;
			}

			break;
		case DNHelper.LONG:
			if (value instanceof Long[]) {
				for (int i = 0; i < ((Long[]) value).length; i++) {
					size += DNHelper.LONGSIZE;
				}
			} else {
				size += DNHelper.LONGSIZE;
			}
			break;
		case DNHelper.BYTE:
			if (value instanceof Byte[]) {
				for (int i = 0; i < ((Byte[]) value).length; i++) {
					size += DNHelper.BYTESIZE;
				}
			} else {
				size += DNHelper.BYTESIZE;
			}
			break;
		case DNHelper.CHAR:
			if (value instanceof Character[]) {
				for (int i = 0; i < ((Character[]) value).length; i++) {
					size += DNHelper.CHARSIZE;
				}
			} else {
				size += DNHelper.CHARSIZE;
			}
			break;
		case DNHelper.STRING:
			if (value instanceof String[]) {
				for (String tempString : (String[]) value) {
					size += DNHelper.STRINGSIZE;
					size += tempString.length() * 2;
				}
			} else {
				size += DNHelper.STRINGSIZE;
				size += ((String) value).length() * 2;
			}

			break;
		case DNHelper.FLOAT:
			if (value instanceof Float[]) {
				for (int i = 0; i < ((Float[]) value).length; i++) {
					size += DNHelper.FLOATSIZE;
				}
			} else {
				size += DNHelper.FLOATSIZE;
			}
			break;
		case DNHelper.DOUBLE:
			if (value instanceof Double[]) {
				for (int i = 0; i < ((Double[]) value).length; i++) {
					size += DNHelper.DOUBLESIZE;
				}
			} else {
				size += DNHelper.DOUBLESIZE;
			}
			break;
		case DNHelper.BOOLEAN:
			if (value instanceof Boolean[]) {
				for (int i = 0; i < ((Boolean[]) value).length; i++) {
					size += DNHelper.BOOLEANSIZE;
				}
			} else {
				size += DNHelper.BOOLEANSIZE;
			}
			break;
		}

		size += name.length() * 2 + 4;
		size += 1 + 4;
		return size;
	}
}
