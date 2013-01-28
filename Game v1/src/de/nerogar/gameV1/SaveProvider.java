package de.nerogar.gameV1;

import java.io.File;
import java.util.ArrayList;

import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.level.WorldData;

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
			DNFile tempWorldFile = new DNFile(saves[i] + WorldData.FILENAME);
			tempWorldFile.load();

			String saveName = tempWorldFile.getString("levelName") + " (" + saves[i].getName() + ")";

			buffer[i] = saveName;
		}
		return buffer;
	}

	public void loadWorld(Game game, int index) {
		game.world.initiateWorld(saves[index].getName());
	}

	public void renameWorld(int index, String newName) {
		DNFile tempWorldFile = new DNFile(saves[index] + WorldData.FILENAME);
		tempWorldFile.load();
		tempWorldFile.addNode("levelName", newName);
		tempWorldFile.save();
	}
}