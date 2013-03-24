package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;

public class AlertGetMessage extends Alert {
	public String text;
	public boolean cancelAllowed;
	public boolean hasNewText = false;
	private GElementButton okButton, cancelButton;
	private GElementTextField textTextField;
	private boolean removed = false;

	public AlertGetMessage(Game game, String message, boolean cancelAllowed) {
		super(game, message);
		this.cancelAllowed = cancelAllowed;
		setButtons();
	}

	@Override
	public void init() {
		messageLabel = new GElementTextLabel(genNewID(), 0.15f, 0.35f, 0.7f, 0.1f, message, FontRenderer.CENTERED);
		addGElement(messageLabel);

		textTextField = new GElementTextField(0.3f, 0.45f, 0.4f, 0.1f, "", "Buttons/textField.png");
		addGElement(textTextField);

	}

	private void setButtons() {
		if (cancelAllowed) {
			okButton = new GElementButton(genNewID(), 0.3f, 0.6f, 0.2f, 0.07f, "ok", FontRenderer.CENTERED, "Buttons/button.png", false, "");
			cancelButton = new GElementButton(genNewID(), 0.5f, 0.6f, 0.2f, 0.07f, "cancel", FontRenderer.CENTERED, "Buttons/button.png", false, "");

			addGElement(cancelButton);
		} else {
			okButton = new GElementButton(genNewID(), 0.4f, 0.6f, 0.2f, 0.07f, "ok", FontRenderer.CENTERED, "Buttons/button.png", false, "");
		}

		addGElement(okButton);
	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == okButton.id) {
			text = textTextField.getText();
			hasNewText = true;
			close();
		} else if (id == cancelButton.id) {
			//hasNewText = false;
			close();
		}
	}

	public String getText() {
		if (hasNewText) {
			hasNewText = false;
			return text;
		} else {
			return null;
		}
	}

	public void close() {
		game.guiList.removeAlert(this);
		removed = true;
		textTextField.setText("");
	}

	public boolean updateNext() {
		return !removed;
	}
}
