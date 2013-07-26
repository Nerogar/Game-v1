package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.physics.*;

public abstract class EntityParticle extends EntityPhysic {

	public Vector3d standardAcceleration = new Vector3d(0, 0, 0);
	public float friction = 10; //

	public EntityParticle(Game game, World world, ObjectMatrix matrix, float mass) {
		super(game, world, matrix, mass);
		texture = "houses/cone.png";/* Textur name */

		standardAcceleration = new Vector3d(0, -GameOptions.instance.GRAVITY, 0);
	}

	@Override
	public void updatePhysics(float time) {
		updatePosition(time);
	}

	public void updatePosition(float time) {

		velocity.add(Vector3d.multiply(Vector3d.add(Vector3d.multiply(force, inverseMass), Vector3d.multiply(standardAcceleration, time)), 1));

		matrix.position.add(Vector3d.multiply(velocity, time));

		force.set(0, 0, 0);

		double height = world.land.getHeight(matrix.getPosition().getXf(), matrix.getPosition().getZf());
		if (matrix.getPosition().getY() < height) {
			matrix.getPosition().setY(height);
			if (velocity.clone().setY(0).getValue() < friction * time) {
				velocity.multiply(0);
			} else {
				Vector3d antiVelocity = velocity.clone().setY(0).invert().normalize().multiply(friction * time);
				//if (antiVelocity.getX() != 0) System.out.println(antiVelocity);
				velocity.add(antiVelocity);
				//velocity.setY(0);
				//velocity.multiplyX(.9);
				//velocity.multiplyZ(.9);
			}
		}
	}
}