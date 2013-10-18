package de.nerogar.gameV1.network;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class PacketSetTarget extends PacketEntity {

	public int targetID;

	public PacketSetTarget() {
		channel = ENTITY_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile("");
		data.addNode("tID", targetID);
		data.addNode("id", entityID);

		packedData = data.toByteArray();

	}

	@Override
	public void unpack() {
		data = new DNFile("");
		data.fromByteArray(packedData);

		targetID = data.getInt("tID");
		entityID = data.getInt("id");
	}

	@Override
	public String getName() {
		return "setTarget";
	}
}
