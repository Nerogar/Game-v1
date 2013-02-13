package de.nerogar.gameV1.level;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.generator.LevelGenerator;
import de.nerogar.gameV1.image.TextureBank;
import de.nerogar.gameV1.physics.ObjectMatrix;
import de.nerogar.gameV1.physics.Ray;

public class Land {
	public ArrayList<Chunk> chunks;
	private Chunk[][] chunkGrid;
	private int chunkGridMinX, chunkGridMinZ;
	public long seed;
	public String saveName;
	public int maxChunkLoadDistance = GameOptions.instance.getIntOption("loaddistance");
	public int chunkUpdatesPerFrame = 4;
	private Game game;
	public World world;
	public LevelGenerator levelGenerator;
	public EntityTestparticle markerCone;

	public Land(Game game, World world) {
		chunks = new ArrayList<Chunk>();
		this.game = game;
		this.world = world;
		markerCone = new EntityTestparticle(game, new ObjectMatrix());
		world.spawnEntity(markerCone);
	}

	public Chunk generateLand(Chunk chunk, Position chunkPosition) {
		chunk = levelGenerator.generateLevel(new Chunk(chunkPosition, saveName, world), (int) chunkPosition.x, (int) chunkPosition.z);

		return chunk;
	}

	/*public Chunk getChunk(Position chunkPosition) {
		int index = isChunkLoaded(chunkPosition);
		if (index != -1) return chunks.get(index);
		return null;
	}*/

	private void rebuildChunkGrid() {
		if (chunks.size() == 0) {
			chunkGrid = null;
			return;
		}

		int minX = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int minZ = Integer.MAX_VALUE;
		int maxZ = Integer.MIN_VALUE;

		for (int i = 0; i < chunks.size(); i++) {
			Chunk tempChunk = chunks.get(i);
			if (tempChunk.chunkPosition.x < minX) minX = tempChunk.chunkPosition.x;
			if (tempChunk.chunkPosition.x > maxX) maxX = tempChunk.chunkPosition.x;
			if (tempChunk.chunkPosition.z < minZ) minZ = tempChunk.chunkPosition.z;
			if (tempChunk.chunkPosition.z > maxZ) maxZ = tempChunk.chunkPosition.z;
		}

		chunkGridMinX = minX;
		chunkGridMinZ = minZ;

		chunkGrid = new Chunk[maxX - minX + 1][maxZ - minZ + 1];

		for (int i = 0; i < chunks.size(); i++) {
			Chunk tempChunk = chunks.get(i);
			chunkGrid[tempChunk.chunkPosition.x - chunkGridMinX][tempChunk.chunkPosition.z - chunkGridMinZ] = tempChunk;
		}
	}

	private void rebuildChunkList() {
		if (chunkGrid == null) return;
		chunks = new ArrayList<Chunk>();
		chunks.ensureCapacity(chunkGrid.length * chunkGrid[0].length);
		for (int i = 0; i < chunkGrid.length; i++) {
			for (int j = 0; j < chunkGrid[0].length; j++) {
				if (chunkGrid[i][j] != null) chunks.add(chunkGrid[i][j]);
			}
		}
	}

