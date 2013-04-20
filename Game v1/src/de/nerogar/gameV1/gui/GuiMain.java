package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;

public class GuiMain extends Gui {
	private GElementButton singleplayerButton;
	private GElementButton multiplayerButton;
	private GElementButton videosettingsButton;
	private GElementButton audioButtonPlay, audioButtonStop;
	private GElementButton exitButton;
	private GElementButton clientButton;
	private GElementButton serverButton;
	private GElementSlider pitchSlider;

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
		addGElement(new GElementTextLabel(genNewID(), 0.0f, 0.0f, 1.0f, 0.2f, Character.valueOf((char) 16) + " " + titel + " " + Character.valueOf((char) 16), FontRenderer.CENTERED));
		//addGElement(new GElementTextLabel(genNewID(), 0.0f, 0.0f, 1.0f, 0.2f, Character.valueOf((char) 16) + " DEEN GAME " + Character.valueOf((char) 16), FontRenderer.CENTERED));

		//addGElement(new GElementTextLabel(genNewID(), 0.0f, -0.5f, 1.0f, 2.0f, "G", FontRenderer.CENTERED));

		singleplayerButton = new GElementButton(genNewID(), 0.3f, 0.2f, 0.4f, 0.1f, "Singleplayer", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		multiplayerButton = new GElementButton(genNewID(), 0.3f, 0.3f, 0.4f, 0.1f, "Multiplayer", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		addGElement(new GElementTextLabel(genNewID(), 0.7f, 0.3f, 0.1f, 0.1f, "(wip!)", FontRenderer.LEFT));
		videosettingsButton = new GElementButton(genNewID(), 0.3f, 0.4f, 0.4f, 0.1f, "video settings", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		audioButtonPlay = new GElementButton(genNewID(), 0.3f, 0.5f, 0.2f, 0.05f, "play", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		audioButtonStop = new GElementButton(genNewID(), 0.5f, 0.5f, 0.2f, 0.05f, "stop", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		addGElement(new GElementTextLabel(genNewID(), 0.0f, 0.7f, 1f, 0.05f, "", FontRenderer.CENTERED));
		addGElement(new GElementTextLabel(genNewID(), 0.0f, 0.75f, 1f, 0.05f, "", FontRenderer.CENTERED));
		pitchSlider = new GElementSlider(genNewID(), 0.75f, 0.5f, 0.2f, 0.05f, 0.025f, 0.0, 100.0, "", "Buttons/button.png", "Buttons/slider.png");
		pitchSlider.position = 5;

		exitButton = new GElementButton(genNewID(), 0.3f, 0.6f, 0.4f, 0.1f, "exit", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		//network test
		clientButton = new GElementButton(genNewID(), 0.3f, 0.95f, 0.2f, 0.05f, "client", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		serverButton = new GElementButton(genNewID(), 0.5f, 0.95f, 0.2f, 0.05f, "server", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		addGElement(clientButton);
		addGElement(serverButton);

		addGElement(singleplayerButton);
		addGElement(multiplayerButton);
		addGElement(videosettingsButton);
		addGElement(audioButtonPlay);
		addGElement(audioButtonStop);
		addGElement(exitButton);
		addGElement(pitchSlider);

		updateGui();
	}

	@Override
	public void updateGui() {
		//if (game.debugFelk.bgMusic == null) return;
		//if (game.debugFelk.bgMusic2 == null) return;
		//if (!game.debugFelk.bgMusic.isPlaying())
		//	audioButtonPlay.text = "play";
		//else
		//	audioButtonPlay.text = "pause";
		//game.debugFelk.bgMusic.setGain((float)pitchSlider.position);
		//game.debugFelk.bgMusic2.setGain((float)pitchSlider.position);
		//if (game.debugFelk.sound == null) return;
		//game.debugFelk.sound.setGain((float)pitchSlider.position);
	}

	@Override
	public void render() {
		RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == singleplayerButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiLoadWorld(game));
		} else if (id == multiplayerButton.id) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMultiplayer(game));
		} else if (id == videosettingsButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiVideoSettings(game));
		} else if (id == exitButton.id && mouseButton == 0) {
			game.running = false;
		} else if (id == audioButtonPlay.id && mouseButton == 0) {
			if (!game.debugFelk.sound.isPlaying()) {
				game.debugFelk.sound.play();
			} else {
				game.debugFelk.sound.pause();
			}
		} else if (id == audioButtonStop.id && mouseButton == 0) {
			game.debugFelk.sound.stop();
		} else if (id == clientButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiClient(game));
		} else if (id == serverButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiServer(game));
		}
	}
}