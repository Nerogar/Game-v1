package de.nerogar.gameV1.network;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class PacketExitGame extends Packet {

	public PacketExitGame() {
		channel = WORLD_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile("");
		packedData = data.toByteArray();
	}

	@Override
	public void unpack() {
		data = new DNFile("");
		data.fromByteArray(packedData);
	}

	@Override
	public String getName() {
		return "ExitGame";
	}
}
