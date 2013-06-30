package de.nerogar.gameV1.network;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class PacketSpawnEntity extends Packet {
	public String tagName;
	public DNFile entityData;
	
	public PacketSpawnEntity() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile("");
		data.addNode("tagName", tagName);
		data.addNode("entityData", entityData.toByteArray());

		packedData = data.toByteArray();

	}

	@Override
	public void unpack() {
		data = new DNFile("");
		data.fromByteArray(packedData);

		tagName = data.getString("tagName");
		entityData = new DNFile("");
		entityData.fromByteArray(data.getByteArray("entityData"));
	}

	@Override
	public String getName() {
		return "spawnEntity";
	}
}
