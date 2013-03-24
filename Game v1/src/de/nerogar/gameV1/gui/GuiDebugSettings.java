package de.nerogar.gameV1.gui;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.RenderHelper;

public class GuiDebugSettings extends Gui {
	private GElementButton showAABBsButton, okButton;

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

		//AABBs
		addGElement(new GElementTextLabel(genNewID(), 0.05f, 0.2f, 0.2f, 0.1f, "show AABBs:", FontRenderer.LEFT));
		showAABBsButton = new GElementButton(genNewID(), 0.25f, 0.2f, 0.2f, 0.1f, String.valueOf(GameOptions.instance.getBoolOption("showAABBs")), FontRenderer.LEFT, "Buttons/button.png", false, "");

		//ok button
		okButton = new GElementButton(genNewID(), 0.3f, 0.8f, 0.4f, 0.1f, "Ok", FontRenderer.LEFT, "Buttons/button.png", false, "");

		addGElement(showAABBsButton);
		addGElement(okButton);
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
		if (id == showAABBsButton.id) {
			GameOptions.instance.switchBoolOption("showAABBs");
			showAABBsButton.text = String.valueOf(GameOptions.instance.getBoolOption("showAABBs"));
		} else if (id == okButton.id) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiVideoSettings(game));
		}
	}
}
