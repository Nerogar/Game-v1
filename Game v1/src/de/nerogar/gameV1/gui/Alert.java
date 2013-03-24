package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.RenderHelper;

public class Alert extends Gui {
	public String message;
	public GElementTextLabel messageLabel;
	private GElementButton okButton;
	private boolean removed = false;

	public Alert(Game game, String message) {
		super(game);
		this.message = message;
		messageLabel.text = message;
	}

	@Override
	public void init() {
		messageLabel = new GElementTextLabel(genNewID(), 0.15f, 0.35f, 0.7f, 0.1f, message, FontRenderer.CENTERED);
		addGElement(messageLabel);

		okButton = new GElementButton(genNewID(), 0.4f, 0.6f, 0.2f, 0.07f, "ok", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		addGElement(okButton);
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == okButton.id) {
			close();
		}
	}

	@Override
	public boolean pauseGame() {
		return true;
	}

	public void render() {
		RenderHelper.renderAlertBackground();
		super.render();
	}

	public void close() {
		game.guiList.removeAlert(this);
		removed = true;
	}

	public boolean updateNext() {
		return !removed;
	}
}
