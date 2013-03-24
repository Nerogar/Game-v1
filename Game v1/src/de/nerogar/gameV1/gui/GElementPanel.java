package de.nerogar.gameV1.gui;

import org.lwjgl.opengl.GL11;

import de.nerogar.gameV1.image.*;

public class GElementPanel extends GElement {
	private String bgImage;
	private TextureBank textureBank = TextureBank.instance;

	public GElementPanel(float xPos, float yPos, float width, float height, String bgImage) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.bgImage = bgImage;

		textureBank.loadTexture(bgImage);

	}

	public void render() {
		//textureBank.getTexture("Panel preset.png").bind();
		textureBank.bindTexture(bgImage);
		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(0, 0);
		GL11.glVertex3f(xPos + height, yPos, -1);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex3f(xPos, yPos, -1);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex3f(xPos + height, yPos + width, -1);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex3f(xPos, yPos + width, -1);

		GL11.glEnd();
	}
}
