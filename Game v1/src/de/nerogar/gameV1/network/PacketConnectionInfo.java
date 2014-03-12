package de.nerogar.gameV1.network;

import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;

public class PacketConnectionInfo extends Packet {
	public String version;
	public String username;

	@Override
	public void pack() {
		data = new DNFile();
		version = Game.version;
		username = GameOptions.instance.getOption("playerName");

		data.addString("version", version);
		data.addString("username", username);

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

		version = data.getString("version");
		username = data.getString("username");
	}

	@Override
	public String getName() {
		return "ConnectionInfo";
	}
}
