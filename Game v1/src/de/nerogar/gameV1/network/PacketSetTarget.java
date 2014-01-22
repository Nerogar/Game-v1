package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketSetTarget extends PacketEntity {

	public int targetID;

	public PacketSetTarget() {
		channel = ENTITY_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile();
		data.addInt("tID", targetID);
		data.addInt("id", entityID);

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

		targetID = data.getInt("tID");
		entityID = data.getInt("id");
	}

	@Override
	public String getName() {
		return "setTarget";
	}
}
