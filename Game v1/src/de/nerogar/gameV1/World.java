package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.gameV1.ai.*;
import de.nerogar.gameV1.generator.LevelGenerator;
import de.nerogar.gameV1.gui.GuiMain;
import de.nerogar.gameV1.gui.GuiPauseMenu;
import de.nerogar.gameV1.internalServer.InternalServer;
import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.network.*;
import de.nerogar.gameV1.physics.CollisionComparer;

public class World {
	public Game game;
	public EntityList entityList;
	public int maxEntityID = 0;
	public Land land;
	public WorldData worldData;
	public boolean isLoaded = false;
	public Position loadPosition;
	private int maxChunkRenderDistance = GameOptions.instance.getIntOption("renderdistance");
	public CollisionComparer collisionComparer;
	public Server server;
	public Client client;
	public InternalServer internalServer;
	public boolean serverWorld;
	public Player player;

	public Pathfinder pathfinder;

	public World(Game game, boolean serverWorld) {
		this.game = game;
		this.serverWorld = serverWorld;
		entityList = new EntityList(game, this);
		this.land = new Land(game, this);
		this.collisionComparer = new CollisionComparer(this);
		entityList.setCollisionComparer(collisionComparer);
		if (serverWorld) {

		} else {
			player = new Player(game, this);
		}
		pathfinder = new Pathfinder(land);
	}

	public void initiateWorld(String levelName, long seed) {
		worldData = new WorldData(this, levelName);
		worldData.seed = seed;
		initiateWorld(levelName);
	}

	public void initiateWorld(String levelName) {
		if (!serverWorld) RenderHelper.renderLoadingScreen("Lade Welt...");
		if (worldData == null) {
			worldData = new WorldData(this, levelName);
			worldData.load();
		}

		land.saveName = worldData.saveName;
		land.seed = worldData.seed;
		land.levelGenerator = new LevelGenerator(land);

		if (serverWorld) {
			loadPosition = new Position();

			land.loadAllAroundXZ(loadPosition);
			for (int i = 0; i < land.chunks.size(); i++) {
				PacketChunkData chunkData = new PacketChunkData();
				Chunk sendChunk = land.chunks.get(i);
				chunkData.chunkFile = sendChunk.getChunkFile();
				chunkData.chunkPos = sendChunk.chunkPosition;
				server.broadcastData(chunkData);
			}
		}

		isLoaded = true;
		//land.loadAllAroundXZ(loadPosition);
		System.out.println("Initiated Level: " + worldData.levelName + " / seed: " + worldData.seed);
		//ab hier kommt nur temporärer code zum hinzufügen von test-entities
		/*
				entityList.addEntity(new EntityBlockDebug(game, new ObjectMatrix(new Vector3(6, 5, 1), new Vector3(0, 0, 0), new Vector3(1, 1, 1)), 10, 1F));

				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						entityList.addEntity(new EntityHouse(game, new ObjectMatrix(new Vector3(i * 1 - 10, 0, j * 1 - 10), new Vector3(0, 0, 0), new Vector3(1, 1, 1))));
					}
				}

				entityList.addEntity(new EntityBlock(game, new ObjectMatrix(new Vector3(-6, 10, 1), new Vector3(0, 0, 0), new Vector3(1, 1, 1)), 10f, 1f));
				*/
		// Der letzte, zusätzliche Parameter ist Testweise die Skalierung (Der AABB)
	}

	public void initiateClientWorld(Client client) {
		isLoaded = true;
		this.client = client;
		player.camera.init();
		loadPosition = player.camera.getCamCenter().toPosition();
	}

	public void closeWorld() {
		//if (!serverWorld) RenderHelper.renderLoadingScreen("Speichere Welt...");

		if (serverWorld) {
			exitWorld(true);
		} else {
			PacketExitGame exitPacket = new PacketExitGame();
			client.sendPacket(exitPacket);
		}
		System.gc();
	}

	public void exitWorld(boolean save) {
		isLoaded = false;
		land.unloadAll(save);
		entityList.unloadAll();
		collisionComparer.cleanup();
		if (save) {
			worldData.save();
			worldData = null;
		}
	}

	public void updateServer() {
		ArrayList<Client> clients = server.getClients();

		for (int i = 0; i < clients.size(); i++) {
			if (clients.get(i).connectionInfo != null) {
				Client connectionClient = clients.get(i);
				ArrayList<Packet> receivedPackets = connectionClient.getData(Packet.WORLD_CHANNEL);
				if (receivedPackets != null) {
					for (Packet packet : receivedPackets) {
						processServerPackets(connectionClient, packet);
					}
				}
			}
		}

		land.loadChunksAroundXZ(loadPosition);
	}

	private void processServerPackets(Client connectionClient, Packet packet) {
		if (packet instanceof PacketExitGame) {
			PacketExitGame exitGame = (PacketExitGame) packet;
			closeWorld();
			server.broadcastData(exitGame);

			internalServer.stopServer();
		} else if (packet instanceof PacketBuildHouse) {
			PacketBuildHouse buildingData = (PacketBuildHouse) packet;

			Entity newBuilding = Entity.getEntity(game, this, BuildingBank.getBuildingName(buildingData.buildingID));

			Position buildPosition = buildingData.buildPos;
			newBuilding.matrix.position = new Vector3d(buildPosition.x, game.world.land.getHeight(new Position(buildPosition.x, buildPosition.z)), buildPosition.z);
			newBuilding.init(this);
			spawnEntity(newBuilding);
		}
	}

	public void updateClient() {
		if (client == null) return;
		ArrayList<Packet> receivedPackets = client.getData(Packet.WORLD_CHANNEL);
		if (receivedPackets != null) {
			for (Packet packet : receivedPackets) {
				processClientPackets(packet);
			}
		}
	}

