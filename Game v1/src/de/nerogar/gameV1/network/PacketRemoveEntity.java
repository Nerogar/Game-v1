package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketRemoveEntity extends Packet {

	public int id;

	public PacketRemoveEntity() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile();
		data.addInt("id", id);

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

		id = data.getInt("id");
	}

	@Override
	public String getName() {
		return "removeEntity";
	}
}
