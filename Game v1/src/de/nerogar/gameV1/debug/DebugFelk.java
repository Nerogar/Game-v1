package de.nerogar.gameV1.debug;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.animation.Bone;
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

	public DebugFelk(Game game) {
		this.game = game;
	}

	public void startup() {

		//SoundManager.instance.preLoadSounds();
		//SoundManager.setListener(new Vector3d(0,0,0), new Vector3d(0,0,0), new Vector3d(0,0,-1), new Vector3d(0,1,0));
		sound = SoundManager.instance.create("forecast_elevator.ogg", ALSource.PRIORITY_MODERATE, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), true, false, .4f, 1f);

		Bone rootBone = new Bone(null, new Vector3d(5, 5, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		Bone[] bones = new Bone[2];
		bones[0] = new Bone(rootBone, new Vector3d(2, 1, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		bones[1] = new Bone(bones[0], new Vector3d(1, 2, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));
		//bones[2] = new Bone(bones[1], new Vector3d(3, 0, 0), new Vector3d(1, 1, 1), new Vector3d(0, 0, 0));

		testSkelett = new Skeleton(rootBone, bones);

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

		testSkelett.updateSkeleton();

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
