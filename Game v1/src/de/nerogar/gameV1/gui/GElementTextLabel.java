package de.nerogar.gameV1.gui;

public class GElementTextLabel extends GElement {
	public boolean aktiveMessage;
	public String text;
	int alignment;

	public GElementTextLabel(int id, float xPos, float yPos, float width, float height, String text, int alignment) {
		this.id = id;

		this.xPos = xPos * xScale;
		this.yPos = yPos * yScale;
		this.width = width * xScale;
		this.height = height * yScale;

		this.text = text;
		this.alignment = alignment;
	}

	public void render() {
		if (!visible) return;

		FontRenderer.renderFont(text, (int) xPos, (int) (yPos + height / 4f), width, height / 2f, alignment);
	}
}
