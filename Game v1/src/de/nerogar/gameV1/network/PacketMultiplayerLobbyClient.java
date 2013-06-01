package de.nerogar.gameV1.network;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class PacketMultiplayerLobbyClient extends Packet {
	public boolean readyState;

	public PacketMultiplayerLobbyClient() {
		channel = LOBBY_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile("");
		data.addNode("readyState", readyState);

		packedData = data.toByteArray();

	}

	@Override
	public void unpack() {
		data = new DNFile("");
		data.fromByteArray(packedData);

		readyState = data.getBoolean("readyState");
	}

	@Override
	public String getName() {
		return "MultiplayerLobyClient";
	}
}
