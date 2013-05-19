package de.nerogar.gameV1.animation;

import java.util.ArrayList;
import java.util.Collections;

import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class KeyframeSet {

	private ArrayList<Keyframe> kf_pos_x = new ArrayList<Keyframe>();
	private ArrayList<Keyframe> kf_pos_y = new ArrayList<Keyframe>();
	private ArrayList<Keyframe> kf_pos_z = new ArrayList<Keyframe>();
	private ArrayList<Keyframe> kf_sca_x = new ArrayList<Keyframe>();
	private ArrayList<Keyframe> kf_sca_y = new ArrayList<Keyframe>();
	private ArrayList<Keyframe> kf_sca_z = new ArrayList<Keyframe>();
	private ArrayList<Keyframe> kf_rot_x = new ArrayList<Keyframe>();
	private ArrayList<Keyframe> kf_rot_y = new ArrayList<Keyframe>();
	private ArrayList<Keyframe> kf_rot_z = new ArrayList<Keyframe>();

	public KeyframeSet() {

	}

	public void addKeyframes(byte kfType, Keyframe... kfs) {
		addKeyframes(getArraylistForType(kfType), kfs);
	}
	
	public void addOverallKeyframe(ObjectMatrix values, float offset, byte interpolation_type) {
		addKeyframes(Keyframe.POS_X, new Keyframe(offset, values.position.getXf(), interpolation_type));
		addKeyframes(Keyframe.POS_Y, new Keyframe(offset, values.position.getYf(), interpolation_type));
		addKeyframes(Keyframe.POS_Z, new Keyframe(offset, values.position.getZf(), interpolation_type));
		addKeyframes(Keyframe.SCA_X, new Keyframe(offset, values.scaling.getXf(), interpolation_type));
		addKeyframes(Keyframe.SCA_Y, new Keyframe(offset, values.scaling.getYf(), interpolation_type));
		addKeyframes(Keyframe.SCA_Z, new Keyframe(offset, values.scaling.getZf(), interpolation_type));
		addKeyframes(Keyframe.ROT_X, new Keyframe(offset, values.rotation.getXf(), interpolation_type));
		addKeyframes(Keyframe.ROT_Y, new Keyframe(offset, values.rotation.getYf(), interpolation_type));
		addKeyframes(Keyframe.ROT_Z, new Keyframe(offset, values.rotation.getZf(), interpolation_type));
	}

	public void addKeyframes(ArrayList<Keyframe> arraylist, Keyframe... kfs) {
		for (Keyframe keyframe : kfs)
			arraylist.add(keyframe);
		sortKeyframes(arraylist);
	}

	public ArrayList<Keyframe> getArraylistForType(byte kfType) {
		switch (kfType) {
		case Keyframe.POS_X:
			return kf_pos_x;
		case Keyframe.POS_Y:
			return kf_pos_y;
		case Keyframe.POS_Z:
			return kf_pos_z;
		case Keyframe.SCA_X:
			return kf_sca_x;
		case Keyframe.SCA_Y:
			return kf_sca_y;
		case Keyframe.SCA_Z:
			return kf_sca_z;
		case Keyframe.ROT_X:
			return kf_rot_x;
		case Keyframe.ROT_Y:
			return kf_rot_y;
		case Keyframe.ROT_Z:
			return kf_rot_z;
		}
		return new ArrayList<Keyframe>();
	}

	private void sortKeyframes(ArrayList<Keyframe> arraylist) {
		Collections.sort(arraylist, new KeyframeComparator());
	}

	/*public ObjectMatrix getInterpolatedKeyframe(double offset) {

		Keyframe kf1 = null, kf2 = null;
		if (keyframes.get(keyframes.size() - 1).offset < 1f) {
			keyframes.add(new Keyframe(1f, Keyframe.INTERPOLATE_HOLD, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(1, 1, 1)));
		}
		if (keyframes.get(0).offset > 0f) {
			addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), new Vector3d(1, 1, 1)));
		}
		//Keyframe kf1 = new Keyframe(0, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1));
		//Keyframe kf2 = new Keyframe(1, Keyframe.INTERPOLATE_HOLD, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1));
		for (int i = 0; i < keyframes.size(); i++) {
			kf2 = keyframes.get(i).clone();
			if (kf2.offset > offset)
				break;
			else if (i == keyframes.size() - 1) {
				kf1 = keyframes.get(keyframes.size() - 2);
				kf2 = keyframes.get(keyframes.size() - 1);
				break;
			}
			kf1 = kf2.clone();
		}
		ObjectMatrix interpolated = Interpolator.interpolate(kf1, kf2, offset);
		return interpolated;
	}*/

	public ObjectMatrix getInterpolatedObjectMatrix(float offset) {
		ObjectMatrix interpolated = new ObjectMatrix();
		float pos_x = getInterpolatedValue(offset, kf_pos_x, 0);
		float pos_y = getInterpolatedValue(offset, kf_pos_y, 0);
		float pos_z = getInterpolatedValue(offset, kf_pos_z, 0);
		float sca_x = getInterpolatedValue(offset, kf_sca_x, 1);
		float sca_y = getInterpolatedValue(offset, kf_sca_y, 1);
		float sca_z = getInterpolatedValue(offset, kf_sca_z, 1);
		float rot_x = getInterpolatedValue(offset, kf_rot_x, 0);
		float rot_y = getInterpolatedValue(offset, kf_rot_y, 0);
		float rot_z = getInterpolatedValue(offset, kf_rot_z, 0);
		interpolated.position = new Vector3d(pos_x, pos_y, pos_z);
		interpolated.scaling = new Vector3d(sca_x, sca_y, sca_z);
		interpolated.rotation = new Vector3d(rot_x, rot_y, rot_z);
		return interpolated;
	}

	public float getInterpolatedValue(float offset, ArrayList<Keyframe> keyframes, float def) {
		if (keyframes.size() == 0) return def;
		Keyframe kf1 = null, kf2 = null;
		if (keyframes.get(keyframes.size() - 1).offset < 1f) {
			keyframes.add(new Keyframe(1f, def, Keyframe.INTERPOLATE_HOLD));
		}
		if (keyframes.get(0).offset > 0f) {
			addKeyframes(keyframes, new Keyframe(0f, def, Keyframe.INTERPOLATE_LINEAR));
		}
		for (int i = 0; i < keyframes.size(); i++) {
			kf2 = keyframes.get(i).clone();
			if (kf2.offset > offset)
				break;
			else if (i == keyframes.size() - 1) {
				kf1 = keyframes.get(keyframes.size() - 2);
				kf2 = keyframes.get(keyframes.size() - 1);
				break;
			}
			kf1 = kf2.clone();
		}
		return Interpolator.interpolateValue(kf1, kf2, offset);
	}
}
