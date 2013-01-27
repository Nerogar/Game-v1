package de.nerogar.gameV1.DNFileSystem;

public class DNHelper {

	final static byte FOLDER = 0;
	final static byte INTEGER = 1;
	final static byte LONG = 2;
	final static byte BYTE = 3;
	final static byte CHAR = 4;
	final static byte STRING = 5;
	final static byte FLOAT = 6;
	final static byte DOUBLE = 7;
	final static byte BOOLEAN = 8;
	
	final static int FOLDERSIZE = 9; //+name * 2
	final static byte INTEGERSIZE = 4;
	final static byte LONGSIZE = 8;
	final static byte BYTESIZE = 1;
	final static byte CHARSIZE = 2;
	final static byte STRINGSIZE = 4; //+länge * 2
	final static byte FLOATSIZE = 4;
	final static byte DOUBLESIZE = 8;
	final static byte BOOLEANSIZE = 1;
}
