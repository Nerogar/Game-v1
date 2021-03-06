package de.nerogar.gameV1.generator;

import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.internalServer.Faction;
import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class SpawnRegionPopulator extends Populator {

	@Override
	public void execute(Chunk chunk) {
		if (chunk.chunkPosition.equals(new Position(0, 0))) {
			for (int i = 0; i < 64; i++) {
				for (int j = 0; j < 64; j++) {
					float oldHeight = chunk.getLocalHeight(i, j);
					double dist = Math.sqrt((32 - i) * (32 - i) + (32 - j) * (32 - j));
					dist = MathHelper.clamp((dist / 10f) - 1, 0, 1);
					chunk.setLocalHeight(i, j, (float) (oldHeight * dist + (1 - dist)));
					chunk.setLocalTile(i, j, Tile.TILE_GRASS);
				}
			}

			int towerCount = 6;
			float radius = 5;
			for (int i = 0; i < towerCount; i++) {
				float angle = (float) (((float) i / towerCount) * Math.PI * 2);

				Vector3d position = (new Vector3d(MathHelper.sin((float) angle), 0, MathHelper.cos(angle))).multiply(radius);

				EntityEnergyTower tower = new EntityEnergyTower(chunk.world.game, chunk.world, new ObjectMatrix(Vector3d.add(new Vector3d(32, 1, 32), position)));
				tower.faction = Faction.factionBlueServer;
				chunk.spawnEntityLocal(tower);
			}
		}
	}
}
