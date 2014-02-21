package de.nerogar.gameV1.gui;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector2d;
import de.nerogar.gameV1.World;

public class GuiDebug extends Gui {
	private GElementTextLabel fpsLabel;
	private GElementTextLabel[] labels;
	private GElementButton resetCamButton;
	private int delayCount = 0;
	private World world;

	public GuiDebug(Game game, World world) {
		super(game);
		this.world = world;
	}

	@Override
	public String getName() {
		return "debug";
	}

	@Override
	public void init() {
		labels = new GElementTextLabel[10];

		labels[0] = new GElementTextLabel(genNewID(), 0.01F, 0F, .5F, .05F, "Game v1", FontRenderer.LEFT);
		labels[1] = new GElementTextLabel(genNewID(), 0.01F, 0.03F, 1F, .05F, "", FontRenderer.LEFT);

		labels[2] = new GElementTextLabel(genNewID(), 0.01F, 0.09F, .5F, .05F, "", FontRenderer.LEFT);
		labels[3] = new GElementTextLabel(genNewID(), 0.01F, 0.12F, .5F, .05F, "", FontRenderer.LEFT);
		labels[4] = new GElementTextLabel(genNewID(), 0.01F, 0.15F, .5F, .05F, "", FontRenderer.LEFT);
		labels[5] = new GElementTextLabel(genNewID(), 0.01F, 0.18F, .5F, .05F, "", FontRenderer.LEFT);
		labels[6] = new GElementTextLabel(genNewID(), 0.01F, 0.21F, .5F, .05F, "", FontRenderer.LEFT);

		labels[7] = new GElementTextLabel(genNewID(), 0.01F, 0.24F, .5F, .05F, "", FontRenderer.LEFT);
		labels[8] = new GElementTextLabel(genNewID(), 0.01F, 0.27F, .5F, .05F, "", FontRenderer.LEFT);
		labels[9] = new GElementTextLabel(genNewID(), 0.01F, 0.30F, .5F, .05F, "", FontRenderer.LEFT);

		resetCamButton = new GElementButton(genNewID(), 0.95f, 0.98f, 0.05f, 0.02f, "res cam", FontRenderer.LEFT, "buttons/button.png", false, "");

		fpsLabel = new GElementTextLabel(genNewID(), 0.84F, 0F, .5F, .05F, "FPS: 0", FontRenderer.LEFT);

		//testLabel = new GuiTextLabel(genNewID(), 0.0f, 0.0f, 1.0f, 0.3f, "test");
		for (int i = 0; i < labels.length; i++)
			addGElement(labels[i]);
		addGElement(fpsLabel);

		addGElement(resetCamButton);

		//addGElement(label1);
		//game.world.collisionComparer.renderGrid();
		//updateGui();
	}

	@Override
	public void updateGui() {
		//testLabel.text = "test";
		delayCount++;
		if (delayCount > 10) {
			fpsLabel.text = "FPS: " + String.valueOf(game.timer.mfFps);
			delayCount = 0;
		}
		labels[1].text = "Entities: " + world.entityList.entities.size() + ", TempEntities: " + world.entityList.tempEntities.size() + ", Vergleiche: " + world.collisionComparer.comparations;

		updateStressTimes();

		if (world.player != null) {
			labels[7].text = "X: " + world.player.camera.scrollX;
			labels[8].text = "Y: " + world.player.camera.scrollY;
			labels[9].text = "Z: " + world.player.camera.scrollZ;
		}
	}

	private void updateStressTimes() {

		long tMain = game.stressTimeMainloop;
		long tRender = game.stressTimeRender;
		long tUpdate = game.stressTimeUpdate;
		long tTotal = game.stressTimeTotal;
		long tRest = tTotal - (tMain + tRender + tUpdate);

		labels[2].text = "PHYSIC+:     " + ((int) 100 * tMain / tTotal) + "% (" + ((int) tMain / 1000000) + "ms)";
		labels[3].text = "PRERENDER:   " + ((int) 100 * tRender / tTotal) + "% (" + ((int) tRender / 1000000) + "ms)";
		labels[4].text = "WAIT/RENDER: " + ((int) 100 * tUpdate / tTotal) + "% (" + ((int) tUpdate / 1000000) + "ms)";
		labels[5].text = "REST:        " + ((int) 100 * tRest / tTotal) + "% (" + ((int) tRest / 1000000) + "ms)";
		labels[6].text = "TOTAL:       " + ((int) tTotal / 1000000) + " ms";

	}

	@Override
	public void clickButton(int id, int mouseButton) {
		if (id == resetCamButton.id && mouseButton == 0) {
			world.player.camera.setCenter(new Vector2d(0, 0));
			world.player.camera.setRotation(0, 0);
			world.player.camera.updatePostition();
		}
	}
}