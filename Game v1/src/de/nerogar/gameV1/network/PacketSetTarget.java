package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.gameV1.Vector3d;

public class PacketSetTarget extends PacketEntity {

	public static final int TYPE_FOLLOW = 0;
	public static final int TYPE_ATTACK = 1;

	public int targetID;
	public Vector3d targetPosition;
	public int type = TYPE_ATTACK;

	public PacketSetTarget() {
		channel = ENTITY_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile();
		data.addInt("id", entityID);

		data.addInt("tID", targetID);

		if (targetPosition != null) {
			double[] tPosArray = new double[3];
			tPosArray[0] = targetPosition.getX();
			tPosArray[1] = targetPosition.getY();
			tPosArray[2] = targetPosition.getZ();
			data.addDouble("tp", tPosArray);
		} else {
			data.addDouble("tp", null);
		}
		data.addInt("t", type);

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

		targetID = data.getInt("tID");

		double[] tPosArray = data.getDoubleArray("tp");
		if (tPosArray != null) {
			targetPosition = new Vector3d(tPosArray[0], tPosArray[1], tPosArray[2]);
		}

		type = data.getInt("t");
	}

	@Override
	public String getName() {
		return "setTarget";
	}
}
