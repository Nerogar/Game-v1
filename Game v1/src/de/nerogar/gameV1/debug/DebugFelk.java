package de.nerogar.gameV1.debug;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.animation.Animation;
import de.nerogar.gameV1.animation.Bone;
import de.nerogar.gameV1.animation.Keyframe;
import de.nerogar.gameV1.animation.KeyframeSet;
import de.nerogar.gameV1.animation.MeshVertex;
import de.nerogar.gameV1.animation.Skeleton;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityPhysic;
import de.nerogar.gameV1.physics.ObjectMatrix;
import de.nerogar.gameV1.sound.ALBufferBank;
import de.nerogar.gameV1.sound.ALSource;
import de.nerogar.gameV1.sound.SoundManager;

public class DebugFelk {

	private Game game;
	public ALSource sound;
	public Skeleton testSkelett;
	public Animation testAnimation = new Animation();
	public MeshVertex testVertex, testVertex1, testVertexA, testVertexB, testVertex2;
	private Vector3d testVector;

	public DebugFelk(Game game) {
		this.game = game;
	}

	public void startup() {

		//SoundManager.instance.preLoadSounds();
		//SoundManager.setListener(new Vector3d(0,0,0), new Vector3d(0,0,0), new Vector3d(0,0,-1), new Vector3d(0,1,0));
		sound = SoundManager.instance.create("forecast_elevator.ogg", ALSource.PRIORITY_MODERATE, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), true, false, .4f, 1f);

		Bone rootBone = new Bone(null, 0, new Vector3d(5, 5, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));

