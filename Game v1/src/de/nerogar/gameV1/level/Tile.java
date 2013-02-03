package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Vector2d;

public abstract class Tile {
	public static final TileWater TILE_WATER = new TileWater();
	public static final TileGrass TILE_GRASS = new TileGrass();

	public Vector2d texturePos1, texturePos2;

	public Tile() {

	}

	public abstract String gettextureName();

	public void setTextureCoords(Vector2d texturePos1, Vector2d texturePos2) {
		this.texturePos1 = texturePos1;
		this.texturePos2 = texturePos2;

	}
	
	public static void initTileList(){
		
	}
}
