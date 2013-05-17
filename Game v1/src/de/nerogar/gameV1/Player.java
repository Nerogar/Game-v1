package de.nerogar.gameV1;

import de.nerogar.gameV1.gui.GuiBuildingTest;
import de.nerogar.gameV1.level.BuildingBank;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityBuilding;
import de.nerogar.gameV1.level.Position;

public class Player {
	private Game game;
	private World world;
	private EntityBuilding buildingOnCursor;
	private boolean isbuildingOnCursorBuildable;
	public GuiBuildingTest guiBuildingTest;

	public Player(Game game, World world) {
		this.game = game;
		this.world = world;
		guiBuildingTest = new GuiBuildingTest(game);
		game.guiList.addGui(guiBuildingTest);
	}

	public void renderInWorld(World world) {
		renderEntityOnCursor(world);
	}

	public void update() {
		updateBuilding();
	}

	private void updateBuilding() {
		if (guiBuildingTest.idChanged) {
			buildingOnCursor = (EntityBuilding) Entity.getEntity(game, world, BuildingBank.getBuildingName(guiBuildingTest.getBuildingId()));
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

				Position floorPos = new Position(MathHelper.roundDownToInt(buildingOnCursor.matrix.position.getX(), 1), MathHelper.roundDownToInt(buildingOnCursor.matrix.position.getZ(), 1));
				floorPos.subtract(buildingOnCursor.centerPosition);
				isbuildingOnCursorBuildable = world.land.isBuildable(buildingOnCursor, floorPos);
			} else {
				buildingOnCursor.matrix.setPosition(null);
			}
		}

		if (InputHandler.isMouseButtonPressed(0) && isbuildingOnCursorBuildable) {
			if (buildingOnCursor != null && buildingOnCursor.matrix.position != null) {
				EntityBuilding building = (EntityBuilding) Entity.getEntity(game, world, BuildingBank.getBuildingName(guiBuildingTest.getBuildingId()));
				building.init(world);
				building.matrix.setPosition(buildingOnCursor.matrix.position);
				world.spawnEntity(building);
			}
		}
	}

	private void renderEntityOnCursor(World world) {
		if (buildingOnCursor != null && buildingOnCursor.matrix.position != null) {
			buildingOnCursor.opacity = 0.5f;
			//buildingOnCursor.render();

			if (isbuildingOnCursorBuildable) {
				buildingOnCursor.render();
			}

			RenderHelper.enableAlpha();
			Vector2d posA = new Vector2d(buildingOnCursor.getAABB().a.getX(), buildingOnCursor.getAABB().a.getZ());
			Vector2d posB = new Vector2d(buildingOnCursor.getAABB().b.getX(), buildingOnCursor.getAABB().b.getZ());
			Vector3d a = new Vector3d(posA.getX(), world.land.getHeight(posA) + 0.1, posA.getZ());
			Vector3d b = new Vector3d(posA.getX(), world.land.getHeight(posA.getX(), posB.getZ()) + 0.1, posB.getZ());
			Vector3d c = new Vector3d(posB.getX(), world.land.getHeight(posB) + 0.1, posB.getZ());
			Vector3d d = new Vector3d(posB.getX(), world.land.getHeight(posB.getX(), posA.getZ()) + 0.1, posA.getZ());
			if (isbuildingOnCursorBuildable) {
				//RenderHelper.drawQuad(a, b, c, d, 0x00ff0066);
			} else {
				RenderHelper.drawQuad(a, b, c, d, 0xff000066);
			}
			RenderHelper.disableAlpha();
		}
	}
}
