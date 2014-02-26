package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.gameV1.level.Position;

public class FactionPacketBuildHouse extends FactionPacket {

	public String buildingID;
	public Position buildPos;

	public FactionPacketBuildHouse() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile();
		data.addString("id", buildingID);
		data.addInt("x", buildPos.x);
		data.addInt("z", buildPos.z);
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

		factionID = data.getInt("fID");
		buildingID = data.getString("id");
		buildPos = new Position(data.getInt("x"), data.getInt("z"));
	}

	@Override
	public String getName() {
		return "buildHouse";
	}
}
