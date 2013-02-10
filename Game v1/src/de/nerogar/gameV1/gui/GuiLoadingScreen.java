package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;

public class GuiLoadingScreen extends Gui {

	public GuiLoadingScreen(Game game) {
		super(game);
	}

	@Override
	public String getName() {
		return "loadingScreen";
	}

	@Override
	public boolean pauseGame() {
		return false;
	}

	@Override
	public void init() {
		textLabels.add(new GElementTextLabel(genNewID(), 0.0f, 0.0f, 1.0f, 1.0f, "Game", FontRenderer.CENTERED));
		updateGui();
	}

	@Override
	public void updateGui() {

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