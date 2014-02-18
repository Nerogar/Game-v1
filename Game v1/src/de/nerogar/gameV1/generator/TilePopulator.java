package de.nerogar.gameV1.generator;

import de.nerogar.gameV1.level.Chunk;
import de.nerogar.gameV1.level.Tile;

public class TilePopulator extends Populator {

	@Override
	public void execute(Chunk chunk) {
		for (int i = 0; i < Chunk.CHUNKSIZE; i++) {
			for (int j = 0; j < Chunk.CHUNKSIZE; j++) {
				if (chunk.heightMap[i][j] < 0.6) {
					chunk.tileMap[i][j] = Tile.TILE_WATER.id;
				} else if (chunk.heightMap[i][j] < 1.5) {
					chunk.tileMap[i][j] = Tile.TILE_GRASS.id;
				} else {
					chunk.tileMap[i][j] = Tile.TILE_DIRT.id;
				}
			}
		}
	}
}
