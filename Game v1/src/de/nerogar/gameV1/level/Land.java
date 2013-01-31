package de.nerogar.gameV1.level;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.generator.LevelGenerator;

public class Land {
	public ArrayList<Chunk> chunks;
	public long seed;
	public String saveName;
	private int maxChunkLoadDistance = GameOptions.instance.getIntOption("loaddistance");
	public int chunkUpdatesPerFrame = 4;
	private Game game;
	private World world;

	public Land(Game game, World world) {
		chunks = new ArrayList<Chunk>();
		this.game = game;
		this.world = world;
	}

	public Chunk generateLand(Chunk chunk, Position chunkPosition) {
		LevelGenerator levelGen = new LevelGenerator(seed, (int) chunkPosition.x, (int) chunkPosition.z);
		chunk = levelGen.generateLevel(new Chunk(chunkPosition, saveName, world));

		return chunk;
	}

	public Chunk getChunk(Position chunkPosition) {
		int index = isChunkLoaded(chunkPosition);
		if (index != -1) return chunks.get(index);
		return new Chunk(new Position(0, 0), saveName, world); // wenn Chunk nicht vorhanden ist, wird ein leerer Chunk zurückgegeben.
		// return null; 
		// könnte auch NULL zurück geben, verursacht aber Schwierigkeitenin der späteren Verarbeitung!
	}

	public boolean loadChunksAroundXZ(Position blockPosition) {
		maxChunkLoadDistance = GameOptions.instance.getIntOption("loaddistance");
		int chunkUpdates = 0;
		for (int i = chunks.size() - 1; i >= 0; i--) {
			boolean flag1 = chunks.get(i).chunkPosition.x < getChunkPosition(blockPosition).x - maxChunkLoadDistance;
			boolean flag2 = chunks.get(i).chunkPosition.x >= getChunkPosition(blockPosition).x + maxChunkLoadDistance + 1;
			boolean flag3 = chunks.get(i).chunkPosition.z < getChunkPosition(blockPosition).z - maxChunkLoadDistance;
			boolean flag4 = chunks.get(i).chunkPosition.z >= getChunkPosition(blockPosition).z + maxChunkLoadDistance + 1;
			if ((flag1 || flag2 || flag3 || flag4) && chunkUpdates < chunkUpdatesPerFrame) {
				unloadChunk(i);
				chunkUpdates++;
			}
		}

		for (int i = getChunkPosition(blockPosition).x - maxChunkLoadDistance; i < getChunkPosition(blockPosition).x + maxChunkLoadDistance + 1; i++) {
			for (int j = getChunkPosition(blockPosition).z - maxChunkLoadDistance; j < getChunkPosition(blockPosition).z + maxChunkLoadDistance + 1; j++) {
				if (isChunkLoaded(new Position(i, j)) == -1 && chunkUpdates < chunkUpdatesPerFrame) {
					loadChunk(new Position(i, j));
					chunkUpdates++;
				}
			}
		}

		return chunkUpdates > 0;

	}

	private int isChunkLoaded(Position chunkPosition) {
		for (int i = 0; i < chunks.size(); i++) {
			if (chunks.get(i).equals(chunkPosition)) { return i; }
		}

		return -1;
	}

	public void loadChunk(Position chunkPosition) {
		Chunk chunk = new Chunk(chunkPosition, saveName, world);
		if (!chunk.load()) {

			chunk = generateLand(chunk, chunkPosition);
			// System.out.println("generated Chunk: " + chunkPosition.x + " / "+ chunkPosition.y + "   (" + (chunks.size() + 1) + ")");
		} else {
			// System.out.println("loaded Chunk: " + chunkPosition.x + " / " +chunkPosition.y + "   (" + (chunks.size() + 1) + ")");
		}

		chunks.add(chunk);

		game.world.collisionComparer.newGrid();
	}

	public void laodAllAroundXZ(Position blockPosition) {
		while (loadChunksAroundXZ(blockPosition)) {

		}
	}

	public void regenChunk(Position chunkPosition) {
		if (isChunkLoaded(chunkPosition) != -1) unloadChunk(chunkPosition);

		Chunk chunk = new Chunk(chunkPosition, saveName, world);

		chunk = generateLand(chunk, chunkPosition);

		chunks.add(chunk);

		game.world.collisionComparer.newGrid();
	}

