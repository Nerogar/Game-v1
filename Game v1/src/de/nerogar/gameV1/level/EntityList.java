package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Timer;
import de.nerogar.gameV1.physics.CollisionComparer;
import de.nerogar.gameV1.physics.Line;

public class EntityList {
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Integer> entityID = new ArrayList<Integer>();
	public int maxID;
	CollisionComparer collisionComparer;
	Game game;

	public EntityList(Game game) {
		this.game = game;
	}

	public Entity getEntity(int id) {
		return entities.get(entityID.indexOf(id));
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
		entityID.add(maxID + 1);
		entity.game = game;
	}

	public void removeNullEntities() {
		for (int i = entities.size() - 1; i >= 0; i--) {
			if (entities.get(i) == null) {
				entities.remove(i);
			}
		}
	}

	public ArrayList<Entity> getEntitiesInSight(Line ray) {
		return collisionComparer.getEntitiesInRay(ray);
	}

	public void update(Game game) {
		//for (int i = 0; i < entities.size(); i++) {
		//	entities.get(i).update(Timer.instance.delta / 1000F);
		//}
		for (Entity e : entities) {
			e.update(Timer.instance.delta / 1000F);
		}

		game.world.collisionComparer.updateGrid();
		game.world.collisionComparer.compare();
	}

	public void unloadAll() {
		entities.clear();
	}

	public void setCollisionComparer(CollisionComparer collisionComparer) {
		this.collisionComparer = collisionComparer;
	}

	public void render(Position loadPosition, int maxChunkRenderDistance) {
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);

			if (MathHelper.roundUpToInt(entity.matrix.position.getX(), Chunk.CHUNKSIZE) >= (loadPosition.x + 1) - maxChunkRenderDistance * Chunk.CHUNKSIZE && MathHelper.roundDownToInt(entity.matrix.position.getX(), Chunk.CHUNKSIZE) <= loadPosition.x + maxChunkRenderDistance * Chunk.CHUNKSIZE) {
				if (MathHelper.roundUpToInt(entity.matrix.position.getZ(), Chunk.CHUNKSIZE) >= (loadPosition.z + 1) - maxChunkRenderDistance * Chunk.CHUNKSIZE && MathHelper.roundDownToInt(entity.matrix.position.getZ(), Chunk.CHUNKSIZE) <= loadPosition.z + maxChunkRenderDistance * Chunk.CHUNKSIZE) {
					entity.render();
				}
			}
		}
	}
}