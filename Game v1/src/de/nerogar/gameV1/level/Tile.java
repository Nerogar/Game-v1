package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Vector2d;
import de.nerogar.gameV1.graphics.SpriteSheet;

public abstract class Tile {
	public static final TileDirt TILE_DIRT = new TileDirt();
	public static final TileGrass TILE_GRASS = new TileGrass();
	public static final TileWater TILE_WATER = new TileWater();
	public static Tile[] tileList = new Tile[256];

	public Vector2d texturePos1, texturePos2;
	public int id;

	public Tile() {

	}

	public abstract String getTextureName();

	public void setTextureCoords(Vector2d texturePos1, Vector2d texturePos2) {
		this.texturePos1 = texturePos1;
		this.texturePos2 = texturePos2;
	}

	public static Tile getTileByID(int id) {
		return tileList[id];
	}

	private static void registerTile(Tile tile, int id) {
		tile.id = id;
		tileList[id] = tile;
	}

	public static void initTileList() {
		registerTile(TILE_DIRT, 0);
		registerTile(TILE_GRASS, 1);
		registerTile(TILE_WATER, 2);

		//compile Textures
		SpriteSheet terrainSheet = new SpriteSheet("terrainSheet");

		for (int i = 0; i < tileList.length; i++) {
			if (tileList[i] != null) {
				terrainSheet.addTexture(tileList[i].getTextureName());
			}
		}
		terrainSheet.compile();

		for (int i = 0; i < tileList.length; i++) {
			if (tileList[i] != null) {
				tileList[i].setTextureCoords(terrainSheet.getTexturePosition1(tileList[i].getTextureName()), terrainSheet.getTexturePosition2(tileList[i].getTextureName()));
			}
		}
	}
}
