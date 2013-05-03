package de.nerogar.gameV1.gui;

import java.util.Random;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.SaveProvider;

public class GuiCreateWorld extends Gui {
	private GElementButton createButton, backButton;
	private GElementTextField worldNameTextField, seedTextField;
	SaveProvider saveProvider;

	public GuiCreateWorld(Game game) {
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
		saveProvider = new SaveProvider();
		setTitel("Create World");

		addGElement(new GElementTextLabel(genNewID(), 0.05f, 0.2f, 0.4f, 0.1f, "name:", FontRenderer.LEFT));
		worldNameTextField = new GElementTextField(0.5f, 0.2f, 0.4f, 0.1f, "", "Buttons/textField.png");

		addGElement(new GElementTextLabel(genNewID(), 0.05f, 0.3f, 0.4f, 0.1f, "seed:", FontRenderer.LEFT));
		addGElement(new GElementTextLabel(genNewID(), 0.05f, 0.35f, 0.4f, 0.1f, "(leave blank for a random seed)", FontRenderer.LEFT));
		seedTextField = new GElementTextField(0.5f, 0.3f, 0.4f, 0.1f, "", "Buttons/textField.png");
		seedTextField.numOnly = true;

		createButton = new GElementButton(genNewID(), 0.3f, 0.7f, 0.4f, 0.1f, "create new World", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		backButton = new GElementButton(genNewID(), 0.3f, 0.8f, 0.4f, 0.1f, "back", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		addGElement(worldNameTextField);
		addGElement(seedTextField);
		addGElement(createButton);
		addGElement(backButton);
	}

	@Override
	public void updateGui() {
		createButton.enabled = !worldNameTextField.getText().equals("");
	}

	@Override
	public void render() {
		RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == createButton.id && mouseButton == 0) {
			String name = worldNameTextField.getText();
			String seed = seedTextField.getText();

			if (!name.equals("")) {
				if (!seed.equals("")) {
					saveProvider.loadWorld(game, worldNameTextField.getText(), Long.parseLong(seedTextField.getText()));
				} else {
					Random rd = new Random();
					saveProvider.loadWorld(game, worldNameTextField.getText(), rd.nextLong());
				}
				game.guiList.removeGui(getName());
			}
		} else if (id == backButton.id && mouseButton == 0) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiLoadWorld(game));
		}
	}
}