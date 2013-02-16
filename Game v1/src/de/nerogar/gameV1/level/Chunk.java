package de.nerogar.gameV1.level;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector2d;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.ai.PathNode;

public class Chunk {
	public static final int CHUNKSIZE = 64;
	public static final int GENERATESIZE = Chunk.CHUNKSIZE + 1;
	public float[][] heightMap = new float[GENERATESIZE][GENERATESIZE];
	public boolean[][] walkableMap = new boolean[CHUNKSIZE][CHUNKSIZE];
	public PathNode[][] nodeMap;
	public int[][] tileMap = new int[CHUNKSIZE][CHUNKSIZE];

	private final String fileExtension = ".chu";
	private String filename;
	private String dirname = "saves/";
	public Position chunkPosition;
	public FloatBuffer vertexData, colorData, textureData;
	int vboVertexHandle, vboColorHandle, vboTextureHandle;
	public World world;

	public Chunk(Position chunkPosition, String saveName, World world) {
		dirname += saveName + "/";
		filename = dirname + "chunk_" + String.valueOf(chunkPosition.x) + "#" + String.valueOf(chunkPosition.z) + fileExtension;
		this.chunkPosition = chunkPosition;
		vertexData = BufferUtils.createFloatBuffer(CHUNKSIZE * CHUNKSIZE * 4 * 3);
		colorData = BufferUtils.createFloatBuffer(CHUNKSIZE * CHUNKSIZE * 4 * 3);
		textureData = BufferUtils.createFloatBuffer(CHUNKSIZE * CHUNKSIZE * 4 * 2);
		this.world = world;
	}

	public void updateMaps() {
		updateWalkableMap();
		world.pathfinder.updateNodeMap(this);
		updateVbo();
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

		float[] vertices = new float[CHUNKSIZE * CHUNKSIZE * 4 * 3];
		float[] colors = new float[CHUNKSIZE * CHUNKSIZE * 4 * 3];
		float[] texCoords = new float[CHUNKSIZE * CHUNKSIZE * 4 * 2];

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

				vertices[(j + (CHUNKSIZE * i)) * 12 + 0] = i;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 2] = j;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 1] = heightMap[i][j];
				colors[(j + (CHUNKSIZE * i)) * 12 + 0] = heightMap[i][j] / 5;
				colors[(j + (CHUNKSIZE * i)) * 12 + 1] = heightMap[i][j] / 5;
				colors[(j + (CHUNKSIZE * i)) * 12 + 2] = heightMap[i][j] / 5;
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 0] = textPos1.getXf();
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 1] = textPos1.getYf();

				vertices[(j + (CHUNKSIZE * i)) * 12 + 3] = i;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 5] = j + 1;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 4] = heightMap[i][j + 1];
				colors[(j + (CHUNKSIZE * i)) * 12 + 3] = heightMap[i][j + 1] / 5;
				colors[(j + (CHUNKSIZE * i)) * 12 + 4] = heightMap[i][j + 1] / 5;
				colors[(j + (CHUNKSIZE * i)) * 12 + 5] = heightMap[i][j + 1] / 5;
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 2] = textPos2.getXf();
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 3] = textPos1.getYf();

				vertices[(j + (CHUNKSIZE * i)) * 12 + 6] = i + 1;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 8] = j + 1;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 7] = heightMap[i + 1][j + 1];
				colors[(j + (CHUNKSIZE * i)) * 12 + 6] = heightMap[i + 1][j + 1] / 5;
				colors[(j + (CHUNKSIZE * i)) * 12 + 7] = heightMap[i + 1][j + 1] / 5;
				colors[(j + (CHUNKSIZE * i)) * 12 + 8] = heightMap[i + 1][j + 1] / 5;
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 4] = textPos2.getXf();
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 5] = textPos2.getYf();

				vertices[(j + (CHUNKSIZE * i)) * 12 + 9] = i + 1;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 11] = j;
				vertices[(j + (CHUNKSIZE * i)) * 12 + 10] = heightMap[i + 1][j];
				colors[(j + (CHUNKSIZE * i)) * 12 + 9] = heightMap[i + 1][j] / 5;
				colors[(j + (CHUNKSIZE * i)) * 12 + 10] = heightMap[i + 1][j] / 5;
				colors[(j + (CHUNKSIZE * i)) * 12 + 11] = heightMap[i + 1][j] / 5;
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 6] = textPos1.getXf();
				texCoords[(j + (CHUNKSIZE * i)) * 8 + 7] = textPos2.getYf();
			}
		}

		vertexData.put(vertices);
		vertexData.flip();

		colorData.put(colors);
		colorData.flip();

		textureData.put(texCoords);
		textureData.flip();

		vboVertexHandle = glGenBuffers();
		vboColorHandle = glGenBuffers();
		vboTextureHandle = glGenBuffers();

		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
		glBufferData(GL_ARRAY_BUFFER, colorData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glBindBuffer(GL_ARRAY_BUFFER, vboTextureHandle);
		glBufferData(GL_ARRAY_BUFFER, textureData, GL_STATIC_DRAW);
		glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public void render() {
		glBindBuffer(GL_ARRAY_BUFFER, vboVertexHandle);
		glVertexPointer(3, GL_FLOAT, 0, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, vboColorHandle);
		glColorPointer(3, GL_FLOAT, 0, 0L);

		glBindBuffer(GL_ARRAY_BUFFER, vboTextureHandle);
		glTexCoordPointer(2, GL_FLOAT, 0, 0L);

		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		glDrawArrays(GL_QUADS, 0, (Chunk.CHUNKSIZE) * (Chunk.CHUNKSIZE) * 4);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
	}

	public boolean load() {

		if (new File(filename).exists()) {
			DNFile chunkFile = new DNFile(filename);
			chunkFile.load();

			for (int i = 0; i < GENERATESIZE; i++) {
				heightMap[i] = chunkFile.getFloatArray("heightmap." + i);
			}

			for (int i = 0; i < CHUNKSIZE; i++) {
				tileMap[i] = chunkFile.getIntArray("tilemap." + i);
			}

			for (int i = 0; i < chunkFile.getFoldersSize(Entity.NODEFOLDERSAVENAME); i++) {
				Entity entity = Entity.getEntity(world.game, chunkFile.getString(Entity.NODEFOLDERSAVENAME + "." + i + ".type"));
				entity.load(chunkFile, Entity.NODEFOLDERSAVENAME + "." + i);
				//System.out.println("laoded entity at X:" + entity.matrix.position.x + " Y:" + entity.matrix.position.y + " Z:" + entity.matrix.position.z);
				spawnEntity(entity);
			}

			updateMaps();
			return true;
		}
		return false;
	}

	public void save() {
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
		world.collisionComparer.removeEntitiesInChunk(this);

		int entityIndex = 0;
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i).saveEntity) {
				entities.get(i).save(chunkFile, Entity.NODEFOLDERSAVENAME + "." + entityIndex);
				entityIndex++;
			}
		}

		chunkFile.save();
	}

	public void cleanup() {
		glDeleteBuffers(vboVertexHandle);
		glDeleteBuffers(vboColorHandle);
		glDeleteBuffers(vboTextureHandle);
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