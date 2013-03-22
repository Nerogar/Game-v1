package de.nerogar.gameV1.network;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class PacketConnectionInfo extends Packet {
	public String version;

	@Override
	public void pack() {
		data = new DNFile("");
		data.addNode("version", version);

		packedData = data.toByteArray();
	}

	@Override
	public void unpack() {
		data = new DNFile("");
		data.fromByteArray(packedData);

		version = data.getString("version");
	}
}
