package de.nerogar.gameV1.animation;

public class Keyframe {

	public static final byte POS_X = 0;
	public static final byte POS_Y = 1;
	public static final byte POS_Z = 2;
	public static final byte SCA_X = 3;
	public static final byte SCA_Y = 4;
	public static final byte SCA_Z = 5;
	public static final byte ROT_X = 6;
	public static final byte ROT_Y = 7;
	public static final byte ROT_Z = 8;

	public static final byte INTERPOLATE_LINEAR = 0;
	public static final byte INTERPOLATE_HOLD = 1;
	public static final byte INTERPOLATE_BEZIER = 2;
	
	public float offset;
	public byte interpolation_type;
	public float value;

	public Keyframe(float offset, float value) {
		this(offset, value, Keyframe.INTERPOLATE_LINEAR);
	}
		
	public Keyframe(float offset, float value, byte interpolation_type) {
		this.offset = offset;
		this.value = value;
		this.interpolation_type = interpolation_type;
	}
	
	public Keyframe clone() {
		return new Keyframe(offset, value, interpolation_type);
	}

}
