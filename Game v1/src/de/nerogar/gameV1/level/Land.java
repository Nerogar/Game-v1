package de.nerogar.gameV1.level;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.generator.LevelGenerator;
import de.nerogar.gameV1.image.SpriteSheet;
import de.nerogar.gameV1.image.TextureBank;
import de.nerogar.gameV1.physics.ObjectMatrix;
import de.nerogar.gameV1.physics.Ray;

public class Land {
	private static SpriteSheet floorSprites;
	public ArrayList<Chunk> chunks;
	public long seed;
	public String saveName;
	public int maxChunkLoadDistance = GameOptions.instance.getIntOption("loaddistance");
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
			if (chunks.get(i).equals(chunkPosition)) {
				return i;
			}
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

	public void loadAllAroundXZ(Position blockPosition) {
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
		world.collisionComparer.newGrid();
	}

	private Position getChunkPosition(double x, double z) {

		int chPosX = MathHelper.divDownToInt(x, Chunk.CHUNKSIZE);
		int chPosZ = MathHelper.divDownToInt(z, Chunk.CHUNKSIZE);

		return new Position(chPosX, chPosZ);
	}

	private Position getChunkPosition(Position blockPosition) {
		Position chunkPosition = new Position(0, 0);

		chunkPosition.x = MathHelper.divDownToInt(blockPosition.x, Chunk.CHUNKSIZE);
		chunkPosition.z = MathHelper.divDownToInt(blockPosition.z, Chunk.CHUNKSIZE);

		return chunkPosition;
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
		TextureBank.instance.bindTexture("floorTexture");

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

	static {
		floorSprites = new SpriteSheet("floorTexture");
		floorSprites.addTexture("terrain/floor.png");
		floorSprites.addTexture("terrain/water.png");
		floorSprites.addTexture("terrain/grass.png");
		floorSprites.compile();
		Chunk.floorSprites = floorSprites;

	}

	public double getHeight(Position pos) {
		return getHeight(pos.x, pos.z);
	}

	public void click(int button, Vector3d pos) {
		if (button == 0) {
			EntityTestparticle entity = new EntityTestparticle(game, new ObjectMatrix(pos.add(new Vector3d(0, 10, 0))));
			world.spawnEntity(entity);
			entity.addForce(new Vector3d(0,-10000,0));
		}
	}
}