	public int loadChunksAroundXZ(Position blockPosition) {
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
				if (getChunk(new Position(i, j)) == null && chunkUpdates < chunkUpdatesPerFrame) {
					loadChunk(new Position(i, j));
					chunkUpdates++;
				}
			}
		}

		return chunkUpdates;
	}

	public Chunk getChunk(Position chunkPosition) {
		if (chunkGrid == null) return null;
		if (chunkPosition.x < chunkGridMinX || chunkPosition.z < chunkGridMinZ || chunkPosition.x > chunkGridMinX + chunkGrid.length - 1 || chunkPosition.z > chunkGridMinZ + chunkGrid[0].length - 1) return null;

		Chunk tempChunk = chunkGrid[chunkPosition.x - chunkGridMinX][chunkPosition.z - chunkGridMinZ];
		return tempChunk;
	}

	private void loadChunk(Position chunkPosition) {
		RenderHelper.updateLoadingScreen("Lade Chunk!");
		Chunk chunk = new Chunk(chunkPosition, saveName, world);
		if (!chunk.load()) {

			chunk = generateLand(chunk, chunkPosition);
			// System.out.println("generated Chunk: " + chunkPosition.x + " / "+ chunkPosition.y + "   (" + (chunks.size() + 1) + ")");
		} else {
			// System.out.println("loaded Chunk: " + chunkPosition.x + " / " +chunkPosition.y + "   (" + (chunks.size() + 1) + ")");
		}

		chunks.add(chunk);
		rebuildChunkGrid();
		updateWalkMapNodeNeighbors(chunk);
		game.world.collisionComparer.newGrid();
	}

	public void loadAllAroundXZ(Position blockPosition) {
		int i = 0;
		int max = (GameOptions.instance.getIntOption("loaddistance") * 2) + 1;
		max *= max;
		int updates = loadChunksAroundXZ(blockPosition);
		while (updates > 0) {
			i += updates;
			updates = loadChunksAroundXZ(blockPosition);
			RenderHelper.updateLoadingScreen("Lade Chunk " + i + "/" + max);
		}
	}

	public void regenChunk(Position chunkPosition) {
		if (getChunk(chunkPosition) != null) unloadChunk(chunkPosition);

		Chunk chunk = new Chunk(chunkPosition, saveName, world);
		chunk = generateLand(chunk, chunkPosition);
		chunks.add(chunk);

		updateWalkMapNodeNeighbors(chunk);
		game.world.collisionComparer.newGrid();
	}

	public void unloadAll() {
		for (int i = chunks.size() - 1; i >= 0; i--) {
			RenderHelper.updateLoadingScreen("Speichere Chunk " + i);
			unloadChunk(chunks.get(i).chunkPosition);
		}
	}

	private void unloadChunk(int index) {
		unloadChunk(chunks.get(index).chunkPosition);
	}

	private void unloadChunk(Position chunkPosition) {
		Chunk tempChunk = getChunk(chunkPosition);
		if (tempChunk == null) return;
		tempChunk.save();
		
		tempChunk.cleanup();
		chunkGrid[chunkPosition.x - chunkGridMinX][chunkPosition.z - chunkGridMinZ] = null;
		rebuildChunkList();
		updateWalkMapNodeNeighbors(tempChunk);
		world.collisionComparer.newGrid();
	}

	public void updateWalkMapNodeNeighbors(Chunk chunk) {
		for (int i = chunk.chunkPosition.x - 1; i <= chunk.chunkPosition.x + 1; i++) {
			for (int j = chunk.chunkPosition.z - 1; j <= chunk.chunkPosition.z + 1; j++) {
				//System.out.println("update neighbors of (" + i + "|" + j + ")");
				//System.out.println("chunkpos: " + chunk.chunkPosition + " || --> " + i + "|" + j);
				world.pathfinder.updateNodeNeighbors(getChunk(new Position(i, j)));
			}
		}
	}

	public Position getChunkPosition(double x, double z) {
		int chPosX = MathHelper.divDownToInt(x, Chunk.CHUNKSIZE);
		int chPosZ = MathHelper.divDownToInt(z, Chunk.CHUNKSIZE);

		return new Position(chPosX, chPosZ);
	}

	public Position getChunkPosition(Position blockPosition) {
		int chPosX = MathHelper.divDownToInt(blockPosition.x, Chunk.CHUNKSIZE);
		int chPosZ = MathHelper.divDownToInt(blockPosition.z, Chunk.CHUNKSIZE);

		return new Position(chPosX, chPosZ);
	}

	public Position getPositionInChunk(double x, double z) {
		int chPosX = (int) MathHelper.modToInt(x, Chunk.CHUNKSIZE);
		int chPosZ = (int) MathHelper.modToInt(z, Chunk.CHUNKSIZE);

		return new Position(chPosX, chPosZ);
	}

	public Position getPositionInChunk(Position blockPosition) {
		int chPosX = (int) MathHelper.modToInt(blockPosition.x, Chunk.CHUNKSIZE);
		int chPosZ = (int) MathHelper.modToInt(blockPosition.z, Chunk.CHUNKSIZE);

		return new Position(chPosX, chPosZ);
	}

	private boolean isInChunk(Position blockPosition, Chunk chunk) {
		return (blockPosition.x / Chunk.CHUNKSIZE) == chunk.chunkPosition.x && (int) (blockPosition.z / Chunk.CHUNKSIZE) == chunk.chunkPosition.z;
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

	public float getHeight(int x, int z) {
		Position chunkPosition = getChunkPosition(x, z);
		Chunk chunk = getChunk(chunkPosition);

		if (chunk != null) {
			return chunk.getLocalHeight((int) MathHelper.modToInt(x, Chunk.CHUNKSIZE), (int) MathHelper.modToInt(z, Chunk.CHUNKSIZE));
		} else {
			return 0;
		}
	}

	public Vector3d getHighestBetween(Position pos1, Position pos2) {
		Position chunkPos1Temp = getChunkPosition(pos1);
		Position chunkPos2Temp = getChunkPosition(pos2);
		Position chunkPos1 = new Position(0, 0);
		Position chunkPos2 = new Position(0, 0);
		Vector3d highest = new Vector3d(0, 0, 0);
		int heightestValue = 0;

		if (chunkPos1Temp.x < chunkPos2Temp.x) {
			chunkPos1.x = chunkPos1Temp.x;
			chunkPos2.x = chunkPos2Temp.x;
		} else {
			chunkPos1.x = chunkPos2Temp.x;
			chunkPos2.x = chunkPos1Temp.x;
		}

		if (chunkPos1Temp.z < chunkPos2Temp.z) {
			chunkPos1.z = chunkPos1Temp.z;
			chunkPos2.z = chunkPos2Temp.z;
		} else {
			chunkPos1.z = chunkPos2Temp.z;
			chunkPos2.z = chunkPos1Temp.z;
		}

		for (int i = 0; i < chunks.size(); i++) {
			Chunk chunk = chunks.get(i);
			if (chunk.chunkPosition.x >= chunkPos1.x && chunk.chunkPosition.x <= chunkPos2.x) {
				if (chunk.chunkPosition.z >= chunkPos1.z && chunk.chunkPosition.z <= chunkPos2.z) {
					//MathHelper.modToInt(x, Chunk.CHUNKSIZE), MathHelper.modToInt(z, Chunk.CHUNKSIZE)
					int x0 = (int) MathHelper.modToInt(chunkPos1.x, Chunk.CHUNKSIZE);
					int x1 = (int) MathHelper.modToInt(chunkPos2.x, Chunk.CHUNKSIZE);
					int z0 = (int) MathHelper.modToInt(chunkPos1.z, Chunk.CHUNKSIZE);
					int z1 = (int) MathHelper.modToInt(chunkPos2.z, Chunk.CHUNKSIZE);
					if (!isInChunk(pos1, chunk)) {
						x0 = 0;
						z0 = 0;
					}
					if (!isInChunk(pos2, chunk)) {
						x1 = Chunk.CHUNKSIZE;
						z1 = Chunk.CHUNKSIZE;
					}
					for (int x = x0; x < x1; x++) {
						for (int z = z0; z < z1; z++) {
							double posHeight = chunk.getLocalHeight(x, z);
							if (posHeight > heightestValue) {
								highest = new Vector3d(x, posHeight, z);
							}
						}
					}
				}
			}
		}
		return highest;
	}

	public Vector3d getFloorpointInSight(Ray ray) {
		return world.collisionComparer.getNearestFloorIntersectionWithRay(ray, world);
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
		TextureBank.instance.bindTexture("terrainSheet");
		Position chunkLoadPosition = getChunkPosition(loadPosition);
		for (int i = chunkLoadPosition.x - maxChunkRenderDistance; i <= chunkLoadPosition.x + maxChunkRenderDistance; i++) {
			for (int j = chunkLoadPosition.z - maxChunkRenderDistance; j <= chunkLoadPosition.z + maxChunkRenderDistance; j++) {
				Chunk tempChunk = getChunk(new Position(i, j));
				if (tempChunk != null) {
					glPushMatrix();
					glTranslatef(tempChunk.chunkPosition.x * Chunk.CHUNKSIZE, 0, tempChunk.chunkPosition.z * Chunk.CHUNKSIZE);//position anpassen
					tempChunk.render();
					glPopMatrix();
				}
			}
		}
	}

	public double getHeight(Position pos) {
		return getHeight(pos.x, pos.z);
	}

	public void click(int button, Vector3d pos) {
		/*if (button == 0) {
			markerCone.setForce(new Vector3d(0, 0, 0));
			markerCone.matrix.position.set(Vector3d.add(pos, new Vector3d(0, 10, 0)));
			markerCone.addForce(new Vector3d(0, -10000, 0));
			if (!world.containsEntity(markerCone)) world.spawnEntity(markerCone);
		}*/
	}
}