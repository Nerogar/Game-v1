package de.nerogar.gameV1.gui;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.*;
import de.nerogar.gameV1.level.EntityHut;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class GuiIngameOverlay extends Gui {

	private Player player;

	private GElementButton hutButton;

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
		hutButton = new GElementButton(genNewID(), 0.0f, 0.2f, 0.1f, 0.1f, "hut", FontRenderer.LEFT, "buttons/button.png", false, "");

		addGElement(hutButton);
	}

	@Override
	public void updateGui() {
		if (InputHandler.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiDebugSettings(game));
		}
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
		}
	}
}
