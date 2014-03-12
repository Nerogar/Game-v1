package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.gameV1.ai.*;
import de.nerogar.gameV1.generator.LevelGenerator;
import de.nerogar.gameV1.graphics.RenderScene;
import de.nerogar.gameV1.gui.*;
import de.nerogar.gameV1.internalServer.Faction;
import de.nerogar.gameV1.internalServer.InternalServer;
import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.network.*;
import de.nerogar.gameV1.physics.CollisionComparer;
import de.nerogar.gameV1.physics.Ray;

public class World extends RenderScene{
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
	public Faction[] factions;
	public Player player;

	public Pathfinder pathfinder;
	private PathNode testNode;

	public World(Game game, boolean serverWorld) {
		this.game = game;
		this.serverWorld = serverWorld;
		entityList = new EntityList(game, this);
		this.land = new Land(game, this);
		this.collisionComparer = new CollisionComparer(this);
		entityList.setCollisionComparer(collisionComparer);
		if (serverWorld) {

		} else {

		}
	}

	public void initiateWorld(String levelName, long seed, Faction[] factions) {
		worldData = new WorldData(this, levelName);
		worldData.seed = seed;
		initiateWorld(levelName, factions);
	}

	public void initiateWorld(String levelName, Faction[] factions) {
		if (worldData == null) {
			worldData = new WorldData(this, levelName);
			worldData.load();
		}

		this.factions = factions;
		pathfinder = new Pathfinder(land);
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

		recalcFactionEntities();

		System.out.println("Initiated Level: " + worldData.levelName + " / seed: " + worldData.seed);
	}

	public void initiateClientWorld(Client client, Faction faction) {
		isLoaded = true;
		this.client = client;
		player = new Player(game, this, faction);
		game.guiList.addGui(new GuiIngameOverlay(game, player));
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
		game.guiList.removeGui(new GuiIngameOverlay(game, player));

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

		//test pathfinder:

		/*PathNode node1 = pathfinder.getNode(new Vector2d(-6.62, 19.66).toPosition());
		PathNode node2 = pathfinder.getNode(new Vector2d(-6.51, 20.48).toPosition());

		Path np = new Path(node1, node2, new Vector2d(-6.62, 19.66), new Vector2d(-6.51, 20.48));
		testNode = pathfinder.getNode(new Vector2d(-6.62, 19.66).toPosition());*/
		/*System.out.println("--");
		for (PathNode pn : np.finalPath) {
			for (PathNode neighbor : pn.neighbors) {
				System.out.println(pn.size + "|" + pn + "|" + pn.locX + " " + pn.locZ + " : " + neighbor + "|" + neighbor.locX + " " + neighbor.locZ + "|" + neighbor.size);
			}
			System.out.println();
		}*/
	}

	private void processServerPackets(Client connectionClient, Packet packet) {
		if (packet instanceof PacketExitGame) {
			PacketExitGame exitGame = (PacketExitGame) packet;
			closeWorld();
			server.broadcastData(exitGame);

			internalServer.stopServer();
		} else if (packet instanceof FactionPacketBuildHouse) {
			FactionPacketBuildHouse buildingData = (FactionPacketBuildHouse) packet;

			EntityBuilding newBuilding = (EntityBuilding) Entity.getEntity(game, this, buildingData.buildingID);
			newBuilding.faction = Faction.getFaction(buildingData.factionID);
			System.out.println(buildingData.factionID);

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
		//testNode = pathfinder.getNode(new Vector2d(-6.62, 19.66).toPosition());
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

	public void update(float time) {
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
			entityList.update(game, receivedPackets, time);
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

			entityList.update(game, receivedPackets, time);
		}

		if (!serverWorld) {
			player.update();
		}

	}

	public void render(double time) {
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
		land.render(time, loadPosition, maxChunkRenderDistance);
		entityList.render(time, loadPosition, maxChunkRenderDistance);
		game.debugFelk.additionalRender();
		game.debugNerogar.additionalRender();

		collisionComparer.renderGrid();
		InputHandler.renderMouseRay();
		ArrayList<Position> temppositions = collisionComparer.getGridPositionsInRay2(new Ray(InputHandler.get3DmouseStart(), InputHandler.get3DmouseDirection()));
		for (Position p : temppositions) {
			collisionComparer.renderBox(p.x, p.z);
		}

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

		if (testNode != null) {
			testNode.draw(0.5f, 0.0f, 0.0f);
			/*for(PathNode neighbors:testNode.neighbors){
				neighbors.draw(0.0f, 0.5f, 0.0f);
			}*/
		}

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

					recalcFactionEntities();

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
		if (isLoaded) {
			entityList.entities.remove(entity);

			recalcFactionEntities();
		}
	}

	public Entity getEntityByID(int id) {
		return entityList.entities.get(id);
	}

	public void recalcFactionEntities() {
		for (Faction f : factions) {
			f.recalcFactionEntities(entityList);
		}
	}
}
