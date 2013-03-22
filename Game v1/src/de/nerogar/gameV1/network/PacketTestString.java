package de.nerogar.gameV1.network;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class PacketTestString extends Packet {
	public String name;
	public String testString;

	@Override
	public void pack() {
		data = new DNFile("");
		data.addNode("name", name);
		data.addNode("testString", testString);

		packedData = data.toByteArray();
	}

	@Override
	public void unpack() {
		data = new DNFile("");
		data.fromByteArray(packedData);

		testString = data.getString("testString");
		name = data.getString("name");
	}
}
