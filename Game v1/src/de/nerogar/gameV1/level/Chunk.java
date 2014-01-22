package de.nerogar.gameV1.level;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import de.nerogar.DNFileSystem.DNFile;
import de.nerogar.gameV1.*;
import de.nerogar.gameV1.ai.PathNode;

public class Chunk {
	public static final int CHUNKSIZE = 64;
	public static final int GENERATESIZE = Chunk.CHUNKSIZE + 1;
	public float[][] heightMap = new float[GENERATESIZE][GENERATESIZE];
	public boolean[][] walkableMap = new boolean[CHUNKSIZE][CHUNKSIZE];
	public PathNode[][] nodeMap;
	public int[][] tileMap = new int[CHUNKSIZE][CHUNKSIZE];
	public Position chunkPosition;

	private final String fileExtension = ".chu";
	private String filename;
	private String dirname = "saves/";

	public static int TILE_ID_LOCATION;
	public static int TILE_ID00_LOCATION;
	public static int TILE_ID01_LOCATION;
	public static int TILE_ID10_LOCATION;
	public static int TILE_ID11_LOCATION;

	private static final int POSITIONSIZE = CHUNKSIZE * CHUNKSIZE * 4 * 3;
	private static final int TEXTURESIZE = CHUNKSIZE * CHUNKSIZE * 4 * 2;
	private static final int VERTEXNUMBER = CHUNKSIZE * CHUNKSIZE * 4;
	private static final int ATTRIBNUMBER = VERTEXNUMBER * 5;

	public FloatBuffer vertexData;
	public IntBuffer atribData;
	public int vboVertexHandle, atribHandle;

	public World world;
	private boolean serverChunk;

	public Chunk(Position chunkPosition, String saveName, World world, boolean serverChunk) {
		this.serverChunk = serverChunk;
		dirname += saveName + "/";
		filename = dirname + "chunk_" + String.valueOf(chunkPosition.x) + "#" + String.valueOf(chunkPosition.z) + fileExtension;
		this.chunkPosition = chunkPosition;
		if (!serverChunk) {
			vertexData = BufferUtils.createFloatBuffer(POSITIONSIZE + TEXTURESIZE);
			atribData = BufferUtils.createIntBuffer(ATTRIBNUMBER);
		}

		this.world = world;
	}

	public void updateMaps() {
		updateWalkableMap();
		world.pathfinder.updateNodeMap(this);
		if (!serverChunk) {
			updateVbo();
		}
	}

	public void updateWalkableMap() {
		for (int i = 0; i < CHUNKSIZE; i++) {
			for (int j = 0; j < CHUNKSIZE; j++) {
				boolean walkable = true;

				//float h1 = getLocalHeight(i, j);
				//float h2 = getLocalHeight(i + 1, j);
				//float h3 = getLocalHeight(i, j + 1);
				//float h4 = getLocalHeight(i + 1, j + 1);
				//float maxHeightDifference = 0.6f;
				/*if (MathHelper.getHightest(h1, h2, h3, h4) - MathHelper.getLowest(h1, h2, h3, h4) > maxHeightDifference) {
					walkable = false;
				}*/
				if (tileMap[i][j] != Tile.TILE_WATER.id) {
					walkable = true;
				} else {
					walkable = false;
				}

				walkableMap[i][j] = walkable;
			}
		}
	}

	public PathNode getPathNode(int x, int z) {
		x = (int) MathHelper.modToInt(x, CHUNKSIZE);
		z = (int) MathHelper.modToInt(z, CHUNKSIZE);

		return nodeMap[x][z];
	}

