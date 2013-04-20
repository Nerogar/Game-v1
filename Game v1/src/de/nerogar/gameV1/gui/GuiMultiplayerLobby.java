package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;

public class GuiMultiplayerLobby extends Gui {
	private GElementButton startButton, backButton;
	private GElementButton readyButton, kickButton;
	private GElementListBox playersList;
	private boolean readyState = false;

	public GuiMultiplayerLobby(Game game) {
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
		setTitel("Multiplayer Lobby");

		playersList = new GElementListBox(genNewID(), 0.5f, 0.2f, 0.5f, 0.4f, new String[] { "a", "b" }, "Buttons/button.png", "Buttons/scrollbar.png");
		playersList.sliderWidth = 0.02f;

		kickButton = new GElementButton(genNewID(), 0.1f, 0.5f, 0.2f, 0.1f, "kick", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		addGElement(new GElementTextLabel(genNewID(), 0.5f, 0.7f, 0.1f, 0.1f, "ready:", FontRenderer.RIGHT));
		readyButton = new GElementButton(genNewID(), 0.6f, 0.7f, 0.05f, 0.1f, "ready", FontRenderer.CENTERED, "Buttons/button.png", true, "Buttons/tick.png");

		startButton = new GElementButton(genNewID(), 0.1f, 0.7f, 0.35f, 0.1f, "start Game", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		backButton = new GElementButton(genNewID(), 0.1f, 0.85f, 0.3f, 0.1f, "back", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		addGElement(playersList);
		addGElement(kickButton);
		addGElement(readyButton);
		addGElement(startButton);
		addGElement(backButton);
	}

	@Override
	public void updateGui() {
		if (readyState) {
			readyButton.overlayImage = "Buttons/tick.png";
		} else {
			readyButton.overlayImage = "Buttons/cross.png";
		}

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
			game.guiList.addGui(new GuiMultiplayerCreate(game));
		} else if (id == readyButton.id) {
			readyState = !readyState;
		}
	}
}