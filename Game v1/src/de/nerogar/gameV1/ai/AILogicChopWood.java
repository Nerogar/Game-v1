package de.nerogar.gameV1.ai;

import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class AILogicChopWood extends AILogic {

	EntityTree tree;
	float speed;
	float progress;

	public AILogicChopWood(EntityFighting entity, EntityTree tree, float speed) {
		super(entity);
		this.tree = tree;
		this.speed = speed;
	}

	@Override
	public void update(float time) {
		Vector3d dist = Vector3d.subtract(entity.matrix.getPosition(), tree.matrix.getPosition());

		if (dist.getValue() < 1) {
			progress += time;

			System.out.println(progress + " : " + time);
			if (progress > speed) {
				tree.size--;

				EntityWood wood = new EntityWood(tree.world.game, tree.world, new ObjectMatrix(entity.matrix.getPosition().clone()));
				tree.world.spawnEntity(wood);

				removeLogic(this);
			}

			if (tree.size <= 0) {
				tree.remove();

			}
		}
	}
}
