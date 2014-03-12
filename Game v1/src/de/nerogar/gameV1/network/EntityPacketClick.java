package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class EntityPacketClick extends EntityPacket {

	public int mouseButton;

	public EntityPacketClick() {
		channel = ENTITY_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile();
		data.addInt("button", mouseButton);
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

		mouseButton = data.getInt("button");
		entityID = data.getInt("id");
	}

	@Override
	public String getName() {
		return "clickEntity";
	}
}
