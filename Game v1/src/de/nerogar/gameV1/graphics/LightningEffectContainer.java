package de.nerogar.gameV1.graphics;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.World;

public class LightningEffectContainer extends RenderScene {

	private Game game;
	private World world;

	private ArrayList<Lightning> lightningList;

	public LightningEffectContainer(Game game, World world) {
		this.game = game;
		this.world = world;
		lightningList = new ArrayList<Lightning>();
	}

	public void addLightning(Lightning lightning) {
		lightningList.add(lightning);
		lightning.startTime = game.timer.time;
	}

	public void update(double time) {
		for (Lightning l : lightningList) {
			l.update(time);
		}

		for (int i = lightningList.size() - 1; i >= 0; i--) {
			if (lightningList.get(i) == null || lightningList.get(i).dead) {
				lightningList.remove(i);
			}
		}
	}

	public void render(double time) {
		world.pushMatrix();

		glDisable(GL_TEXTURE_2D);
		glColor4f(0.2f, 0.2f, 1f, 1f);

		glBegin(GL_LINES);
		for (Lightning l : lightningList) {
			for (int i = 1; i < l.vertices.length; i++) {
				//for (int i = 2; i < 3; i++) {
				glVertex3f(l.vertices[i].getXf(), l.vertices[i].getYf(), l.vertices[i].getZf());
				glVertex3f(l.vertices[i - 1].getXf(), l.vertices[i - 1].getYf(), l.vertices[i - 1].getZf());
			}
		}

		glEnd();

		world.popMatrix();
	}
}
