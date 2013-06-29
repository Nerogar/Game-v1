package de.nerogar.gameV1.network;

import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.level.Position;

public class PacketBuildHouse extends Packet {

	public int buildingID;
	public Position buildPos;

	public PacketBuildHouse() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile("");
		data.addNode("id", buildingID);
		data.addNode("x", buildPos.x);
		data.addNode("z", buildPos.z);

		packedData = data.toByteArray();

	}

	@Override
	public void unpack() {
		data = new DNFile("");
		data.fromByteArray(packedData);

		buildingID = data.getInt("id");
		buildPos = new Position(data.getInt("x"), data.getInt("z"));
	}

	@Override
	public String getName() {
		return "buildHouse";
	}
}
