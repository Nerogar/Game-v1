package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.DNFileSystem.DNNodePath;

public class EntityPacketUpdate extends EntityPacket {

	public static final String ENTITY_DATA_PATHNAME = "ed";
	public DNNodePath entityData;

	public EntityPacketUpdate() {
		channel = ENTITY_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();
		data.addInt("id", entityID);

		data.addPathObject(entityData);
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

		entityData = data.getPath(ENTITY_DATA_PATHNAME);
		entityID = data.getInt("id");
	}

	@Override
	public String getName() {
		return "moveEntity";
	}
}