	public void updateVbo() {

		float[] vertices = new float[POSITIONSIZE + TEXTURESIZE];
		int[] atributes = new int[ATTRIBNUMBER];

		Position chunkOffset = Position.multiply(chunkPosition, CHUNKSIZE);

		for (int i = 0; i < CHUNKSIZE; i++) {
			for (int j = 0; j < CHUNKSIZE; j++) {

				Vector2d textPos1 = Tile.getTileByID(tileMap[i][j]).texturePos1;
				Vector2d textPos2 = Tile.getTileByID(tileMap[i][j]).texturePos2;

				vertices[(j + (CHUNKSIZE * i)) * 20 + 0] = i + chunkOffset.x;
				vertices[(j + (CHUNKSIZE * i)) * 20 + 1] = heightMap[i][j];
				vertices[(j + (CHUNKSIZE * i)) * 20 + 2] = j + chunkOffset.z;
				vertices[(j + (CHUNKSIZE * i)) * 20 + 3] = textPos1.getXf();
				vertices[(j + (CHUNKSIZE * i)) * 20 + 4] = textPos1.getZf();

				vertices[(j + (CHUNKSIZE * i)) * 20 + 5] = i + chunkOffset.x;
				vertices[(j + (CHUNKSIZE * i)) * 20 + 6] = heightMap[i][j + 1];
				vertices[(j + (CHUNKSIZE * i)) * 20 + 7] = j + 1 + chunkOffset.z;
				vertices[(j + (CHUNKSIZE * i)) * 20 + 8] = textPos2.getXf();
				vertices[(j + (CHUNKSIZE * i)) * 20 + 9] = textPos1.getZf();

				vertices[(j + (CHUNKSIZE * i)) * 20 + 10] = i + 1 + chunkOffset.x;
				vertices[(j + (CHUNKSIZE * i)) * 20 + 11] = heightMap[i + 1][j + 1];
				vertices[(j + (CHUNKSIZE * i)) * 20 + 12] = j + 1 + chunkOffset.z;
				vertices[(j + (CHUNKSIZE * i)) * 20 + 13] = textPos2.getXf();
				vertices[(j + (CHUNKSIZE * i)) * 20 + 14] = textPos2.getZf();

				vertices[(j + (CHUNKSIZE * i)) * 20 + 15] = i + 1 + chunkOffset.x;
				vertices[(j + (CHUNKSIZE * i)) * 20 + 16] = heightMap[i + 1][j];
				vertices[(j + (CHUNKSIZE * i)) * 20 + 17] = j + chunkOffset.z;
				vertices[(j + (CHUNKSIZE * i)) * 20 + 18] = textPos1.getXf();
				vertices[(j + (CHUNKSIZE * i)) * 20 + 19] = textPos2.getZf();

				//attributes
				int i2 = i + 1 >= CHUNKSIZE ? i : i + 1;
				int j2 = j + 1 >= CHUNKSIZE ? j : j + 1;
				int i0 = i - 1 < 0 ? 0 : i - 1;
				int j0 = j - 1 < 0 ? 0 : j - 1;

				atributes[(j + (CHUNKSIZE * i)) * 20 + 0] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 1] = tileMap[i0][j0];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 2] = tileMap[i0][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 3] = tileMap[i][j0];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 4] = tileMap[i][j];

