package de.nerogar.gameV1.ai;

import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.level.EntityFighting;
import de.nerogar.gameV1.level.Position;

public class AILogicGoToPosition extends AILogic {

	private Vector3d targetPos;
	private Path path;

	public AILogicGoToPosition(EntityFighting entity, Vector3d targetPos) {
		super(entity);
		this.targetPos = targetPos;
		if (entity.world != null) {
			Pathfinder pathFinder = entity.world.pathfinder;
			PathNode start = pathFinder.getNode(new Position(entity.matrix.position));
			PathNode destination = pathFinder.getNode(new Position(targetPos));
			path = new Path(start, destination);
		}
	}

	@Override
	public void update(float time) {

	}
}
