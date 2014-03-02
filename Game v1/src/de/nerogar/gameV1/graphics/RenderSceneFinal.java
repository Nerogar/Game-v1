package de.nerogar.gameV1.graphics;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import de.nerogar.gameV1.*;

public class RenderSceneFinal extends RenderScene {

	public Game game;

	@Override
	public void render(double time) {
		RenderEngine.instance.setOrtho();
		RenderHelper.enableAlpha();

		drawTexture(game.world.colorTextureID, time);
		drawTexture(game.guiList.colorTextureID, time);

		RenderHelper.disableAlpha();
	}

	public void drawTexture(int id, double time) {
		glEnable(GL_TEXTURE_2D);

		glBindTexture(GL_TEXTURE_2D, id);

		glBegin(GL_QUADS);

		glTexCoord2f(0, 1);
		glVertex3f(0, 0, -1);

		glTexCoord2f(0, 0);
		glVertex3f(0, Display.getHeight(), -1);

		glTexCoord2f(1, 0);
		glVertex3f(Display.getWidth(), Display.getHeight(), -1);

		glTexCoord2f(1, 1);
		glVertex3f(Display.getWidth(), 0, -1);

		glEnd();
	}

}
