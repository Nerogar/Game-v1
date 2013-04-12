package de.nerogar.gameV1.internalServer;

import de.nerogar.gameV1.World;

public class InternalServer extends Thread {
	//server
	private final int TPS = 10;
	private final double TICK_TIME = (double) 1000 / TPS;
	private long lastUpdate;
	private boolean running = true;

	//game
	public World world;

	public InternalServer() {
		setName("IntenalServerThread");
		System.out.println("Initiated Server");
	}

	@Override
	public void run() {
		System.out.println("Started Server");
		lastUpdate = System.nanoTime();
		while (running) {
			mainloop();
			sync();
		}
	}

	private void mainloop() {
		//for (long i = 0; i < 100000000L; i++);
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
