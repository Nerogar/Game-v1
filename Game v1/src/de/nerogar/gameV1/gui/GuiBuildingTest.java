package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;

public class GuiBuildingTest extends Gui {
	private GElementButton buttonNone, buttonBlue, buttonGreen, buttonOrange, buttonPink, buttonRed;
	public int selectedID = 0;
	public boolean idChanged = false;
	
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
		textLabels.add(new GElementTextLabel(genNewID(), 0.00f, 0.0f, 0.2f, 0.05f, "Hausfarbe:", FontRenderer.LEFT));
		buttonNone   = new GElementButton(genNewID(), 0.2f, 0.0f, 0.19f, 0.05f, "kein Haus", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		buttonBlue   = new GElementButton(genNewID(), 0.4f, 0.0f, 0.1f, 0.05f, "blau", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		buttonGreen  = new GElementButton(genNewID(), 0.5f, 0.0f, 0.1f, 0.05f, "grün", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		buttonOrange = new GElementButton(genNewID(), 0.6f, 0.0f, 0.1f, 0.05f, "orange", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		buttonPink   = new GElementButton(genNewID(), 0.7f, 0.0f, 0.1f, 0.05f, "pink", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		buttonRed    = new GElementButton(genNewID(), 0.8f, 0.0f, 0.1f, 0.05f, "rot", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		buttons.add(buttonNone);
		buttons.add(buttonBlue);
		buttons.add(buttonGreen);
		buttons.add(buttonOrange);
		buttons.add(buttonPink);
		buttons.add(buttonRed);
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
		if (id == buttonNone.id) {
			idChanged = true;
			selectedID = -1;
			game.debugFelk.selectedBuildingID = -1;
		} else if (id == buttonBlue.id) {
			idChanged = true;
			selectedID = 0;
			game.debugFelk.selectedBuildingID = 0;
		} else if (id == buttonGreen.id) {
			idChanged = true;
			selectedID = 1;
			game.debugFelk.selectedBuildingID = 1;
		} else if (id == buttonOrange.id) {
			idChanged = true;
			selectedID = 2;
			game.debugFelk.selectedBuildingID = 2;
		} else if (id == buttonPink.id) {
			idChanged = true;
			selectedID = 3;
			game.debugFelk.selectedBuildingID = 3;
		} else if (id == buttonRed.id) {
			idChanged = true;
			selectedID = 4;
			game.debugFelk.selectedBuildingID = 4;
		}
	}
	
	public int getBuildingId(){
		idChanged = false;
		return selectedID;
	}
}
