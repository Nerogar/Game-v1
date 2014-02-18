package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;

public class AlertYesNo extends Alert {
	public boolean returnBoolean;
	public boolean hasNewBoolean = false;
	private GElementButton yesButton, noButton;
	private boolean removed = false;

	public AlertYesNo(Game game, String message) {
		super(game, message);
	}

	@Override
	public void init() {
		messageLabel = new GElementTextLabel(genNewID(), 0.15f, 0.35f, 0.7f, 0.1f, message, FontRenderer.CENTERED);
		addGElement(messageLabel);

		yesButton = new GElementButton(genNewID(), 0.3f, 0.6f, 0.2f, 0.07f, "yes", FontRenderer.CENTERED, "buttons/button.png", false, "");
		noButton = new GElementButton(genNewID(), 0.5f, 0.6f, 0.2f, 0.07f, "no", FontRenderer.CENTERED, "buttons/button.png", false, "");

		addGElement(yesButton);
		addGElement(noButton);
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == yesButton.id) {
			returnBoolean = true;
			close();
		} else if (id == noButton.id) {
			returnBoolean = false;
			close();
		}
	}

	public boolean getClicked() {
		if (hasNewBoolean) {
			hasNewBoolean = false;
			return returnBoolean;
		} else {
			return false;
		}
	}

	public void close() {
		game.guiList.removeAlert(this);
		removed = true;
		hasNewBoolean = true;
	}

	public boolean updateNext() {
		return !removed;
	}
}
