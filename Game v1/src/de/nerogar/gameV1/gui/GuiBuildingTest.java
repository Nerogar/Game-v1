package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Player;
import de.nerogar.gameV1.level.BuildingBank;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityBuilding;

public class GuiBuildingTest extends Gui {
	private GElementButton buttonNone, buttonBlue, buttonGreen, buttonOrange, buttonPink, buttonRed;
	public Player player;

	public GuiBuildingTest(Game game) {
		super(game);
	}

	@Override
	public String getName() {
		return "buildingTest";
	}

	@Override
	public void init() {
		addGElement(new GElementTextLabel(genNewID(), 0.00f, 0.0f, 0.2f, 0.05f, "Hausfarbe:", FontRenderer.LEFT));
		buttonNone = new GElementButton(genNewID(), 0.2f, 0.0f, 0.19f, 0.05f, "kein Haus", FontRenderer.CENTERED, "buttons/button.png", false, "");
		buttonBlue = new GElementButton(genNewID(), 0.4f, 0.0f, 0.1f, 0.05f, "blau", FontRenderer.CENTERED, "buttons/button.png", false, "");
		buttonGreen = new GElementButton(genNewID(), 0.5f, 0.0f, 0.1f, 0.05f, "grün", FontRenderer.CENTERED, "buttons/button.png", false, "");
		buttonOrange = new GElementButton(genNewID(), 0.6f, 0.0f, 0.1f, 0.05f, "orange", FontRenderer.CENTERED, "buttons/button.png", false, "");
		buttonPink = new GElementButton(genNewID(), 0.7f, 0.0f, 0.1f, 0.05f, "pink", FontRenderer.CENTERED, "buttons/button.png", false, "");
		buttonRed = new GElementButton(genNewID(), 0.8f, 0.0f, 0.1f, 0.05f, "rot", FontRenderer.CENTERED, "buttons/button.png", false, "");

		addGElement(buttonNone);
		addGElement(buttonBlue);
		addGElement(buttonGreen);
		addGElement(buttonOrange);
		addGElement(buttonPink);
		addGElement(buttonRed);
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

		if (id == buttonNone.id && mouseButton == 0) {
			player.buildingOnCursor = null;
			player.buildingOnCursorID = -1;
		} else if (id == buttonBlue.id && mouseButton == 0) {
			player.buildingOnCursorID = 0;
			player.buildingOnCursor = (EntityBuilding) Entity.getEntity(game, player.world, BuildingBank.getBuildingName(player.buildingOnCursorID));
			player.buildingOnCursor.init(player.world);
		} else if (id == buttonGreen.id && mouseButton == 0) {
			player.buildingOnCursorID = 1;
			player.buildingOnCursor = (EntityBuilding) Entity.getEntity(game, player.world, BuildingBank.getBuildingName(player.buildingOnCursorID));
			player.buildingOnCursor.init(player.world);
		} else if (id == buttonOrange.id && mouseButton == 0) {
			player.buildingOnCursorID = 2;
			player.buildingOnCursor = (EntityBuilding) Entity.getEntity(game, player.world, BuildingBank.getBuildingName(player.buildingOnCursorID));
			player.buildingOnCursor.init(player.world);
		} else if (id == buttonPink.id && mouseButton == 0) {
			player.buildingOnCursorID = 3;
			player.buildingOnCursor = (EntityBuilding) Entity.getEntity(game, player.world, BuildingBank.getBuildingName(player.buildingOnCursorID));
			player.buildingOnCursor.init(player.world);
		} else if (id == buttonRed.id && mouseButton == 0) {
			player.buildingOnCursorID = 4;
			player.buildingOnCursor = (EntityBuilding) Entity.getEntity(game, player.world, BuildingBank.getBuildingName(player.buildingOnCursorID));
			player.buildingOnCursor.init(player.world);
		}
	}
}
