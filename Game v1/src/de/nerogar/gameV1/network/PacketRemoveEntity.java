package de.nerogar.gameV1.network;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class PacketRemoveEntity extends Packet {

	public int id;

	public PacketRemoveEntity() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile("");
		data.addNode("id", id);

		packedData = data.toByteArray();

	}

	@Override
	public void unpack() {
		data = new DNFile("");
		data.fromByteArray(packedData);

		id = data.getInt("id");
	}

	@Override
	public String getName() {
		return "removeEntity";
	}
}
