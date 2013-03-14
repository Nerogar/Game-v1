package de.nerogar.gameV1.gui;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.RenderEngine;

public abstract class Gui {
	public ArrayList<GElementButton> buttons = new ArrayList<GElementButton>();
	public ArrayList<GElementTextField> textFields = new ArrayList<GElementTextField>();
	public ArrayList<GElementTextLabel> textLabels = new ArrayList<GElementTextLabel>();
	public int activeTextField = -1;
	public ArrayList<GElementPanel> panels = new ArrayList<GElementPanel>();
	public ArrayList<GElementSlider> sliders = new ArrayList<GElementSlider>();
	public ArrayList<GElementOptionSlider> optionSliders = new ArrayList<GElementOptionSlider>();
	public ArrayList<GElementListBox> lists = new ArrayList<GElementListBox>();
	private GElementTextLabel titelLabel = new GElementTextLabel(genNewID(), 0.0f, 0.05f, 1.0f, 0.1f, "", FontRenderer.CENTERED);
	public RenderEngine renderEngine = RenderEngine.instance;
	public Game game;
	public int lastID = -1;

	public Gui(Game game) {
		this.game = game;
		textLabels.add(titelLabel);
		init();
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

	public final void update() {
		updateGui();
		handleClicks();
		if (activeTextField != -1) {
			textFields.get(activeTextField).handleKeyboardInput();
		}
	}

	public void updateGui() {

	}

	private void handleClicks() {
		boolean leftClicked = InputHandler.isMouseButtonPressed(0);
		boolean leftReleased = InputHandler.isMouseButtonReleased(0);
		boolean rightClicked = InputHandler.isMouseButtonPressed(1);

		int[] hoveredButtons = getHoveredButtons();
		for (int button : hoveredButtons) {
			if (leftClicked) clickButton(button, 0);
			if (rightClicked) clickButton(button, 1);
		}

		boolean clickedTextField = false;

		for (int i = 0; i < textFields.size(); i++) {
			if (textFields.get(i).isHovered() && leftClicked) {
				activeTextField = i;
				clickedTextField = true;
			}
		}

		if (!clickedTextField && leftClicked) activeTextField = -1;

		for (GElementSlider s : sliders) {
			s.update(leftClicked, leftReleased);
		}
		for (GElementOptionSlider s : optionSliders) {
			s.update(leftClicked, leftReleased);
		}
		for (GElementListBox s : lists) {
			s.update(leftClicked, leftReleased);
		}

	}

	public void clickButton(int id, int mouseButton) {

	}

	public void render() {

		renderGui();
	}

	public void renderGui() {

		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).render();
		}
		for (int i = 0; i < textLabels.size(); i++) {
			textLabels.get(i).render();
		}
		for (int i = 0; i < textFields.size(); i++) {
			textFields.get(i).render(i == activeTextField);
		}
		for (int i = 0; i < panels.size(); i++) {
			panels.get(i).render();
		}
		for (GElementSlider s : sliders) {
			s.render();
		}
		for (GElementOptionSlider s : optionSliders) {
			s.render();
		}
		for (GElementListBox s : lists) {
			s.render();
		}
	}

	public void setTitel(String titel) {
		titelLabel.text = titel;
	}

	private int[] getHoveredButtons() {
		ArrayList<Integer> b = new ArrayList<Integer>();

		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i).isHovered()) b.add(buttons.get(i).id);
		}

		int[] temp = new int[b.size()];
		for (int i = 0; i < b.size(); i++) {
			temp[i] = b.get(i);
		}
		return temp;
	}
}
