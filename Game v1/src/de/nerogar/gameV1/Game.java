package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.gui.*;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityPhysic;
import de.nerogar.gameV1.level.EntityTestparticle;
import de.nerogar.gameV1.level.Tile;
import de.nerogar.gameV1.physics.ObjectMatrix;
import de.nerogar.gameV1.sound.Sound;

public class Game implements Runnable {
	public boolean running = true;

	public World world;
	public Game game;
	public GuiList guiList = new GuiList();
	public RenderEngine renderEngine = RenderEngine.instance;
	private long[] stressTimes = new long[4];
	public long stressTimeMainloop = 0;
	public long stressTimeRender = 0;
	public long stressTimeUpdate = 0;
	public long stressTimeTotal = 0;
	public Sound bgMusic;

	public void run() {
		this.game = this;

		try {

			init();

			Timer.instance.registerEvent("gc", 10);

			InputHandler.loadGamepad();
			InputHandler.registerGamepadButton("start", "7", 0.25f);
			InputHandler.registerGamepadButton("back", "6", 0.25f);

			// OpenAL Test

			bgMusic = new Sound(new File("res/sound/music.wav"), new Vector3d(0, 0, 0), true);

			while (running) {
				stressTimes[0] = System.nanoTime();
				mainloop();
				stressTimes[1] = System.nanoTime();
				render();
				stressTimes[2] = System.nanoTime();
				Display.update();
				Display.sync(GameOptions.instance.getIntOption("fps"));
				stressTimes[3] = System.nanoTime();
				if (Timer.instance.shellExecute("gc")) {
					System.gc();
					System.out.println("gc");
				}

				bgMusic.update();
				System.out.println(bgMusic.getOffset());

				updateStressTimes();
				//InputHandler.printGamepadButtons();
			}

			bgMusic.destroy();
			AL.destroy();

			if (world.isLoaded) world.closeWorld();
			renderEngine.cleanup();
			GameOptions.instance.save();

			//} catch (LWJGLException | IOException e) {
		} catch (Exception e) {
			Logger.printThrowable(e, "gotta catch 'em all", false);
		}

	}

	private void updateStressTimes() {
		stressTimeMainloop = stressTimes[1] - stressTimes[0];
		stressTimeRender = stressTimes[2] - stressTimes[1];
		stressTimeUpdate = stressTimes[3] - stressTimes[2];
		stressTimeTotal = System.nanoTime() - stressTimes[0];
	}

	private void mainloop() {
		InputHandler.update(game);

		if (InputHandler.isKeyPressed(Keyboard.KEY_F3) || InputHandler.isGamepadButtonPressed("back")) {
			//GameOptions.instance.setOption("debug", String.valueOf(!GameOptions.instance.getBoolOption("debug")));
			GameOptions.instance.switchBoolOption("debug");
			if (!GameOptions.instance.getBoolOption("debug")) {
				guiList.removeGui(new GuiDebug(game));
			} else {
				guiList.addGui(new GuiDebug(game));
			}
		}

		if (Display.isCloseRequested()) { // Exit if window is closed
			running = false;
		}

		if (InputHandler.isKeyPressed(Keyboard.KEY_F11)) {
			renderEngine.toggleFullscreen();
		}

		if (InputHandler.isKeyPressed(Keyboard.KEY_0)) {
			EntityTestparticle entity = new EntityTestparticle(game, new ObjectMatrix());
			world.entityList.addEntity(entity);
		}

		if (InputHandler.isKeyPressed(Keyboard.KEY_U)) {
			ArrayList<Entity> entities = world.entityList.entities;
			System.out.println(entities.size() + " entities gefunden.");
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i) instanceof EntityPhysic) {
					EntityPhysic entity = (EntityPhysic) entities.get(i);
					entity.addForce(new Vector3d(0, 500, 0));
				}
			}
		}

		if (InputHandler.isKeyPressed(Keyboard.KEY_1)) {
			System.out.println("playing: " + AL10.AL_PLAYING);
			System.out.println("paused: " + AL10.AL_PAUSED);
			System.out.println("stopped: " + AL10.AL_STOPPED);
			System.out.println("initial: " + AL10.AL_INITIAL);

			if (InputHandler.isKeyDown(Keyboard.KEY_1)) {
				bgMusic.setOffset((float) Math.random());
			}

			if (InputHandler.isKeyPressed(Keyboard.KEY_2)) {
				bgMusic.crash();
			}

			if (InputHandler.isKeyReleased(Keyboard.KEY_2)) {
				bgMusic.uncrash();
			}

			/*if (true) { //test bei sichtbarer plane

				Vector3d p1 = new Vector3d(-2f, 5f, 0f);
				Vector3d p2 = new Vector3d(2f, 5f, 0f);
				Vector3d p3 = new Vector3d(2f, 9f, 0f);
				Vector3d p4 = new Vector3d(-2f, 9f, 0f);
				Line ray = new Line(InputHandler.get3DmousePosition(), InputHandler.get3DmouseDirection());
				Plane plane = new Plane(p1, p2, p3);

				Vector3d intersection = CollisionComparer.getLinePolygonIntersection(ray, new Vector3d[] { p1, p2, p3, p4 });
				Vector3d intersectionC = PhysicHelper.getLinePlaneIntersection(ray, plane);

				if (intersection == null) {
					System.out.println("kein Schnittpunkt!");
				} else {
					System.out.println("Schnittpunkt bei: " + intersection.toString());
				}
				if (intersectionC != null) {
					System.out.println("---" + intersectionC.toString());
				}

			}*/

			//update game logics
			Timer.instance.update();
			guiList.update();
			if (!guiList.pauseGame()) {
				world.update();
			}
			Sound.setListener(new Vector3d(world.camera.scrollX, world.camera.scrollY, world.camera.scrollZ), new Vector3d(), new Vector3d());
		}
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		world.render();
		guiList.render();
	}

	private void init() {
		world = new World(game);
		if (GameOptions.instance.getBoolOption("debug")) {
			guiList.addGui(new GuiDebug(game));
		}
		guiList.addGui(new GuiMain(game));
		Entity.initEntityList(game);
		Tile.initTileList();
		Timer.instance.init();

		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}
}
