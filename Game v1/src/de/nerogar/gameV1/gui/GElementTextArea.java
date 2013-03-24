package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.RenderHelper;

public class GElementTextArea extends GElement {
	public boolean aktiveMessage;
	public String[] text;
	int alignment;

	public GElementTextArea(int id, float xPos, float yPos, float width, float height, int lineNumber, int alignment) {
		this.id = id;

		this.xPos = xPos * xScale;
		this.yPos = yPos * yScale;
		this.width = width * xScale;
		this.height = height * yScale;

		text = new String[lineNumber];
		this.alignment = alignment;
	}

	public void addTextLine(String line) {
		for (int i = text.length - 2; i >= 0; i--) {
			text[i + 1] = text[i];
		}
		text[0] = line;
	}

	public void render() {
		if (!visible) return;

		int color = 0x00000050;
		int fallof = 8;

		RenderHelper.renderColorTransition(xPos / xScale, yPos / yScale, width / xScale, fallof / yScale, 0, color, RenderHelper.VERT);
		RenderHelper.renderColorTransition(xPos / xScale, (yPos + fallof) / yScale, width / xScale, (height - fallof * 2) / yScale, color, color, RenderHelper.VERT);
		RenderHelper.renderColorTransition(xPos / xScale, (yPos + height - fallof) / yScale, width / xScale, fallof / yScale, color, 0, RenderHelper.VERT);

		for (int i = 0; i < text.length; i++) {
			if (text[i] != null) {
				float yOffs = (height / text.length) * i;
				float textHeight = (height / text.length);

				FontRenderer.renderFont(text[i], (int) xPos, (int) (yPos + height) - yOffs - textHeight + (textHeight / 4), width, textHeight / 2f, alignment);
			}
		}

		//FontRenderer.renderFont(text, (int) xPos, (int) (yPos + height / 4f), width, height / 2f, alignment);
	}
}
