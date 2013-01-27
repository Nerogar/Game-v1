package de.nerogar.gameV1.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class GElementTextLabel {
	public int id;
	private float xPos, yPos;
	private float width, height;
	public boolean visible = true;
	public boolean aktiveMessage;
	public String text;
	int alignment;

	public GElementTextLabel(int id, float xPos, float yPos, float width, float height, String text, int alignment) {
		this.id = id;
		float xScale = Display.getWidth();
		float yScale = Display.getHeight();

		this.xPos = xPos * xScale;
		this.yPos = yPos * yScale;
		this.width = width * xScale;
		this.height = height * yScale;

		this.text = text;
		this.alignment = alignment;
	}

	public boolean isHovered() {
		int x = Mouse.getX();
		int y = Display.getHeight() - Mouse.getY();
		boolean flagX = x >= xPos && x <= xPos + width;
		boolean flagY = y >= yPos && y <= yPos + height;

		if (flagX && flagY && visible) return true;
		return false;
	}

	public void render() {
		if (!visible) return;

		FontRenderer.renderFont(text, (int) xPos, (int) (yPos + height / 4f), width, height / 2f, alignment);
	}
}
