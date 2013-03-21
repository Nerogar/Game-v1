package de.nerogar.gameV1;

public class GameResources {

	public int[] resources = new int[3];
	
	public static int CURRENCY_ID = 0;
	public static int WOOD_ID = 1;
	public static int STONE_ID = 2;
	
	public GameResources() {
		this(5000, 3000, 2000);
	}
	
	public GameResources(int i, int j, int k) {
		resources[CURRENCY_ID] = i;
		resources[WOOD_ID] = j;
		resources[STONE_ID] = k;
	}

	public int getResource(int id) {
		return resources[id];
	}
	
	
	
}
