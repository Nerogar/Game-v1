package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.nerogar.gameV1.image.TextureBank;

public class RenderHelper {
	public static final int VERT = 1001;
	public static final int HORIZ = 1002;

	public static float getR(int code) {
		return ((code & 0xff000000) >>> 24) / 255f;
	}

	public static float getG(int code) {
		return ((code & 0x00ff0000) >>> 16) / 255f;
	}

	public static float getB(int code) {
		return ((code & 0x0000ff00) >>> 8) / 255f;
	}

	public static float getA(int code) {
		return (code & 0x000000ff) / 255f;
	}

	public static void renderColorTransition(float x, float y, float width, float height, int color1, int color2, int direction) {
		float xScale = Display.getWidth();
		float yScale = Display.getHeight();
		x *= xScale;
		y *= yScale;
		width *= xScale;
		height *= yScale;

		glDisable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);

		if (direction == VERT) {
			glColor4f(getR(color1), getG(color1), getB(color1), getA(color1));
			glVertex3f(x + width, y, -1);
			glVertex3f(x, y, -1);
			glColor4f(getR(color2), getG(color2), getB(color2), getA(color2));
			glVertex3f(x, y + height, -1);
			glVertex3f(x + width, y + height, -1);
		} else if (direction == HORIZ) {
			glColor4f(getR(color1), getG(color1), getB(color1), getA(color1));
			glVertex3f(x, y, -1);
			glVertex3f(x, y + height, -1);
			glColor4f(getR(color2), getG(color2), getB(color2), getA(color2));
			glVertex3f(x + width, y + height, -1);
			glVertex3f(x + width, y, -1);
		}

		glEnd();
		glEnable(GL_TEXTURE_2D);

		glEnd();
	}

	public static void renderImage(float x, float y, float width, float height, String textureName) {
		TextureBank.instance.bindTexture(textureName);

		float xScale = Display.getWidth();
		float yScale = Display.getHeight();
		x *= xScale;
		y *= yScale;
		width *= xScale;
		height *= yScale;

		glEnable(GL_TEXTURE_2D);
		glBegin(GL_QUADS);

		glColor3f(1, 1, 1);
		GL11.glTexCoord2f(0, 0);
		glVertex3f(x, y, -1);
		GL11.glTexCoord2f(0, 1);
		glVertex3f(x, y + height, -1);
		GL11.glTexCoord2f(1, 1);
		glVertex3f(x + width, y + height, -1);
		GL11.glTexCoord2f(1, 0);
		glVertex3f(x + width, y, -1);

		glEnd();
	}

	public static void renderDefaultWorldBackground() {
		RenderHelper.renderColorTransition(0.0f, 0.0f, 1.0f, 1.0f, 0x88bbffff, 0x000055ff, RenderHelper.VERT);
		glClear(GL_DEPTH_BUFFER_BIT);
	}

	public static void renderDefaultGuiBackground() {
		RenderHelper.renderColorTransition(0.0f, 0.0f, 1.0f, 1.0f, 0x601010ff, 0x200000ff, RenderHelper.VERT);
	}

	public static void renderDefaultGuiIngameBackground() {
		RenderHelper.renderColorTransition(0.0f, 0.0f, 1.0f, 1.0f, 0x00000090, 0x00000090, RenderHelper.VERT);
	}

	public static void renderAlertBackground() {
		RenderHelper.renderColorTransition(0.02f, 0.3f, 0.08f, 0.4f, 0x00000000, 0x000000F0, RenderHelper.HORIZ);
		RenderHelper.renderColorTransition(0.1f, 0.3f, 0.8f, 0.4f, 0x000000F0, 0x000000F0, RenderHelper.HORIZ);
		RenderHelper.renderColorTransition(0.9f, 0.3f, 0.08f, 0.4f, 0x000000F0, 0x00000000, RenderHelper.HORIZ);
	}
	
	public static void enableAlpha(){
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void disableAlpha(){
		glDisable(GL_BLEND);
	}
}
