package de.nerogar.gameV1.gui;

import org.lwjgl.input.Keyboard;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.GameOptions;
import de.nerogar.gameV1.InputHandler;
import de.nerogar.gameV1.RenderEngine;
import de.nerogar.gameV1.RenderHelper;

public class GuiVideoSettings extends Gui {
	private int displayMode;
	private GElementButton vSyncButton, debugSettingsButton, okButton;
	private GElementOptionSlider resSlider;
	private GElementSlider fovSlider, fpsSlider, loaddistanceSlider, renderdistanceSlider;

	public GuiVideoSettings(Game game) {
		super(game);
	}

	@Override
	public boolean pauseGame() {
		return true;
	}

	@Override
	public String getName() {
		return "videoSettings";
	}

	@Override
	public void init() {
		displayMode = renderEngine.displayMode;
		setTitel("Video Settings");

		//resolution
		addGElement(new GElementTextLabel(genNewID(), 0.05f, 0.2f, 0.2f, 0.1f, "resolution:", FontRenderer.LEFT));
		resSlider = new GElementOptionSlider(genNewID(), 0.25f, 0.2f, 0.2f, 0.1f, 0.025f, renderEngine.getDisplayModesAsString(), "Buttons/button.png", "Buttons/slider.png");
		resSlider.position = displayMode;

		//vSync
		addGElement(new GElementTextLabel(genNewID(), 0.05f, 0.35f, 0.2f, 0.1f, "vSync:", FontRenderer.LEFT));
		vSyncButton = new GElementButton(genNewID(), 0.25f, 0.35f, 0.2f, 0.1f, String.valueOf(GameOptions.instance.getBoolOption("vSync")), FontRenderer.LEFT, "Buttons/button.png", false, "");

		//fps limit
		addGElement(new GElementTextLabel(genNewID(), 0.05f, 0.5f, 0.2f, 0.1f, "fps limit:", FontRenderer.LEFT));
		fpsSlider = new GElementSlider(genNewID(), 0.25f, 0.5f, 0.2f, 0.1f, 0.025f, 20, 144, "", "Buttons/button.png", "Buttons/slider.png");
		fpsSlider.position = GameOptions.instance.getDoubleOption("fps");
		fpsSlider.allowFloat = false;

		//fov
		addGElement(new GElementTextLabel(genNewID(), 0.55f, 0.2f, 0.2f, 0.1f, "fov:", FontRenderer.LEFT));
		fovSlider = new GElementSlider(genNewID(), 0.75f, 0.2f, 0.2f, 0.1f, 0.025f, 40, 110, "", "Buttons/button.png", "Buttons/slider.png");
		fovSlider.position = GameOptions.instance.getDoubleOption("fov");
		fovSlider.allowFloat = false;

		//loaddistance
		addGElement(new GElementTextLabel(genNewID(), 0.55f, 0.35f, 0.2f, 0.1f, "loaddistance:", FontRenderer.LEFT));
		loaddistanceSlider = new GElementSlider(genNewID(), 0.75f, 0.35f, 0.2f, 0.1f, 0.025f, 0, 20, "", "Buttons/button.png", "Buttons/slider.png");
		loaddistanceSlider.position = GameOptions.instance.getIntOption("loaddistance");
		loaddistanceSlider.allowFloat = false;

		//renderdistance
		addGElement(new GElementTextLabel(genNewID(), 0.55f, 0.5f, 0.2f, 0.1f, "renderdistance:", FontRenderer.LEFT));
		renderdistanceSlider = new GElementSlider(genNewID(), 0.75f, 0.5f, 0.2f, 0.1f, 0.025f, 0, 20, "", "Buttons/button.png", "Buttons/slider.png");
		renderdistanceSlider.position = GameOptions.instance.getIntOption("renderdistance");
		renderdistanceSlider.allowFloat = false;

		//debug settings
		debugSettingsButton = new GElementButton(genNewID(), 0.75f, 0.8f, 0.2f, 0.05f, "debug Settings", FontRenderer.LEFT, "Buttons/button.png", false, "");

		//ok button
		okButton = new GElementButton(genNewID(), 0.3f, 0.8f, 0.4f, 0.1f, "Ok", FontRenderer.LEFT, "Buttons/button.png", false, "");

		/*optionSliders.add(resSlider);
		buttons.add(vSyncButton);
		sliders.add(fovSlider);
		sliders.add(fpsSlider);
		sliders.add(loaddistanceSlider);
		sliders.add(renderdistanceSlider);
		buttons.add(debugSettingsButton);
		buttons.add(okButton);*/

		addGElement(resSlider);
		addGElement(vSyncButton);
		addGElement(fovSlider);
		addGElement(fpsSlider);
		addGElement(loaddistanceSlider);
		addGElement(renderdistanceSlider);
		addGElement(debugSettingsButton);
		addGElement(okButton);

	}

	@Override
	public void updateGui() {
		GameOptions.instance.setOption("fov", String.valueOf(fovSlider.position));
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
		if (id == vSyncButton.id && mouseButton == 0) {
			GameOptions.instance.switchBoolOption("vSync");
			vSyncButton.text = String.valueOf(GameOptions.instance.getBoolOption("vSync"));
		} else if (id == debugSettingsButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiDebugSettings(game));
		} else if (id == okButton.id && mouseButton == 0) {
			if (resSlider.position != displayMode) {
				renderEngine.setDisplayMode(resSlider.position);
			}
			RenderEngine.instance.setVSync();
			GameOptions.instance.setIntOption("fps", (int) fpsSlider.position);
			GameOptions.instance.setIntOption("renderdistance", (int) renderdistanceSlider.position);
			GameOptions.instance.setIntOption("loaddistance", (int) loaddistanceSlider.position);

			game.guiList.removeGui(getName());
			if (game.world.isLoaded) {
				game.guiList.addGui(new GuiPauseMenu(game));
			} else {
				game.guiList.addGui(new GuiMain(game));
			}
		}
	}
}
