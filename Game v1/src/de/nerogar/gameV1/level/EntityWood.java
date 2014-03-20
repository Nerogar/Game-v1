package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.*;
import de.nerogar.gameV1.network.EntityPacket;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityWood extends Entity {

	public EntityWood(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		boundingBox = new BoundingAABB(new Vector3d(-0.2, 0, -0.2), new Vector3d(0.2, 0.5, 0.2));
	}

	@Override
	public void init(World world) {
		setSprite(1, "entities/wood/texture.png");
	}

	@Override
	public void updateServer(float time, ArrayList<EntityPacket> packets) {

	}

	@Override
	public void updateClient(float time, ArrayList<EntityPacket> packets) {

	}

	@Override
	public void load(DNNodePath folder) {
		// TODO Auto-generated method stub
		super.load(folder);
	}

	@Override
	public void save(DNNodePath folder) {
		// TODO Auto-generated method stub
		super.save(folder);
	}

	@Override
	public void saveProperties(DNNodePath folder) {

	}

	@Override
	public void loadProperties(DNNodePath folder) {

	}

	@Override
	public void interact() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getNameTag() {
		return "wood";
	}
}
