package de.nerogar.gameV1.ai;

import java.util.ArrayList;

public class Path {
	private Pathfinder pathfinder;
	private PathNode start;
	private PathNode destination;
	private ArrayList<PathNode> openList = new ArrayList<PathNode>();
	private ArrayList<PathNode> closedList = new ArrayList<PathNode>();
	public ArrayList<PathNode> finalPath = new ArrayList<PathNode>();
	private boolean goalFound = false;
	public double pathLength = 0;

	public Path(PathNode start, PathNode destination, Pathfinder pathfinder) {
		this.start = start;
		this.destination = destination;

		this.pathfinder = pathfinder;
		find();
	}

	public void find() {
		openList = new ArrayList<PathNode>();
		closedList = new ArrayList<PathNode>();
		finalPath = new ArrayList<PathNode>();
		goalFound = false;
		openList.add(start);

		while (!goalFound) {
			calcNewIteration();
		}
	}

	public void calcNewIteration() {
		if (goalFound) return;

		for (int i = 0; i < openList.size(); i++) {
			openList.get(i).calcF(destination.x, destination.z);
		}

		int lowestF = -1;
		double lowestFValue = Double.MAX_VALUE;

		for (int i = 0; i < openList.size(); i++) {
			if (openList.get(i).f <= lowestFValue) {
				lowestF = i;
				lowestFValue = openList.get(i).f;
			}
		}

		if (lowestF != -1) {
			updateAroundNode(lowestF);
		} else {
			System.out.println("FEHLER BEIM PATHFINDING (updateAroundNode(-1))");
			goalFound = true;
		}
	}

	public void updateAroundNode(int nodeIndex) {
		PathNode node = openList.get(nodeIndex);

		//node.open = false;
		openList.remove(nodeIndex);
		closedList.add(node);
		
		for (int i = 0; i < node.neighbors.length; i++) {
			PathNode tempNode = node.neighbors[i];
			int posOpenList = positionInList(openList, tempNode);
			int posClosedList = positionInList(closedList, tempNode);
			if (pathfinder.isWalkable(node, tempNode)) {
				if (posClosedList == -1 && posOpenList == -1) {

					tempNode.g = node.g + node.neighborDistance[i];
					tempNode.parent = node;
					openList.add(tempNode);

					if (tempNode == destination) {
						goalFound = true;
						calcFinalPath(tempNode);
					}

				} else if (posClosedList == -1 && posOpenList != -1) {
					if (tempNode.g > node.g + node.neighborDistance[i]) {
						tempNode.g = node.g + node.neighborDistance[i];
						tempNode.parent = node;

						if (tempNode == destination) {
							goalFound = true;
							calcFinalPath(tempNode);
						}
					}
				}
			}
		}
	}

	public int positionInList(ArrayList<PathNode> list, PathNode node) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i) == node) return i;
		}

		return -1;
	}

	public void calcFinalPath(PathNode node) {
		pathLength = node.g;
		while (node.parent != null) {

			//System.out.println("final path: " + node.x + "|" + node.z);
			finalPath.add(node);
			PathNode tempNode = node.parent;
			node.parent = null;
			node = tempNode;

		}
	}
}
