package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketSpawnEntity extends Packet {
	public String tagName;
	public DNFile entityData;

	public PacketSpawnEntity() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile();
		data.addString("tagName", tagName);
		data.addByte("entityData", entityData.toByteArray());

		packedData = data.toByteArray();

	}

	@Override
	public void unpack() {
		data = new DNFile();
		entityData = new DNFile();

		try {
			data.fromByteArray(packedData);
			entityData.fromByteArray(data.getByteArray("entityData"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		tagName = data.getString("tagName");
	}

	@Override
	public String getName() {
		return "spawnEntity";
	}
}
