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

	public void fromFloatArray(float[] values) {
		if (values.length == 9) {
			position = new Vector3d(values[0], values[1], values[2]);
			rotation = new Vector3d(values[3], values[4], values[5]);
			scaling = new Vector3d(values[6], values[7], values[8]);
		}
	}

	public float[] toFloatArray() {
		float[] values = new float[9];
		values[0] = position.getXf();
		values[1] = position.getYf();
		values[2] = position.getZf();

		values[3] = rotation.getXf();
		values[4] = rotation.getYf();
		values[5] = rotation.getZf();

		values[6] = scaling.getXf();
		values[7] = scaling.getYf();
		values[8] = scaling.getZf();

		return values;
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

	public ObjectMatrix clone() {
		return new ObjectMatrix(position.clone(), rotation.clone(), scaling.clone());
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