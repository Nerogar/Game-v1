package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketMultiplayerLobbyClient extends Packet {
	public boolean readyState;

	public PacketMultiplayerLobbyClient() {
		channel = LOBBY_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile();
		data.addBoolean("readyState", readyState);

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

		readyState = data.getBoolean("readyState");
	}

	@Override
	public String getName() {
		return "MultiplayerLobyClient";
	}
}
