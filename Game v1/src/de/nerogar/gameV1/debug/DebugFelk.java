package de.nerogar.gameV1.debug;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.MathHelper;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityPhysic;
import de.nerogar.gameV1.matrix.Matrix;
import de.nerogar.gameV1.matrix.MatrixHelperR3;
import de.nerogar.gameV1.matrix.MatrixMultiplicationException;
import de.nerogar.gameV1.physics.Line;
import de.nerogar.gameV1.sound.ALBufferBank;
import de.nerogar.gameV1.sound.ALSource;
import de.nerogar.gameV1.sound.SoundManager;

public class DebugFelk {

	private Game game;
	public ALSource sound;

	public DebugFelk(Game game) {
		this.game = game;
	}

	public void startup() {

		//SoundManager.instance.preLoadSounds();
		//SoundManager.setListener(new Vector3d(0,0,0), new Vector3d(0,0,0), new Vector3d(0,0,-1), new Vector3d(0,1,0));
		sound = SoundManager.instance.create("forest.ogg", ALSource.PRIORITY_MODERATE, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), true, false, .4f, 1f);
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
			try {
				v2 = Matrix.multiply(m, v).toVector3d();
			} catch (MatrixMultiplicationException e) {
				e.printStackTrace();
			}
			System.out.println(v2.toString());
		}

		if (InputHandler.isKeyDown(Keyboard.KEY_1)) {
			Vector3d a = new Vector3d(10, 10, 10);
			//Vector3d b = MatrixHelper.rotateR3(a, new Vector3d(0, 1, 0), (float) MathHelper.DegToRad(90));
			Vector3d b = MatrixHelperR3.rotateAt(a, new Line(new Vector3d(8, 8, 8), new Vector3d(1, 0, 1)), (float) MathHelper.DegToRad(180));
			System.out.println(b);
		}

	}

	public void end() {
		SoundManager.instance.clear();
		ALBufferBank.instance.clear();
		//bgMusic.destroy();
		//bgMusic2.destroy();
	}

}
