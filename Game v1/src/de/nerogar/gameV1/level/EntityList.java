package de.nerogar.gameV1.level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.network.Packet;
import de.nerogar.gameV1.network.PacketEntity;
import de.nerogar.gameV1.physics.CollisionComparer;
import de.nerogar.gameV1.physics.Ray;

public class EntityList {
	public HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
	public ArrayList<Entity> tempEntities = new ArrayList<Entity>();
	public ArrayList<Entity> newEntities = new ArrayList<Entity>();
	public ArrayList<Entity> newTempEntities = new ArrayList<Entity>();
	private boolean updateInProgress = false;
	public int maxID;
	CollisionComparer collisionComparer;
	Game game;
	World world;

	public EntityList(Game game, World world) {
		this.game = game;
		this.world = world;
	}

	public void addEntity(Entity entity, World world) {
		if (!updateInProgress) {
			entities.put(entity.id, entity);
			entity.game = game;
			entity.init(world);
		} else {
			newEntities.add(entity);
			entity.world = world;
		}
	}

	public void addTempEntity(Entity entity, World world) {
		if (!updateInProgress) {
			tempEntities.add(entity.id, entity);
			entity.game = game;
			entity.init(world);
		} else {
			newTempEntities.add(entity);
			entity.world = world;
		}
	}

	private void addNewEntities() {
		for (int i = 0; i < newEntities.size(); i++) {
			addEntity(newEntities.get(i), newEntities.get(i).world);
		}

		for (int i = 0; i < newTempEntities.size(); i++) {
			addTempEntity(newTempEntities.get(i), newTempEntities.get(i).world);
		}

		newEntities = new ArrayList<Entity>();
		newTempEntities = new ArrayList<Entity>();
	}

	public void removeNullEntities() {
		Iterator<Entry<Integer, Entity>> iterator = entities.entrySet().iterator();
		while (iterator.hasNext()) {
		    Map.Entry<Integer, Entity> entry = iterator.next();
		    if (entry.getValue() == null || entry.getValue().markToRemove) {
		    	iterator.remove();
		    }
		}

		for (int i = tempEntities.size() - 1; i >= 0; i--) {
			if (tempEntities.get(i) == null || tempEntities.get(i).markToRemove) {
				tempEntities.remove(i);
			}
		}
	}

	public Entity[] getEntitiesInSight(Ray ray) {
		return collisionComparer.getEntitiesInRay(ray);
	}

	public void update(Game game, ArrayList<Packet> receivedPackets) {
		//for (int i = 0; i < entities.size(); i++) {
		//	entities.get(i).update(Timer.instance.delta / 1000F);
		//}
		removeNullEntities();
		updateInProgress = true;

		HashMap<Integer, ArrayList<PacketEntity>> sortedPackets = new HashMap<Integer, ArrayList<PacketEntity>>();

		for (int id : entities.keySet()) {
			sortedPackets.put(id, new ArrayList<PacketEntity>());
		}

		if (receivedPackets != null) {
			for (Packet packet : receivedPackets) {
				PacketEntity entityPacket = (PacketEntity) packet;

				ArrayList<PacketEntity> entityPacketList = sortedPackets.get(entityPacket.entityID);
				if (entityPacketList != null) {
					entityPacketList.add(entityPacket);
				}
			}
		}

		for (Entity e : entities.values()) {
			e.update(game.timer.delta / 1000F, sortedPackets.get(e.id));
		}

		for (int i = 0; i < tempEntities.size(); i++) {
			tempEntities.get(i).update(game.timer.delta / 1000F, null);
		}

		updateInProgress = false;
		addNewEntities();

		world.collisionComparer.updateGrid();
		world.collisionComparer.compare();
	}

	public void unloadAll() {
		entities.clear();
	}

	public void setCollisionComparer(CollisionComparer collisionComparer) {
		this.collisionComparer = collisionComparer;
	}

	public void render(Position loadPosition, int maxChunkRenderDistance) {
		for (Entity entity : world.entityList.entities.values()) {
			renderEntity(loadPosition, maxChunkRenderDistance, entity);
		}

		for (Entity entity : world.entityList.tempEntities) {
			renderEntity(loadPosition, maxChunkRenderDistance, entity);
		}
	}

	private void renderEntity(Position loadPosition, int maxChunkRenderDistance, Entity entity) {
		if (MathHelper.roundUpToInt(entity.matrix.position.getX(), Chunk.CHUNKSIZE) >= (loadPosition.x + 1) - maxChunkRenderDistance * Chunk.CHUNKSIZE && MathHelper.roundDownToInt(entity.matrix.position.getX(), Chunk.CHUNKSIZE) <= loadPosition.x + maxChunkRenderDistance * Chunk.CHUNKSIZE) {
			if (MathHelper.roundUpToInt(entity.matrix.position.getZ(), Chunk.CHUNKSIZE) >= (loadPosition.z + 1) - maxChunkRenderDistance * Chunk.CHUNKSIZE && MathHelper.roundDownToInt(entity.matrix.position.getZ(), Chunk.CHUNKSIZE) <= loadPosition.z + maxChunkRenderDistance * Chunk.CHUNKSIZE) {
				entity.render();
			}
		}
	}
}
