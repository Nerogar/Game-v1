package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.*;
import de.nerogar.gameV1.network.EntityPacket;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityEnergyTower extends Entity {

	public EntityEnergyTower(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		boundingBox = new BoundingAABB(new Vector3d(-2, 0, -2), new Vector3d(2, 4, 2));
	}

	@Override
	public void init(World world) {
		setObject("entities/shrine/mesh", "entities/shrine/texture.png");
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
	public String getNameTag() {
		return "energyTower";
	}

	@Override
	public void updateServer(float time, ArrayList<EntityPacket> packets) {

	}

	@Override
	public void updateClient(float time, ArrayList<EntityPacket> packets) {

	}

}