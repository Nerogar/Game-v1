package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.network.*;

public class GuiClient extends Gui {
	private GElementButton startClientButton, backButton, sendDataButton;
	private GElementTextField adressText, sendText;
	private Client client;
	private Object syncObject;

	public GuiClient(Game game) {
		super(game);
		syncObject = new Object();

	}

	@Override
	public boolean pauseGame() {
		return true;
	}

	@Override
	public String getName() {
		return "client";
	}

	@Override
	public void init() {
		setTitel("Client");

		startClientButton = new GElementButton(genNewID(), 0.6f, 0.3f, 0.3f, 0.1f, "start", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		sendDataButton = new GElementButton(genNewID(), 0.3f, 0.7f, 0.4f, 0.1f, "send", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		backButton = new GElementButton(genNewID(), 0.3f, 0.8f, 0.4f, 0.1f, "back", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		sendText = new GElementTextField(0.1f, 0.5f, 0.8f, 0.1f, "text", "Buttons/textField.png");
		adressText = new GElementTextField(0.1f, 0.3f, 0.4f, 0.1f, "localhost", "Buttons/textField.png");

		buttons.add(backButton);
		buttons.add(sendDataButton);
		buttons.add(startClientButton);
		textFields.add(adressText);
		textFields.add(sendText);
	}

	@Override
	public void updateGui() {
		sendDataButton.enabled = client != null && client.connected;
	}

	@Override
	public void render() {
		RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == backButton.id) {
			if (client != null) {
				client.stopClient();
			}
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMain(game));
		} else if (id == sendDataButton.id) {
			if (client != null) {
				DNFile file = new DNFile("");
				file.addNode("test", sendText.getText());
				long time1 = System.nanoTime();
				client.addPackage(file);
				long time2 = System.nanoTime();
				client.startSending();
				long time3 = System.nanoTime();
				System.out.println("Add: " + (time2 - time1) / 1000000D + " ms");
				System.out.println("Send: " + (time3 - time2) / 1000000D + " ms");
			}
		} else if (id == startClientButton.id) {
			client = new Client(syncObject, adressText.getText());
			client.start();
		}
	}
}