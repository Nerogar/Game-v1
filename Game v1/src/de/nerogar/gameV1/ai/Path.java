package de.nerogar.gameV1.ai;

import java.util.ArrayList;
import java.util.LinkedList;

import de.nerogar.gameV1.MathHelper;

public class Path {
	private Pathfinder pathfinder;
	private PathNode start;
	private PathNode destination;
	public ArrayList<PathNode> openList = new ArrayList<PathNode>();
	public LinkedList<PathNode> closedList = new LinkedList<PathNode>();

	public ArrayList<PathNode> finalPath = new ArrayList<PathNode>();
	private boolean goalFound = false;
	public double pathLength = 0;

	public Path(PathNode start, PathNode destination, Pathfinder pathfinder) {
		this.start = start;
		this.destination = destination;

		//System.out.println(start.x + " " + start.z + "||" + destination.x + " " + destination.z);

		this.pathfinder = pathfinder;
		find();
		cleanup();
	}

	public void find() {
		openList = new ArrayList<PathNode>();
		closedList = new LinkedList<PathNode>();
		finalPath = new ArrayList<PathNode>();
		goalFound = false;
		openList.add(start);

		if (!start.walkable || !destination.walkable) {
			goalFound = true;
			calcFinalPath(start);
			System.out.println("Konnte keinen Weg finden");
		} else if (start == destination) {
			goalFound = true;
			calcFinalPath(start);
		}

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

		/*for (int i = 0; i < openList.size(); i++) {
			if (openList.get(i).f <= lowestFValue) {
				lowestF = i;
				lowestFValue = openList.get(i).f;
			}
		} Wird nichtmehr gebraucht, da Liste jetzt sortiert */

		if (openList.size() != 0) lowestF = 0;

		if (lowestF != -1) {
			updateAroundNode(lowestF);
		} else {
			System.out.println("FEHLER BEIM PATHFINDING (updateAroundNode(-1)) (warscheinlich kein Weg möglich)");
			goalFound = true;
		}
	}

	public void updateAroundNode(int nodeIndex) {
		PathNode node = openList.get(nodeIndex);

		openList.remove(nodeIndex);
		closedList.add(node);
		node.closed = true;

		for (int i = 0; i < node.neighbors.length; i++) {
			PathNode tempNode = node.neighbors[i];
			//if (pathfinder.isWalkable(node, tempNode)) {
			if (!tempNode.closed && !tempNode.opened) {

				tempNode.g = node.g + node.neighborDistance[i];
				tempNode.parent = node;
				tempNode.calcF(destination.x, destination.z);
				addToOpenList(tempNode);
				tempNode.opened = true;

				if (tempNode == destination) {
					goalFound = true;
					calcFinalPath(tempNode);
				}

			} else if (!tempNode.closed && tempNode.opened) {
				if (tempNode.g > node.g + node.neighborDistance[i]) {
					tempNode.g = node.g + node.neighborDistance[i];
					tempNode.parent = node;

					if (tempNode == destination) {
						goalFound = true;
						calcFinalPath(tempNode);
					}
				}
			}
			//}
		}
	}

	private void addToOpenList(PathNode node) { //binary insertion
		if (openList.size() == 0) {
			openList.add(node);
			return;
		}
		if (node.f < openList.get(0).f) {
			openList.add(0, node);
			return;
		}
		if (node.f > openList.get(openList.size() - 1).f) {
			openList.add(openList.size(), node);
			return;
		}

		int smallIndex = 0;
		int bigIndex = openList.size() - 1;
		boolean inserted = false;
		while (!inserted) {
			int testIndex = MathHelper.roundUpToInt(((bigIndex - smallIndex) / 2d), 1) + smallIndex;
			PathNode testNode = openList.get(testIndex);
			if (node.f < testNode.f) {
				bigIndex = testIndex;
			} else if (node.f > testNode.f) {
				smallIndex = testIndex;
			} else if (node.f == testNode.f) {
				openList.add(testIndex, node);
				return;
			}
			if (smallIndex == bigIndex || smallIndex == bigIndex - 1) {
				openList.add(bigIndex, node);
				inserted = true;
			}
		}
	}

	/*private void addToOpenList(PathNode node) {
		if (openList.size() == 0) {
			openList.add(node);
			return;
		}
		if (node.f < openList.get(0).f) {
			openList.add(0, node);
			return;
		}
		if (node.f > openList.get(openList.size() - 1).f) {
			openList.add(openList.size(), node);
			return;
		}

		int smallIndex = 0;
		int bigIndex = openList.size() - 1;
		boolean inserted = false;
		System.out.println("size: " + openList.size());
		while (!inserted) {
			System.out.println("small: " + smallIndex + "|big: " + bigIndex);
			int testIndex = MathHelper.roundUpToInt(((bigIndex - smallIndex) / 2d), 1) + smallIndex;
			System.out.println("testindex: " + testIndex);
			for (PathNode testNode : openList) {
				System.out.print("   " + testNode.f);
			}
			System.out.print("||" + node.f);
			System.out.println();
			if (node.f < openList.get(testIndex).f) {
				bigIndex = testIndex;
			} else if (node.f > openList.get(testIndex).f) {
				smallIndex = testIndex;
			} else if (node.f == openList.get(testIndex).f) {
				openList.add(testIndex, node);
				return;
			}
			if (smallIndex == bigIndex || smallIndex == bigIndex - 1) {
				openList.add(bigIndex, node);
				inserted = true;
				System.out.println("broken");
			}
		}
	}*/

	public void calcFinalPath(PathNode node) {
		pathLength = node.g;
		while (node.parent != null) {
			//System.out.println("final path: " + node.x + "|" + node.z);
			finalPath.add(node);
			PathNode tempNode = node.parent;
			node.parent = null;
			node = tempNode;
		}
		finalPath.add(node);
	}

	private void cleanup() {
		for (PathNode node : closedList) {
			node.opened = false;
			node.closed = false;
		}
		for (PathNode node : openList) {
			node.opened = false;
			node.closed = false;
		}
	}
}
