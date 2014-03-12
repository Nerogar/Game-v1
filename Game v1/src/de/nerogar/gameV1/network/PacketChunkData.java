package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.gameV1.level.Position;

public class PacketChunkData extends Packet {

	public DNFile chunkFile;
	public Position chunkPos;

	public PacketChunkData() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();
		data.addInt("posX", chunkPos.x);
		data.addInt("posZ", chunkPos.z);
		data.addByte("chunkData", chunkFile.toByteArray());
		packedData = data.toByteArray();
	}

	@Override
	public void unpack() {
		data = new DNFile();
		chunkFile = new DNFile();
		try {
			data.fromByteArray(packedData);
			chunkFile.fromByteArray(data.getByteArray("chunkData"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		chunkPos = new Position(data.getInt("posX"), data.getInt("posZ"));
	}

	@Override
	public String getName() {
		return "ChunkData";
	}
}
