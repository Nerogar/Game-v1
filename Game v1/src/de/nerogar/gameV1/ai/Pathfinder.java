package de.nerogar.gameV1.ai;

import de.nerogar.gameV1.level.Chunk;

public class Pathfinder {

	public void updateNodeMap(Chunk chunk) { //fertig
		PathNode[][] nodeMap = chunk.nodeMap;
		boolean[][] walkableMap = chunk.walkableMap;

		//long time1 = System.nanoTime(); //time1

		nodeMap = new PathNode[walkableMap.length][walkableMap[0].length];
		for (int i = 0; i < walkableMap.length; i++) {
			for (int j = 0; j < walkableMap.length; j++) {
				PathNode newNode = new PathNode(i, j);
				newNode.walkable = walkableMap[i][j];
				nodeMap[i][j] = newNode;
			}
		}

		//long time2 = System.nanoTime(); //time2

		int iteration = 2;
		int iterationHalf = 1;
		while (iteration <= walkableMap.length && iteration <= 16) {

			for (int i = 0; i < walkableMap.length; i += iteration) {
				for (int j = 0; j < walkableMap.length; j += iteration) {

					boolean m00 = nodeMap[i][j].mergeable;
					boolean m01 = nodeMap[i][j + (iterationHalf)].mergeable;
					boolean m10 = nodeMap[i + (iterationHalf)][j].mergeable;
					boolean m11 = nodeMap[i + (iterationHalf)][j + (iterationHalf)].mergeable;

					if (m00 && m01 && m10 && m11) {
						//calc node-connection
						boolean w00 = nodeMap[i][j].walkable;
						boolean w01 = nodeMap[i][j + (iterationHalf)].walkable;
						boolean w10 = nodeMap[i + (iterationHalf)][j].walkable;
						boolean w11 = nodeMap[i + (iterationHalf)][j + (iterationHalf)].walkable;

						if (w00 == w01 && w01 == w10 && w10 == w11) {
							PathNode newNode = new PathNode(i, j);
							newNode.size = iteration;
							newNode.mergeable = true;
							newNode.walkable = w00;

							for (int i2 = 0; i2 < iteration; i2++) {
								for (int j2 = 0; j2 < iteration; j2++) {
									nodeMap[i + i2][j + j2] = newNode;
								}
							}

						} else {
							nodeMap[i][j].mergeable = false;
							nodeMap[i][j + (iterationHalf)].mergeable = false;
							nodeMap[i + (iterationHalf)][j].mergeable = false;
							nodeMap[i + (iterationHalf)][j + (iterationHalf)].mergeable = false;
						}
					} else {
						for (int i2 = 0; i2 < iteration; i2++) {
							for (int j2 = 0; j2 < iteration; j2++) {
								nodeMap[i + i2][j + j2].mergeable = false;
							}
						}
					}
				}
			}

			iteration *= 2;
			iterationHalf *= 2;
		}

		//long time3 = System.nanoTime(); //time3

		for (int i = 0; i < nodeMap.length; i++) {
			for (int j = 0; j < nodeMap[i].length; j = nodeMap[i][j].y + nodeMap[i][j].size) {
				if (!nodeMap[i][j].neighborsProcessed) {
					PathNode node = nodeMap[i][j];

					int x0 = node.x - 1;
					int x1 = node.x + node.size;
					int y0 = node.y - 1;
					int y1 = node.y + node.size;

					x0 = x0 < 0 ? 0 : x0;
					x1 = x1 < nodeMap.length ? x1 : nodeMap.length - 1;
					y0 = y0 < 0 ? 0 : y0;
					y1 = y1 < nodeMap.length ? y1 : nodeMap.length - 1;

					if (y0 >= 0 && y0 != node.y) {
						for (int i1 = x0; i1 <= x1; i1 = nodeMap[i1][y0].x + nodeMap[i1][y0].size) {
							node.addNeighbor(nodeMap[i1][y0]);
						}
					}

					if (y1 < nodeMap.length && y1 != node.y + node.size - 1) {
						for (int i1 = x0; i1 <= x1; i1 = nodeMap[i1][y1].x + nodeMap[i1][y1].size) {
							node.addNeighbor(nodeMap[i1][y1]);
						}
					}

					if (x0 >= 0 && x0 != node.x) {
						for (int i1 = y0 + 1; i1 < y1; i1 = nodeMap[x0][i1].y + nodeMap[x0][i1].size) {
							node.addNeighbor(nodeMap[x0][i1]);
						}
					}

					if (x1 < nodeMap.length && x1 != node.x + node.size - 1) {
						for (int i1 = y0 + 1; i1 < y1; i1 = nodeMap[x1][i1].y + nodeMap[x1][i1].size) {
							node.addNeighbor(nodeMap[x1][i1]);
						}
					}

					nodeMap[i][j].neighborsProcessed = true;
					node.finalizeNeighbors();
				}
			}
		}

		//long time4 = System.nanoTime(); //time4

		//System.out.println("--1: " + (time2 - time1) / 1000000D + " ms");
		//System.out.println("--2: " + (time3 - time2) / 1000000D + " ms");
		//System.out.println("--3: " + (time4 - time3) / 1000000D + " ms");
	}
}
