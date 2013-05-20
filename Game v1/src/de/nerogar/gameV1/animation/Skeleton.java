package de.nerogar.gameV1.animation;

import de.nerogar.gameV1.Vector3d;

public class Skeleton {

	public Bone[] bones;
	public Bone rootBone;
	
	public Skeleton(Bone rootBone, Bone[] bones) {
		this.rootBone = rootBone;
		this.bones = bones;
	}
	
	public Vector3d translateMeshVertex(MeshVertex meshVertex) {
		Vector3d v = new Vector3d(0, 0, 0);
		for(int i = 0; i < 4; i++) {
			if (meshVertex.boneIDs[i] != Bone.NO_BONE) {
				Bone bone = bones[meshVertex.boneIDs[i]];
				Vector3d local = Vector3d.subtract(meshVertex.vector, bone.transPos);
				Vector3d translated = bone.transform(local);
				v.add(translated.multiply(meshVertex.weights[i]));
			}
		}
		return v;
	}
	
	public void drawSkeleton() {
		rootBone.renderBone();
		for(Bone bone: bones) {
			bone.renderBone();
		}
	}
	
	public void update() {
		markDirty();
		rootBone.update();
		for(Bone bone: bones) {
			if (!bone.isUpdated()) bone.update();
		}
	}

	private void markDirty() {
		rootBone.markDirty();
		for(Bone bone: bones) {
			bone.markDirty();
		}
	}
	
}
