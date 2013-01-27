package de.nerogar.gameV1.physics;

import de.nerogar.gameV1.Vector3d;

public class Ray extends Line {

	public Ray(Vector3d start, Vector3d direction) {
		super(start, direction);
	}
	
	public String toString() {
		return "|"+super.toString();
	}

}
