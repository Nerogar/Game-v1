package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;

public class GuiMultiplayerCreate extends Gui {
	private GElementButton createButton, backButton;
	private GElementTextField portText;

	public GuiMultiplayerCreate(Game game) {
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
		//addGElement(new GElementTextLabel(genNewID(), 0.0f, 0.05f, 0.4f, 0.1f, "Multiplayer", FontRenderer.CENTERED));
		setTitel("Create game");

		float adressY = 0.35f;
		addGElement(new GElementTextLabel(genNewID(), 0.1f, adressY, 0.4f, 0.1f, "start on port: ", FontRenderer.LEFT));
		portText = new GElementTextField(0.5f, adressY, 0.15f, 0.1f, "4200", "Buttons/textField.png");

		createButton = new GElementButton(genNewID(), 0.1f, 0.7f, 0.3f, 0.1f, "create", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		backButton = new GElementButton(genNewID(), 0.1f, 0.85f, 0.3f, 0.1f, "back", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		addGElement(portText);

		addGElement(createButton);
		addGElement(backButton);
	}

	@Override
	public void updateGui() {
		createButton.enabled = !portText.getText().equals("");
	}

	@Override
	public void render() {
		RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == backButton.id) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMultiplayer(game));
		} else if (id == createButton.id) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMultiplayerLobby(game));
		}
	}
}