package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.physics.*;

public abstract class EntityParticle extends EntityPhysic {

	private Vector3d standardAcceleration = new Vector3d(0, 0, 0);

	public EntityParticle(Game game, ObjectMatrix matrix, String objectName, float mass) {
		super(game, matrix, objectName, mass);
		texture = "houses/cone.png";/* Textur name */

		standardAcceleration = new Vector3d(0, -GameOptions.instance.GRAVITY, 0);
	}

	public void updatePhysics(float time) {
		updatePosition(time);
	}

	public void updatePosition(float time) {

		matrix.position.add(Vector3d.multiply(velocity, time));

		velocity.add(Vector3d.multiply(Vector3d.add(Vector3d.multiply(force, inverseMass), standardAcceleration), time));

		force.set(0, 0, 0);

		if (matrix.getPosition().getY() < game.world.land.getHeight(matrix.getPosition().getXf(), matrix.getPosition().getZf())) {
			matrix.getPosition().setY(game.world.land.getHeight(matrix.getPosition().getXf(), matrix.getPosition().getZf()));
			velocity.setY(0);
			velocity.multiplyX(.9);
			velocity.multiplyZ(.9);
		}
	}

}