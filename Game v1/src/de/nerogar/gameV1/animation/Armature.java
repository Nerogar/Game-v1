package de.nerogar.gameV1.animation;

import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.matrix.Matrix;
import de.nerogar.gameV1.matrix.Matrix44;

public class Armature {

	public Bone[] bones;
	public Bone rootBone;

	public Armature(Bone rootBone, Bone[] bones) {
		this.rootBone = rootBone;
		this.bones = bones;
	}

	public Vector3d translateMeshVertex(MeshVertex meshVertex) {
		Matrix44 m = new Matrix44();
		for (int i = 0; i < 4; i++) {
			if (meshVertex.boneIDs[i] != Bone.NO_BONE) {
				Bone bone = bones[meshVertex.boneIDs[i]];
				m.add((Matrix44) Matrix.multiply(bone.getGlobalTransformationMatrix(), meshVertex.weights[i]));
			}
		}
		return m.multiply(meshVertex.vector.toMatrix41()).toVector3d();
	}
	
	public void updateTransformationMatrix(MeshVertex meshVertex) {
		Matrix44 m = new Matrix44();
		for (int i = 0; i < 4; i++) {
			if (meshVertex.boneIDs[i] != Bone.NO_BONE) {
				Bone bone = bones[meshVertex.boneIDs[i]];
				m.add((Matrix44) Matrix.multiply(bone.getGlobalTransformationMatrix(), meshVertex.weights[i]));
			}
		}
		meshVertex.transformationMatrix = m;
	}

	public void drawArmature() {
		rootBone.renderBone();
		for (Bone bone : bones) {
			bone.renderBone();
		}
	}

	public void update() {
		markDirty();
		rootBone.update();
		for (Bone bone : bones) {
			if (!bone.isUpdated()) bone.update();
		}
	}

	private void markDirty() {
		rootBone.markDirty();
		for (Bone bone : bones) {
			bone.markDirty();
		}
	}

}
