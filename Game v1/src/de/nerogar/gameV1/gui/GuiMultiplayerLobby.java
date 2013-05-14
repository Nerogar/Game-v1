package de.nerogar.gameV1.gui;

import java.util.ArrayList;
import java.util.HashMap;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.network.*;

public class GuiMultiplayerLobby extends Gui {
	private GElementButton startButton, backButton;
	private GElementButton readyButton, kickButton;
	private GElementListBox playersList;
	private HashMap<Client, Boolean> readyStates;
	private boolean readyState = false;
	private Server server;
	private Client client;

	public GuiMultiplayerLobby(Game game, Server server, Client client) {
		super(game);
		this.server = server;
		this.client = client;
		readyStates = new HashMap<Client, Boolean>();
		PacketMultiplayerLobbyClient lobbyInfoClient = new PacketMultiplayerLobbyClient();
		lobbyInfoClient.readyState = false;
		client.sendPacket(lobbyInfoClient);

		aktivateStartGameButton();
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
		addGElement(backButton);
	}

	public void aktivateStartGameButton() {
		if (server != null) addGElement(startButton);
	}

	@Override
	public void updateGui() {
		//update player List as server
		if (server != null) {
			ArrayList<Client> clients = server.getClients();
			//String[] clientNames = new String[clients.size()];
			PacketMultiplayerLobbyInfo packetLobbyInfo = new PacketMultiplayerLobbyInfo();

			packetLobbyInfo.playerNames = new String[clients.size()];
			packetLobbyInfo.playerReadyStates = new boolean[clients.size()];

			for (int i = 0; i < clients.size(); i++) {
				if (clients.get(i).connectionInfo != null) {
					Client connectionClient = clients.get(i);
					ArrayList<Packet> receivedPackets = connectionClient.getData(Packet.LOBBY_CHANNEL);
					if(receivedPackets!=null){
						for (Packet packet : receivedPackets) {
							processServerPackets(connectionClient, packet);
						}
					}
				}
			}
			//playersList.text = clientNames;
			//server.broadcastData(packetLobbyInfo);
		}

		//update packets
		ArrayList<Packet> receivedPackets = client.getData(Packet.LOBBY_CHANNEL);
		if (receivedPackets != null) {
			processPackets(receivedPackets);
		}

		//process connection reset
		if (!client.connected) {
			disconnect();
			game.guiList.alert(new Alert(game, client.closeMessage));
		}

		//update ready state button
		if (readyState) {
			readyButton.overlayImage = "Buttons/tick.png";
		} else {
			readyButton.overlayImage = "Buttons/cross.png";
		}
	}

	public void processPackets(ArrayList<Packet> receivedPackets) {
		for (Packet packet : receivedPackets) {
			if (packet instanceof PacketMultiplayerLobbyInfo) {
				PacketMultiplayerLobbyInfo lobbyInfo = (PacketMultiplayerLobbyInfo) packet;
				String[] clientNames = lobbyInfo.playerNames;
				if (clientNames != null) {
					playersList.text = clientNames;
				}
			}
		}
	}

	public void processServerPackets(Client connectionClient, Packet packet) {
		if (packet instanceof PacketMultiplayerLobbyClient) {
			PacketMultiplayerLobbyClient lobbyInfoClient = (PacketMultiplayerLobbyClient) packet;
			readyStates.put(client, lobbyInfoClient.readyState);

			PacketMultiplayerLobbyInfo packetLobbyInfo = new PacketMultiplayerLobbyInfo();
			ArrayList<Client> clients = server.getClients();
			packetLobbyInfo.playerNames = new String[clients.size()];
			packetLobbyInfo.playerReadyStates = new boolean[clients.size()];

			for (int i = 0; i < clients.size(); i++) {
				if (clients.get(i).connectionInfo != null) {
					Client serverClient = clients.get(i);
					packetLobbyInfo.playerNames[i] = serverClient.connectionInfo.username;
					//packetLobbyInfo.playerReadyStates[i] = readyStates.get(serverClient);
				}
			}

			server.broadcastData(packetLobbyInfo);
		}
	}

	public void disconnect() {
		if (server != null) {
			server.stopServer();
		}
		client.stopClient();
		game.guiList.removeGui(getName());
		game.guiList.addGui(new GuiMultiplayer(game));
	}

	@Override
	public void render() {
		RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == backButton.id && mouseButton == 0) {
			disconnect();
		} else if (id == readyButton.id && mouseButton == 0) {
			readyState = !readyState;
		}
	}
}