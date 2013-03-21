package de.nerogar.gameV1.level;

import java.util.ArrayList;

public class AsyncLevelLoader extends Thread {

	private ArrayList<Chunk> chunkRequestBuffer = new ArrayList<Chunk>();
	private ArrayList<Chunk> chunkLoadBuffer = new ArrayList<Chunk>();
	private ArrayList<Chunk> chunkSaveBuffer = new ArrayList<Chunk>();
	private Object syncObject = new Object();
	private boolean running = true;

	public AsyncLevelLoader() {
		setName("AsyncLevelLoader");
	}

	@Override
	public void run() {
		while (running) {
			if (chunkRequestBuffer.size() == 0 && chunkSaveBuffer.size() == 0) {
				synchronized (syncObject) {
					try {
						syncObject.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			Chunk tempChunk = null;

			synchronized (chunkRequestBuffer) {
				if (chunkRequestBuffer.size() > 0) {
					tempChunk = chunkRequestBuffer.get(0);
					chunkLoadBuffer.remove(0);
				}
			}

			if (tempChunk != null) {
				tempChunk.load();
				synchronized (chunkLoadBuffer) {
					chunkLoadBuffer.add(tempChunk);
				}
			}
		}
	}

	public void requestChunk(Chunk chunk) {
		synchronized (chunkRequestBuffer) {
			chunkRequestBuffer.add(chunk);
		}

		synchronized (syncObject) {
			syncObject.notify();
		}
	}

	public void addChunkToSaveList(Chunk chunk) {

	}

	public ArrayList<Chunk> getChunkBuffer() {
		ArrayList<Chunk> retBuffer = new ArrayList<Chunk>();
		if (chunkLoadBuffer.size() > 0) {
			synchronized (chunkLoadBuffer) {
				for (int i = chunkLoadBuffer.size() - 1; i >= 0; i--) {
					retBuffer.add(chunkLoadBuffer.get(i));
					chunkLoadBuffer.remove(i);
				}
			}
		}
		return retBuffer;
	}

	public void kill() {
		running = false;
		synchronized (syncObject) {
			syncObject.notify();
		}
	}

}
