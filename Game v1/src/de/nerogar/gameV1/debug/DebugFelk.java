package de.nerogar.gameV1.debug;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.animation.Animation;
import de.nerogar.gameV1.animation.Bone;
import de.nerogar.gameV1.animation.Keyframe;
import de.nerogar.gameV1.animation.KeyframeSet;
import de.nerogar.gameV1.animation.Skeleton;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityPhysic;
import de.nerogar.gameV1.matrix.Matrix;
import de.nerogar.gameV1.sound.ALBufferBank;
import de.nerogar.gameV1.sound.ALSource;
import de.nerogar.gameV1.sound.SoundManager;

public class DebugFelk {

	private Game game;
	public ALSource sound;
	public Skeleton testSkelett;
	public Animation testAnimation = new Animation();

	public DebugFelk(Game game) {
		this.game = game;
	}

	public void startup() {

		//SoundManager.instance.preLoadSounds();
		//SoundManager.setListener(new Vector3d(0,0,0), new Vector3d(0,0,0), new Vector3d(0,0,-1), new Vector3d(0,1,0));
		sound = SoundManager.instance.create("forecast_elevator.ogg", ALSource.PRIORITY_MODERATE, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), true, false, .4f, 1f);

		Bone rootBone = new Bone(null, 0, new Vector3d(5, 5, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));

