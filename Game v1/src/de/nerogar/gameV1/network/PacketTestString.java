package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketTestString extends Packet {
	public String name;
	public String testString;

	@Override
	public void pack() {
		data = new DNFile();
		data.addString("name", name);
		data.addString("testString", testString);

		packedData = data.toByteArray();
	}

	@Override
	public void unpack() {
		data = new DNFile();
		try {
			data.fromByteArray(packedData);
		} catch (IOException e) {
			e.printStackTrace();
		}

		testString = data.getString("testString");
		name = data.getString("name");
	}

	@Override
	public String getName() {
		return "TestString";
	}
}
