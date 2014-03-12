package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.RenderHelper;

public class GuiMultiplayer extends Gui {
	private GElementButton createGameButton;
	private GElementButton joinGameButton;
	private GElementButton backButton;
	private GElementTextField playerNameText;

	public GuiMultiplayer(Game game) {
		super(game);
	}

	@Override
	public boolean pauseGame() {
		return true;
	}

	@Override
	public String getName() {
		return "createWorld";
	}

	@Override
	public void init() {
		setTitel("Multiplayer");

		float nameY = 0.2f;
		addGElement(new GElementTextLabel(genNewID(), 0.1f, nameY, 0.2f, 0.1f, "name:", FontRenderer.LEFT));
		playerNameText = new GElementTextField(0.3f, nameY, 0.3f, 0.1f, GameOptions.instance.getOption("playerName"), "buttons/textField.png");
		
		createGameButton = new GElementButton(genNewID(), 0.1f, 0.7f, 0.35f, 0.1f, "create Game", FontRenderer.CENTERED, "buttons/button.png", false, "");

		joinGameButton = new GElementButton(genNewID(), 0.6f, 0.7f, 0.35f, 0.1f, "join Game", FontRenderer.CENTERED, "buttons/button.png", false, "");

		backButton = new GElementButton(genNewID(), 0.1f, 0.85f, 0.3f, 0.1f, "back", FontRenderer.CENTERED, "buttons/button.png", false, "");

		addGElement(playerNameText);
		addGElement(createGameButton);
		addGElement(joinGameButton);
		addGElement(backButton);
	}

	@Override
	public void updateGui() {
		GameOptions.instance.setOption("playerName", playerNameText.getText());
	}

	@Override
	public void render() {
		RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == backButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMain(game));
		} else if (id == createGameButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMultiplayerCreate(game));
		} else if (id == joinGameButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMultiplayerJoin(game));
		}
	}
}