package de.nerogar.gameV1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.gameV1.internalServer.Faction;
import de.nerogar.gameV1.internalServer.InternalServer;
import de.nerogar.gameV1.level.WorldData;
import de.nerogar.gameV1.network.Client;
import de.nerogar.gameV1.network.Server;

public class SaveProvider {

	private String dirname = "saves/";
	private File[] saves;

	public void updateSaves() {
		File dir = new File(dirname);

		if (!dir.exists()) {
			dir.mkdirs();
			saves = new File[0];
		} else {
			File[] savesBuffer;
			savesBuffer = dir.listFiles();
			if (savesBuffer == null) {
				System.out.println("Could not load Saves");
				return;
			}
			ArrayList<File> tempSaves = new ArrayList<File>();

			for (File file : savesBuffer) {
				if (file.isDirectory()) {
					if (new File(file.getPath() + WorldData.FILENAME).exists()) {
						tempSaves.add(file);
					}
				}
			}

			saves = new File[tempSaves.size()];
			for (int i = 0; i < saves.length; i++) {
				saves[i] = tempSaves.get(i);
			}
		}
	}

	public String[] getSavesAsStrings() {
		String[] buffer = new String[saves.length];
		for (int i = 0; i < saves.length; i++) {
			DNFile tempWorldFile = new DNFile();
			try {
				tempWorldFile.load(saves[i] + WorldData.FILENAME);
			} catch (IOException e) {
				e.printStackTrace();
			}

			String saveName = tempWorldFile.getString("levelName") + " (" + saves[i].getName() + ")";

			buffer[i] = saveName;
		}
		return buffer;
	}

	public void loadWorld(Game game, int index) {
		//game.world.initiateWorld(saves[index].getName());

		Server server = new Server(GameOptions.instance.STANDARDPORT);
		Client client = new Client("localhost", server.port);
		
		InternalServer internalServer = new InternalServer(game, server);
		game.internalServer = internalServer;
		internalServer.initiateWorld(saves[index].getName(), new Faction[] { Faction.factionBlueServer });
		game.world.initiateClientWorld(client, Faction.factionBlueClient, null);
	}

	public void loadWorld(Game game, String name, long seed) {
		//game.world.initiateWorld(name, seed);

		Server server = new Server(GameOptions.instance.STANDARDPORT);
		Client client = new Client("localhost", server.port);

		InternalServer internalServer = new InternalServer(game, server);
		game.internalServer = internalServer;
		internalServer.initiateWorld(name, seed, new Faction[] { Faction.factionBlueServer });
		game.world.initiateClientWorld(client, Faction.factionBlueClient, null);
	}

	public void renameWorld(int index, String newName) {
		DNFile tempWorldFile = new DNFile();
		try {
			tempWorldFile.load(saves[index] + WorldData.FILENAME);
			tempWorldFile.addString("levelName", newName);
			tempWorldFile.save(saves[index] + WorldData.FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean deleteWorld(int index) {
		if (!saves[index].exists()) return false;
		boolean success = deleteFolder(saves[index]);
		updateSaves();
		return success;
	}

	public static boolean deleteFolder(File folder) {
		boolean success = true;
		File[] files = folder.listFiles();
		if (files != null) { //some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					success = (success && f.delete());
				}
			}
		}
		success = (success && folder.delete());
		return success;
	}
}
