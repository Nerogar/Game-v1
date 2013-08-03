package de.nerogar.gameV1.gui;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.RenderHelper;

public class GuiIngameOverlay extends Gui {

	public GuiIngameOverlay(Game game) {
		super(game);
	}

	@Override
	public String getName() {
		return "ingameOverlay";
	}

	@Override
	public void init() {

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
		RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {

	}
}
