package de.nerogar.gameV1.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.nerogar.gameV1.image.*;

public class GElementOptionSlider {
	public int id;
	private float xPos, yPos;
	private float width, height, sliderWidth;
	public boolean enabled = true;
	public boolean visible = true;
	public boolean aktiveMessage;
	public String text, bgImage, sliderImage;
	private TextureBank textureBank = TextureBank.instance;
	public int position;
	public String[] options;
	private boolean isDragging = false;

	public GElementOptionSlider(int id, float xPos, float yPos, float width, float height, float sliderWidth, String[] options, String bgImage, String sliderImage) {
		this.id = id;
		float xScale = Display.getWidth();
		float yScale = Display.getHeight();

		this.xPos = xPos * xScale;
		this.yPos = yPos * yScale;
		this.width = width * xScale;
		this.height = height * yScale;
		this.sliderWidth = sliderWidth * xScale;
		this.options = options;

		this.bgImage = bgImage;
		this.sliderImage = sliderImage;
		textureBank.loadTexture(bgImage);
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
			position = (int) ((Mouse.getX() - xPos - sliderWidth / 2) / (width - sliderWidth) * (options.length - 1) + 0.5);
			if (position > options.length - 1) position = options.length - 1;
			if (position < 0) position = 0;
		}
		text = String.valueOf(Math.round(position * 100D) / 100D);
	}

	public void render() {
		if (!visible) return;
		textureBank.getTexture(bgImage).bind();

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

		textureBank.getTexture(sliderImage).bind();

		double scale = (width - sliderWidth) / (options.length - 1);
		float sliderXPos = (float) (xPos + position * scale);

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

		FontRenderer.renderFont(options[position], (int) xPos, (int) (yPos + height / 4f), width, height / 2f);
	}
}
