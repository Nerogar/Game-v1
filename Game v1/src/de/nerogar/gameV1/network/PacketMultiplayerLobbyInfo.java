package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketMultiplayerLobbyInfo extends Packet {
	public String[] playerNames;
	public boolean[] playerReadyStates;

	public PacketMultiplayerLobbyInfo() {
		channel = LOBBY_CHANNEL;
	}

	@Override
	public void pack() {

		data = new DNFile();
		data.addString("playernames", playerNames);
		data.addBoolean("playerReadyStates", playerReadyStates);

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

		playerNames = data.getStringArray("playernames");
		playerReadyStates = data.getBooleanArray("playerReadyStates");

	}

	@Override
	public String getName() {
		return "MultiplayerLobbyInfo";
	}
}
