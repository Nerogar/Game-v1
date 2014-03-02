package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Player;
import de.nerogar.gameV1.level.EntityEnergyTower;
import de.nerogar.gameV1.level.EntityHut;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class GuiIngameOverlay extends Gui {

	private Player player;

	private GElementButton hutButton;
	private GElementButton warriorHutButton;

	public GuiIngameOverlay(Game game, Player player) {
		super(game);

		this.player = player;
	}

	@Override
	public String getName() {
		return "ingameOverlay";
	}

	@Override
	public void init() {
		hutButton = new GElementButton(genNewID(), 0.0f, 0.2f, 0.05f, 0.1f, "hut", FontRenderer.LEFT, "buttons/button.png", false, "");
		warriorHutButton = new GElementButton(genNewID(), 0.05f, 0.2f, 0.05f, 0.1f, "tower", FontRenderer.LEFT, "buttons/button.png", false, "");

		addGElement(hutButton);
		addGElement(warriorHutButton);
	}

	@Override
	public void updateGui() {

	}

	@Override
	public void render() {
		//RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == hutButton.id) {
			player.buildingOnCursor = new EntityHut(game, game.world, new ObjectMatrix());
			player.buildingOnCursor.init(player.world);
			player.buildingOnCursor.faction = player.ownFaction;
		} else if (id == warriorHutButton.id) {
			player.buildingOnCursor = new EntityEnergyTower(game, game.world, new ObjectMatrix());
			player.buildingOnCursor.init(player.world);
			player.buildingOnCursor.faction = player.ownFaction;
		}

	}
}
