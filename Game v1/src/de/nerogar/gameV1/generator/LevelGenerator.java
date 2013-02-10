package de.nerogar.gameV1.generator;

import java.util.ArrayList;

import de.nerogar.gameV1.image.Noise;
import de.nerogar.gameV1.level.Chunk;
import de.nerogar.gameV1.level.Land;
import de.nerogar.gameV1.level.Tile;

public class LevelGenerator {
	private Noise[] noise;
	private final int GENERATESIZE = Chunk.GENERATESIZE;
	private final int REPEATOFFSET = 10000;
	private long seed;
	private float[][][] values = new float[2][GENERATESIZE][GENERATESIZE];
	private ArrayList<Populator> populators = new ArrayList<Populator>();

	public LevelGenerator(Land land) {
		this.seed = land.seed;
		addDefaultPopulators();
	}

	private void addDefaultPopulators() {
		addPopulator(new TestPopulator());
	}

	public Chunk generateLevel(Chunk chunk, int x, int y) {
		noise = new Noise[2];
		long biomeSize = Long.MAX_VALUE - seed;
		noise[0] = new Noise(GENERATESIZE, 255, 5, 32, biomeSize + (REPEATOFFSET * x) + y, true, REPEATOFFSET);
		noise[1] = new Noise(GENERATESIZE, 255, 3, 16, seed + (REPEATOFFSET * x) + y, true, REPEATOFFSET);

		for (int i = 0; i < GENERATESIZE; i++) {
			for (int j = 0; j < GENERATESIZE; j++) {

				float m = (float) (noise[0].getNoisePoint(i, j));
				values[0][i][j] = (float) ((Math.cos(m * 8.0f) + 1.0f) / 2.0f);
				values[0][i][j] = values[0][i][j] * 9.0f - 8.0f;
				values[0][i][j] = (values[0][i][j] < 0.0f ? 1.0f : 0.0f);

				m = (float) (noise[1].getNoisePoint(i, j)) * 5;
				values[1][i][j] = m;

				//float n = values[0][i][j] * values[1][i][j];
				float n = values[1][i][j];
				chunk.heightMap[i][j] = n;
			}
		}

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
		populate(chunk);
		chunk.updateMaps();
		return chunk;
	}

	private void populate(Chunk chunk) {
		for (int i = 0; i < populators.size(); i++) {
			populators.get(i).execute(chunk);
		}
	}

	public void addPopulator(Populator populator) {
		populators.add(populator);
	}

}