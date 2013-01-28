package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.physics.CollisionComparer;
import de.nerogar.gameV1.physics.Line;
import de.nerogar.gameV1.gui.GuiPauseMenu;
import de.nerogar.gameV1.image.TextureBank;

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
		//ab hier kommt nur tempor�rer code zum hinzuf�gen von test-entities
		/*
				entityList.addEntity(new EntityBlockDebug(game, new ObjectMatrix(new Vector3(6, 5, 1), new Vector3(0, 0, 0), new Vector3(1, 1, 1)), 10, 1F));

				for (int i = 0; i < 10; i++) {
					for (int j = 0; j < 10; j++) {
						entityList.addEntity(new EntityHouse(game, new ObjectMatrix(new Vector3(i * 1 - 10, 0, j * 1 - 10), new Vector3(0, 0, 0), new Vector3(1, 1, 1))));
					}
				}

				entityList.addEntity(new EntityBlock(game, new ObjectMatrix(new Vector3(-6, 10, 1), new Vector3(0, 0, 0), new Vector3(1, 1, 1)), 10f, 1f));
				*/
		// Der letzte, zus�tzliche Parameter ist Testweise die Skalierung (Der AABB)
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

	}

	public void render() {
		if (!isLoaded) return;

		maxChunkRenderDistance = GameOptions.instance.getIntOption("renderdistance");
		RenderEngine.instance.setOrtho();
		RenderHelper.renderDefaultWorldBackground();

		RenderEngine.instance.setPerspective();

		TextureBank.instance.getTexture("terrain/floor.png").bind();

		glPushMatrix();

		glRotatef(camera.rotationDown, 1.0f, 0.0f, 0.0f);//blick nach unten drehen
		glRotatef(camera.rotation, 0.0f, 1.0f, 0.0f);//drehen
		glTranslatef(-camera.scrollX, -camera.scrollY, -camera.scrollZ);//position anpassen

		//land.renderOverlay();
		land.render(loadPosition, maxChunkRenderDistance);
		entityList.render(loadPosition, maxChunkRenderDistance);

		game.world.collisionComparer.renderGrid();
		InputHandler.renderMouseRay();
		Line sightRay = new Line(InputHandler.get3DmousePosition(), InputHandler.get3DmouseDirection());
		if (GameOptions.instance.getBoolOption("debug")) entityList.getEntitiesInSight(sightRay);

		glPopMatrix();
	}
}