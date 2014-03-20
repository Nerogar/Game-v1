package de.nerogar.gameV1.level;

import static org.lwjgl.opengl.GL20.*;

import java.util.*;
import java.util.Map.Entry;

import de.nerogar.gameV1.*;
import de.nerogar.gameV1.graphics.Shader;
import de.nerogar.gameV1.graphics.ShaderBank;
import de.nerogar.gameV1.network.*;
import de.nerogar.gameV1.physics.CollisionComparer;
import de.nerogar.gameV1.physics.Ray;

public class EntityList {
	public HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
	public ArrayList<Entity> tempEntities = new ArrayList<Entity>();
	public ArrayList<Entity> newEntities = new ArrayList<Entity>();
	public ArrayList<Entity> newTempEntities = new ArrayList<Entity>();
	private boolean updateInProgress = false;
	public int maxID;
	private CollisionComparer collisionComparer;
	private Game game;
	private World world;
	private Shader entityShader;

	public EntityList(Game game, World world) {
		this.game = game;
		this.world = world;
		setupShaders();
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
		boolean removed = false;

		Iterator<Entry<Integer, Entity>> iterator = entities.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, Entity> entry = iterator.next();
			if (entry.getValue() == null || entry.getValue().markToRemove) {
				iterator.remove();
				removed = true;
			}
		}

		for (int i = tempEntities.size() - 1; i >= 0; i--) {
			if (tempEntities.get(i) == null || tempEntities.get(i).markToRemove) {
				tempEntities.remove(i);
				removed = true;
			}
		}

		if (removed) world.recalcFactionEntities();
	}

	public Entity[] getEntitiesInSight(Ray ray) {
		return collisionComparer.getEntitiesInRay(ray);
	}

	public void update(Game game, ArrayList<Packet> receivedPackets, float time) {
		//for (int i = 0; i < entities.size(); i++) {
		//	entities.get(i).update(Timer.instance.delta / 1000F);
		//}
		removeNullEntities();
		updateInProgress = true;

		HashMap<Integer, ArrayList<EntityPacket>> sortedPackets = new HashMap<Integer, ArrayList<EntityPacket>>();

		for (int id : entities.keySet()) {
			sortedPackets.put(id, new ArrayList<EntityPacket>());
		}

		if (receivedPackets != null) {
			for (Packet packet : receivedPackets) {
				EntityPacket entityPacket = (EntityPacket) packet;

				if (packet instanceof EntityPacketMove) {
					EntityPacketMove moveEntityPacket = (EntityPacketMove) packet;
					updateEntityPosition(moveEntityPacket);
				} else if (packet instanceof EntityPacketUpdate) {
					EntityPacketUpdate updateEntityPacket = (EntityPacketUpdate) packet;
					updateEntityProperties(updateEntityPacket);
				} else {
					ArrayList<EntityPacket> entityPacketList = sortedPackets.get(entityPacket.entityID);
					if (entityPacketList != null) {
						entityPacketList.add(entityPacket);
					}
				}

			}
		}

		for (Entity e : entities.values()) {
			//float time = game.timer.delta / 1000F;
			e.update(time, sortedPackets.get(e.id));
			if (e instanceof EntityFighting) {
				((EntityFighting) e).aiContainer.update(time);
			}
		}

		for (int i = 0; i < tempEntities.size(); i++) {
			tempEntities.get(i).update(time, null);
		}

		updateInProgress = false;
		addNewEntities();

		world.collisionComparer.updateGrid();
		world.collisionComparer.compare();
	}

	private void updateEntityPosition(EntityPacketMove moveEntityPacket) {
		if (!world.serverWorld) {
			Entity tempEntity = entities.get(moveEntityPacket.entityID);
			if (moveEntityPacket.includeScale) {
				tempEntity.matrix = moveEntityPacket.objectMatrix;
			} else {
				tempEntity.matrix.setPosition(moveEntityPacket.objectMatrix.position);
				tempEntity.matrix.setRotation(moveEntityPacket.objectMatrix.rotation);
			}
		}
	}

	private void updateEntityProperties(EntityPacketUpdate updateEntityPacket) {
		if (!world.serverWorld) {
			Entity tempEntity = entities.get(updateEntityPacket.entityID);

			tempEntity.loadProperties(updateEntityPacket.entityData);
		}
	}

	public void unloadAll() {
		entities.clear();
	}

	public void setCollisionComparer(CollisionComparer collisionComparer) {
		this.collisionComparer = collisionComparer;
	}

	public void render(double time, Position loadPosition, int maxChunkRenderDistance) {
		entityShader.activate();
		updateEntityShader();

		for (Entity entity : world.entityList.entities.values()) {
			renderEntity(loadPosition, maxChunkRenderDistance, entity);
		}

		for (Entity entity : world.entityList.tempEntities) {
			renderEntity(loadPosition, maxChunkRenderDistance, entity);
		}

		if (world.player != null) world.player.renderInEntityList();
		
		entityShader.deactivate();
	}

	private void renderEntity(Position loadPosition, int maxChunkRenderDistance, Entity entity) {
		if (MathHelper.roundUpToInt(entity.matrix.position.getX(), Chunk.CHUNKSIZE) >= (loadPosition.x + 1) - maxChunkRenderDistance * Chunk.CHUNKSIZE && MathHelper.roundDownToInt(entity.matrix.position.getX(), Chunk.CHUNKSIZE) <= loadPosition.x + maxChunkRenderDistance * Chunk.CHUNKSIZE) {
			if (MathHelper.roundUpToInt(entity.matrix.position.getZ(), Chunk.CHUNKSIZE) >= (loadPosition.z + 1) - maxChunkRenderDistance * Chunk.CHUNKSIZE && MathHelper.roundDownToInt(entity.matrix.position.getZ(), Chunk.CHUNKSIZE) <= loadPosition.z + maxChunkRenderDistance * Chunk.CHUNKSIZE) {
				entity.render();
			}
		}
	}

	private void updateEntityShader() {
		glUniform1f(entityShader.uniforms.get("time"), (System.nanoTime() / 1000000000f));
		glUniformMatrix3(entityShader.uniforms.get("matEyeSpace"), false, world.player.camera.matBuffer);
	}

	public void setupShaders() {
		ShaderBank.instance.createShaderProgramm("entity");
		entityShader = ShaderBank.instance.getShader("entity");

		entityShader.setVertexShader("res/shaders/entityShader.vert");
		entityShader.setFragmentShader("res/shaders/entityShader.frag");
		entityShader.compile();

		entityShader.activate();

		entityShader.uniforms.put("time", glGetUniformLocation(entityShader.shaderHandle, "time"));
		entityShader.uniforms.put("matEyeSpace", glGetUniformLocation(entityShader.shaderHandle, "matEyeSpace"));

		entityShader.deactivate();
	}
}
