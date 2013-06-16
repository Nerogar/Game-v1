package de.nerogar.gameV1.gui;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.network.*;

public class GuiServer extends Gui {
	private GElementButton backButton;
	private GElementTextLabel recievedText;
	private GElementTextArea clientArea;
	private Server server;

	public GuiServer(Game game) {
		super(game);
		server = new Server(GameOptions.instance.STANDARDPORT);
	}

	@Override
	public boolean pauseGame() {
		return true;
	}

	@Override
	public String getName() {
		return "server";
	}

	@Override
	public void init() {
		setTitel("Server");

		backButton = new GElementButton(genNewID(), 0.3f, 0.8f, 0.4f, 0.1f, "back", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		recievedText = new GElementTextLabel(genNewID(), 0.1f, 0.7f, 0.8f, 0.1f, "", FontRenderer.CENTERED);
		clientArea = new GElementTextArea(genNewID(), 0.2f, 0.2f, 0.6f, 0.5f, 20, FontRenderer.LEFT);

		addGElement(clientArea);
		addGElement(recievedText);
		addGElement(backButton);

	}

	@Override
	public void updateGui() {
		Client client = null;
		for (int i = 0; i < server.getClients().size(); i++) {
			if (server.getClients().get(i) != null && server.getClients().get(i).connected) {
				client = server.getClients().get(i);
			}
		}

		if (client != null && client.connected) {
			ArrayList<Packet> packets = client.getData(Packet.DEFAULT_CHANNEL);
			if (packets != null) {
				for (int i = 0; i < packets.size(); i++) {
					if (packets.get(i) instanceof PacketTestString) {
						clientArea.addTextLine(((PacketTestString) packets.get(i)).testString);
					}
				}
			} else {
				recievedText.text = client.getAdress() + " connected";
			}
		} else {
			recievedText.text = "no client connected";
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
			server.stopServer();
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMain(game));
		}
	}
}