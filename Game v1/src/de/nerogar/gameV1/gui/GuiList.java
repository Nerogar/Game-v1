package de.nerogar.gameV1.gui;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;

import de.nerogar.gameV1.RenderEngine;
import de.nerogar.gameV1.RenderHelper;

public class GuiList {
	private ArrayList<Gui> guis = new ArrayList<Gui>();
	private ArrayList<Alert> alerts = new ArrayList<Alert>();
	private ArrayList<Gui> newGuis = new ArrayList<Gui>();
	private ArrayList<Alert> newAlerts = new ArrayList<Alert>();

	public void alert(Alert alert) {
		newAlerts.add(alert);
	}

	public void removeAlert(Alert alert) {
		for (int i = 0; i < alerts.size(); i++) {
			if (alerts.get(i) == alert) {
				alerts.remove(i);
			}
		}
	}

	public void addGui(Gui gui) {
		if (!isGuiLoaded(gui.getName())) {
			newGuis.add(gui);
		}
	}

	public void removeGui(String name) {
		for (int i = 0; i < guis.size(); i++) {
			if (guis.get(i).getName().equals(name)) {
				guis.remove(i);
			}
		}
	}

	public void removeGui(Gui gui) {
		removeGui(gui.getName());
	}

	public boolean isGuiLoaded(String name) {
		for (int i = 0; i < guis.size(); i++) {
			if (guis.get(i).getName().equals(name)) return true;
		}
		return false;
	}

	public boolean isGuiLoaded(Gui gui) {
		return isGuiLoaded(gui.getName());
	}

	public boolean noGuiLoaded() {
		return (guis.size() == 0);
	}

	public boolean activeAlert() {
		return (alerts.size() != 0);
	}

	public boolean pauseGame() {
		for (int i = 0; i < guis.size(); i++) {
			if (guis.get(i).pauseGame()) { return true; }
		}

		for (int i = 0; i < alerts.size(); i++) {
			if (alerts.get(i).pauseGame()) { return true; }
		}

		for (int i = 0; i < newGuis.size(); i++) {
			if (newGuis.get(i).pauseGame()) { return true; }
		}

		for (int i = 0; i < newAlerts.size(); i++) {
			if (newAlerts.get(i).pauseGame()) { return true; }
		}

		return false;
	}

	public void update() {
		if (noGuiLoaded() && !activeAlert()) return;
		if (!activeAlert()) {
			for (int i = 0; i < guis.size(); i++) {
				guis.get(i).update();
			}
		}

		boolean updateNextAlert = true;
		for (int i = alerts.size() - 1; i >= 0; i--) {
			Alert alert = alerts.get(i);
			if (updateNextAlert) alert.update();
			if (!alert.updateNext()) updateNextAlert = false;
		}
	}

	public void render() {

		for (int i = 0; i < newGuis.size(); i++) {
			guis.add(newGuis.get(i));
		}
		newGuis = new ArrayList<Gui>();

		for (int i = 0; i < newAlerts.size(); i++) {
			alerts.add(newAlerts.get(i));
		}
		newAlerts = new ArrayList<Alert>();

		RenderEngine.instance.setOrtho();
		beginRender();
		if (noGuiLoaded() && !activeAlert()) { return; }

		for (int i = 0; i < guis.size(); i++) {
			guis.get(i).render();
		}

		for (int i = 0; i < alerts.size(); i++) {
			alerts.get(i).render();
		}

		endRender();
	}

	public void beginRender() {
		RenderHelper.enableAlpha();
	}

	public void endRender() {
		RenderHelper.disableAlpha();
	}
}