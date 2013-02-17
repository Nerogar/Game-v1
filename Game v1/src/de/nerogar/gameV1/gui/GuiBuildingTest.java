package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;

public class GuiBuildingTest extends Gui {
	private GElementButton buttonBlue, buttonGreen, buttonOrange, buttonPink, buttonRed, buttonYellow;

	public GuiBuildingTest(Game game) {
		super(game);
	}

	@Override
	public boolean pauseGame() {
		return false;
	}

	@Override
	public String getName() {
		return "buildingTest";
	}

	@Override
	public void init() {
		textLabels.add(new GElementTextLabel(genNewID(), 0.00f, 0.0f, 0.2f, 0.04f, "Hausfarbe:", FontRenderer.LEFT));
		buttonBlue   = new GElementButton(genNewID(), 0.2f, 0.0f, 0.1f, 0.04f, "blau", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		buttonGreen  = new GElementButton(genNewID(), 0.3f, 0.0f, 0.1f, 0.04f, "grün", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		buttonOrange = new GElementButton(genNewID(), 0.4f, 0.0f, 0.1f, 0.04f, "orange", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		buttonPink   = new GElementButton(genNewID(), 0.5f, 0.0f, 0.1f, 0.04f, "pink", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		buttonRed    = new GElementButton(genNewID(), 0.6f, 0.0f, 0.1f, 0.04f, "rot", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		buttonYellow = new GElementButton(genNewID(), 0.7f, 0.0f, 0.1f, 0.04f, "gelb", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		buttons.add(buttonBlue);
		buttons.add(buttonGreen);
		buttons.add(buttonOrange);
		buttons.add(buttonPink);
		buttons.add(buttonRed);
		buttons.add(buttonYellow);
	}

	@Override
	public void updateGui() {
		//
	}

	@Override
	public void render() {
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == buttonBlue.id) {
			game.debugFelk.selectedBuildingID = 0;
		} else if (id == buttonGreen.id) {
			game.debugFelk.selectedBuildingID = 1;
		} else if (id == buttonOrange.id) {
			game.debugFelk.selectedBuildingID = 2;
		} else if (id == buttonPink.id) {
			game.debugFelk.selectedBuildingID = 3;
		} else if (id == buttonRed.id) {
			game.debugFelk.selectedBuildingID = 4;
		} else if (id == buttonYellow.id) {
			game.debugFelk.selectedBuildingID = 5;
		}
	}
}
