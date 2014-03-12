package de.nerogar.gameV1.ai;

import de.nerogar.gameV1.Vector2d;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.level.EntityFighting;
import de.nerogar.gameV1.level.Position;

public class AILogicGoToPosition extends AILogic {

	//private Vector3d targetPos;
	private Path path;
	private float progress;
	private int targetIndex;

	public AILogicGoToPosition(EntityFighting entity, Vector3d targetPos) {
		super(entity);
		//this.targetPos = targetPos;
		if (entity.world != null) {
			Pathfinder pathFinder = entity.world.pathfinder;
			PathNode start = pathFinder.getNode(new Position(entity.matrix.position));
			PathNode destination = pathFinder.getNode(new Position(targetPos));
			long time1 = System.nanoTime();
			path = new Path(start, destination, entity.matrix.position.toVector2d(), targetPos.toVector2d());
			long time2 = System.nanoTime();
			System.out.println("path generated in: " + ((time2 - time1) / 1000000f));
		}
	}

	@Override
	public void update(float time) {
		progress += (entity.moveSpeed * time);

		if (path.finalPathPositions == null || path.finalPathPositions.length == 1 || path.finalPathDisances[path.finalPathDisances.length - 1] < progress) {
			if (path.destination.walkable) {
				moveToPosition(path.destinationPosition);
			}
			removeLogic(this);
			return;
		}

		for (int i = targetIndex; i < path.finalPathDisances.length; i++) {
			if (path.finalPathDisances[i] > progress) {
				targetIndex = i - 1;
				break;
			}
		}

		float progressFromLastNode = progress - path.finalPathDisances[targetIndex];

		moveToPosition(Vector2d.subtract(path.finalPathPositions[targetIndex + 1], path.finalPathPositions[targetIndex]).normalize().multiply(progressFromLastNode).add(path.finalPathPositions[targetIndex]));
	}

	private void moveToPosition(Vector2d position) {
		Vector3d newPos = position.toVector3d();
		newPos.setY(entity.world.land.getHeight(newPos));
		entity.move(newPos);
		//System.out.println(newPos);
	}
}
