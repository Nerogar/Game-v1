package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.*;
import de.nerogar.gameV1.graphics.Lightning;
import de.nerogar.gameV1.network.EntityPacket;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityEnergyTower extends EntityBuilding {

	public EntityEnergyTower(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		size = new Position(2, 2);
		centerPosition = new Position(1, 1);
		resourceCost = new GameResources(200, 100, 100);
		boundingBox = new BoundingAABB(new Vector3d(-0.5, 0, -0.5), new Vector3d(0.5, 3, 0.5));
	}

	@Override
	public void init(World world) {
		setObject("entities/energyTower/mesh", "entities/energyTower/texture.png");
		//setSprite(1, "houses/test1-1.png");
	}

	@Override
	public void saveProperties(DNNodePath folder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadProperties(DNNodePath folder) {
		// TODO Auto-generated method stub

	}

	public void interact() {
		//droppe Holz
		//reduziere Holz
	}

	@Override
	public void click(int key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateServer(float time, ArrayList<EntityPacket> packets) {
		for (EntityFighting factionEntity : faction.factionEntities) {
			if (isDistanceSmaller(factionEntity, 20f)) {
				factionEntity.energy = factionEntity.getMaxEnergy();

			}
		}
	}

	@Override
	public void updateClient(float time, ArrayList<EntityPacket> packets) {
		for(EntityFighting ef:faction.factionEntities){
			for (int i = 0; i < 1; i++) {
				world.lightningEffectContainer.addLightning(new Lightning(Vector3d.add(matrix.getPosition(), new Vector3d(0, 2.8, 0)), Vector3d.add(ef.matrix.getPosition(), new Vector3d(0, 2.8, 0)), .05f));
			}
		}
		
		/*if (Math.random() < 1) {	
			for (int i = 0; i < 3; i++) {
				Vector3d dest = new Vector3d(Math.random() * 64, Math.random() * 100, Math.random() * 64);
				world.lightningEffectContainer.addLightning(new Lightning(Vector3d.add(matrix.getPosition(), new Vector3d(0, 2.8, 0)), dest, .05f));
			}
		}*/
	}

	@Override
	public String getNameTag() {
		return "energyTower";
	}

	@Override
	public int getMaxEnergy() {
		return -1;
	}
}