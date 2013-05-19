package de.nerogar.gameV1.physics;

import de.nerogar.gameV1.MathHelper;
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
		this(pos, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1));
	}

	public ObjectMatrix(Vector3d pos, Vector3d rot, Vector3d scal) {
		position = pos;
		scaling = scal;
		rotation = rot;
	}

	public void setPosition(Vector3d vector) {
		position = vector;
	}

	public void setScaling(Vector3d vector) {
		scaling = vector;
	}
	
	public void setRotation(Vector3d vector) {
		rotation = vector;
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

	//TODO WICHTIG: Skalierung und Rotationen wurden in folgenden Funktionen u.a. noch nicht eingebaut

	public BoundingAABB getTransformedAABB(BoundingAABB bound) {

		// Rotation fehlt

		BoundingAABB bounding = new BoundingAABB(new Vector3d(bound.a), new Vector3d(bound.b));

		bounding.a.multiply(scaling);
		bounding.b.multiply(scaling);

		bounding.a.add(position);
		bounding.b.add(position);

		return bounding;

	}

	public BoundingCircle getTransformedCircle(BoundingCircle bound) {

		BoundingCircle bounding = new BoundingCircle(new Vector3d(bound.M), bound.r);

		bounding.M.add(position);
		bounding.M.add(position);

		double highest = MathHelper.getHightest(scaling.getX(), scaling.getY(), scaling.getZ());
		bounding.r *= highest;

		return bounding;

	}

	//TODO getTransformedOBB fehlt

}