package de.nerogar.gameV1.ai;

import java.util.ArrayList;
import java.util.Random;

import static org.lwjgl.opengl.GL11.*;

import de.nerogar.gameV1.Vector2d;
import de.nerogar.gameV1.level.Chunk;

public class PathNode {
	public PathNode parent;
	public double g, f, h;
	public int x, z;
	public int locX, locZ;
	public int size = 1;
	public boolean mergeable = true;
	public boolean walkable;
	public PathNode[] neighbors;
	public double[] neighborDistance;
	public ArrayList<PathNode> neighborsTemp = new ArrayList<PathNode>();
	public boolean neighborsProcessed = false;
	public boolean closed = false;
	public boolean opened = false;

	//
	public float colR, colG, colB;

	public PathNode(int x, int z, Chunk chunk) {
		this.locX = x;
		this.locZ = z;
		this.x = x + chunk.chunkPosition.x * Chunk.CHUNKSIZE;
		this.z = z + chunk.chunkPosition.z * Chunk.CHUNKSIZE;

		Random rnd = new Random();

		colR = rnd.nextFloat();
		colG = rnd.nextFloat();
		colB = rnd.nextFloat();
	}

	public void calcF(int x, int y) {
		h = (Math.abs(this.x - x) + Math.abs(this.z - y)) * 10;
		//double hX = Math.abs(this.x - x);
		//double hY = Math.abs(this.y - y);

		//h = Math.sqrt(hX * hX + hY * hY)*10;

		f = g + h;

		//System.out.println("G: " + g + "|H: " + h);
	}

	public void clearNeighbors() {
		neighborsTemp = new ArrayList<PathNode>();
	}

	public void addNeighbor(PathNode node) {
		for (int i = 0; i < neighborsTemp.size(); i++) {
			if (neighborsTemp.get(i) == node) { return; }
		}
		neighborsTemp.add(node);
	}

	public void finalizeNeighbors() {
		neighbors = new PathNode[neighborsTemp.size()];
		neighborDistance = new double[neighborsTemp.size()];
		for (int i = 0; i < neighbors.length; i++) {
			neighbors[i] = neighborsTemp.get(i);

			double distX = Math.abs(((x + (size / 2f)) - (neighbors[i].x + (neighbors[i].size / 2f))));
			double distY = Math.abs(((z + (size / 2f)) - (neighbors[i].z + (neighbors[i].size / 2f))));

			neighborDistance[i] = Math.sqrt(distX * distX + distY * distY) * 10;
		}
	}

	public Vector2d getCenter() {
		return new Vector2d(x + (size / 2f), z + (size / 2f));
	}

	public void draw(float r, float g, float b) {
		glDisable(GL_TEXTURE_2D);
		glColor3f(r, g, b);
		glBegin(GL_QUADS);
		glVertex3f(x, 5, z);
		glVertex3f(x, 5, z + size);
		glVertex3f(x + size, 5, z + size);
		glVertex3f(x + size, 5, z);
		glEnd();
		glEnable(GL_TEXTURE_2D);
	}
}
