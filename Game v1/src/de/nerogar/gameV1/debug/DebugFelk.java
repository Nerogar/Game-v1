package de.nerogar.gameV1.debug;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityPhysic;
import de.nerogar.gameV1.level.EntityTestparticle;
import de.nerogar.gameV1.physics.ObjectMatrix;
import de.nerogar.gameV1.sound.Sound;

public class DebugFelk {

	private Game game;
	public Sound bgMusic;
	public Sound bgMusic2;

	public DebugFelk(Game game) {
		this.game = game;
	}

	public void startup() {
		try {
			bgMusic = new Sound(new File("res/sound/forecast.ogg"), new Vector3d(0, 0, 0), true);
			bgMusic2 = new Sound(new File("res/sound/forecast_elevator.ogg"), new Vector3d(0, 0, 0), true);
			bgMusic2.play();
		} catch (LWJGLException | IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		bgMusic.update();
		bgMusic2.update();

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

		if (InputHandler.isKeyDown(Keyboard.KEY_1)) {
			bgMusic.setOffset((float) Math.random());
		}

		if (InputHandler.isKeyPressed(Keyboard.KEY_2)) {
			bgMusic.crash();
		}

		if (InputHandler.isKeyReleased(Keyboard.KEY_2)) {
			bgMusic.uncrash();
		}
		
	}

	public void end() {
		bgMusic.destroy();
		bgMusic2.destroy();
	}

}
