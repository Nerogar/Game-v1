package de.nerogar.gameV1.network;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class PacketMultiplayerLobbyInfo extends Packet {
	public String[] playerNames;
	public boolean[] playerReadyStates;

	public PacketMultiplayerLobbyInfo() {
		channel = LOBBY_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile("");

		data.addNode("playernames", playerNames);
		data.addNode("playerreadyStates", playerReadyStates);

		packedData = data.toByteArray();
	}

	@Override
	public void unpack() {
		data = new DNFile("");
		data.fromByteArray(packedData);

		playerNames = data.getStringArray("playernames");
		playerReadyStates = data.getBooleanArray("playerreadyStates");
	}
}
