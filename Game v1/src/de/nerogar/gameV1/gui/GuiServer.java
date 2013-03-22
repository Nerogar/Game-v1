package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.network.*;

public class GuiServer extends Gui {
	private GElementButton backButton;
	private GElementTextLabel recievedText;
	private Server server;

	public GuiServer(Game game) {
		super(game);
		server = new Server();
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
		recievedText = new GElementTextLabel(genNewID(), 0.1f, 0.5f, 0.8f, 0.1f, "", FontRenderer.CENTERED);

		textLabels.add(recievedText);
		buttons.add(backButton);

	}

	@Override
	public void updateGui() {
		Client client = null;
		for (int i = 0; i < server.getClients().size(); i++) {
			if (server.getClients().get(i) != null) {
				client = server.getClients().get(i);
			}

		}
		if (client != null) {
			Packet packet = client.getData();
			if (packet != null && packet instanceof PacketTestString) {
				recievedText.text = ((PacketTestString) packet).testString;
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
		if (id == backButton.id) {
			server.stopServer();
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMain(game));
		}
	}
}