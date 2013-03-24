package de.nerogar.gameV1.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public abstract class GElement {

	public int id;
	protected float xPos, yPos;
	protected float width, height;
	protected float xScale = Display.getWidth();
	protected float yScale = Display.getHeight();

	public boolean visible = true;
	public boolean enabled = true;

	public abstract void render();

	public void update(boolean leftClicked, boolean leftReleased) {

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

}