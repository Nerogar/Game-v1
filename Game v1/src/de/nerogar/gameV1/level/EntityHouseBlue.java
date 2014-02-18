package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.*;
import de.nerogar.gameV1.network.PacketClickEntity;
import de.nerogar.gameV1.network.PacketEntity;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityHouseBlue extends EntityBuilding {

	public EntityHouseBlue(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		size = new Position(2, 2);
		centerPosition = new Position(1, 1);
		resourceCost = new GameResources(200, 100, 100);
		boundingBox = new BoundingAABB(new Vector3d(-size.x / 2, 0, -size.z / 2), new Vector3d(size.x / 2, 1, size.z / 2));
	}

	@Override
	public void init(World world) {
		setObject("entities/houseBlue/mesh", "entities/houseBlue/texture.png");
	}

	@Override
	public void updateServer(float time, ArrayList<PacketEntity> packets) {
		for (PacketEntity packet : packets) {
			if (packet instanceof PacketClickEntity) {
				int mouseButton = ((PacketClickEntity) packet).mouseButton;
				if (mouseButton == 1) {
					remove();
				}
			}
		}
	}

	@Override
	public void updateClient(float time, ArrayList<PacketEntity> packets) {
		ObjectMatrix particleMatrix = new ObjectMatrix(new Vector3d(matrix.position.getX() + Math.random() * 2 - 1, matrix.position.getY() + 1, matrix.position.getZ() + Math.random() * 2 - 1));
		world.spawnEntity(new EntityTestparticle(game, world, particleMatrix));
	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub
	}

	@Override
	public String getNameTag() {
		return "HouseBlue";
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
	public void click(int key) {
		// TODO Auto-generated method stub
		if (key == 1) remove();
	}
}
