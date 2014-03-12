package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.*;
import de.nerogar.gameV1.network.EntityPacket;
import de.nerogar.gameV1.network.EntityPacketClick;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityHousePink extends EntityBuilding {

	public EntityHousePink(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		size = new Position(2, 2);
		centerPosition = new Position(1, 1);
		resourceCost = new GameResources(200, 100, 100);
		boundingBox = new BoundingAABB(new Vector3d(-size.x / 2, 0, -size.z / 2), new Vector3d(size.x / 2, 1, size.z / 2));
	}

	@Override
	public void init(World world) {
		setObject("entities/housePink/mesh", "entities/housePink/texture.png");
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
	public void click(int key) {
		// TODO Auto-generated method stub
		if (key == 1) remove();
	}

	@Override
	public void updateServer(float time, ArrayList<EntityPacket> packets) {
		for (EntityPacket packet : packets) {
			if (packet instanceof EntityPacketClick) {
				int mouseButton = ((EntityPacketClick) packet).mouseButton;
				if (mouseButton == 1) {
					System.out.println(faction.id);
				}
			}
		}
	}

	@Override
	public void updateClient(float time, ArrayList<EntityPacket> packets) {

	}
	
	@Override
	public String getNameTag() {
		return "housePink";
	}
	
	@Override
	public int getMaxEnergy() {
		return 1;
	}
}
