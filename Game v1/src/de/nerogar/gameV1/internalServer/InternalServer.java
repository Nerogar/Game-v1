package de.nerogar.gameV1.internalServer;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Timer;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.network.Server;

public class InternalServer extends Thread {
	//server
	private final int TPS = 10;
	private final double TICK_TIME = (double) 1000 / TPS;
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
		timer = new Timer();
		timer.init();
		this.server = server;
	}

	public void initiateWorld(String levelName, long seed) {
		world.initiateWorld(levelName, seed);
		start();
	}

	public void initiateWorld(String levelName) {
		world.initiateWorld(levelName);
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
		world.update();
	}

	public void cleanup() {
		world.closeWorld();
		server.stopServer();
	}

	private void sync() {
		try {
			long currentTime = System.nanoTime();
			double deltaTime = (currentTime - lastUpdate) / 1000000d;

			System.out.println("Server delta: " + deltaTime);
			if (deltaTime < TICK_TIME) {
				Thread.sleep((long) (TICK_TIME - deltaTime));
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
