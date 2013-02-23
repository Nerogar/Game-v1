package de.nerogar.gameV1.physics;

import de.nerogar.gameV1.Vector3d;

public class ObjectMatrix {
	public Vector3d position;
	public Vector3d rotation;
	public Vector3d scaling;

	public ObjectMatrix() {
		position = new Vector3d(0, 0, 0);
		rotation = new Vector3d(0, 0, 0);
		scaling = new Vector3d(1, 1, 1);
	}

	public ObjectMatrix(Vector3d pos) {
		this(pos, new Vector3d(), new Vector3d(1, 1, 1));
	}

	public ObjectMatrix(Vector3d pos, Vector3d rot, Vector3d scal) {
		position = pos;
		rotation = rot;
		scaling = scal;
	}

	public void setPosition(Vector3d vector) {
		position = vector;
	}

	public void addPosition(Vector3d vector) {
		position.add(vector);
	}

	public void resetPosition() {
		position = new Vector3d(0, 0, 0);
	}

	public void setRotation(Vector3d vector) {
		rotation = vector;
	}

	public void addRotation(Vector3d vector) {
		rotation.add(vector);
	}

	public void resetRotation() {
		rotation = new Vector3d(0, 0, 0);
	}

	public void setScaling(Vector3d vector) {
		scaling = vector;
	}

	public void addScaling(Vector3d vector) {
		scaling.add(vector);
	}

	public void resetScaling() {
		scaling = new Vector3d(0, 0, 0);
	}

	public Vector3d getPosition() {
		return position;
	}

	public Vector3d getRotation() {
		return rotation;
	}

	public Vector3d getScaling() {
		return scaling;
	}

	// WICHTIG: Skalierung und Rotationen wurden in folgenden Funktionen u.a. noch nicht eingebaut

	public BoundingAABB getTransformedAABB(BoundingAABB bound) {

		// Rotation fehlt

		BoundingAABB n = new BoundingAABB(new Vector3d(bound.a), new Vector3d(bound.b));

		n.a.multiply(scaling);
		n.b.multiply(scaling);

		n.a.add(position);
		n.b.add(position);

		return n;

	}

	public BoundingCircle getTransformedCircle(BoundingCircle bound) {

		BoundingCircle n = new BoundingCircle(new Vector3d(bound.M), bound.r);

		n.M.add(position);
		n.M.add(position);

		double max = scaling.getX();
		if (scaling.getY() > max) max = scaling.getY();
		if (scaling.getZ() > max) max = scaling.getZ();
		n.r *= max;

		return n;

	}

	// getTransformedOBB fehlt

}