package de.nerogar.gameV1.animation;

import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.matrix.Matrix44;

public class MeshVertex {

	public Vector3d vector;
	public int[] boneIDs = new int[4];
	public float[] weights = new float[4];
	private boolean corrupt = false;
	public Matrix44 transformationMatrix;

	public MeshVertex(Vector3d vector, int[] bones, float[] weights) {
		this.vector = vector;
		this.boneIDs = bones;
		this.weights = weights;
		init();
	}

	private void init() {
		if (boneIDs.length != 4 || weights.length != 4) {
			setCorrupt(true);
			return;
		}

		float sum = 0;
		for (int i = 0; i < 4; i++) {
			if (boneIDs[i] == Bone.NO_BONE) weights[i] = 0;
			sum += weights[i];
		}
		if (sum == 0) {
			setCorrupt(true);
			return;
		}
		for (int i = 0; i < 4; i++) {
			weights[i] /= sum;
		}
	}

	public boolean isCorrupt() {
		return corrupt;
	}

	public void setCorrupt(boolean corrupt) {
		this.corrupt = corrupt;
	}

}
