package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class EntityPacketRemoveHouse extends EntityPacket {

	public EntityPacketRemoveHouse() {
		channel = ENTITY_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile();
		data.addInt("id", entityID);
		data.addInt("fID", factionID);

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

		entityID = data.getInt("id");
		factionID = data.getInt("fID");
	}

	@Override
	public String getName() {
		return "removeHouse";
	}
}
