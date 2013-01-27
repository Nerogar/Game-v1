package de.nerogar.gameV1.physics;

import de.nerogar.gameV1.Vector3d;

public class BoundingOBB extends Bounding {

	public Vector3d[] p;

	public BoundingOBB() {
		for (int i = 0; i < 8; i++) {
			p[i] = new Vector3d(0f, 0f, 0f);
		}

	}

	public BoundingOBB(Vector3d[] p) {
		for (int i = 0; i < 8; i++) {
			this.p[i] = p[i];
		}
	}

	@Override
	public BoundingOBB clone() {
		return new BoundingOBB(p);
	}

}