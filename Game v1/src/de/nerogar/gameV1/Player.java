package de.nerogar.gameV1;

import static org.lwjgl.opengl.GL20.*;
import de.nerogar.gameV1.graphics.Shader;
import de.nerogar.gameV1.graphics.ShaderBank;
import de.nerogar.gameV1.gui.GuiBuildingTest;
import de.nerogar.gameV1.internalServer.Faction;
import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.network.*;
import de.nerogar.gameV1.physics.Ray;

public class Player {
	public Game game;
	public World world;
	public Camera camera;
	public Faction ownFaction;
	public EntityBuilding buildingOnCursor;
	private boolean isbuildingOnCursorBuildable;
	private Ray sightRay;

	public EntityFighting selectedUnit;

	public GuiBuildingTest guiBuildingTest;

	public Player(Game game, World world, Faction faction) {
		this.game = game;
		this.world = world;
		this.ownFaction = faction;
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
		sightRay = new Ray(InputHandler.get3DmouseStart(), InputHandler.get3DmouseDirection());
		Vector3d floorIntersection = world.land.getFloorpointInSight(sightRay);
		InputHandler.set3DmousePosition(floorIntersection);

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

				isbuildingOnCursorBuildable = isbuildingOnCursorBuildable && ownFaction.isEntityInTowerRange(buildingOnCursor);
			} else {
				buildingOnCursor.matrix.setPosition(null);
			}
		}
		
	}

	private void renderEntityOnCursor(World world) {
		Shader terrainShader = ShaderBank.instance.getShader("terrain");
		terrainShader.activate();

		if (buildingOnCursor != null && buildingOnCursor.matrix.position != null) {
			//buildingOnCursor.opacity = 0.5f;
			//buildingOnCursor.render();

			RenderHelper.enableAlpha();
			if (isbuildingOnCursorBuildable) {
				buildingOnCursor.render();
			}
			RenderHelper.disableAlpha();

			if (isbuildingOnCursorBuildable) {
				//RenderHelper.drawQuad(a, b, c, d, 0x00ff0066);
				glUniform1i(terrainShader.uniforms.get("buildQuadRender"), 0);
			} else {
				//Vector2d posA = new Vector2d(buildingOnCursor.getAABB().a.getX(), buildingOnCursor.getAABB().a.getZ());
				//Vector2d posB = new Vector2d(buildingOnCursor.getAABB().b.getX(), buildingOnCursor.getAABB().b.getZ());

				Vector2d posA = new Vector2d(buildingOnCursor.matrix.getPosition().getX() - (buildingOnCursor.centerPosition.x), buildingOnCursor.matrix.getPosition().getZf() - (buildingOnCursor.centerPosition.z));
				Vector2d posB = new Vector2d(buildingOnCursor.matrix.getPosition().getX() + (buildingOnCursor.size.x - buildingOnCursor.centerPosition.x), buildingOnCursor.matrix.getPosition().getZf() + (buildingOnCursor.size.z - buildingOnCursor.centerPosition.z));

				glUniform2f(terrainShader.uniforms.get("buildQuadA"), posA.getXf(), posA.getZf());
				glUniform2f(terrainShader.uniforms.get("buildQuadB"), posB.getXf(), posB.getZf());
				glUniform1i(terrainShader.uniforms.get("buildQuadRender"), 1);
				glUniform4f(terrainShader.uniforms.get("buildQuadColor"), 1.0f, 0.0f, 0.0f, 1.0f);
			}

		} else {
			glUniform1i(terrainShader.uniforms.get("buildQuadRender"), 0);
		}

		terrainShader.deactivate();
	}

	public void handleMouseClick() {
		if (game.guiList.usedGui) return;
		Entity[] clickedEntities = world.entityList.getEntitiesInSight(sightRay);

		/*if (clickedEntities.length > 0) {
			Entity clickedEntity = clickedEntities[0];

			if (clickedEntity instanceof EntityFighting) {
				if (InputHandler.isMouseButtonPressed(1)) {
					selectedUnit = (EntityFighting) clickedEntity;
					System.out.println("entitySelected");
				} else if (InputHandler.isMouseButtonPressed(0)) {
					if (selectedUnit != null) selectedUnit.sendStartAttack((EntityFighting) clickedEntity);
					System.out.println("entityTargeted");
				}

			}
			if (InputHandler.isMouseButtonPressed(0)) {
				EntityPacketClick clickPacket = new EntityPacketClick();
				clickPacket.entityID = clickedEntity.id;
				clickPacket.mouseButton = 0;
				world.client.sendPacket(clickPacket);
			} else if (InputHandler.isMouseButtonPressed(1)) {
				EntityPacketClick clickPacket = new EntityPacketClick();
				clickPacket.entityID = clickedEntity.id;
				clickPacket.mouseButton = 1;
				world.client.sendPacket(clickPacket);
			}

		} else {
			if (InputHandler.isMouseButtonPressed(1)) selectedUnit = null;

			if (InputHandler.get3DmousePosition() != null) {
				if (InputHandler.isMouseButtonPressed(0)) {
					if (selectedUnit != null) {
						selectedUnit.sendStartMoving(InputHandler.get3DmousePosition());
					}

					//world.land.click(0, floorIntersection);
					//ObjectMatrix om = new ObjectMatrix(new Vector3d(Math.floor(floorIntersection.getX()), floorIntersection.getY(), Math.floor(floorIntersection.getZ())));
					//spawnEntity(new EntityHouse(game, om));
					// wtf, warum machst du das hierhin?
				}

				if (InputHandler.isMouseButtonPressed(1)) {
					world.land.click(1, InputHandler.get3DmousePosition());
				}

				if (InputHandler.isMouseButtonPressed(2)) {
					world.land.click(2, InputHandler.get3DmousePosition());
				}

				if (InputHandler.isMouseButtonPressed(0) && isbuildingOnCursorBuildable) {
					if (buildingOnCursor != null && buildingOnCursor.matrix.position != null) {
						FactionPacketBuildHouse packetBuildHouse = new FactionPacketBuildHouse();
						packetBuildHouse.factionID = ownFaction.id;
						packetBuildHouse.buildingID = buildingOnCursor.getNameTag();
						packetBuildHouse.buildPos = new Position(MathHelper.roundDownToInt(buildingOnCursor.matrix.position.getX(), 1), MathHelper.roundDownToInt(buildingOnCursor.matrix.position.getZ(), 1));

						world.client.sendPacket(packetBuildHouse);

						//world.spawnEntity(buildingOnCursor);
						buildingOnCursor = null;
					}
				}
			}
		}*/

		if (buildingOnCursor != null) {
			if (InputHandler.isMouseButtonPressed(1)) {
				buildingOnCursor = null;
			} else if (InputHandler.isMouseButtonPressed(0)) {
				if (isbuildingOnCursorBuildable) {
					FactionPacketBuildHouse packetBuildHouse = new FactionPacketBuildHouse();
					packetBuildHouse.factionID = ownFaction.id;
					packetBuildHouse.buildingID = buildingOnCursor.getNameTag();
					packetBuildHouse.buildPos = new Position(MathHelper.roundDownToInt(buildingOnCursor.matrix.position.getX(), 1), MathHelper.roundDownToInt(buildingOnCursor.matrix.position.getZ(), 1));

					world.client.sendPacket(packetBuildHouse);

					buildingOnCursor = null;
				}
			}
		} else {
			if (clickedEntities.length > 0) {
				Entity clickedEntity = clickedEntities[0];

				if (clickedEntity instanceof EntityFighting) {
					if (InputHandler.isMouseButtonPressed(1)) {
						selectedUnit = (EntityFighting) clickedEntity;
						System.out.println("entitySelected");
					} else if (InputHandler.isMouseButtonPressed(0)) {
						if (selectedUnit != null) {
							selectedUnit.sendStartAttack((EntityFighting) clickedEntity);
							System.out.println("entityTargeted");
						}
					}
				}
			} else {
				if (InputHandler.isMouseButtonPressed(0)) {
					if (selectedUnit != null) {
						if (InputHandler.get3DmousePosition() != null) {
							selectedUnit.sendStartMoving(InputHandler.get3DmousePosition());
							System.out.println("entitySend");
						}
					}
				}
			}
		}
	}
}
