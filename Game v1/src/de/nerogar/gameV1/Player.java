package de.nerogar.gameV1;

import de.nerogar.gameV1.gui.GuiBuildingTest;
import de.nerogar.gameV1.level.BuildingBank;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityBuilding;
import de.nerogar.gameV1.level.Position;

public class Player {
	private Game game;
	private EntityBuilding buildingOnCursor;
	public GuiBuildingTest guiBuildingTest;

	public Player(Game game) {
		this.game = game;
		guiBuildingTest = new GuiBuildingTest(game);
		game.guiList.addGui(guiBuildingTest);
	}

	public void renderInWorld(World world) {
		renderEntityOnCursor(world);
	}

	public void update(World world) {
		updateBuilding(world);
	}

	private void updateBuilding(World world) {
		if (guiBuildingTest.idChanged) {
			buildingOnCursor = (EntityBuilding) Entity.getEntity(game, BuildingBank.getBuildingName(guiBuildingTest.getBuildingId()));
			if (buildingOnCursor != null) {
				buildingOnCursor.init(world);
				buildingOnCursor.opacity = 0.7f;
			}
		}
		if (buildingOnCursor != null) {
			Vector3d mousePos = InputHandler.get3DmousePosition();
			if (mousePos != null) {
				int x = (int) Math.round(mousePos.getX());
				int z = (int) Math.round(mousePos.getZ());
				Vector3d pos = new Vector3d(x, game.world.land.getHeight(new Position(x, z)), z);
				buildingOnCursor.matrix.setPosition(pos);
			} else {
				buildingOnCursor.matrix.setPosition(null);
			}
		}
	}

	private void renderEntityOnCursor(World world) {
		if (buildingOnCursor != null && buildingOnCursor.matrix.position != null) {
			buildingOnCursor.opacity = 0.5f;
			buildingOnCursor.render();

			Position pos = new Position(MathHelper.roundDownToInt(buildingOnCursor.matrix.position.getX(), 1), MathHelper.roundDownToInt(buildingOnCursor.matrix.position.getZ(), 1));
			boolean buildable = world.land.isBuildable(buildingOnCursor, pos);
			RenderHelper.enableAlpha();
			Vector2d posA = new Vector2d(buildingOnCursor.getAABB().a.getX(), buildingOnCursor.getAABB().a.getZ());
			Vector2d posB = new Vector2d(buildingOnCursor.getAABB().b.getX(), buildingOnCursor.getAABB().b.getZ());
			Vector3d a = new Vector3d(posA.getX(), world.land.getHeight(posA) + 0.1, posA.getZ());
			Vector3d b = new Vector3d(posA.getX(), world.land.getHeight(posA.getX(), posB.getZ()) + 0.1, posB.getZ());
			Vector3d c = new Vector3d(posB.getX(), world.land.getHeight(posB) + 0.1, posB.getZ());
			Vector3d d = new Vector3d(posB.getX(), world.land.getHeight(posB.getX(), posA.getZ()) + 0.1, posA.getZ());
			if (buildable) {
				RenderHelper.drawQuad(a, b, c, d, 0, 1, 0, 0.3f);
			} else {
				RenderHelper.drawQuad(a, b, c, d, 1, 0, 0, 0.3f);
			}
			RenderHelper.disableAlpha();
		}
	}
}
