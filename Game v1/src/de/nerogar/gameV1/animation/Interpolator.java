package de.nerogar.gameV1.animation;

import de.nerogar.gameV1.Vector3d;

public class Interpolator {

	public static Keyframe interpolate(Keyframe kf1, Keyframe kf2, double offset) {

		Keyframe interpolated = null;
		double localOffset = (offset - kf1.offset) / (kf2.offset - kf1.offset);
		switch (kf1.interpolation_type) {
		case Keyframe.INTERPOLATE_HOLD:
			interpolated = kf1.clone();
			break;
		case Keyframe.INTERPOLATE_LINEAR:
			Vector3d diffRotation = Vector3d.subtract(kf2.rotation, kf1.rotation);
			Vector3d diffScaling = Vector3d.subtract(kf2.scaling, kf1.scaling);
			interpolated = kf1.clone();
			interpolated.rotation.add(diffRotation.multiply(localOffset));
			interpolated.scaling.add(diffScaling.multiply(localOffset));
			break;
		case Keyframe.INTERPOLATE_BEZIER:
			// TODO noch zu faul
			interpolated = kf1.clone();
			break;
		}

		//System.out.println("rotation: "+interpolated.rotation.toString()+" at local Offset "+Math.round(localOffset*10000)/100f+"%");
		return interpolated;
	}
}
