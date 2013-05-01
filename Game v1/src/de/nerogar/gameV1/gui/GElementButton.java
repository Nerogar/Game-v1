package de.nerogar.gameV1.gui;

import org.lwjgl.opengl.GL11;

import de.nerogar.gameV1.graphics.*;

public class GElementButton extends GElement {
	public boolean aktiveMessage;
	public String text, bgImage, overlayImage;
	public int alignment;
	public boolean useImage;
	private TextureBank textureBank = TextureBank.instance;

	public GElementButton(int id, float xPos, float yPos, float width, float height, String text, int alignment, String bgImage, boolean useImage, String overlayImage) {
		this.id = id;

		this.xPos = xPos * xScale;
		this.yPos = yPos * yScale;
		this.width = width * xScale;
		this.height = height * yScale;

		this.text = text;
		this.alignment = alignment;
		this.useImage = useImage;
		this.bgImage = bgImage;
		this.overlayImage = overlayImage;
		textureBank.loadTexture(bgImage);
		if (useImage) textureBank.loadTexture(overlayImage);
	}

	public String getName() {
		return "button";
	}

	public void render() {
		if (!visible) return;
		textureBank.bindTexture(bgImage);

		boolean hovered = isHovered();
		int state;

		if (!enabled) state = 0;
		else if (!hovered) state = 1;
		else if (hovered) state = 2;
		else if (aktiveMessage) state = 3;
		else state = 0;

		float yOffset = 1f / 4f * state;

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glColor3f(1.0f, 1.0f, 1.0f);

		GL11.glTexCoord2f(1, yOffset + 1f / 4f);
		GL11.glVertex3f(xPos + width, yPos + height, -1f);
		GL11.glTexCoord2f(1, yOffset);
		GL11.glVertex3f(xPos + width, yPos, -1f);
		GL11.glTexCoord2f(0, yOffset);
		GL11.glVertex3f(xPos, yPos, -1f);
		GL11.glTexCoord2f(0, yOffset + 1f / 4f);
		GL11.glVertex3f(xPos, yPos + height, -1f);

		GL11.glEnd();

		if (!useImage) {
			FontRenderer.renderFont(text, (int) xPos, (int) (yPos + height / 4f), width, height / 2f, alignment);
		} else {
			textureBank.bindTexture(overlayImage);
			GL11.glBegin(GL11.GL_QUADS);

			GL11.glTexCoord2f(1, 1);
			GL11.glVertex3f(xPos + width, yPos + height, -1f);
			GL11.glTexCoord2f(1, 0);
			GL11.glVertex3f(xPos + width, yPos, -1f);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3f(xPos, yPos, -1f);
			GL11.glTexCoord2f(0, 1);
			GL11.glVertex3f(xPos, yPos + height, -1f);

			GL11.glEnd();

		}

		//FontRenderer.renderFont(text, (int) xPos, (int) yPos,height, width);
	}
}