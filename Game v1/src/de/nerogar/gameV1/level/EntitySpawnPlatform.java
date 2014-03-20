package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.*;
import de.nerogar.gameV1.network.EntityPacket;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntitySpawnPlatform extends EntityBuilding {

	public EntitySpawnPlatform(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		size = new Position(10, 10);
		centerPosition = new Position(5, 5);
		boundingBox = new BoundingAABB(new Vector3d(-size.x / 2, 0, -size.z / 2), new Vector3d(size.x / 2, 1, size.z / 2));
	}

	@Override
	public void init(World world) {
		setObject("entities/spawnPlatform/mesh", "entities/spawnPlatform/texture.png");
	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub
	}

	@Override
	public void saveProperties(DNNodePath folder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void loadProperties(DNNodePath folder) {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateServer(float time, ArrayList<EntityPacket> packets) {
		super.updateServer(time, packets);
	}

	@Override
	public void updateClient(float time, ArrayList<EntityPacket> packets) {

	}

	@Override
	public String getNameTag() {
		return "spawnPlatform";
	}

	@Override
	public int getMaxEnergy() {
		return -1;
	}
}
