package de.nerogar.gameV1.gui;

import static org.lwjgl.opengl.GL11.*;

import de.nerogar.gameV1.graphics.TextureBank;

public class FontRenderer {
	public static final int LEFT = 1001;
	public static final int CENTERED = 1002;
	public static final int RIGHT = 1003;

	private static TextureBank textureBank = TextureBank.instance;

	public static void renderFont(String textString, float x, float y, float width, float height) {
		renderFont(textString, x, y, width, height, LEFT);
	}

	public static void renderFont(String textString, float x, float y, float width, float height, int alignment) {
		
		if (textString == null) return;
		textureBank.bindTexture("font.png");
		
		float texSize = 1 / 16f;
		char[] text = textString.toCharArray();

		if (getWidth(textString, height) > width) {
			int yOffs = (int) ((height - getHeight(textString, width)) / 2);
			y += yOffs;
			height = getHeight(textString, width);
		}

		int xOffs = (int) ((width - getWidth(textString, height)) / 2);
		if (alignment == LEFT) {

		} else if (alignment == CENTERED) {
			x += xOffs;
		} else if (alignment == RIGHT) {
			x += xOffs * 2;
		}

		for (int i = 0; i < text.length; i++) {
			int xIndex = ((int) text[i]) % 16;
			int yIndex = ((int) text[i]) / 16;

			float tx1 = x + height * i;
			float ty1 = y;
			float tx2 = x + height * (i + 1);
			float ty2 = y + height;

			float sx1 = texSize * xIndex;
			float sy1 = texSize * yIndex;
			float sx2 = texSize * (xIndex + 1);
			float sy2 = texSize * (yIndex + 1);
			drawChar(tx1, ty1, tx2, ty2, sx1, sy1, sx2, sy2);
		}
	}

	private static void drawChar(float tx1, float ty1, float tx2, float ty2, float sx1, float sy1, float sx2, float sy2) {
		glColor3f(1.0f, 1.0f, 1.0f);
		glBegin(GL_QUADS);

		glTexCoord2f(sx1, sy1);
		glVertex3f(tx1, ty1, -1);

		glTexCoord2f(sx1, sy2);
		glVertex3f(tx1, ty2, -1);

		glTexCoord2f(sx2, sy2);
		glVertex3f(tx2, ty2, -1);

		glTexCoord2f(sx2, sy1);
		glVertex3f(tx2, ty1, -1);

		glEnd();
	}

	public static float getWidth(String text, float height) {
		return text.length() * height;
	}

	public static float getHeight(String text, float width) {
		return width / (float) text.length();
	}
}
