package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;

public class GuiMain extends Gui {
	private GElementButton newGameButton;
	private GElementButton loadGameButton;
	private GElementButton videosettingsButton;
	private GElementButton audioButtonPlay, audioButtonStop;
	private GElementButton exitButton;
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
		textLabels.add(new GElementTextLabel(genNewID(), 0.0f, 0.0f, 1.0f, 0.2f, Character.valueOf((char) 16) + " " + titel + " " + Character.valueOf((char) 16), FontRenderer.CENTERED));
		//textLabels.add(new GElementTextLabel(genNewID(), 0.0f, 0.0f, 1.0f, 0.2f, Character.valueOf((char) 16) + " DEEN GAME " + Character.valueOf((char) 16), FontRenderer.CENTERED));

		//textLabels.add(new GElementTextLabel(genNewID(), 0.0f, -0.5f, 1.0f, 2.0f, "G", FontRenderer.CENTERED));

		newGameButton = new GElementButton(genNewID(), 0.3f, 0.2f, 0.4f, 0.1f, "new game", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		loadGameButton = new GElementButton(genNewID(), 0.3f, 0.3f, 0.4f, 0.1f, "load game", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		videosettingsButton = new GElementButton(genNewID(), 0.3f, 0.4f, 0.4f, 0.1f, "video settings", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		audioButtonPlay = new GElementButton(genNewID(), 0.3f, 0.5f, 0.2f, 0.05f, "play", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		audioButtonStop = new GElementButton(genNewID(), 0.5f, 0.5f, 0.2f, 0.05f, "stop", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		textLabels.add(new GElementTextLabel(genNewID(), 0.0f, 0.5f, 0.3f, 0.05f, "jazz nyan cat:", FontRenderer.RIGHT));
		pitchSlider = new GElementSlider(genNewID(), 0.75f, 0.5f, 0.2f, 0.05f, 0.025f, 0.2, 2.5, "", "Buttons/button.png", "Buttons/slider.png");
		pitchSlider.position = 1.0;

		exitButton = new GElementButton(genNewID(), 0.3f, 0.6f, 0.4f, 0.1f, "exit", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		
		buttons.add(newGameButton);
		buttons.add(loadGameButton);
		buttons.add(videosettingsButton);
		buttons.add(audioButtonPlay);
		buttons.add(audioButtonStop);
		buttons.add(exitButton);
		sliders.add(pitchSlider);

		updateGui();
	}

	@Override
	public void updateGui() {
		if (game.debugFelk.bgMusic == null) return;
		if (game.debugFelk.bgMusic2 == null) return;
		if (!game.debugFelk.bgMusic.isPlaying())
			audioButtonPlay.text = "play";
		else
			audioButtonPlay.text = "pause";
		game.debugFelk.bgMusic.setPitch((float)pitchSlider.position);
		game.debugFelk.bgMusic2.setPitch((float)pitchSlider.position);
	}

	@Override
	public void render() {
		RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == newGameButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiCreateWorld(game));
		} else if (id == loadGameButton.id) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiLoadWorld(game));
		} else if (id == videosettingsButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiVideoSettings(game));
		} else if (id == exitButton.id && mouseButton == 0) {
			game.running = false;
		} else if (id == audioButtonPlay.id && mouseButton == 0) {
			if (!game.debugFelk.bgMusic.isPlaying()) {
				game.debugFelk.bgMusic2.pause();
				game.debugFelk.bgMusic.setOffset(game.debugFelk.bgMusic2.getOffset());
				game.debugFelk.bgMusic.play();
			} else {
				game.debugFelk.bgMusic2.play();
				game.debugFelk.bgMusic2.setOffset(game.debugFelk.bgMusic.getOffset());
				game.debugFelk.bgMusic.pause();
			}
		} else if (id == audioButtonStop.id && mouseButton == 0) {
			game.debugFelk.bgMusic.stop();
			game.debugFelk.bgMusic2.stop();
		}
	}
}