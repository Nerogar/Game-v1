package de.nerogar.gameV1.network;

import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.level.Position;

public class PacketChunkData extends Packet {

	public DNFile chunkFile;
	public Position chunkPos;

	public PacketChunkData() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile("");
		data.addNode("posX", chunkPos.x);
		data.addNode("posZ", chunkPos.z);
		data.addNode("chunkData", chunkFile.toByteArray());
		packedData = data.toByteArray();
	}

	@Override
	public void unpack() {
		data = new DNFile("");
		data.fromByteArray(packedData);

		chunkFile = new DNFile("");
		chunkFile.fromByteArray(data.getByteArray("chunkData"));
		chunkPos = new Position(data.getInt("posX"), data.getInt("posZ"));
	}
}
