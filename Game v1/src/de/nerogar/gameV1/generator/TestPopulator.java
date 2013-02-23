package de.nerogar.gameV1.generator;

import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class TestPopulator extends Populator {

	@Override
	public void execute(Chunk chunk) {
		//chunk.setHeight((int) (Math.random() * 64), (int) (Math.random() * 64), 50);

		for (int i = 0; i < (int) (Math.random() * 50); i++) {

			float x = (float) (Math.random() * Chunk.CHUNKSIZE);
			float z = (float) (Math.random() * Chunk.CHUNKSIZE);

			if (chunk.getLocalTile((int) Math.floor(x), (int) Math.floor(z)) != Tile.TILE_WATER) {
				float y = (float) chunk.heightMap[(int) x][(int) z];
				float rotation = (float) (Math.random() * 360);
				chunk.spawnEntityLocal(new EntityTree(chunk.world.game, new ObjectMatrix(new Vector3d(x, y, z), new Vector3d(0, 0, rotation), new Vector3d(1, 1, 1))));
			}

		}

		if (Math.random() * 15 < 1) {
			float x = (float) (Math.random() * Chunk.CHUNKSIZE);
			float z = (float) (Math.random() * Chunk.CHUNKSIZE);

			if (chunk.getLocalTile((int) Math.floor(x), (int) Math.floor(z)) != Tile.TILE_WATER) {
				float y = (float) chunk.heightMap[(int) x][(int) z];
				float rotation = (float) (Math.random() * 360);
				chunk.spawnEntityLocal(new EntityShrine(chunk.world.game, new ObjectMatrix(new Vector3d(x, y, z), new Vector3d(0, 0, rotation), new Vector3d(1, 1, 1))));
			}
		}

		//chunk.spawnEntityLocal(new EntityTree(chunk.world.game, new ObjectMatrix(new Vector3(30, 10, 30), new Vector3(0, 0, 0), new Vector3(1, 1, 1))));
	}
}
