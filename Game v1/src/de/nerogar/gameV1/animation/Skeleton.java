package de.nerogar.gameV1.animation;

public class Skeleton {

	public Bone[] bones;
	public Bone rootBone;
	
	public Skeleton(Bone rootBone, Bone[] bones) {
		this.rootBone = rootBone;
		this.bones = bones;
	}
	
	public void drawSkeleton() {
		//rootBone.renderBone();
		for(Bone bone: bones) {
			bone.renderBone();
		}
	}
	
	public void updateSkeleton() {
		rootBone.updateLocalAxes();
		for(Bone bone: bones) {
			bone.updateLocalAxes();
		}
	}
	
}
