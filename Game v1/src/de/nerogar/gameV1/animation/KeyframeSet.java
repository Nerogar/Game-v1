package de.nerogar.gameV1.animation;

import java.util.ArrayList;
import java.util.Collections;

import de.nerogar.gameV1.Vector3d;

public class KeyframeSet {

	private ArrayList<Keyframe> keyframes = new ArrayList<Keyframe>();

	public KeyframeSet() {

	}

	public void addKeyframes(Keyframe... kfs) {
		for (Keyframe keyframe : kfs)
			this.keyframes.add(keyframe);
		sortKeyframes();
	}

	private void sortKeyframes() {
		Collections.sort(keyframes, new KeyframeComparator());
	}

	public Keyframe getInterpolatedKeyframe(double offset) {
		Keyframe kf1 = new Keyframe(0, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1));
		Keyframe kf2 = new Keyframe(1, Keyframe.INTERPOLATE_HOLD, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1));
		int i;
		for (i = 0; i < keyframes.size(); i++) {
			kf2 = keyframes.get(i).clone();
			if (kf2.offset > offset)
				break;
			else if (i == keyframes.size() - 1) {
				kf1 = kf2.clone();
				kf2 = new Keyframe(0, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1));
				kf2.offset = 1;
				break;
			}
			kf1 = kf2.clone();
		}
		return Interpolator.interpolate(kf1, kf2, offset);
	}
}
