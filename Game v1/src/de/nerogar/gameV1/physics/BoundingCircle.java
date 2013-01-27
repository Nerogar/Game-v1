package de.nerogar.gameV1.physics;

import de.nerogar.gameV1.Vector3d;

public class BoundingCircle extends Bounding {

	public float r = 0;
	public Vector3d M = new Vector3d(0, 0, 0);

	public BoundingCircle() {

	}

	public BoundingCircle(Vector3d vM, float vr) {

		set(vM, vr);

	}

	public void set(Vector3d vM, float vr) {
		M = vM;
		r = vr;
	}

	@Override
	public BoundingCircle clone() {
		return new BoundingCircle(M, r);
	}

}
