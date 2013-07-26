package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL20.*;

import de.nerogar.gameV1.graphics.Shader;
import de.nerogar.gameV1.graphics.ShaderBank;
import de.nerogar.gameV1.gui.GuiBuildingTest;
import de.nerogar.gameV1.level.Entity;
import de.nerogar.gameV1.level.EntityBuilding;
import de.nerogar.gameV1.level.Position;
import de.nerogar.gameV1.network.PacketBuildHouse;
import de.nerogar.gameV1.network.PacketClickEntity;
import de.nerogar.gameV1.physics.Ray;

public class Player {
	public Game game;
	public World world;
	public Camera camera;
	public int buildingOnCursorID = -1;
	public EntityBuilding buildingOnCursor;
	private boolean isbuildingOnCursorBuildable;
	public GuiBuildingTest guiBuildingTest;

	public Player(Game game, World world) {
		this.game = game;
		this.world = world;
		guiBuildingTest = new GuiBuildingTest(game);
		guiBuildingTest.player = this;
		game.guiList.addGui(guiBuildingTest);
		camera = new Camera(world);
	}

	public void renderInWorld(World world) {
		renderEntityOnCursor(world);
	}

	public void update() {
		updateBuilding();
		handleMouseClick();
	}

	private void updateBuilding() {
		/*if (guiBuildingTest.idChanged) {
			buildingOnCursor = (EntityBuilding) Entity.getEntity(game, world, BuildingBank.getBuildingName(guiBuildingTest.getBuildingId()));
			if (buildingOnCursor != null) {
				buildingOnCursor.init(world);
				buildingOnCursor.opacity = 0.7f;
			}
		}*/

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
				PacketBuildHouse packetBuildHouse = new PacketBuildHouse();
				packetBuildHouse.buildingID = buildingOnCursorID;
				packetBuildHouse.buildPos = new Position(MathHelper.roundDownToInt(buildingOnCursor.matrix.position.getX(), 1), MathHelper.roundDownToInt(buildingOnCursor.matrix.position.getZ(), 1));

				world.client.sendPacket(packetBuildHouse);

				//world.spawnEntity(buildingOnCursor);
				buildingOnCursor = null;
			}
		}
	}

	private void renderEntityOnCursor(World world) {
		if (buildingOnCursor != null && buildingOnCursor.matrix.position != null) {
			//buildingOnCursor.opacity = 0.5f;
			//buildingOnCursor.render();

			if (isbuildingOnCursorBuildable) {
				buildingOnCursor.render();
			}

			RenderHelper.enableAlpha();

			Shader terrainShader = ShaderBank.instance.getShader("terrain");
			terrainShader.activate();
			if (isbuildingOnCursorBuildable) {
				//RenderHelper.drawQuad(a, b, c, d, 0x00ff0066);
				glUniform1i(terrainShader.uniforms.get("buildQuadRender"), 0);
			} else {
				Vector2d posA = new Vector2d(buildingOnCursor.getAABB().a.getX(), buildingOnCursor.getAABB().a.getZ());
				Vector2d posB = new Vector2d(buildingOnCursor.getAABB().b.getX(), buildingOnCursor.getAABB().b.getZ());
				Vector3d a = new Vector3d(posA.getX(), world.land.getHeight(posA) + 0.1, posA.getZ());
				Vector3d b = new Vector3d(posA.getX(), world.land.getHeight(posA.getX(), posB.getZ()) + 0.1, posB.getZ());
				Vector3d c = new Vector3d(posB.getX(), world.land.getHeight(posB) + 0.1, posB.getZ());
				Vector3d d = new Vector3d(posB.getX(), world.land.getHeight(posB.getX(), posA.getZ()) + 0.1, posA.getZ());
				//RenderHelper.drawQuad(a, b, c, d, 0xff000066);

				glUniform2f(terrainShader.uniforms.get("buildQuadA"), a.getXf(), a.getZf());
				glUniform2f(terrainShader.uniforms.get("buildQuadB"), c.getXf(), c.getZf());
				glUniform1i(terrainShader.uniforms.get("buildQuadRender"), 1);
				glUniform4f(terrainShader.uniforms.get("buildQuadColor"), 1.0f, 0.0f, 0.0f, 1.0f);
			}
			RenderHelper.disableAlpha();
			terrainShader.deactivate();
		}
	}

	public void handleMouseClick() {
		Ray sightRay = new Ray(InputHandler.get3DmouseStart(), InputHandler.get3DmouseDirection());
		//long time1 = System.nanoTime();
		Entity[] clickedEntities = world.entityList.getEntitiesInSight(sightRay);
		//long time2 = System.nanoTime();
		//System.out.println("Pick time: " + (time2 - time1) / 1000000D);
		//double time1 = System.nanoTime();
		Vector3d floorIntersection = world.land.getFloorpointInSight(sightRay);
		//double time2 = System.nanoTime();
		//if (Timer.instance.getFramecount() % 60 == 0 && GameOptions.instance.getBoolOption("debug")) System.out.println("zeit für Bodenkollisionsberechnung letzten Frame: " + ((time2 - time1) / 1000000) + "ms");

		if (clickedEntities.length > 0) {

			if (InputHandler.isMouseButtonPressed(0)) {
				PacketClickEntity clickPacket = new PacketClickEntity();
				clickPacket.entityID = clickedEntities[0].id;
				clickPacket.mouseButton = 0;
				world.client.sendPacket(clickPacket);
			} else if (InputHandler.isMouseButtonPressed(1)) {
				PacketClickEntity clickPacket = new PacketClickEntity();
				clickPacket.entityID = clickedEntities[0].id;
				clickPacket.mouseButton = 1;
				world.client.sendPacket(clickPacket);
			}

		}

		/*if (floorIntersection != null) {
			if (InputHandler.isMouseButtonPressed(0)) {
				land.click(0, floorIntersection);
				if (pathStart != null) {
					pathEnd = pathfinder.getNode(new Position(MathHelper.roundDownToInt(floorIntersection.getX(), 1), MathHelper.roundDownToInt(floorIntersection.getZ(), 1)));
					//pathEnd = pathfinder.getNode(new Position(-28, 0));
					int multiplier = 10;
					long time1 = System.nanoTime();
					for (int i = 0; i < multiplier; i++) {
						path = new Path(pathStart, pathEnd);
					}
					long time2 = System.nanoTime();
					System.out.println("Calculated "+multiplier+" Paths -> total: " + ((time2 - time1) / 1000000d) + "ms | individual: " + ((time2 - time1) / (1000000d * multiplier)));
				}
			} else if (InputHandler.isMouseButtonPressed(1)) {
				land.click(1, floorIntersection);
				pathStart = pathfinder.getNode(new Position(MathHelper.roundDownToInt(floorIntersection.getX(), 1), MathHelper.roundDownToInt(floorIntersection.getZ(), 1)));
				//pathStart = pathfinder.getNode(new Position(12, 28));
			}
		}*/

		InputHandler.set3DmousePosition(floorIntersection);
		if (floorIntersection != null) {
			if (InputHandler.isMouseButtonPressed(0)) {
				world.land.click(0, floorIntersection);
				//ObjectMatrix om = new ObjectMatrix(new Vector3d(Math.floor(floorIntersection.getX()), floorIntersection.getY(), Math.floor(floorIntersection.getZ())));
				//spawnEntity(new EntityHouse(game, om));
				// wtf, warum machst du das hierhin?
			}
			if (InputHandler.isMouseButtonPressed(1)) {
				world.land.click(1, floorIntersection);
			}
			if (InputHandler.isMouseButtonPressed(2)) {
				world.land.click(2, floorIntersection);
			}
			world.land.setMousePos(floorIntersection);
		}
	}
}
