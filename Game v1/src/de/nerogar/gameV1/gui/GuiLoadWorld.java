package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;
import de.nerogar.gameV1.SaveProvider;

public class GuiLoadWorld extends Gui {
	private GElementButton deleteButton;
	private GElementButton loadButton;
	private GElementButton renameButton;
	private GElementButton backButton;
	private GElementListBox saveList;
	private AlertGetMessage newWorldNameAlert;
	private AlertYesNo deleteWorldAlert;
	SaveProvider saveProvider;

	public GuiLoadWorld(Game game) {
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

		saveProvider.updateSaves();
		String[] saveNames = saveProvider.getSavesAsStrings();

		textLabels.add(new GElementTextLabel(genNewID(), 0.0f, 0.05f, 0.4f, 0.1f, "load world", FontRenderer.CENTERED));

		loadButton = new GElementButton(genNewID(), 0.05f, 0.35f, 0.3f, 0.1f, "load world", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		loadButton.enabled = false;
		
		renameButton = new GElementButton(genNewID(), 0.05f, 0.5f, 0.3f, 0.1f, "rename world", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		renameButton.enabled = false;
		
		deleteButton = new GElementButton(genNewID(), 0.05f, 0.65f, 0.3f, 0.1f, "delete world", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		deleteButton.enabled = false;

		backButton = new GElementButton(genNewID(), 0.05f, 0.8f, 0.3f, 0.1f, "back", FontRenderer.CENTERED, "Buttons/button.png", false, "");

		saveList = new GElementListBox(genNewID(), 0.4f, 0.0f, 0.6f, 1.0f, saveNames, "Buttons/button.png", "Buttons/scrollbar.png");
		saveList.showedItems = 10;

		lists.add(saveList);
		buttons.add(deleteButton);
		buttons.add(loadButton);
		buttons.add(renameButton);
		buttons.add(backButton);

		newWorldNameAlert = new AlertGetMessage(game, "Enter new worldname.", true);
		deleteWorldAlert = new AlertYesNo(game, "delete?");
	}

	@Override
	public void updateGui() {
		loadButton.enabled = saveList.clickedIndex != -1;
		renameButton.enabled = saveList.clickedIndex != -1;
		deleteButton.enabled = saveList.clickedIndex != -1;

		if (newWorldNameAlert.hasNewText) {
			String newWorldName = newWorldNameAlert.getText();
			if (newWorldName != null) {
				saveProvider.renameWorld(saveList.clickedIndex, newWorldName);
				saveList.text = saveProvider.getSavesAsStrings();
			}
		}
		if (deleteWorldAlert.hasNewBoolean && deleteWorldAlert.returnBoolean) {
			System.out.println(true+"");
			//System.out.println(saveProvider.deleteWorld(saveList.clickedIndex));
		}

	}

	@Override
	public void render() {
		RenderHelper.renderDefaultGuiBackground();
		renderGui();
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == loadButton.id) {
			game.guiList.removeGui(getName());
			saveProvider.loadWorld(game, saveList.clickedIndex);
		} else if (id == renameButton.id) {
			game.guiList.alert(newWorldNameAlert);
		} else if (id == deleteButton.id) {
			game.guiList.alert(deleteWorldAlert);
		} else if (id == backButton.id) {
			game.guiList.removeGui(getName());
			game.guiList.addGui(new GuiMain(game));
		}
	}
}