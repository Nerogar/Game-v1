package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.gameV1.level.Position;

public class PacketBuildHouse extends Packet {

	public int buildingID;
	public Position buildPos;

	public PacketBuildHouse() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile();
		data.addInt("id", buildingID);
		data.addInt("x", buildPos.x);
		data.addInt("z", buildPos.z);

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

		buildingID = data.getInt("id");
		buildPos = new Position(data.getInt("x"), data.getInt("z"));
	}

	@Override
	public String getName() {
		return "buildHouse";
	}
}
