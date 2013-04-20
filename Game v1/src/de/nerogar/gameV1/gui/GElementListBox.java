package de.nerogar.gameV1.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.image.*;

public class GElementListBox extends GElement {
	public int max;
	private float sliderHeight;
	public float sliderWidth = 0.03f;
	public float showedItems = 5;

	public boolean aktiveMessage;
	public String bgImage, sliderImage;
	public String[] text;
	private TextureBank textureBank = TextureBank.instance;
	public double position;
	private boolean isDragging = false;
	public int clickedIndex = -1;

	public GElementListBox(int id, float xPos, float yPos, float width, float height, String[] text, String bgImage, String sliderImage) {
		this.id = id;

		this.xPos = xPos * xScale;
		this.yPos = yPos * yScale;
		this.width = width * xScale;
		this.height = height * yScale;
		this.sliderHeight = (height / 5 * 2) * yScale;

		this.text = text;
		this.bgImage = bgImage;
		this.sliderImage = sliderImage;
		textureBank.loadTexture(bgImage);
	}

	public String getName() {
		return "slider";
	}

	public boolean isHoveredInArea(float x, float y, float width, float height) {
		if (!enabled) return false;
		int MouseX = Mouse.getX();
		int MouseY = Display.getHeight() - Mouse.getY();

		boolean flagX = MouseX >= x && MouseX < x + width;
		boolean flagY = MouseY >= y && MouseY < y + height;

		if (flagX && flagY && enabled && visible && isHovered()) return true;
		return false;
	}

	public void update(boolean leftClicked, boolean leftReleased) {

		this.max = (int) (text.length - showedItems);
		if (max < 0) max = 0;

		if (leftClicked && sliderWidth > 0 && isHoveredInArea(xPos + width - sliderWidth * xScale, yPos, sliderWidth * xScale, height)) isDragging = true;
		if (leftReleased) isDragging = false;

		if (isDragging) {
			position = (((Display.getHeight() - Mouse.getY()) - yPos - sliderHeight / 2) / (height - sliderHeight) * (max) + 0);

			if (position > max) position = max;
			if (position < 0) position = 0;
		}

		if (leftClicked) {
			if (isHoveredInArea(xPos, yPos, width - sliderWidth * xScale, height)) {
				int MouseY = (int) (Display.getHeight() - Mouse.getY() - yPos);

				clickedIndex = (int) (position + MouseY / (height / showedItems));
				if (clickedIndex < 0 || clickedIndex > text.length - 1) clickedIndex = -1;
			}
		}
	}

	public void render() {
		if (!visible) return;

		int color = 0x00000050;
		int fallof = 5;

		RenderHelper.renderColorTransition(xPos / xScale, yPos / yScale, width / xScale, fallof / yScale, 0, color, RenderHelper.VERT);
		RenderHelper.renderColorTransition(xPos / xScale, (yPos + fallof) / yScale, width / xScale, (height - fallof * 2) / yScale, color, color, RenderHelper.VERT);
		RenderHelper.renderColorTransition(xPos / xScale, (yPos + height - fallof) / yScale, width / xScale, fallof / yScale, color, 0, RenderHelper.VERT);

		//render items
		for (int i = 0; i < text.length; i++) {

			float itemX = (float) xPos;
			float itemWidth = width - sliderWidth * xScale;
			float itemY = (float) (yPos - position * (height / showedItems) + (height / showedItems) * i);
			float itemHeight = height / showedItems;

			if (itemY < yPos + height && itemY + itemHeight >= yPos) {
				boolean hovered = isHoveredInArea(itemX, itemY, itemWidth, itemHeight);
				int state;

				if (!enabled) state = 0;
				else if (!hovered) state = 1;
				else if (hovered) state = 2;
				else if (aktiveMessage) state = 3;
				else state = 0;
				if (clickedIndex == i) state = 3;

				float yOffset = 1f / 4f * state;

				textureBank.bindTexture(bgImage);
				GL11.glBegin(GL11.GL_QUADS);

				GL11.glColor3f(1.0f, 1.0f, 1.0f);

				GL11.glTexCoord2f(1, yOffset + 1f / 4f);
				GL11.glVertex3f(itemX + itemWidth, itemY + itemHeight, -1f);
				GL11.glTexCoord2f(1, yOffset);
				GL11.glVertex3f(itemX + itemWidth, itemY, -1f);
				GL11.glTexCoord2f(0, yOffset);
				GL11.glVertex3f(itemX, itemY, -1f);
				GL11.glTexCoord2f(0, yOffset + 1f / 4f);
				GL11.glVertex3f(itemX, itemY + itemHeight, -1f);

				GL11.glEnd();

				FontRenderer.renderFont(text[i], (float) itemX, itemY + height / (showedItems * 4), itemWidth, height / (showedItems * 2));
			}

		}

		//render slider
		boolean hovered = isHoveredInArea(xPos + width - sliderWidth * xScale, yPos, sliderWidth * xScale, height);
		int state;

		if (!enabled) state = 0;
		else if (!hovered) state = 1;
		else if (hovered) state = 2;
		else if (aktiveMessage) state = 3;
		else state = 0;

		float yOffset = 1f / 4f * state;

		textureBank.bindTexture(sliderImage);

		if (max <= 0) max = 1;
		double scale = (height - sliderHeight) / max;

		float sliderYPos = (float) (yPos + position * scale);

		float sliderXPos = xPos + width - sliderWidth * xScale;

		GL11.glBegin(GL11.GL_QUADS);

		GL11.glTexCoord2f(1, yOffset + 1f / 4f);
		GL11.glVertex3f((float) (sliderXPos + sliderWidth * xScale), sliderYPos + sliderHeight, -1f);
		GL11.glTexCoord2f(1, yOffset);
		GL11.glVertex3f((float) (sliderXPos + sliderWidth * xScale), sliderYPos, -1f);
		GL11.glTexCoord2f(0.5f, yOffset);
		GL11.glVertex3f(sliderXPos, sliderYPos, -1f);
		GL11.glTexCoord2f(0.5f, yOffset + 1f / 4f);
		GL11.glVertex3f(sliderXPos, sliderYPos + sliderHeight, -1f);

		GL11.glEnd();

	}
}
