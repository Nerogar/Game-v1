package de.nerogar.gameV1.gui;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.RenderHelper;

public class GuiPauseMenu extends Gui {
	private GElementButton resumeButton;
	private GElementButton closeGameButton;
	private GElementButton videosettingsButton;

	public GuiPauseMenu(Game game) {
		super(game);
	}

	@Override
	public String getName() {
		return "main";
	}

	@Override
	public boolean pauseGame() {
		return true;
	}

	@Override
	public void init() {
		setTitel("Pause");
		
		resumeButton = new GElementButton(genNewID(), 0.3f, 0.2f, 0.4f, 0.1f, "resume", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		videosettingsButton = new GElementButton(genNewID(), 0.3f, 0.35f, 0.4f, 0.1f, "video settings", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		closeGameButton = new GElementButton(genNewID(), 0.3f, 0.5f, 0.4f, 0.1f, "close game", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		buttons.add(resumeButton);
		buttons.add(closeGameButton);
		buttons.add(videosettingsButton);
		updateGui();
	}

	@Override
	public void updateGui() {
		if (InputHandler.isKeyPressed(Keyboard.KEY_ESCAPE) || InputHandler.isGamepadButtonPressed("start")) {
			game.guiList.removeGui(getName());
		}
	}

	@Override
	public void render() {
		RenderHelper.renderDefaultGuiIngameBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == resumeButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
		} else if (id == closeGameButton.id && mouseButton == 0) {
			game.world.closeWorld();
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMain(game));
		} else if (id == videosettingsButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiVideoSettings(game));
		}
	}
}