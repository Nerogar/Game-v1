package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.network.PacketEntity;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityHouse extends Entity {

	public EntityHouse(Game game, World world, ObjectMatrix matrix) {
		//super(game, matrix, data, "houses/cone");
		super(game, world, matrix);

		boundingBox = new BoundingAABB(new Vector3d(-1, 0, -1), new Vector3d(1, 2, 1));
	}

	@Override
	public void init(World world) {
		setObject("houses/test1", "houses/test1-1.png");
		//setSprite(1, "houses/test1-1.png");
	}

	//public void update(float time) {
	//	matrix.getPosition().y = game.world.land.getHeight(matrix.getPosition().x, matrix.getPosition().z);
	//}

	public void interact() {
		//bilde aus
	}

	@Override
	public String getNameTag() {
		return "House";
	}

	@Override
	public void saveProperties(DNFile chunkFile, String folder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadProperties(DNFile chunkFile, String folder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void click(int key) {
		if (key == 1) remove();
	}

	@Override
	public void update(float time, ArrayList<PacketEntity> packets) {
		// TODO Auto-generated method stub
		
	}
}