		Bone arm1 = new Bone(rootBone, 3, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone arm2 = new Bone(arm1, 3, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone wrist = new Bone(arm2, 1, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone thumbsplit = new Bone(wrist, 0.6, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(1, -1.1, 0));
		Bone fingersplit1 = new Bone(wrist, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, -0.45, 0));
		Bone fingersplit2 = new Bone(wrist, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, -0.15, 0));
		Bone fingersplit3 = new Bone(wrist, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0.15, 0));
		Bone fingersplit4 = new Bone(wrist, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0.45, 0));
		Bone thumbA = new Bone(thumbsplit, 0.6, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone thumbB = new Bone(thumbA, 0.6, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone finger1A = new Bone(fingersplit1, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone finger1B = new Bone(finger1A, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone finger2A = new Bone(fingersplit2, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone finger2B = new Bone(finger2A, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone finger3A = new Bone(fingersplit3, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone finger3B = new Bone(finger3A, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone finger4A = new Bone(fingersplit4, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone finger4B = new Bone(finger4A, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));

		Bone[] bones = new Bone[]{arm1, arm2, wrist, thumbsplit, fingersplit1, fingersplit2, fingersplit3, fingersplit4,
				thumbA, thumbB, finger1A, finger1B, finger2A, finger2B, finger3A, finger3B, finger4A, finger4B};
		
		//bones[0] = new Bone(rootBone, 5, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, .2));
		//bones[1] = new Bone(bones[0], 5, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, -.4, 0));
		//bones[2] = new Bone(bones[1], 5, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(.1, 0, 0));

		testSkelett = new Skeleton(rootBone, bones);

		KeyframeSet kfSet0 = new KeyframeSet();
		kfSet0.addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, bones[0].defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.1f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(-1.57, -1.57, 0), new Vector3d(1, 1, 1)),
			new Keyframe(0.9f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(-1.57, -1.57, 0), new Vector3d(1, 1, 1)),
			new Keyframe(1f, Keyframe.INTERPOLATE_LINEAR, bones[1].defRotation, new Vector3d(1, 1, 1)));
		testAnimation.keyframesMap.put(bones[0], kfSet0);
		
		KeyframeSet kfSetThumbA = new KeyframeSet();
		kfSetThumbA.addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, thumbA.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.1f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.9f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(1f, Keyframe.INTERPOLATE_LINEAR, thumbA.defRotation, new Vector3d(1, 1, 1)));
		testAnimation.keyframesMap.put(thumbA, kfSetThumbA);
		KeyframeSet kfSetThumbB = new KeyframeSet();
		kfSetThumbB.addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, thumbB.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.1f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.9f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(1f, Keyframe.INTERPOLATE_LINEAR, thumbB.defRotation, new Vector3d(1, 1, 1)));
		testAnimation.keyframesMap.put(thumbB, kfSetThumbB);
		
		KeyframeSet kfSetFinger1A = new KeyframeSet();
		kfSetFinger1A.addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, finger1A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.1f, Keyframe.INTERPOLATE_LINEAR, finger1A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.2f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.5f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.6f, Keyframe.INTERPOLATE_LINEAR, finger1A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(1f, Keyframe.INTERPOLATE_LINEAR, finger1A.defRotation, new Vector3d(1, 1, 1)));
		testAnimation.keyframesMap.put(finger1A, kfSetFinger1A);
		KeyframeSet kfSetFinger1B = new KeyframeSet();
		kfSetFinger1B.addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, finger1B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.1f, Keyframe.INTERPOLATE_LINEAR, finger1B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.2f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.5f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.6f, Keyframe.INTERPOLATE_LINEAR, finger1B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(1f, Keyframe.INTERPOLATE_LINEAR, finger1B.defRotation, new Vector3d(1, 1, 1)));
		testAnimation.keyframesMap.put(finger1B, kfSetFinger1B);
		
		KeyframeSet kfSetFinger2A = new KeyframeSet();
		kfSetFinger2A.addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, finger2A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.2f, Keyframe.INTERPOLATE_LINEAR, finger2A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.3f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.6f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.7f, Keyframe.INTERPOLATE_LINEAR, finger2A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(1f, Keyframe.INTERPOLATE_LINEAR, finger2A.defRotation, new Vector3d(1, 1, 1)));
		testAnimation.keyframesMap.put(finger2A, kfSetFinger2A);
		KeyframeSet kfSetFinger2B = new KeyframeSet();
		kfSetFinger2B.addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, finger2B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.2f, Keyframe.INTERPOLATE_LINEAR, finger2B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.3f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.6f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.7f, Keyframe.INTERPOLATE_LINEAR, finger2B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(1f, Keyframe.INTERPOLATE_LINEAR, finger2B.defRotation, new Vector3d(1, 1, 1)));
		testAnimation.keyframesMap.put(finger2B, kfSetFinger2B);
		
		KeyframeSet kfSetFinger3A = new KeyframeSet();
		kfSetFinger3A.addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, finger3A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.3f, Keyframe.INTERPOLATE_LINEAR, finger3A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.4f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.7f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.8f, Keyframe.INTERPOLATE_LINEAR, finger3A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(1f, Keyframe.INTERPOLATE_LINEAR, finger3A.defRotation, new Vector3d(1, 1, 1)));
		testAnimation.keyframesMap.put(finger3A, kfSetFinger3A);
		KeyframeSet kfSetFinger3B = new KeyframeSet();
		kfSetFinger3B.addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, finger3B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.3f, Keyframe.INTERPOLATE_LINEAR, finger3B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.4f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.7f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.8f, Keyframe.INTERPOLATE_LINEAR, finger3B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(1f, Keyframe.INTERPOLATE_LINEAR, finger3B.defRotation, new Vector3d(1, 1, 1)));
		testAnimation.keyframesMap.put(finger3B, kfSetFinger3B);
		
		KeyframeSet kfSetFinger4A = new KeyframeSet();
		kfSetFinger4A.addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, finger4A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.4f, Keyframe.INTERPOLATE_LINEAR, finger4A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.5f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.8f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.9f, Keyframe.INTERPOLATE_LINEAR, finger4A.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(1f, Keyframe.INTERPOLATE_LINEAR, finger4A.defRotation, new Vector3d(1, 1, 1)));
		testAnimation.keyframesMap.put(finger4A, kfSetFinger4A);
		KeyframeSet kfSetFinger4B = new KeyframeSet();
		kfSetFinger4B.addKeyframes(new Keyframe(0f, Keyframe.INTERPOLATE_LINEAR, finger4B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.4f, Keyframe.INTERPOLATE_LINEAR, finger4B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(0.5f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.8f, Keyframe.INTERPOLATE_LINEAR, new Vector3d(0, 0, -1.57), new Vector3d(1, 1, 1)),
			new Keyframe(0.9f, Keyframe.INTERPOLATE_LINEAR, finger4B.defRotation, new Vector3d(1, 1, 1)),
			new Keyframe(1f, Keyframe.INTERPOLATE_LINEAR, finger4B.defRotation, new Vector3d(1, 1, 1)));
		testAnimation.keyframesMap.put(finger4B, kfSetFinger4B);
		
		testAnimation.length = 3000;

		//if (sound != null) sound.play();
		//AL10.alDopplerVelocity(320f);
	}

	public void run() {

		sound.setPosition(game.world.camera.getCamPosition());
		SoundManager.setListenerLazy(game.world.camera);
		SoundManager.instance.update();

		if (InputHandler.isKeyPressed(Keyboard.KEY_U)) {
			ArrayList<Entity> entities = game.world.entityList.entities;
			System.out.println(entities.size() + " entities gefunden.");
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i) instanceof EntityPhysic) {
					EntityPhysic entity = (EntityPhysic) entities.get(i);
					entity.addForce(new Vector3d(0, 5, 0));
				}
			}
		}

		if (InputHandler.isKeyDown(Keyboard.KEY_0)) {
			Matrix m = new Matrix(3, 3);
			m.set(0, 2, 1);
			m.set(1, 1, 1);
			m.set(2, 0, 1);
			Matrix v = new Vector3d(2, 3, 4).toMatrix();
			Vector3d v2 = null;
			v2 = Matrix.multiply(m, v).toVector3d();
			System.out.println(v2.toString());
		}

		if (InputHandler.isKeyPressed(Keyboard.KEY_J)) {
			testAnimation.play();
		}

		if (InputHandler.isKeyDown(Keyboard.KEY_X)) {
			testSkelett.rootBone.relRotation.add(new Vector3d(MathHelper.DegToRad(1), 0, 0));
		}

		if (InputHandler.isKeyDown(Keyboard.KEY_Y)) {
			testSkelett.rootBone.relRotation.add(new Vector3d(0, MathHelper.DegToRad(1), 0));
		}

		if (InputHandler.isKeyDown(Keyboard.KEY_Z)) {
			testSkelett.rootBone.relRotation.add(new Vector3d(0, 0, MathHelper.DegToRad(1)));
		}

		if (InputHandler.isKeyDown(Keyboard.KEY_1)) {
			testSkelett.bones[1].relRotation.add(new Vector3d(MathHelper.DegToRad(1), 0, 0));
		}

		if (InputHandler.isKeyDown(Keyboard.KEY_2)) {
			testSkelett.bones[1].relRotation.add(new Vector3d(0, MathHelper.DegToRad(1), 0));
		}

		if (InputHandler.isKeyDown(Keyboard.KEY_3)) {
			testSkelett.bones[1].relRotation.add(new Vector3d(0, 0, MathHelper.DegToRad(1)));
		}

		testAnimation.update();
		testSkelett.update();

	}

	public void end() {
		SoundManager.instance.clear();
		ALBufferBank.instance.clear();
		//bgMusic.destroy();
		//bgMusic2.destroy();
	}

	public void additionalRender() {
		testSkelett.drawSkeleton();
	}

}
