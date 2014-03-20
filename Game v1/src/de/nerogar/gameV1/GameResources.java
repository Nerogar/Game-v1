package de.nerogar.gameV1;

public class GameResources {

	public int[] resources = new int[2];

	public static int WOOD_ID = 0;
	public static int STONE_ID = 1;

	public GameResources() {
		this(0, 0);
	}

	public GameResources(int wood, int stone) {
		resources[WOOD_ID] = wood;
		resources[STONE_ID] = stone;
	}

	public int getResource(int id) {
		return resources[id];
	}
}
