package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;

public class PacketExitMultiplayerLobby extends Packet {

	public PacketExitMultiplayerLobby() {
		channel = LOBBY_CHANNEL;
	}

	@Override
	public void pack() {
		data = new DNFile();
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
	}

	@Override
	public String getName() {
		return "ExitMultiplayerLobby";
	}
}
