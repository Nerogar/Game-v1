package de.nerogar.gameV1.level;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.*;
import de.nerogar.gameV1.network.*;
import de.nerogar.gameV1.object.*;
import de.nerogar.gameV1.physics.*;

public abstract class Entity {
	public Game game;
	public World world;
	public int id;
	public Bounding boundingBox = new BoundingAABB();
	public ObjectMatrix matrix = new ObjectMatrix(new Vector3d(0, 0, 0), // Position 0
			new Vector3d(0, 0, 0), // Rotation 0
			new Vector3d(1, 1, 1)); // Skalierung 1
	public ObjectMatrix serverMatrix = new ObjectMatrix(new Vector3d(0, 0, 0), // Position 0
			new Vector3d(0, 0, 0), // Rotation 0
			new Vector3d(1, 1, 1)); // Skalierung 1
	public long serverPosTime;

	public Object3D object;
	public String texture;
	public boolean saveEntity = true;
	public boolean markToRemove = false;
	public float opacity = 1;

	public static final String NODEFOLDERSAVENAME = "entities";
	private static HashMap<String, Class<? extends Entity>> entityList = new HashMap<String, Class<? extends Entity>>();

	public Entity(Game game, World world, ObjectMatrix matrix) {
		this.game = game;
		this.world = world;
		this.matrix = matrix;
		if (world != null && world.serverWorld) setID();
	}

	private void setID() {
		world.maxEntityID++;
		id = world.maxEntityID;
	}

	public void setObject(String objectName, String textureName) {
		if (world.serverWorld) return;
		if (objectName != "") {
			object = Object3DBank.instance.getObject(objectName);
		} else {
			object = null;
		}
		texture = textureName;
	}

	public void setSprite(float size, String textureName) {
		if (world.serverWorld) return;
		object = new ObjectSprite(size, world.player.camera);
		texture = textureName;
	}

	public World getWorld() {
		return world;
	}

	public void update(float time, ArrayList<EntityPacket> packets) {
		if (world.serverWorld) {
			updateServer(time, packets);
		} else {
			updateClient(time, packets);
		}
	}

	public abstract void updateServer(float time, ArrayList<EntityPacket> packets);

	public abstract void updateClient(float time, ArrayList<EntityPacket> packets);

	public Bounding getBoundingBox() {

		if (boundingBox instanceof BoundingAABB) return matrix.getTransformedAABB((BoundingAABB) boundingBox);
		if (boundingBox instanceof BoundingCircle) return matrix.getTransformedCircle((BoundingCircle) boundingBox);

		// getTransformedOBB fehlt

		return new BoundingAABB();
	}

	public BoundingAABB getAABB() {

		return PhysicHelper.toAABB(getBoundingBox());

	}

	public void move(Vector3d pos) {
		matrix.setPosition(pos);
		broadcastObjectMatrix();
	}

	public void move(Vector3d pos, Vector3d rot) {
		matrix.setPosition(pos);
		matrix.setRotation(rot);
		broadcastObjectMatrix();
	}

	private void broadcastObjectMatrix() {
		if (world.serverWorld) {
			EntityPacketMove moveEntityPacket = new EntityPacketMove();
			moveEntityPacket.objectMatrix = matrix;
			moveEntityPacket.entityID = id;
			world.server.broadcastData(moveEntityPacket);
		}
	}

	public void load(DNNodePath folder) {
		matrix.fromFloatArray(folder.getFloatArray("om"));
		id = folder.getInt("id");

		loadProperties(folder);
	}

	public void save(DNNodePath folder) {
		folder.addString("type", getNameTag());
		folder.addFloat("om", matrix.toFloatArray());
		folder.addInt("id", id);

		saveProperties(folder);
	}

	public abstract void saveProperties(DNNodePath folder);

	public abstract void loadProperties(DNNodePath folder);

	public void broadcastPropertyUpdates() {
		if (world.serverWorld) {
			EntityPacketUpdate updateEntityPacket = new EntityPacketUpdate();
			DNNodePath entityData = new DNNodePath(EntityPacketUpdate.ENTITY_DATA_PATHNAME);
			saveProperties(entityData);
			updateEntityPacket.entityData = entityData;
			updateEntityPacket.entityID = id;
			world.server.broadcastData(updateEntityPacket);
		}
	}

	public void render() {
		// BoundingRender.renderAABB((BoundingAABB)getBoundingBox(), 0x00FF00);
		if (GameOptions.instance.getBoolOption("debug")) {
			displayBoundingBox(getBoundingBox(), 0x00FF00);
		}

		if (object != null) {
			object.render(matrix, texture, opacity);
		}
	}

	public void displayBoundingBox(Bounding b, int color) {

		if (b instanceof BoundingAABB) BoundingRender.renderAABB((BoundingAABB) b, color);
		if (b instanceof BoundingCircle) BoundingRender.renderBall((BoundingCircle) b, color);

		// renderOBB fehlt

	}

	public abstract void interact(); // wenn andere entities oder objekte mit dieser entity interagieren

	public abstract void click(int key);// klick mit der maus, macht sinn, oder?

	public abstract String getNameTag();

	public abstract void init(World world);

	// register der Entities, wird zum laden/speichern gebraucht
	public static void registerEntity(Entity entity) {
		entityList.put(entity.getNameTag(), (Class<? extends Entity>) entity.getClass());
	}

	public void remove() {
		markToRemove = true;
		if (world.serverWorld) {
			PacketRemoveEntity removePacket = new PacketRemoveEntity();
			removePacket.id = id;
			world.server.broadcastData(removePacket);
		}
	}

	public static Entity getEntity(Game game, World world, String tagName) {
		if (tagName == null) return null;
		try {
			Constructor<? extends Entity> contructor = entityList.get(tagName).getConstructor(new Class[] { Game.class, World.class, ObjectMatrix.class });
			Entity entity = contructor.newInstance(game, world, new ObjectMatrix());
			return entity;
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void initEntityList(Game game) {
		ObjectMatrix objectMatrix = new ObjectMatrix();
		registerEntity(new EntityTree(game, null, objectMatrix));
		registerEntity(new EntityHouse(game, null, objectMatrix));
		registerEntity(new EntityHouseBlue(game, null, objectMatrix));
		registerEntity(new EntityHouseGreen(game, null, objectMatrix));
		registerEntity(new EntityHouseOrange(game, null, objectMatrix));
		registerEntity(new EntityHousePink(game, null, objectMatrix));
		registerEntity(new EntityHouseRed(game, null, objectMatrix));
		registerEntity(new EntityShrine(game, null, objectMatrix));
		registerEntity(new EntityTestparticle(game, null, objectMatrix));
		registerEntity(new EntityHut(game, null, objectMatrix));
		registerEntity(new EntityTestSoldier(game, null, objectMatrix));
		registerEntity(new EntitySpawnPlatform(game, null, objectMatrix));
		registerEntity(new EntityWood(game, null, objectMatrix));
	}

}
