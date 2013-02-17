package de.nerogar.gameV1.debug;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityPhysic;
import de.nerogar.gameV1.level.EntityTestparticle;
import de.nerogar.gameV1.physics.ObjectMatrix;
import de.nerogar.gameV1.sound.ALBufferBank;
import de.nerogar.gameV1.sound.ALSource;
import de.nerogar.gameV1.sound.SoundManager;

public class DebugFelk {

	private Game game;
	//public Testsound bgMusic;
	//public Testsound bgMusic2;
	public ALSource sound;
	public int selectedBuildingID = 0;

	public DebugFelk(Game game) {
		this.game = game;
	}

	public void startup() {

		//SoundManager.instance.preLoadSounds();
		//SoundManager.setListener(new Vector3d(0,0,0), new Vector3d(0,0,0), new Vector3d(0,0,-1), new Vector3d(0,1,0));
		sound = SoundManager.instance.create("forest.ogg", ALSource.PRIORITY_MODERATE, new Vector3d(0, 0, 0), new Vector3d(0, 0, 0), true, false, .4f, 1f);
		if (sound != null) sound.play();
		//AL10.alDopplerVelocity(320f);
	}

	public void run() {

		sound.setPosition(game.world.camera.getCamPosition());
		SoundManager.setListenerLazy(game.world.camera);
		SoundManager.instance.update();


		if (InputHandler.isKeyPressed(Keyboard.KEY_0)) selectedBuildingID=0;
		if (InputHandler.isKeyPressed(Keyboard.KEY_1)) selectedBuildingID=1;
		if (InputHandler.isKeyPressed(Keyboard.KEY_2)) selectedBuildingID=2;
		if (InputHandler.isKeyPressed(Keyboard.KEY_3)) selectedBuildingID=3;
		if (InputHandler.isKeyPressed(Keyboard.KEY_4)) selectedBuildingID=4;
		if (InputHandler.isKeyPressed(Keyboard.KEY_5)) selectedBuildingID=5;

		if (InputHandler.isKeyPressed(Keyboard.KEY_U)) {
			ArrayList<Entity> entities = game.world.entityList.entities;
			System.out.println(entities.size() + " entities gefunden.");
			for (int i = 0; i < entities.size(); i++) {
				if (entities.get(i) instanceof EntityPhysic) {
					EntityPhysic entity = (EntityPhysic) entities.get(i);
					entity.addForce(new Vector3d(0, 500, 0));
				}
			}
		}

		/*if (InputHandler.isKeyDown(Keyboard.KEY_LEFT)) {
			sound.setPosition(sound.getPosition().add(new Vector3d(-1,0,0)));
			sound.setVelocity(new Vector3d(-60,0,0));
		}

		if (InputHandler.isKeyDown(Keyboard.KEY_RIGHT)) {
			sound.setPosition(sound.getPosition().add(new Vector3d(+1,0,0)));	
			sound.setVelocity(new Vector3d(60,0,0));
		}*/

	}

	public void end() {
		SoundManager.instance.clear();
		ALBufferBank.instance.clear();
		//bgMusic.destroy();
		//bgMusic2.destroy();
	}

}
