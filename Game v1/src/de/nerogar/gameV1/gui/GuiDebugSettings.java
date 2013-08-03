package de.nerogar.gameV1.gui;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.RenderHelper;

public class GuiDebugSettings extends Gui {
	private GElementButton showAABBsButton, showNetworkTrafficButton, okButton, tempIngameButton;

	//private GElementOptionSlider ;
	//private GElementSlider ;

	public GuiDebugSettings(Game game) {
		super(game);
	}

	@Override
	public boolean pauseGame() {
		return true;
	}

	@Override
	public String getName() {
		return "debugSettings";
	}

	@Override
	public void init() {
		setTitel("Debug Settings");

		//showAABBs
		addGElement(new GElementTextLabel(genNewID(), 0.05f, 0.2f, 0.2f, 0.1f, "show AABBs:", FontRenderer.LEFT));
		showAABBsButton = new GElementButton(genNewID(), 0.25f, 0.2f, 0.2f, 0.1f, String.valueOf(GameOptions.instance.getBoolOption("showAABBs")), FontRenderer.LEFT, "Buttons/button.png", false, "");

		//showNetworkTraffic
		addGElement(new GElementTextLabel(genNewID(), 0.05f, 0.35f, 0.2f, 0.1f, "show Network traffic:", FontRenderer.LEFT));
		showNetworkTrafficButton = new GElementButton(genNewID(), 0.25f, 0.35f, 0.2f, 0.1f, String.valueOf(GameOptions.instance.getBoolOption("showNetworkTraffic")), FontRenderer.LEFT, "Buttons/button.png", false, "");

		//ok button
		okButton = new GElementButton(genNewID(), 0.3f, 0.8f, 0.4f, 0.1f, "Ok", FontRenderer.LEFT, "Buttons/button.png", false, "");

		//tempIngameButton
		tempIngameButton = new GElementButton(genNewID(), 0.7f, 0.8f, 0.4f, 0.1f, "ingame", FontRenderer.LEFT, "Buttons/button.png", false, "");
		
		addGElement(showAABBsButton);
		addGElement(showNetworkTrafficButton);
		addGElement(okButton);
		addGElement(tempIngameButton);
	}

	@Override
	public void updateGui() {
		if (InputHandler.isKeyPressed(Keyboard.KEY_ESCAPE)) clickButton(okButton.id, 0);
	}

	@Override
	public void render() {
		if (game.world.isLoaded) {
			RenderHelper.renderDefaultGuiIngameBackground();
		} else {
			RenderHelper.renderDefaultGuiBackground();
		}
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == showAABBsButton.id && mouseButton == 0) {
			GameOptions.instance.switchBoolOption("showAABBs");
			showAABBsButton.text = String.valueOf(GameOptions.instance.getBoolOption("showAABBs"));
		} else if (id == showNetworkTrafficButton.id && mouseButton == 0) {
			GameOptions.instance.switchBoolOption("showNetworkTraffic");
			showNetworkTrafficButton.text = String.valueOf(GameOptions.instance.getBoolOption("showNetworkTraffic"));
		} else if (id == okButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiVideoSettings(game));
		} else if (id == tempIngameButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiIngameOverlay(game));
		}
	}
}
