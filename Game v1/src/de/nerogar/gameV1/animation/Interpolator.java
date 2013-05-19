package de.nerogar.gameV1.animation;

public class Interpolator {

	/*public static ObjectMatrix interpolate(Keyframe kf1, Keyframe kf2, double offset) {

		ObjectMatrix interpolated = new ObjectMatrix(kf1.position.clone(), kf1.rotation.clone(), kf1.scaling.clone());
		double localOffset = (offset - kf1.offset) / (kf2.offset - kf1.offset);
		switch (kf1.interpolation_type) {
		case Keyframe.INTERPOLATE_HOLD:
			break;
		case Keyframe.INTERPOLATE_LINEAR:
			Vector3d diffPosition = Vector3d.subtract(kf2.position, kf1.position);
			Vector3d diffRotation = Vector3d.subtract(kf2.rotation, kf1.rotation);
			Vector3d diffScaling = Vector3d.subtract(kf2.scaling, kf1.scaling);
			interpolated.position.add(diffPosition.multiply(localOffset));
			interpolated.rotation.add(diffRotation.multiply(localOffset));
			interpolated.scaling.add(diffScaling.multiply(localOffset));
			break;
		case Keyframe.INTERPOLATE_BEZIER:
			// TODO noch zu faul
			break;
		}

		//System.out.println("rotation: "+interpolated.rotation.toString()+" at local Offset "+Math.round(localOffset*10000)/100f+"%");
		return interpolated;
	}*/

	public static float interpolateValue(Keyframe kf1, Keyframe kf2, float offset) {
		float interpolated = 0;
		float localOffset = (offset - kf1.offset) / (kf2.offset - kf1.offset);
		switch (kf1.interpolation_type) {
		case Keyframe.INTERPOLATE_HOLD:
			interpolated = kf1.value;
			break;
		case Keyframe.INTERPOLATE_LINEAR:
			float diff = kf2.value - kf1.value;
			interpolated = kf1.value + diff * localOffset;
			break;
		case Keyframe.INTERPOLATE_BEZIER:
			// TODO noch zu faul
			interpolated = kf1.value;
			break;
		}
		return interpolated;
	}

}
