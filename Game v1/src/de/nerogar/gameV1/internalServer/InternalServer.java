package de.nerogar.gameV1.internalServer;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Timer;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.network.Server;

public class InternalServer extends Thread {
	//server
	public static final int TPS = 10;
	private static final float TICK_TIME = (float) (1f / TPS);
	private long lastUpdate;
	private Timer timer;
	private boolean running = true;

	private Server server;

	//game
	public World world;

	public InternalServer(Game game, Server server) {
		setName("IntenalServerThread");
		System.out.println("Initiated Server");
		world = new World(game, true);
		world.server = server;
		timer = new Timer();
		timer.init();
		this.server = server;
	}

	public void initiateWorld(String levelName, long seed) {
		world.initiateWorld(levelName, seed);
		world.internalServer = this;
		start();
	}

	public void initiateWorld(String levelName) {
		world.initiateWorld(levelName);
		world.internalServer = this;
		start();
	}

	@Override
	public void run() {
		System.out.println("Started Server");
		lastUpdate = System.nanoTime();
		while (running) {
			mainloop();
			timer.update();
			sync();
		}
		cleanup();
	}

	private void mainloop() {
		world.update((float) TICK_TIME);
	}

	public void cleanup() {
		if (world.isLoaded) world.closeWorld();
		server.stopServer();
	}

	private void sync() {
		try {
			long currentTime = System.nanoTime();
			double deltaTime = (currentTime - lastUpdate) / 1000000000d;

			//System.out.println("Server delta: " + deltaTime);
			if (deltaTime < TICK_TIME) {
				Thread.sleep((long) ((TICK_TIME - deltaTime) * 1000f));
			} else {
				System.out.println("server too slow");
			}
			lastUpdate = System.nanoTime();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void stopServer() {
		running = false;
	}
}
