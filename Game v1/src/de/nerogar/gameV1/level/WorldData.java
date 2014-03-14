package de.nerogar.gameV1.level;

import java.io.File;
import java.io.IOException;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.internalServer.Faction;

public class WorldData {
	public String levelName;
	public String saveName;
	public long seed;
	public World world;
	private String dirname = "saves/";
	public static final String FILENAME = "/WorldData.mwd"; // mwd -> Master World Data

	public WorldData(World world, String saveName) {
		this.world = world;
		this.saveName = saveName;
		this.levelName = saveName;
	}

	public boolean load() {

		if (new File(dirname + saveName + FILENAME).exists()) {
			DNFile worldData = new DNFile();
			try {
				worldData.load(dirname + saveName + FILENAME);
			} catch (IOException e) {
				e.printStackTrace();
			}

			world.maxEntityID = worldData.getInt("maxEntityID");
			levelName = worldData.getString("levelName");
			seed = worldData.getLong("seed");

			int[] factionIDs = worldData.getIntArray("factions.IDs");
			world.factions = new Faction[factionIDs.length];

			for (int i = 0; i < factionIDs.length; i++) {
				DNNodePath factionFolder = worldData.getPath("factions." + factionIDs[i]);
				Faction f = Faction.getServerFaction(factionIDs[i]);
				f.load(factionFolder);
				world.factions[i] = f;
			}

			return true;
		}
		return false;
	}

	public void save() {
		if (!new File(dirname + saveName + FILENAME).exists()) {
			new File(dirname + saveName).mkdirs();
		}

		DNFile worldData = new DNFile();

		worldData.addInt("maxEntityID", world.maxEntityID);
		worldData.addString("levelName", levelName);
		worldData.addLong("seed", seed);

		int[] factionIDs = new int[world.factions.length];
		for (int i = 0; i < factionIDs.length; i++) {
			Faction f = world.factions[i];
			DNNodePath factionFolder = worldData.getPath("factions." + f.id);
			f.save(factionFolder);
			factionIDs[i] = f.id;
		}

		worldData.addInt("factions.IDs", factionIDs);

		try {
			worldData.save(dirname + saveName + FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