	public void unloadAll() {
		for (int i = chunks.size() - 1; i >= 0; i--) {
			unloadChunk(i);
		}
	}

	public void unloadChunk(Position chunkPosition) {
		unloadChunk(isChunkLoaded(chunkPosition));
	}

	public void unloadChunk(int chunkIndex) {
		if (chunkIndex == -1) return;
		chunks.get(chunkIndex).save();
		chunks.get(chunkIndex).cleanup();
		chunks.remove(chunkIndex);
		game.world.collisionComparer.newGrid();
	}

	private Position getChunkPosition(double x, double z) {

		double chPosX = MathHelper.divDownToInt(x, Chunk.CHUNKSIZE);
		double chPosZ = MathHelper.divDownToInt(z, Chunk.CHUNKSIZE);

		return new Position((int) chPosX, (int) chPosZ);
	}

	private Position getChunkPosition(Position blockPosition) {
		Position chunkPosition = new Position(0, 0);

		chunkPosition.x = MathHelper.divDownToInt(blockPosition.x, Chunk.CHUNKSIZE);
		chunkPosition.z = MathHelper.divDownToInt(blockPosition.z, Chunk.CHUNKSIZE);

		return chunkPosition;
	}

	public float getHeight(double x, double z) {

		Position chunkPosition = getChunkPosition(x, z);
		Chunk chunk = getChunk(chunkPosition);

		if (chunk != null) {
			return chunk.getLocalHeight(MathHelper.modToInt(x, Chunk.CHUNKSIZE), MathHelper.modToInt(z, Chunk.CHUNKSIZE));
		} else {
			return 0;
		}

	}

	public void renderOverlay() {//test cones
		int xShift = (int) world.camera.scrollX;
		int zShift = (int) world.camera.scrollZ;

		for (int i = -100; i < 100; i++) {
			for (int j = -100; j < 100; j++) {

				glBegin(GL_LINES);
				glDisable(GL_TEXTURE_2D);
				glColor3f(1.0f, 0.0f, 0.0f);

				float scale = 2;
				float h = getHeight(i / scale + xShift, j / scale + zShift);

				glVertex3f(i / scale + xShift, h, j / scale + zShift);
				glVertex3f(i / scale + xShift, h + 1, j / scale + zShift);

				glEnable(GL_TEXTURE_2D);
				glEnd();
			}
		}
	}

	public void saveLandAsImage() {//unfertig
		//int imageWidth = (maxChunkDistance + maxChunkDistance + 1) * Chunk.CHUNKSIZE;
		//int[] pixels = new int[imageWidth * imageWidth];

		for (int i = 0; i < chunks.size(); i++) {
			Chunk tempChunk = chunks.get(i);
			boolean[][] walkableMap = tempChunk.walkableMap;
			for (int i2 = 0; i2 < walkableMap.length; i2++) {
				for (int j2 = 0; j2 < walkableMap.length; j2++) {

				}
			}
		}
	}

	public void render(Position loadPosition, int maxChunkRenderDistance) {

		for (int i = 0; i < chunks.size(); i++) {
			Chunk chunk = chunks.get(i);
			if (chunk.chunkPosition.x >= MathHelper.divDownToInt(loadPosition.x, Chunk.CHUNKSIZE) - maxChunkRenderDistance && chunk.chunkPosition.x <= MathHelper.divDownToInt(loadPosition.x, Chunk.CHUNKSIZE) + maxChunkRenderDistance) {
				if (chunk.chunkPosition.z >= MathHelper.divDownToInt(loadPosition.z, Chunk.CHUNKSIZE) - maxChunkRenderDistance && chunk.chunkPosition.z <= MathHelper.divDownToInt(loadPosition.z, Chunk.CHUNKSIZE) + maxChunkRenderDistance) {
					glPushMatrix();
					glTranslatef(chunks.get(i).chunkPosition.x * Chunk.CHUNKSIZE, 0, chunks.get(i).chunkPosition.z * Chunk.CHUNKSIZE);//position anpassen
					chunks.get(i).render();
					glPopMatrix();
				}
			}

		}
	}
}