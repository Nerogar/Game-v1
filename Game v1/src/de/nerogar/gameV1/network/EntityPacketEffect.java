package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class EntityPacketEffect extends EntityPacket {

	public int effectID;
	public int targetID;

	public EntityPacketEffect() {
		channel = ENTITY_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();
		data.addInt("id", entityID);
		data.addInt("ef", effectID);
		data.addInt("tID", targetID);

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
		effectID = data.getInt("ef");
		targetID = data.getInt("tID");
	}

	@Override
	public String getName() {
		return "effect";
	}
}
