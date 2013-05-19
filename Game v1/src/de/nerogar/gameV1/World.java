package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.network.Client;
import de.nerogar.gameV1.network.Packet;
import de.nerogar.gameV1.network.PacketChunkData;
import de.nerogar.gameV1.network.Server;
import de.nerogar.gameV1.physics.CollisionComparer;
import de.nerogar.gameV1.physics.Ray;
import de.nerogar.gameV1.ai.Path;
import de.nerogar.gameV1.ai.PathNode;
import de.nerogar.gameV1.ai.Pathfinder;
import de.nerogar.gameV1.generator.LevelGenerator;
import de.nerogar.gameV1.gui.GuiPauseMenu;

public class World {
	public Game game;
	public EntityList entityList;
	public Land land;
	public WorldData worldData;
	public boolean isLoaded = false;
	public Position loadPosition;
	private int maxChunkRenderDistance = GameOptions.instance.getIntOption("renderdistance");
	public CollisionComparer collisionComparer;
	public Server server;
	public Client client;
	public boolean serverWorld;
	public ArrayList<Player> players;
	public Player player;

	public Pathfinder pathfinder;
	public Path path;
	public PathNode pathEnd;
	public PathNode pathStart;

	public World(Game game, boolean serverWorld) {
		this.game = game;
		this.serverWorld = serverWorld;
		entityList = new EntityList(game, this);
		this.land = new Land(game, this);
		this.collisionComparer = new CollisionComparer(this);
		entityList.setCollisionComparer(collisionComparer);
		if (serverWorld) {
			players = new ArrayList<Player>();
		} else {
			player = new Player(game, this);
		}
		pathfinder = new Pathfinder(land);
	}

	public void initiateWorld(String levelName, long seed) {
		worldData = new WorldData(levelName);
		worldData.seed = seed;
		initiateWorld(levelName);
	}

	public void initiateWorld(String levelName) {
		if (!serverWorld) RenderHelper.renderLoadingScreen("Lade Welt...");
		if (worldData == null) {
			worldData = new WorldData(levelName);
			worldData.load();
		}

		land.saveName = worldData.saveName;
		land.seed = worldData.seed;
		land.levelGenerator = new LevelGenerator(land);

		if (!serverWorld) {
			player.camera.init();
			loadPosition = player.camera.getCamCenter().toPosition();
		} else {
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

	public void initiateClientWorld() {
		isLoaded = true;
	}

	public void closeWorld() {
		if (!serverWorld) RenderHelper.renderLoadingScreen("Speichere Welt...");
		isLoaded = false;
		land.unloadAll();
		entityList.unloadAll();
		collisionComparer.cleanup();
		worldData.save();
		worldData = null;

		System.gc();
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
		}

		entityList.update(game);

		if (!serverWorld) {
			//if (Keyboard.isKeyDown(Keyboard.KEY_M)) entityList.addEntity(new EntityBlockDebug(game, new ObjectMatrix(), null, 10, 1F));

			Ray sightRay = new Ray(InputHandler.get3DmouseStart(), InputHandler.get3DmouseDirection());
			//long time1 = System.nanoTime();
			Entity[] clickedEntities = entityList.getEntitiesInSight(sightRay);
			//long time2 = System.nanoTime();
			//System.out.println("Pick time: " + (time2 - time1) / 1000000D);
			//double time1 = System.nanoTime();
			Vector3d floorIntersection = land.getFloorpointInSight(sightRay);
			//double time2 = System.nanoTime();
			//if (Timer.instance.getFramecount() % 60 == 0 && GameOptions.instance.getBoolOption("debug")) System.out.println("zeit für Bodenkollisionsberechnung letzten Frame: " + ((time2 - time1) / 1000000) + "ms");

			if (clickedEntities.length > 0) {
				if (InputHandler.isMouseButtonPressed(0)) {
					clickedEntities[0].click(0);
				} else if (InputHandler.isMouseButtonPressed(1)) {
					clickedEntities[0].click(1);
				}
			}

			/*if (floorIntersection != null) {
				if (InputHandler.isMouseButtonPressed(0)) {
					land.click(0, floorIntersection);
					if (pathStart != null) {
						pathEnd = pathfinder.getNode(new Position(MathHelper.roundDownToInt(floorIntersection.getX(), 1), MathHelper.roundDownToInt(floorIntersection.getZ(), 1)));
						//pathEnd = pathfinder.getNode(new Position(-28, 0));
						int multiplier = 10;
						long time1 = System.nanoTime();
						for (int i = 0; i < multiplier; i++) {
							path = new Path(pathStart, pathEnd);
						}
						long time2 = System.nanoTime();
						System.out.println("Calculated "+multiplier+" Paths -> total: " + ((time2 - time1) / 1000000d) + "ms | individual: " + ((time2 - time1) / (1000000d * multiplier)));
					}
				} else if (InputHandler.isMouseButtonPressed(1)) {
					land.click(1, floorIntersection);
					pathStart = pathfinder.getNode(new Position(MathHelper.roundDownToInt(floorIntersection.getX(), 1), MathHelper.roundDownToInt(floorIntersection.getZ(), 1)));
					//pathStart = pathfinder.getNode(new Position(12, 28));
				}
			}*/

			InputHandler.set3DmousePosition(floorIntersection);
			if (floorIntersection != null) {
				if (InputHandler.isMouseButtonPressed(0)) {
					land.click(0, floorIntersection);
					//ObjectMatrix om = new ObjectMatrix(new Vector3d(Math.floor(floorIntersection.getX()), floorIntersection.getY(), Math.floor(floorIntersection.getZ())));
					//spawnEntity(new EntityHouse(game, om));
					// wtf, warum machst du das hierhin?
				}
				if (InputHandler.isMouseButtonPressed(1)) {
					land.click(1, floorIntersection);
				}
				if (InputHandler.isMouseButtonPressed(2)) {
					land.click(2, floorIntersection);
				}
				land.setMousePos(floorIntersection);
			}
			player.update();
		}

		/*PathNode start = pathfinder.getNode(new Position(36, 16));
		PathNode pathEnd = pathfinder.getNode(new Position(0, 8));
		path = new Path(start, pathEnd, pathfinder);*/

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

		if (path != null) {
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
		}

		if (player != null) player.renderInWorld(this);
		glPopMatrix();
	}

	public void spawnEntity(Entity entity) {
		if (isLoaded) entityList.addEntity(entity, this);
	}

	public void despawnEntity(Entity entity) {
		if (isLoaded) entityList.entities.remove(entity);
	}

	public boolean containsEntity(Entity entity) {
		return entityList.containsEntity(entity);
	}
}
