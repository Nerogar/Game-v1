package de.nerogar.gameV1.network;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class PacketClickEntity extends PacketEntity {

	public int mouseButton;

	public PacketClickEntity() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile("");
		data.addNode("button", mouseButton);
		data.addNode("id", entityID);

		packedData = data.toByteArray();

	}

	@Override
	public void unpack() {
		data = new DNFile("");
		data.fromByteArray(packedData);

		mouseButton = data.getInt("button");
		entityID = data.getInt("id");
	}

	@Override
	public String getName() {
		return "clickEntity";
	}
}
