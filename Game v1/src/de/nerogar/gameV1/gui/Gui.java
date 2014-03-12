package de.nerogar.gameV1.gui;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.RenderEngine;

public abstract class Gui {
	public ArrayList<GElement> elements = new ArrayList<GElement>();
	public int activeTextField = -1;
	private GElementTextLabel titelLabel = new GElementTextLabel(genNewID(), 0.0f, 0.05f, 1.0f, 0.1f, "", FontRenderer.CENTERED);

	public RenderEngine renderEngine = RenderEngine.instance;
	public Game game;
	public int lastID = -1;

	public Gui(Game game) {
		this.game = game;
		addGElement(titelLabel);
		init();
	}

	protected void addGElement(GElement element) {
		elements.add(element);
	}

	public String getName() {
		return "standard gui name";
	}

	public boolean pauseGame() {
		return false;
	}

	public abstract void init();

	public int genNewID() {
		lastID++;
		return lastID;
	}

	public final boolean update() {
		updateGui();
		boolean clickedButton = handleClicks();
		if (activeTextField != -1) {
			if (elements.get(activeTextField) instanceof GElementTextField) {
				((GElementTextField) elements.get(activeTextField)).handleKeyboardInput();
			}

		}
		return clickedButton;
	}

	public void updateGui() {

	}

	private boolean handleClicks() {
		boolean leftClicked = InputHandler.isMouseButtonPressed(0);
		boolean leftReleased = InputHandler.isMouseButtonReleased(0);
		boolean rightClicked = InputHandler.isMouseButtonPressed(1);
		boolean clickedButton = false;

		int[] hoveredButtons = getHoveredButtons();
		for (int button : hoveredButtons) {
			if (leftClicked) {
				clickButton(button, 0);
				clickedButton = true;
			}
			if (rightClicked) {
				clickButton(button, 1);
				clickedButton = true;
			}
		}

		boolean clickedTextField = false;

		if (!clickedTextField && leftClicked) activeTextField = -1;

		int index = 0;
		for (GElement element : elements) {

			if (element instanceof GElementTextField) {
				if (element.isHovered() && leftClicked) {
					activeTextField = index;
					clickedTextField = true;
				}
			}

			element.update(leftClicked, leftReleased);

			index++;
		}

		return clickedButton || clickedTextField;
	}

	public void clickButton(int id, int mouseButton) {

	}

	public void render() {

		renderGui();
	}

	public void renderGui() {

		int index = 0;
		for (GElement element : elements) {
			if (element instanceof GElementTextField) {
				((GElementTextField) element).active = index == activeTextField;
			}

			element.render();
			index++;
		}
	}

	public void setTitel(String titel) {
		titelLabel.text = titel;
	}

	private int[] getHoveredButtons() {
		ArrayList<Integer> b = new ArrayList<Integer>();

		for (GElement element : elements) {
			if (element instanceof GElementButton && element.isHovered()) b.add(element.id);
		}

		int[] temp = new int[b.size()];
		for (int i = 0; i < b.size(); i++) {
			temp[i] = b.get(i);
		}
		return temp;
	}
}
