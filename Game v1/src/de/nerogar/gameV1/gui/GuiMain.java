package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;

public class GuiMain extends Gui {
	private GElementButton newGameButton;
	private GElementButton loadGameButton;
	private GElementButton videosettingsButton;
	private GElementButton exitButton;

	public GuiMain(Game game) {
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

		//33-128

		String titel = "";
		for (int i = 0; i < 9; i++) {
			titel += (char) (Math.random() * (128 - 33) + 33);
		}
		textLabels.add(new GElementTextLabel(genNewID(), 0.0f, 0.0f, 1.0f, 0.2f, Character.valueOf((char) 16) + " " + titel + " " + Character.valueOf((char) 16), FontRenderer.CENTERED));
		//textLabels.add(new GElementTextLabel(genNewID(), 0.0f, 0.0f, 1.0f, 0.2f, Character.valueOf((char) 16) + " DEEN GAME " + Character.valueOf((char) 16), FontRenderer.CENTERED));

		//textLabels.add(new GElementTextLabel(genNewID(), 0.0f, -0.5f, 1.0f, 2.0f, "G", FontRenderer.CENTERED));

		newGameButton = new GElementButton(genNewID(), 0.3f, 0.2f, 0.4f, 0.1f, "new game", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		loadGameButton = new GElementButton(genNewID(), 0.3f, 0.3f, 0.4f, 0.1f, "load game", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		videosettingsButton = new GElementButton(genNewID(), 0.3f, 0.4f, 0.4f, 0.1f, "video settings", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		exitButton = new GElementButton(genNewID(), 0.3f, 0.6f, 0.4f, 0.1f, "exit", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		buttons.add(newGameButton);
		buttons.add(loadGameButton);
		buttons.add(videosettingsButton);
		buttons.add(exitButton);

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
		if (id == newGameButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiCreateWorld(game));
		} else if (id == loadGameButton.id) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiLoadWorld(game));
		} else if (id == videosettingsButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiVideoSettings(game));
		} else if (id == exitButton.id && mouseButton == 0) {
			game.running = false;
		}
	}
}