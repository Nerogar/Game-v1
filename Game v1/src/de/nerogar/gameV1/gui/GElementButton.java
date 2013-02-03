package de.nerogar.gameV1.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.nerogar.gameV1.image.*;

public class GElementButton {
	public int id;
	private float xPos, yPos;
	private float width, height;
	public boolean enabled = true;
	public boolean visible = true;
	public boolean aktiveMessage;
	public String text, bgImage, overlayImage;
	public int alignment;
	@SuppressWarnings("unused")
	private boolean useImage;
	private TextureBank textureBank = TextureBank.instance;

	public GElementButton(int id, float xPos, float yPos, float width, float height, String text, int alignment, String bgImage, boolean useImage, String overlayImage) {
		this.id = id;
		float xScale = Display.getWidth();
		float yScale = Display.getHeight();

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

	public boolean isHovered() {
		int x = Mouse.getX();
		int y = Display.getHeight() - Mouse.getY();
		boolean flagX = x >= xPos && x < xPos + width;
		boolean flagY = y >= yPos && y < yPos + height;

		if (flagX && flagY && enabled && visible) return true;
		return false;
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

		FontRenderer.renderFont(text, (int) xPos, (int) (yPos + height / 4f), width, height / 2f, alignment);
		//FontRenderer.renderFont(text, (int) xPos, (int) yPos,height, width);
	}
}