package de.nerogar.gameV1.ai;

import de.nerogar.gameV1.level.Chunk;
import de.nerogar.gameV1.level.Land;
import de.nerogar.gameV1.level.Position;

public class Pathfinder {

	private Land land;

	public Pathfinder(Land land) {
		this.land = land;
	}

	public void updateNodeMap(Chunk chunk) { //fertig
		boolean[][] walkableMap = chunk.walkableMap;

		PathNode[][] nodeMap = new PathNode[walkableMap.length][walkableMap[0].length];
		chunk.nodeMap = nodeMap;
		for (int i = 0; i < walkableMap.length; i++) {
			for (int j = 0; j < walkableMap.length; j++) {
				PathNode newNode = new PathNode(i, j, chunk);
				newNode.walkable = walkableMap[i][j];
				nodeMap[i][j] = newNode;
			}
		}
	}

	public void updateNodeNeighbors(Chunk chunk) {
		if (chunk == null) return;

		PathNode[][] nodeMap = chunk.nodeMap;
		for (int i = 0; i < nodeMap.length; i++) {
			for (int j = 0; j < nodeMap[i].length; j++) {

				if (!nodeMap[i][j].neighborsProcessed) {
					PathNode node = nodeMap[i][j];
					node.clearNeighbors();

					Chunk tempChunk;
					PathNode tempNode;

					int x0 = node.x - 1;
					int x1 = node.x + 1;
					int y0 = node.z - 1;
					int y1 = node.z + 1;

					for (int i1 = x0; i1 <= x1; i1++) {
						for (int j1 = y0; j1 <= y1; j1++) {
							tempChunk = land.getChunk(land.getChunkPosition(i1, j1));
							if (i1 != node.x || j1 != node.z) {
								if (tempChunk != null) {
									tempNode = tempChunk.getPathNode(i1, j1);
									if (isWalkable(node, tempNode)) {
										node.addNeighbor(tempNode);
									}
								}
							}
						}
					}
					nodeMap[i][j].neighborsProcessed = true;
					node.finalizeNeighbors();
				}
			}
		}

		for (int i = 0; i < nodeMap.length; i++) {
			for (int j = 0; j < nodeMap[i].length; j++) {
				nodeMap[i][j].neighborsProcessed = false;
			}
		}
	}

	public boolean isWalkable(PathNode source, PathNode target) {

		if (!(source.walkable && target.walkable)) return false;

		if (source.x == target.x || source.z == target.z) {
			return true;
		} else {
			for (int i = (source.x < target.x ? source.x : target.x); i <= (source.x < target.x ? target.x : source.x); i++) {
				Chunk tempChunk;
				PathNode tempNode;
				boolean flag1 = false;
				boolean flag2 = false;

				tempChunk = land.getChunk(land.getChunkPosition(source.x, target.z));
				if (tempChunk != null) {
					tempNode = tempChunk.getPathNode(source.x, target.z);
					flag1 = tempNode.walkable;
				}

				tempChunk = land.getChunk(land.getChunkPosition(target.x, source.z));
				if (tempChunk != null) {
					tempNode = tempChunk.getPathNode(target.x, source.z);
					flag2 = tempNode.walkable;
				}
				if (flag1 && flag2) { return true; }
			}
		}

		return false;
	}

	public PathNode getNode(Position position) {
		Chunk tempChunk = land.getChunk(land.getChunkPosition(position));
		Position posInChunk = land.getPositionInChunk(position);
		return tempChunk.nodeMap[posInChunk.x][posInChunk.z];
	}
}