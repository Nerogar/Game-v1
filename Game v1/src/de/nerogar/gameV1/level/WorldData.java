package de.nerogar.gameV1.level;

import java.io.File;

import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;

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
			DNFile worldData = new DNFile(dirname + saveName + FILENAME);
			worldData.load();

			world.maxEntityID = worldData.getInt("maxEntityID");
			levelName = worldData.getString("levelName");
			seed = worldData.getLong("seed");
			return true;
		}
		return false;
	}

	public void save() {
		if (!new File(dirname + saveName + FILENAME).exists()) {
			new File(dirname + saveName).mkdirs();
		}

		DNFile worldData = new DNFile(dirname + saveName + FILENAME);

		worldData.addNode("maxEntityID", world.maxEntityID);
		worldData.addNode("levelName", levelName);
		worldData.addNode("seed", seed);

		worldData.save();
	}
}
