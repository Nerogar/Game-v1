package de.nerogar.gameV1.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.nerogar.gameV1.image.*;

public class GElementSlider {
	public int id;
	private float xPos, yPos;
	private float width, height, sliderWidth;
	public boolean enabled = true;
	public boolean visible = true;
	public boolean allowFloat = true;
	public boolean aktiveMessage;
	public String text, bgImage, sliderImage;
	private TextureBank textureBank = TextureBank.instance;
	public double min, max, position;
	private boolean isDragging = false;

	public GElementSlider(int id, float xPos, float yPos, float width, float height, float sliderWidth, double min, double max, String text, String bgImage, String sliderImage) {
		this.id = id;
		float xScale = Display.getWidth();
		float yScale = Display.getHeight();

		this.xPos = xPos * xScale;
		this.yPos = yPos * yScale;
		this.width = width * xScale;
		this.height = height * yScale;
		this.sliderWidth = sliderWidth * xScale;

		this.text = text;
		this.bgImage = bgImage;
		this.sliderImage = sliderImage;
		textureBank.loadTexture(bgImage);
		this.min = min;
		this.max = max;
	}

	public String getName() {
		return "slider";
	}

	public boolean isHovered() {
		if (!enabled) return false;
		int x = Mouse.getX();
		int y = Display.getHeight() - Mouse.getY();

		boolean flagX = x >= xPos && x < xPos + width;
		boolean flagY = y >= yPos && y < yPos + height;

		if (flagX && flagY && enabled && visible) return true;
		return false;
	}

	public void update(boolean leftClicked, boolean leftReleased) {

		if (leftClicked && isHovered()) isDragging = true;
		if (leftReleased) isDragging = false;

		if (isDragging) {
			position = ((Mouse.getX() - xPos - sliderWidth / 2) / (width - sliderWidth) * (max - min) + min);
			if (!allowFloat) position = (int) (position + 0.5);
			if (position > max) position = max;
			if (position < min) position = min;
		}
		if (allowFloat) {
			text = String.valueOf(Math.round(position * 100D) / 100D);
		} else {
			text = String.valueOf((int) position);
		}
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

		GL11.glTexCoord2f(1, .25F + 1f / 4f);
		GL11.glVertex3f(xPos + width, yPos + height, -1f);
		GL11.glTexCoord2f(1, .25F);
		GL11.glVertex3f(xPos + width, yPos, -1f);
		GL11.glTexCoord2f(0, .25F);
		GL11.glVertex3f(xPos, yPos, -1f);
		GL11.glTexCoord2f(0, .25F + 1f / 4f);
		GL11.glVertex3f(xPos, yPos + height, -1f);

		GL11.glEnd();

		textureBank.bindTexture(sliderImage);

		double scale = (width - sliderWidth) / (max - min);
		float sliderXPos = (float) (xPos + position * scale - min * scale);

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(1, yOffset + 1f / 4f);
		GL11.glVertex3f(sliderXPos + sliderWidth, yPos + height, -1f);
		GL11.glTexCoord2f(1, yOffset);
		GL11.glVertex3f(sliderXPos + sliderWidth, yPos, -1f);
		GL11.glTexCoord2f(0, yOffset);
		GL11.glVertex3f(sliderXPos, yPos, -1f);
		GL11.glTexCoord2f(0, yOffset + 1f / 4f);
		GL11.glVertex3f(sliderXPos, yPos + height, -1f);

		GL11.glEnd();

		FontRenderer.renderFont(text, (int) xPos, (int) (yPos + height / 4f), width, height / 2f);
	}
}
