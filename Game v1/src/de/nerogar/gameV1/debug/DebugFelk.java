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

	public DebugFelk(Game game) {
		this.game = game;
	}

	public void startup() {
		/*try {
			bgMusic = new Testsound(new File("res/sound/forecast.ogg"), new Vector3d(0, 0, 0), true);
			bgMusic2 = new Testsound(new File("res/sound/forecast_elevator.ogg"), new Vector3d(0, 0, 0), true);
			bgMusic2.play();
		} catch (LWJGLException | IOException e) {
			e.printStackTrace();
		}*/
		SoundManager.instance.preLoadSounds();
		SoundManager.setListener(new Vector3d(0,0,0), new Vector3d(0,0,0), new Vector3d(0,0,-1), new Vector3d(0,1,0));
		sound = SoundManager.instance.create("forecast_elevator.ogg", ALSource.PRIORITY_MODERATE, new Vector3d(100,0,-15), new Vector3d(100, 0, 0), true, 1f, 1f);
		if (sound != null) sound.play();
	}

	public void run() {
		SoundManager.instance.update();
		//bgMusic.update();
		//bgMusic2.update();
		
		if (InputHandler.isKeyPressed(Keyboard.KEY_0)) {
			EntityTestparticle entity = new EntityTestparticle(game, new ObjectMatrix());
			game.world.entityList.addEntity(entity);
		}

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

		if (InputHandler.isKeyDown(Keyboard.KEY_LEFT)) {
			sound.setPosition(sound.getPosition().add(new Vector3d(-1,0,0)));
			sound.setVelocity(new Vector3d(-10,0,0));
		}

		if (InputHandler.isKeyDown(Keyboard.KEY_RIGHT)) {
			sound.setPosition(sound.getPosition().add(new Vector3d(+1,0,0)));	
			sound.setVelocity(new Vector3d(10,0,0));
		}
		
	}

	public void end() {
		SoundManager.instance.clear();
		ALBufferBank.instance.clear();
		//bgMusic.destroy();
		//bgMusic2.destroy();
	}

}
