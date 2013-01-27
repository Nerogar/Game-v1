package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.gui.*;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.CollisionComparer;
import de.nerogar.gameV1.physics.LineSegment;
import de.nerogar.gameV1.physics.PhysicHelper;
import de.nerogar.gameV1.physics.Plane;
import de.nerogar.gameV1.physics.Line;
import de.nerogar.gameV1.physics.Ray;

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

	public void run() {
		this.game = this;

		try {
			init();
			Timer.instance.registerEvent("gc", 10);

			InputHandler.loadGamepad();
			InputHandler.registerGamepadButton("start", "7", 0.25f);
			InputHandler.registerGamepadButton("back", "6", 0.25f);

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

				updateStressTimes();
				//InputHandler.printGamepadButtons();
			}
			if (world.isLoaded)
				world.closeWorld();
			renderEngine.cleanup();
			GameOptions.instance.save();
		} catch (Throwable e) {
			Logger.printThrowable(e, "gotta catch 'em all", true);
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

		if (InputHandler.isKeyPressed(Keyboard.KEY_1)) {

			Vector3d p1 = new Vector3d(-0.57, -0.95, -1);
			Vector3d p2 = new Vector3d(1.55, -0.73, -1);
			Vector3d p3 = new Vector3d(1.55, -0.73, 1);
			Vector3d p4 = new Vector3d(-0.57, -0.95, 1);
			Vector3d[] points = new Vector3d[] { p1, p2, p3, p4 };
			Line ray = new Line(new Vector3d(-0.35, 0.67, 0), new Vector3d(2.08, -2.31, 0));

			Vector3d intersection = CollisionComparer.getRayPolygonIntersection(ray, points);
			if (intersection == null)
				System.out.println("Kein Treffer.");
			else
				System.out.println("Treffer: " + intersection.toString());
		}

		if (InputHandler.isKeyPressed(Keyboard.KEY_2)) {

			Vector3d p1 = new Vector3d(1, 1, 0);
			Vector3d p2 = new Vector3d(-1, 1, 0);
			Vector3d p3 = new Vector3d(-1, -1, 0);
			Line ray = new Line(new Vector3d(0, 0, -1), new Vector3d(0, 0, 1));
			Plane plane = new Plane(p1, p2, p3);

			Vector3d intersection = PhysicHelper.getRayPlaneIntersection(ray, plane);
			if (intersection == null)
				System.out.println("SCHEISSE!");
			else
				System.out.println("GUT! " + intersection.toString());
			System.out.println("NORMALVEKTOR: " + intersection.toString());
		}

		if (InputHandler.isKeyPressed(Keyboard.KEY_0)) {

			Vector3d sta1 = new Vector3d(13, -10, 0);
			Vector3d dir1 = new Vector3d(1, 1, 3);
			Vector3d sta2 = new Vector3d(12, -11, 0);
			Vector3d dir2 = new Vector3d(2, 2, 2);
			Ray l1 = new Ray(sta1, dir1);
			LineSegment l2 = new LineSegment(sta2, dir2);

			Vector3d intersection = PhysicHelper.getLineLineIntersection(l1, l2);
			if (intersection == null)
				System.out.println("Kein Treffer!");
			else
				System.out.println("Treffer: " + intersection.toString());
		}

		//update game logics
		Timer.instance.update();
		guiList.update();
		if (!guiList.pauseGame()) {
			world.update();
		}
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		world.render();
		guiList.render();
	}

	private void init() throws Exception {
		world = new World(game);
		if (GameOptions.instance.getBoolOption("debug")) {
			guiList.addGui(new GuiDebug(game));
		}
		guiList.addGui(new GuiMain(game));
		Entity.initEntityList(game);
		Timer.instance.init();
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}
}