				atributes[(j + (CHUNKSIZE * i)) * 20 + 5] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 6] = tileMap[i0][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 7] = tileMap[i0][j2];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 8] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 9] = tileMap[i][j2];

				atributes[(j + (CHUNKSIZE * i)) * 20 + 10] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 11] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 12] = tileMap[i][j2];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 13] = tileMap[i2][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 14] = tileMap[i2][j2];

				atributes[(j + (CHUNKSIZE * i)) * 20 + 15] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 16] = tileMap[i][j0];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 17] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 18] = tileMap[i2][j0];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 19] = tileMap[i2][j];

				/*atributes[(j + (CHUNKSIZE * i)) * 20 + 0] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 1] = tileMap[i0][j0];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 2] = tileMap[i0][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 3] = tileMap[i][j0];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 4] = tileMap[i][j];

				atributes[(j + (CHUNKSIZE * i)) * 20 + 5] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 6] = tileMap[i0][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 7] = tileMap[i0][j2];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 8] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 9] = tileMap[i][j2];

				atributes[(j + (CHUNKSIZE * i)) * 20 + 11] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 12] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 13] = tileMap[i][j2];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 14] = tileMap[i2][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 15] = tileMap[i2][j2];
				
				atributes[(j + (CHUNKSIZE * i)) * 20 + 15] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 16] = tileMap[i][j0];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 17] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 18] = tileMap[i2][j0];
				atributes[(j + (CHUNKSIZE * i)) * 20 + 19] = tileMap[i2][j];*/
			}
		}

		vertexData.put(vertices);
		vertexData.flip();
		vboVertexHandle = glGenBuffers();

		atribData.put(atributes);
		atribData.flip();
		atribHandle = glGenBuffers();

		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, atribHandle);
		glBufferData(GL_ARRAY_BUFFER, atribData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void render() {
		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glVertexPointer(3, GL_FLOAT, 20, 0);
		glTexCoordPointer(2, GL_FLOAT, 20, 12);

		glBindBuffer(GL_ARRAY_BUFFER, atribHandle);

		//System.out.println(glGetError());
		//glVertexAttribPointer(TILE_ID_LOCATION, 1, GL_FLOAT, true, 2, 1);
		//RenderEngine.instance.checkErrors();
		glVertexAttribPointer(TILE_ID00_LOCATION, 1, GL_FLOAT, true, 20, 4);
		glVertexAttribPointer(TILE_ID01_LOCATION, 1, GL_FLOAT, true, 20, 8);
		glVertexAttribPointer(TILE_ID10_LOCATION, 1, GL_FLOAT, true, 20, 12);
		glVertexAttribPointer(TILE_ID11_LOCATION, 1, GL_FLOAT, true, 20, 16);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		//glEnableVertexAttribArray(TILE_ID_LOCATION);
		glEnableVertexAttribArray(TILE_ID00_LOCATION);
		glEnableVertexAttribArray(TILE_ID01_LOCATION);
		glEnableVertexAttribArray(TILE_ID10_LOCATION);
		glEnableVertexAttribArray(TILE_ID11_LOCATION);
		glDrawArrays(GL_QUADS, 0, (Chunk.CHUNKSIZE) * (Chunk.CHUNKSIZE) * 4);
		//glDisableVertexAttribArray(TILE_ID_LOCATION);
		glDisableVertexAttribArray(TILE_ID00_LOCATION);
		glDisableVertexAttribArray(TILE_ID01_LOCATION);
		glDisableVertexAttribArray(TILE_ID10_LOCATION);
		glDisableVertexAttribArray(TILE_ID11_LOCATION);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
	}

	public boolean load() {

		if (new File(filename).exists()) {
			DNFile chunkFile = new DNFile();
			try {
				chunkFile.load(filename);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}

			buildChunk(chunkFile);

			return true;
		}
		return false;
	}

	public void buildChunk(DNFile chunkFile) {
		for (int i = 0; i < GENERATESIZE; i++) {
			heightMap[i] = chunkFile.getFloatArray("heightmap." + i);
		}

		for (int i = 0; i < CHUNKSIZE; i++) {
			tileMap[i] = chunkFile.getIntArray("tilemap." + i);
		}

		for (int i = 0; i < chunkFile.getPath(Entity.NODEFOLDERSAVENAME).getPaths().size(); i++) {
			Entity entity = Entity.getEntity(world.game, world, chunkFile.getString(Entity.NODEFOLDERSAVENAME + "." + i + ".type"));
			entity.load(chunkFile.getPath(Entity.NODEFOLDERSAVENAME + "." + i));
			//System.out.println("laoded entity at X:" + entity.matrix.position.x + " Y:" + entity.matrix.position.y + " Z:" + entity.matrix.position.z);
			spawnEntity(entity);
		}

		updateMaps();
	}

	public void save() {
		DNFile chunkFile = getChunkFile();
		world.collisionComparer.removeEntitiesInChunk(this);
		try {
			chunkFile.save(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DNFile getChunkFile() {
		new File(dirname).mkdirs();
		DNFile chunkFile = new DNFile();
		for (int i = 0; i < GENERATESIZE; i++) {
			chunkFile.addFloat("heightmap." + i, heightMap[i]);
		}

		for (int i = 0; i < CHUNKSIZE; i++) {
			chunkFile.addInt("tilemap." + i, tileMap[i]);
		}

		//chunkFile.addFolder(Entity.NODEFOLDERSAVENAME);
		ArrayList<Entity> entities = world.collisionComparer.getEntitiesInChunk(this);

		int entityIndex = 0;
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).saveEntity) {
				entities.get(i).save(chunkFile.getPath(Entity.NODEFOLDERSAVENAME + "." + entityIndex));
				entityIndex++;
			}
		}

		return chunkFile;
	}

	public void cleanup() {
		if (!serverChunk) {
			glDeleteBuffers(vboVertexHandle);
		}

		heightMap = null;
		walkableMap = null;
		nodeMap = null;
	}

	public float getLocalHeight(int x, int z) {
		return heightMap[x][z];
	}

	public float getLocalHeight(double x, double z) {
		float v11 = getLocalHeight((int) x, (int) z);
		float v10 = getLocalHeight((int) x, (int) z + 1);
		float v00 = getLocalHeight((int) x + 1, (int) z + 1);
		float v01 = getLocalHeight((int) x + 1, (int) z);

		float tempX = (float) (x - ((int) x));

		if (tempX < 0) tempX += 1;

		float v0 = (v00 * tempX) + (v10 * (1f - tempX));
		float v1 = (v01 * tempX) + (v11 * (1f - tempX));

		float tempZ = (float) (z % 1);
		if (tempZ < 0) tempZ += 1;

		float ret = (v0 * tempZ) + (v1 * (1f - tempZ));
		return ret;
	}

	public void setLocalHeight(int x, int y, float height) {
		x = x < 0 ? 0 : x;
		x = x > GENERATESIZE ? GENERATESIZE : x;
		y = y < 0 ? 0 : y;
		y = y > GENERATESIZE ? GENERATESIZE : y;
		heightMap[x][y] = height;
	}

	public boolean getLocalWalkable(int x, int z) {
		return walkableMap[x][z];
	}

	public Tile getLocalTile(int x, int z) {
		return Tile.getTileByID(tileMap[x][z]);
	}

	public void spawnEntity(Entity entity) {
		world.entityList.addEntity(entity, world);
	}

	public void spawnEntityLocal(Entity entity) {

		Vector3d position = entity.matrix.position;
		position.addX(chunkPosition.x * CHUNKSIZE);
		position.addZ(chunkPosition.z * CHUNKSIZE);
		//position.x += chunkPosition.x * CHUNKSIZE;
		//position.z += chunkPosition.z * CHUNKSIZE;

		world.entityList.addEntity(entity, world);
	}

	@Override
	public boolean equals(Object o) {

		if (o instanceof Position) {

			Position compChunkPosition = (Position) o;
			return (compChunkPosition.x == chunkPosition.x && compChunkPosition.z == chunkPosition.z);

		} else if (o instanceof Chunk) return this == o;
		return false;
	}
}