	private void processClientPackets(Packet packet) {
		if (packet instanceof PacketChunkData) {
			PacketChunkData chunkData = (PacketChunkData) packet;

			Chunk recChunk = new Chunk(chunkData.chunkPos, land.saveName, this, false);
			recChunk.buildChunk(chunkData.chunkFile);
			land.addChunk(recChunk);
		} else if (packet instanceof PacketExitGame) {
			exitWorld(false);
			game.guiList.addGui(new GuiMain(game));
		} else if (packet instanceof PacketSpawnEntity) {
			PacketSpawnEntity entityData = (PacketSpawnEntity) packet;
			Entity newEntity = Entity.getEntity(game, this, entityData.tagName);
			newEntity.load(entityData.entityData);
			spawnEntity(newEntity);
		} else if (packet instanceof PacketRemoveEntity) {
			PacketRemoveEntity entityData = (PacketRemoveEntity) packet;
			Entity remEntity = entityList.entities.get(entityData.id);
			if (remEntity != null) {
				remEntity.remove();
			}
		}
	}

	public void update() {
		if (!isLoaded) return;

		if (serverWorld) {
			updateServer();
		} else {
			updateClient();
		}

		if (!serverWorld) {
			if (InputHandler.isKeyPressed(Keyboard.KEY_ESCAPE) || InputHandler.isGamepadButtonPressed("start")) {
				game.guiList.addGui(new GuiPauseMenu(game));
			}
			player.camera.updatePostition();
			InputHandler.updateMousePositions(game);
			loadPosition = player.camera.getCamCenter().toPosition();

			ArrayList<Packet> receivedPackets = client.getData(Packet.ENTITY_CHANNEL);
			entityList.update(game, receivedPackets);
		} else {

			ArrayList<Packet> receivedPackets = new ArrayList<Packet>();

			ArrayList<Client> clients = server.getClients();
			for (int i = 0; i < clients.size(); i++) {
				if (clients.get(i).connectionInfo != null) {
					Client connectionClient = clients.get(i);
					ArrayList<Packet> receivedPacketsClient = connectionClient.getData(Packet.ENTITY_CHANNEL);
					if (receivedPacketsClient != null) {
						for (Packet packet : receivedPacketsClient) {
							receivedPackets.add(packet);
						}
					}
				}
			}

			entityList.update(game, receivedPackets);
		}

		if (!serverWorld) {
			player.update();
		}

	}

	public void render() {
		if (!isLoaded) return;

		maxChunkRenderDistance = GameOptions.instance.getIntOption("renderdistance");
		RenderEngine.instance.setOrtho();
		RenderHelper.renderDefaultWorldBackground();

		RenderEngine.instance.setPerspective();

		glPushMatrix();

		glRotatef(player.camera.rotationDown, 1.0f, 0.0f, 0.0f);//blick nach unten drehen
		glRotatef(player.camera.rotation, 0.0f, 1.0f, 0.0f);//drehen
		glTranslatef(-player.camera.scrollX, -player.camera.scrollY, -player.camera.scrollZ);//position anpassen

		//land.renderOverlay();
		land.render(loadPosition, maxChunkRenderDistance);
		entityList.render(loadPosition, maxChunkRenderDistance);
		game.debugFelk.additionalRender();
		game.debugNerogar.additionalRender();

		collisionComparer.renderGrid();
		//InputHandler.renderMouseRay();

		/*if (path != null) {
			glDisable(GL_TEXTURE_2D);
			glBegin(GL_LINES);
			for (int i = 1; i < path.finalPath.size(); i++) {
				glVertex3f((float) path.finalPath.get(i).getCenter().x, 5.1f, (float) path.finalPath.get(i).getCenter().z);
				glVertex3f((float) path.finalPath.get(i - 1).getCenter().x, 5.1f, (float) path.finalPath.get(i - 1).getCenter().z);
			}
			glEnd();
			glEnable(GL_TEXTURE_2D);

			for (int i = 1; i < path.openList.size(); i++) {
				path.openList.get(i).draw(1.0f, 0.5f, 0.5f);
			}
			for (PathNode node : path.closedList) {
				node.draw(0.5f, 1.0f, 0.5f);
			}
			pathStart.draw(0.5f, 0.5f, 1.0f);
			pathEnd.draw(0.8f, 0.8f, 1.0f);
		}*/

		if (player != null) player.renderInWorld(this);
		glPopMatrix();
	}

	public void spawnEntity(Entity entity) {
		if (isLoaded) {
			if (!serverWorld) {
				if (entity.saveEntity) {
					entityList.addEntity(entity, this);
				} else {
					entityList.addTempEntity(entity, this);
				}
			} else {
				if (entity.saveEntity) {
					entityList.addEntity(entity, this);

					PacketSpawnEntity entityPacket = new PacketSpawnEntity();
					entityPacket.tagName = entity.getNameTag();

					DNFile entityData = new DNFile();
					entity.save(entityData);
					entityPacket.entityData = entityData;

					server.broadcastData(entityPacket);
				}
			}

			/*
			if (!serverWorld || entity.saveEntity) { //don't spawn temp-entities on serverworld
				entityList.addEntity(entity, this);
				if (serverWorld && entity.saveEntity) {
					PacketSpawnEntity entityPacket = new PacketSpawnEntity();
					entityPacket.tagName = entity.getNameTag();

					DNFile entityData = new DNFile("");
					entity.save(entityData, "");
					entityPacket.entityData = entityData;

					server.broadcastData(entityPacket);
				}
			}*/
		}
	}

	public void despawnEntity(Entity entity) {
		if (isLoaded) entityList.entities.remove(entity);
	}

	public Entity getEntityByID(int id) {
		return entityList.entities.get(id);
	}
}
