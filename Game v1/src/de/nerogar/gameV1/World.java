package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.physics.CollisionComparer;
import de.nerogar.gameV1.physics.Ray;
import de.nerogar.gameV1.gui.GuiPauseMenu;

public class World {
	public EntityList entityList;
	public WorldData worldData;
	public boolean isLoaded = false;
	public Land land;
	public Camera camera = new Camera();
	private Position loadPosition;
	private int maxChunkRenderDistance = GameOptions.instance.getIntOption("renderdistance");
	public Game game;
	public CollisionComparer collisionComparer;

	public World(Game game) {
		this.game = game;
		this.land = new Land(game, this);
		entityList = new EntityList(game);
		this.collisionComparer = new CollisionComparer(game);
		entityList.setCollisionComparer(collisionComparer);
	}

	public void initiateWorld(String levelName, long seed) {
		worldData = new WorldData(levelName);
		worldData.seed = seed;
		initiateWorld(levelName);
	}

	public void initiateWorld(String levelName) {
		if (worldData == null) {
			worldData = new WorldData(levelName);
			worldData.load();
		}

		land.saveName = worldData.saveName;
		land.seed = worldData.seed;

		camera.init();
		land.laodAllAroundXZ(new Position((int) camera.scrollX, (int) camera.scrollZ));
		isLoaded = true;

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

	public void closeWorld() {
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

		loadPosition = new Position((int) camera.scrollX, (int) camera.scrollZ);
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

		if (InputHandler.isMouseButtonPressed(0)) {
			for (Entity entity : clickedEntities) {
				entity.click(0);
			}
		} else if (InputHandler.isMouseButtonPressed(1)) {
			for (Entity entity : clickedEntities) {
				entity.click(1);
			}
		}

		//Vector3d floorIntersection = land.getFloorpointInSight(sightRay);

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

		//Ray sightRay = new Ray(InputHandler.get3DmousePosition(), InputHandler.get3DmouseDirection());
		//Vector3d floorIntersection = land.getFloorpointInSight(sightRay);
		Vector3d floorIntersection = new Vector3d(10, 10, 10);
		if (floorIntersection != null) {
			glDisable(GL_TEXTURE_2D);
			glBegin(GL_LINES);
			glColor3f(1f, 0f, 0f);
			glVertex3f(floorIntersection.getXf(), floorIntersection.getYf() - 5, floorIntersection.getZf());
			glVertex3f(floorIntersection.getXf(), floorIntersection.getYf() + 50, floorIntersection.getZf());
			glEnd();
		}

		glPopMatrix();
	}
}