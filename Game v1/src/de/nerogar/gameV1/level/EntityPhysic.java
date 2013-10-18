package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.network.PacketEntity;
import de.nerogar.gameV1.physics.ObjectMatrix;

public abstract class EntityPhysic extends Entity {

	float inverseMass = 0;
	public Vector3d velocity = new Vector3d(0, 0, 0);
	public Vector3d force = new Vector3d(0, 0, 0);

	public EntityPhysic(Game game, World world, ObjectMatrix matrix, float mass) {
		super(game, world, matrix);
		setMass(mass);
	}

	public void setMass(float mass) {
		if (mass != 0) inverseMass = 1 / mass;
	}

	public float getMass() {
		return 1 / inverseMass;
	}

	@Override
	public void updateServer(float time, ArrayList<PacketEntity> packets) {
		updatePhysics(time);
	}

	@Override
	public void updateClient(float time, ArrayList<PacketEntity> packets) {
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