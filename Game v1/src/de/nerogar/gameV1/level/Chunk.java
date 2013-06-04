package de.nerogar.gameV1.level;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector2d;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.ai.PathNode;
import de.nerogar.gameV1.debug.DebugNerogar;

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

	private static final int POSITIONSIZE = CHUNKSIZE * CHUNKSIZE * 4 * 3;
	private static final int TEXTURESIZE = CHUNKSIZE * CHUNKSIZE * 4 * 2;
	private static final int VERTEXNUMBER = CHUNKSIZE * CHUNKSIZE * 4;
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
			atribData = BufferUtils.createIntBuffer(POSITIONSIZE + TEXTURESIZE);
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
		int[] atributes = new int[VERTEXNUMBER];

		Position chunkOffset = Position.multiply(chunkPosition, CHUNKSIZE);

		for (int i = 0; i < CHUNKSIZE; i++) {
			for (int j = 0; j < CHUNKSIZE; j++) {

				Vector2d textPos1 = Tile.getTileByID(tileMap[i][j]).texturePos1;
				Vector2d textPos2 = Tile.getTileByID(tileMap[i][j]).texturePos2;

				/*vertices[(j + (CHUNKSIZE * i)) * 12 + 0] = i;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 2] = j;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 1] = heightMap[i][j];
				colors[(j + (CHUNKSIZE * i)) * 12 + 0] = nodeMap[i][j].colR;
				colors[(j + (CHUNKSIZE * i)) * 12 + 1] = nodeMap[i][j].colG;
				colors[(j + (CHUNKSIZE * i)) * 12 + 2] = nodeMap[i][j].colB;
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 0] = textPos1.getXf();
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 1] = textPos1.getYf();

				vertices[(j + (CHUNKSIZE * i)) * 12 + 3] = i;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 5] = j + 1;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 4] = heightMap[i][j + 1];
				colors[(j + (CHUNKSIZE * i)) * 12 + 3] = nodeMap[i][j].colR;
				colors[(j + (CHUNKSIZE * i)) * 12 + 4] = nodeMap[i][j].colG;
				colors[(j + (CHUNKSIZE * i)) * 12 + 5] = nodeMap[i][j].colB;
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 2] = textPos2.getXf();
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 3] = textPos1.getYf();

				vertices[(j + (CHUNKSIZE * i)) * 12 + 6] = i + 1;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 8] = j + 1;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 7] = heightMap[i + 1][j + 1];
				colors[(j + (CHUNKSIZE * i)) * 12 + 6] = nodeMap[i][j].colR;
				colors[(j + (CHUNKSIZE * i)) * 12 + 7] = nodeMap[i][j].colG;
				colors[(j + (CHUNKSIZE * i)) * 12 + 8] = nodeMap[i][j].colB;
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 4] = textPos2.getXf();
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 5] = textPos2.getYf();

				vertices[(j + (CHUNKSIZE * i)) * 12 + 9] = i + 1;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 11] = j;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 10] = heightMap[i + 1][j];
				colors[(j + (CHUNKSIZE * i)) * 12 + 9] = nodeMap[i][j].colR;
				colors[(j + (CHUNKSIZE * i)) * 12 + 10] = nodeMap[i][j].colG;
				colors[(j + (CHUNKSIZE * i)) * 12 + 11] = nodeMap[i][j].colB;
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 6] = textPos1.getXf();
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 7] = textPos2.getYf();*/

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

				atributes[(j + (CHUNKSIZE * i)) * 4 + 0] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 4 + 1] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 4 + 2] = tileMap[i][j];
				atributes[(j + (CHUNKSIZE * i)) * 4 + 3] = tileMap[i][j];
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

		glVertexAttribPointer(DebugNerogar.TILE_ID_LOCATION, 1, GL_FLOAT, true, 4, 0);
		//glVertexAttribPointer(glGetAttribLocation(DebugNerogar.terrainShader.shaderHandle, "tileID"), 1, GL_FLOAT, true, 4, 0);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);

		glEnableVertexAttribArray(DebugNerogar.TILE_ID_LOCATION);
		glDrawArrays(GL_QUADS, 0, (Chunk.CHUNKSIZE) * (Chunk.CHUNKSIZE) * 4);
		glDisableVertexAttribArray(DebugNerogar.TILE_ID_LOCATION);

		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
	}

	public boolean load() {

		if (new File(filename).exists()) {
			DNFile chunkFile = new DNFile(filename);
			chunkFile.load();

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

		for (int i = 0; i < chunkFile.getFoldersSize(Entity.NODEFOLDERSAVENAME); i++) {
			Entity entity = Entity.getEntity(world.game, world, chunkFile.getString(Entity.NODEFOLDERSAVENAME + "." + i + ".type"));
			entity.load(chunkFile, Entity.NODEFOLDERSAVENAME + "." + i);
			//System.out.println("laoded entity at X:" + entity.matrix.position.x + " Y:" + entity.matrix.position.y + " Z:" + entity.matrix.position.z);
			spawnEntity(entity);
		}

		updateMaps();
	}

	public void save() {
		DNFile chunkFile = getChunkFile();
		world.collisionComparer.removeEntitiesInChunk(this);
		chunkFile.save();
	}

	public DNFile getChunkFile() {
		new File(dirname).mkdirs();
		DNFile chunkFile = new DNFile(filename);
		for (int i = 0; i < GENERATESIZE; i++) {
			chunkFile.addNode("heightmap." + i, heightMap[i]);
		}

		for (int i = 0; i < CHUNKSIZE; i++) {
			chunkFile.addNode("tilemap." + i, tileMap[i]);
		}

		chunkFile.addFolder(Entity.NODEFOLDERSAVENAME);
		ArrayList<Entity> entities = world.collisionComparer.getEntitiesInChunk(this);

		int entityIndex = 0;
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).saveEntity) {
				entities.get(i).save(chunkFile, Entity.NODEFOLDERSAVENAME + "." + entityIndex);
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