		Bone arm1 = new Bone(rootBone, 3);
		Bone arm2 = new Bone(arm1, 3);
		Bone wrist = new Bone(arm2, 1);
		Bone thumbsplit = new Bone(wrist, 0.6, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(1, -1.1, 0));
		Bone fingersplit1 = new Bone(wrist, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, -0.45, 0));
		Bone fingersplit2 = new Bone(wrist, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, -0.15, 0));
		Bone fingersplit3 = new Bone(wrist, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0.15, 0));
		Bone fingersplit4 = new Bone(wrist, 0.8, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0.45, 0));
		Bone thumbA = new Bone(thumbsplit, 0.6);
		Bone thumbB = new Bone(thumbA, 0.6);
		Bone finger1A = new Bone(fingersplit1, 0.8);
		Bone finger1B = new Bone(finger1A, 0.8);
		Bone finger2A = new Bone(fingersplit2, 0.8);
		Bone finger2B = new Bone(finger2A, 0.8);
		Bone finger3A = new Bone(fingersplit3, 0.8);
		Bone finger3B = new Bone(finger3A, 0.8);
		Bone finger4A = new Bone(fingersplit4, 0.8);
		Bone finger4B = new Bone(finger4A, 0.8);

		Bone[] bones = new Bone[] { arm1, arm2, wrist, thumbsplit, fingersplit1, fingersplit2, fingersplit3, fingersplit4, thumbA, thumbB, finger1A, finger1B, finger2A, finger2B, finger3A, finger3B, finger4A, finger4B };

		//bones[0] = new Bone(rootBone, 5, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, .2));
		//bones[1] = new Bone(bones[0], 5, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, -.4, 0));
		//bones[2] = new Bone(bones[1], 5, new Vector3d(0, 0, 0), new Vector3d(1, 1, 1), new Vector3d(.1, 0, 0));

		testSkelett = new Skeleton(rootBone, bones);
		testSkelett.update();

		KeyframeSet kfSet0 = new KeyframeSet();
		//kfSet0.addOverallKeyframe(new ObjectMatrix(), 0, KeyframeNew.INTERPOLATE_LINEAR);
		kfSet0.addKeyframes(Keyframe.ROT_X, new Keyframe(0.1f, -1.57f), new Keyframe(0.9f, -1.57f));
		kfSet0.addKeyframes(Keyframe.ROT_Y, new Keyframe(0.1f, -1.57f), new Keyframe(0.9f, -1.57f));
		//kfSet0.addOverallKeyframe(new ObjectMatrix(), 1, KeyframeNew.INTERPOLATE_LINEAR);
		testAnimation.keyframesMap.put(bones[0], kfSet0);

		KeyframeSet kfSetThumbA = new KeyframeSet();
		//kfSetThumbA.addOverallKeyframe(new ObjectMatrix(), 0, KeyframeNew.INTERPOLATE_LINEAR);
		kfSetThumbA.addKeyframes(Keyframe.ROT_Z, new Keyframe(0.1f, -1.57f), new Keyframe(0.9f, -1.57f));
		//kfSetThumbA.addOverallKeyframe(new ObjectMatrix(), 1, KeyframeNew.INTERPOLATE_LINEAR);
		testAnimation.keyframesMap.put(thumbA, kfSetThumbA);
		KeyframeSet kfSetThumbB = new KeyframeSet();
		kfSetThumbB.addKeyframes(Keyframe.ROT_Z, new Keyframe(0.1f, -1.57f), new Keyframe(0.9f, -1.57f));
		testAnimation.keyframesMap.put(thumbB, kfSetThumbB);

		KeyframeSet kfSetFinger1A = new KeyframeSet();
		kfSetFinger1A.addOverallKeyframe(new ObjectMatrix(), 0.1f, Keyframe.INTERPOLATE_LINEAR);
		kfSetFinger1A.addKeyframes(Keyframe.ROT_Z, new Keyframe(0.2f, -1.57f), new Keyframe(0.5f, -1.57f));
		kfSetFinger1A.addOverallKeyframe(new ObjectMatrix(), 0.6f, Keyframe.INTERPOLATE_LINEAR);
		testAnimation.keyframesMap.put(finger1A, kfSetFinger1A);
		KeyframeSet kfSetFinger1B = new KeyframeSet();
		kfSetFinger1B.addOverallKeyframe(new ObjectMatrix(), 0.1f, Keyframe.INTERPOLATE_LINEAR);
		kfSetFinger1B.addKeyframes(Keyframe.ROT_Z, new Keyframe(0.2f, -1.57f), new Keyframe(0.5f, -1.57f));
		kfSetFinger1B.addOverallKeyframe(new ObjectMatrix(), 0.6f, Keyframe.INTERPOLATE_LINEAR);
		testAnimation.keyframesMap.put(finger1B, kfSetFinger1B);

		KeyframeSet kfSetFinger2A = new KeyframeSet();
		kfSetFinger2A.addOverallKeyframe(new ObjectMatrix(), 0.2f, Keyframe.INTERPOLATE_LINEAR);
		kfSetFinger2A.addKeyframes(Keyframe.ROT_Z, new Keyframe(0.3f, -1.57f), new Keyframe(0.6f, -1.57f));
		kfSetFinger2A.addOverallKeyframe(new ObjectMatrix(), 0.7f, Keyframe.INTERPOLATE_LINEAR);
		testAnimation.keyframesMap.put(finger2A, kfSetFinger2A);
		KeyframeSet kfSetFinger2B = new KeyframeSet();
		kfSetFinger2B.addOverallKeyframe(new ObjectMatrix(), 0.2f, Keyframe.INTERPOLATE_LINEAR);
		kfSetFinger2B.addKeyframes(Keyframe.ROT_Z, new Keyframe(0.3f, -1.57f), new Keyframe(0.6f, -1.57f));
		kfSetFinger2B.addOverallKeyframe(new ObjectMatrix(), 0.7f, Keyframe.INTERPOLATE_LINEAR);
		testAnimation.keyframesMap.put(finger2B, kfSetFinger2B);

		KeyframeSet kfSetFinger3A = new KeyframeSet();
		kfSetFinger3A.addOverallKeyframe(new ObjectMatrix(), 0.3f, Keyframe.INTERPOLATE_LINEAR);
		kfSetFinger3A.addKeyframes(Keyframe.ROT_Z, new Keyframe(0.4f, -1.57f), new Keyframe(0.7f, -1.57f));
		kfSetFinger3A.addOverallKeyframe(new ObjectMatrix(), 0.8f, Keyframe.INTERPOLATE_LINEAR);
		testAnimation.keyframesMap.put(finger3A, kfSetFinger3A);
		KeyframeSet kfSetFinger3B = new KeyframeSet();
		kfSetFinger3B.addOverallKeyframe(new ObjectMatrix(), 0.3f, Keyframe.INTERPOLATE_LINEAR);
		kfSetFinger3B.addKeyframes(Keyframe.ROT_Z, new Keyframe(0.4f, -1.57f), new Keyframe(0.7f, -1.57f));
		kfSetFinger3B.addOverallKeyframe(new ObjectMatrix(), 0.8f, Keyframe.INTERPOLATE_LINEAR);
		testAnimation.keyframesMap.put(finger3B, kfSetFinger3B);

		KeyframeSet kfSetFinger4A = new KeyframeSet();
		kfSetFinger4A.addOverallKeyframe(new ObjectMatrix(), 0.4f, Keyframe.INTERPOLATE_LINEAR);
		kfSetFinger4A.addKeyframes(Keyframe.ROT_Z, new Keyframe(0.5f, -1.57f), new Keyframe(0.8f, -1.57f));
		kfSetFinger4A.addOverallKeyframe(new ObjectMatrix(), 0.9f, Keyframe.INTERPOLATE_LINEAR);
		testAnimation.keyframesMap.put(finger4A, kfSetFinger4A);
		KeyframeSet kfSetFinger4B = new KeyframeSet();
		kfSetFinger4B.addOverallKeyframe(new ObjectMatrix(), 0.4f, Keyframe.INTERPOLATE_LINEAR);
		kfSetFinger4B.addKeyframes(Keyframe.ROT_Z, new Keyframe(0.5f, -1.57f), new Keyframe(0.8f, -1.57f));
		kfSetFinger4B.addOverallKeyframe(new ObjectMatrix(), 0.9f, Keyframe.INTERPOLATE_LINEAR);
		testAnimation.keyframesMap.put(finger4B, kfSetFinger4B);

		testAnimation.length = 15000;

		testVertex = new MeshVertex(new Vector3d(8, 6, 0), new int[] { 0, 11, -1, -1 }, new float[] { 1, 1, 0, 0 });

		testVertex1 = new MeshVertex(new Vector3d(5, 5, 0), new int[] { 0, -1, -1, -1 }, new float[] { 1, 0, 0, 0 });
		testVertexA = new MeshVertex(new Vector3d(8, 6, 0), new int[] { 0, 1, -1, -1 }, new float[] { 1, 1, 0, 0 });
		testVertexB = new MeshVertex(new Vector3d(8, 4, 0), new int[] { 0, 1, -1, -1 }, new float[] { 1, 1, 0, 0 });
		testVertex2 = new MeshVertex(new Vector3d(11, 5, 0), new int[] { 1, -1, -1, -1 }, new float[] { 1, 0, 0, 0 });

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
			testSkelett.bones[1].relative.rotation.add(new Vector3d(0, 0, MathHelper.DegToRad(1)));
		}
		
		if (InputHandler.isKeyDown(Keyboard.KEY_1)) {
			long time1 = System.nanoTime();
			int iter = 10000;
			for (int i = 0; i < iter; i++) {
				//testSkelett.translateMeshVertex(testVertex);
				testSkelett.update();
			}
			long time = (System.nanoTime()-time1)/1000000;
			System.out.println(iter+" iterations took "+time+" ms");
		}

		if (InputHandler.isKeyPressed(Keyboard.KEY_J)) {
			testAnimation.play();
		}

		testAnimation.update();
		testSkelett.update();
		testVector = testSkelett.translateMeshVertex(testVertex);

	}

	public void end() {
		SoundManager.instance.clear();
		ALBufferBank.instance.clear();
		//bgMusic.destroy();
		//bgMusic2.destroy();
	}

	public void additionalRender() {
		testSkelett.drawSkeleton();
		testVector.render();
		RenderHelper.drawTriangle(testSkelett.translateMeshVertex(testVertex1), testSkelett.translateMeshVertex(testVertexA), testSkelett.translateMeshVertex(testVertexB), 0xFE33BA99);
		RenderHelper.drawTriangle(testSkelett.translateMeshVertex(testVertex1), testSkelett.translateMeshVertex(testVertexB), testSkelett.translateMeshVertex(testVertexA), 0xFE33BA99);
		RenderHelper.drawTriangle(testSkelett.translateMeshVertex(testVertex2), testSkelett.translateMeshVertex(testVertexA), testSkelett.translateMeshVertex(testVertexB), 0xFE33BA99);
		RenderHelper.drawTriangle(testSkelett.translateMeshVertex(testVertex2), testSkelett.translateMeshVertex(testVertexB), testSkelett.translateMeshVertex(testVertexA), 0xFE33BA99);
	}

}
