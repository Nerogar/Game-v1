package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.physics.ObjectMatrix;

public abstract class EntityPhysic extends Entity {

	float inverseMass = 0;
	public Vector3d velocity = new Vector3d(0, 0, 0);
	public Vector3d force = new Vector3d(0, 0, 0);

	public EntityPhysic(Game game, ObjectMatrix matrix, float mass) {
		super(game, matrix);
		setMass(mass);
	}

	public void setMass(float mass) {
		if (mass != 0) inverseMass = 1 / mass;
	}

	public float getMass() {
		return 1 / inverseMass;
	}

	public void update(float time) {

		updatePhysics(time);
	}

	public void updatePhysics(float time) {

	}

	public void addForce(Vector3d f) {
		force.add(f);
	}
	
	public void setForce(Vector3d f) {
		force.set(f);
	}
	
}