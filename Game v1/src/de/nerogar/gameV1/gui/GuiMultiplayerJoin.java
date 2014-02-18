package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.ServerList;
import de.nerogar.gameV1.network.Client;

public class GuiMultiplayerJoin extends Gui {
	private GElementButton directConnectButton, backButton;
	private GElementButton listConnectButton, addServerButton, removeServerButton;

	private GElementTextField adressText, portText;
	private GElementListBox serverList;

	private AlertGetMessage newServerAdressAlert;
	private Client client;

	public GuiMultiplayerJoin(Game game) {
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
		setTitel("Join game");

		float leftX = 0.05f;
		float rightX = 0.6f;

		ServerList.instance.load();
		String[] servers = ServerList.instance.getAsStringArray();

		serverList = new GElementListBox(genNewID(), rightX, 0.2f, 1 - rightX, 0.5f, servers, "buttons/button.png", "buttons/scrollbar.png");
		listConnectButton = new GElementButton(genNewID(), rightX, 0.85f, 0.2f, 0.1f, "connect", FontRenderer.CENTERED, "buttons/button.png", false, "");
		addServerButton = new GElementButton(genNewID(), rightX, 0.7f, 0.2f, 0.1f, "add server", FontRenderer.CENTERED, "buttons/button.png", false, "");
		removeServerButton = new GElementButton(genNewID(), rightX + 0.2f, 0.7f, 0.2f, 0.1f, "remove server", FontRenderer.CENTERED, "buttons/button.png", false, "");

		addGElement(new GElementTextLabel(genNewID(), leftX, 0.25f, 0.2f, 0.1f, "ip adress:", FontRenderer.RIGHT));
		adressText = new GElementTextField(leftX, 0.35f, 0.2f, 0.1f, "", "buttons/textField.png");
		addGElement(new GElementTextLabel(genNewID(), leftX + 0.2f, 0.25f, 0.2f, 0.1f, "port:", FontRenderer.LEFT));
		portText = new GElementTextField(leftX + 0.2f, 0.35f, 0.15f, 0.1f, "4200", "buttons/textField.png");
		portText.numOnly = true;

		directConnectButton = new GElementButton(genNewID(), 0.1f, 0.7f, 0.3f, 0.1f, "connect", FontRenderer.CENTERED, "buttons/button.png", false, "");

		backButton = new GElementButton(genNewID(), 0.1f, 0.85f, 0.3f, 0.1f, "back", FontRenderer.CENTERED, "buttons/button.png", false, "");

		addGElement(serverList);
		addGElement(listConnectButton);
		addGElement(addServerButton);
		addGElement(removeServerButton);

		addGElement(adressText);
		addGElement(portText);

		addGElement(directConnectButton);
		addGElement(backButton);

		newServerAdressAlert = new AlertGetMessage(game, "new server adress (ip:port)", true);
	}

	@Override
	public void updateGui() {
		directConnectButton.enabled = !adressText.getText().equals("");
		listConnectButton.enabled = serverList.clickedIndex != -1;

		if (newServerAdressAlert.hasNewText) {
			String newServerAdress = newServerAdressAlert.getText();
			if (newServerAdress != null) {
				String[] newServerAdressParts = newServerAdress.split(":");
				String newServerIP = newServerAdressParts[0];
				int newServerPort = GameOptions.instance.STANDARDPORT;
				if (newServerAdressParts.length > 1) {
					try {
						newServerPort = Integer.parseInt(newServerAdressParts[1]);
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}

				ServerList.instance.addServer(newServerIP, newServerPort);
				serverList.text = ServerList.instance.getAsStringArray();
			}
		}
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
			game.guiList.addGui(new GuiMultiplayer(game));
		} else if (id == addServerButton.id && mouseButton == 0) {
			game.guiList.alert(newServerAdressAlert);
		} else if (id == removeServerButton.id && mouseButton == 0) {
			if (serverList.clickedIndex >= 0) {
				ServerList.instance.removeServer(serverList.clickedIndex);
				serverList.text = ServerList.instance.getAsStringArray();
			}
		} else if (id == directConnectButton.id && mouseButton == 0) {
			client = new Client(adressText.getText(), Integer.parseInt(portText.getText()));

			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMultiplayerLobby(game, null, client));
		} else if (id == listConnectButton.id && mouseButton == 0) {
			int index = serverList.clickedIndex;
			if (index != -1) {
				client = new Client(ServerList.instance.getAdress(index), ServerList.instance.getPort(index));

				game.guiList.removeGui(getName());
				game.guiList.addGui(new GuiMultiplayerLobby(game, null, client));
			}
		}
	}
}