package de.nerogar.gameV1.gui;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.network.*;

public class GuiMultiplayerLobby extends Gui {
	private GElementButton startButton, backButton;
	private GElementButton readyButton, kickButton;
	private GElementListBox playersList;
	private boolean readyState = false;
	private Server server;
	private Client client;

	public GuiMultiplayerLobby(Game game, Server server, Client client) {
		super(game);
		this.server = server;
		this.client = client;
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

		playersList = new GElementListBox(genNewID(), 0.5f, 0.2f, 0.5f, 0.4f, new String[] {}, "Buttons/button.png", "Buttons/scrollbar.png");
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
		//update player List as server
		if (server != null) {
			ArrayList<Client> clients = server.getClients();
			String[] clientNames = new String[clients.size()];
			for (int i = 0; i < clientNames.length; i++) {
				if (clients.get(i).connectionInfo != null) {
					clientNames[i] = clients.get(i).connectionInfo.username;
				}
			}
			playersList.text = clientNames;
		}

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
			if (server != null) {
				server.stopServer();
			}
			client.stopClient();
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMultiplayerCreate(game));
		} else if (id == readyButton.id) {
			readyState = !readyState;
		}
	}
}