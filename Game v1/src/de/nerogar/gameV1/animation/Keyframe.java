package de.nerogar.gameV1.animation;

import de.nerogar.gameV1.Vector3d;

public class Keyframe {

	public float offset;
	public byte interpolation_type;
	public Vector3d rotation, scaling;

	public static final byte INTERPOLATE_LINEAR = 0;
	public static final byte INTERPOLATE_HOLD = 1;
	public static final byte INTERPOLATE_BEZIER = 2;

	public Keyframe(float offset, byte interpolation_type, Vector3d rotation, Vector3d scaling) {
		this.offset = offset;
		this.interpolation_type = interpolation_type;
		this.rotation = rotation;
		this.scaling = scaling;
	}
	
	public Keyframe clone() {
		return new Keyframe(offset, interpolation_type, rotation.clone(), scaling.clone());
	}

}
