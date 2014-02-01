package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.debug.DebugFelk;
import de.nerogar.gameV1.debug.DebugNerogar;
import de.nerogar.gameV1.gui.*;
import de.nerogar.gameV1.internalServer.InternalServer;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.Tile;
import de.nerogar.gameV1.network.Client;

public class Game {
	public boolean running = true;
	public static final String version = "test 0.1";

	public Timer timer;
	public InternalServer internalServer;
	public Client client;
	public World world;
	public GuiList guiList = new GuiList();
	public RenderEngine renderEngine = RenderEngine.instance;
	private long[] stressTimes = new long[4];
	public long stressTimeMainloop = 0;
	public long stressTimeRender = 0;
	public long stressTimeUpdate = 0;
	public long stressTimeTotal = 0;
	public DebugFelk debugFelk = new DebugFelk(this);
	public DebugNerogar debugNerogar = new DebugNerogar(this);
	public GameResources gameResources = new GameResources();

	public void run() {
		try {
			init();
			//timer.registerEvent("gc", 10);

			InputHandler.loadGamepad();
			InputHandler.registerGamepadButton("start", "7", 0.25f);
			InputHandler.registerGamepadButton("back", "6", 0.25f);

			debugFelk.startup();
			debugNerogar.startup();

			while (running) {
				stressTimes[0] = System.nanoTime();
				mainloop();
				stressTimes[1] = System.nanoTime();
				render();
				stressTimes[2] = System.nanoTime();
				Display.update();
				Display.sync(GameOptions.instance.getIntOption("fps"));
				stressTimes[3] = System.nanoTime();
				if (timer.shellExecute("gc")) {
					long time1 = System.nanoTime();
					System.gc();
					long time2 = System.nanoTime();
					double gcTime = (time2 - time1) / 1000000D;
					System.out.println("Garbage Collector: " + gcTime + "ms");
				}
				//long err1 = System.nanoTime();
				//RenderEngine.instance.checkErrors();
				//long err2 = System.nanoTime();
				//System.out.println("err: " + ((err2 - err1) / 1000000f));
				updateStressTimes();
				//InputHandler.printGamepadButtons();
			}

			debugFelk.end();
			debugNerogar.end();
			AL.destroy();

			if (world.isLoaded) world.closeWorld();
			if (internalServer != null) internalServer.stopServer();
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
		/*System.out.println("main: " + (stressTimeMainloop/1000000f));
		System.out.println("render: " + (stressTimeRender/1000000f));
		System.out.println("sync: " + (stressTimeUpdate/1000000f));*/
	}

	private void mainloop() {
		InputHandler.update(this);

		if (InputHandler.isKeyPressed(Keyboard.KEY_F3) || InputHandler.isGamepadButtonPressed("back")) {
			//GameOptions.instance.setOption("debug", String.valueOf(!GameOptions.instance.getBoolOption("debug")));
			GameOptions.instance.switchBoolOption("debug");
			if (!GameOptions.instance.getBoolOption("debug")) {
				guiList.removeGui(new GuiDebug(this, world));
			} else {
				guiList.addGui(new GuiDebug(this, world));
			}
		}

		if (Display.isCloseRequested()) { // Exit if window is closed
			running = false;
		}

		if (InputHandler.isKeyPressed(Keyboard.KEY_F11)) {
			renderEngine.toggleFullscreen();
		}

		debugFelk.run();
		debugNerogar.run();

		//update game logics
		timer.update();
		guiList.update();
		if (!guiList.pauseGame()) {
			world.update(timer.delta);
		}
	}

	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		world.render();
		guiList.render();
	}

	private void init() {
		RenderHelper.renderLoadingScreen("Starte Spiel...");

		timer = new Timer();
		timer.init();
		timer.printFPS = true;

		world = new World(this, false);
		if (GameOptions.instance.getBoolOption("debug")) {
			guiList.addGui(new GuiDebug(this, world));
		}
		guiList.addGui(new GuiMain(this));
		Entity.initEntityList(this);
		Tile.initTileList();

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