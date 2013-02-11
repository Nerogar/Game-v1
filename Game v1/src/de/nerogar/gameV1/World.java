package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.physics.CollisionComparer;
import de.nerogar.gameV1.physics.Ray;
import de.nerogar.gameV1.ai.Path;
import de.nerogar.gameV1.ai.PathNode;
import de.nerogar.gameV1.ai.Pathfinder;
import de.nerogar.gameV1.generator.LevelGenerator;
import de.nerogar.gameV1.gui.GuiPauseMenu;

public class World {
	public EntityList entityList;
	public Land land;
	public WorldData worldData;
	public boolean isLoaded = false;
	public Camera camera = new Camera();
	public Position loadPosition = camera.getCamCenter().toPosition();
	private int maxChunkRenderDistance = GameOptions.instance.getIntOption("renderdistance");
	public Game game;
	public CollisionComparer collisionComparer;
	public Pathfinder pathfinder;
	public Path path;
	public PathNode pathEnd;
	public PathNode pathStart;

	public World(Game game) {
		this.game = game;
		entityList = new EntityList(game);
		this.land = new Land(game, this);
		this.collisionComparer = new CollisionComparer(game);
		entityList.setCollisionComparer(collisionComparer);
		pathfinder = new Pathfinder(land);
	}

	public void initiateWorld(String levelName, long seed) {
		worldData = new WorldData(levelName);
		worldData.seed = seed;
		initiateWorld(levelName);
	}

	public void initiateWorld(String levelName) {
		RenderHelper.renderLoadingScreen("Lade Welt...");
		if (worldData == null) {
			worldData = new WorldData(levelName);
			worldData.load();
		}

		//land = new Land(game, this);
		land.saveName = worldData.saveName;
		land.seed = worldData.seed;
		land.levelGenerator = new LevelGenerator(land);

		camera.init();
		land.loadAllAroundXZ(loadPosition);
		isLoaded = true;

		System.out.println("Initiated Level: " + worldData.levelName + " / seed: " + worldData.seed);
		RenderHelper.updateLoadingScreen("Starte Welt...");
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

	public void closeWorld() {
		RenderHelper.renderLoadingScreen("Speichere Welt...");
		isLoaded = false;
		land.unloadAll();
		entityList.unloadAll();
		collisionComparer.cleanup();
		worldData.save();
		worldData = null;
		System.gc();
	}

	public void update() {
		if (!isLoaded) return;

		if (InputHandler.isKeyPressed(Keyboard.KEY_ESCAPE) || InputHandler.isGamepadButtonPressed("start")) {
			game.guiList.addGui(new GuiPauseMenu(game));
		}

		camera.updatePostition();
		InputHandler.updateMousePositions(game);

		loadPosition = camera.getCamCenter().toPosition();
		land.loadChunksAroundXZ(loadPosition);

		entityList.update(game);

		//if (Keyboard.isKeyDown(Keyboard.KEY_M)) entityList.addEntity(new EntityBlockDebug(game, new ObjectMatrix(), null, 10, 1F));

		//soll exception verursachen
		/*Vector3d vTest = null;
		System.out.println(vTest.getValue());*/

		Ray sightRay = new Ray(InputHandler.get3DmousePosition(), InputHandler.get3DmouseDirection());
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

		if (floorIntersection != null) {
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

		glRotatef(camera.rotationDown, 1.0f, 0.0f, 0.0f);//blick nach unten drehen
		glRotatef(camera.rotation, 0.0f, 1.0f, 0.0f);//drehen
		glTranslatef(-camera.scrollX, -camera.scrollY, -camera.scrollZ);//position anpassen

		//land.renderOverlay();
		land.render(loadPosition, maxChunkRenderDistance);
		entityList.render(loadPosition, maxChunkRenderDistance);

		game.world.collisionComparer.renderGrid();
		InputHandler.renderMouseRay();

		if (path != null) {
			glDisable(GL_TEXTURE_2D);
			glBegin(GL_LINES);
			for (int i = 1; i < path.finalPath.size(); i++) {
				glVertex3f((float) path.finalPath.get(i).getCenter().x, 5.1f, (float) path.finalPath.get(i).getCenter().y);
				glVertex3f((float) path.finalPath.get(i - 1).getCenter().x, 5.1f, (float) path.finalPath.get(i - 1).getCenter().y);
			}
			glEnd();
			glEnable(GL_TEXTURE_2D);

			for (int i = 1; i < path.openList.size(); i++) {
				path.openList.get(i).draw(1.0f, 0.5f, 0.5f);
			}
			for (PathNode node :path.closedList) {
				node.draw(0.5f, 1.0f, 0.5f);
			}
			pathStart.draw(0.5f, 0.5f, 1.0f);
			pathEnd.draw(0.8f, 0.8f, 1.0f);
		}

		glPopMatrix();
	}

	public void spawnEntity(Entity entity) {
		if (isLoaded) entityList.addEntity(entity);
	}

	public boolean containsEntity(Entity entity) {
		return entityList.containsEntity(entity);
	}
}
