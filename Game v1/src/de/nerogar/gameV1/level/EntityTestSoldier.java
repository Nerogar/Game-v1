package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.network.PacketEntity;
import de.nerogar.gameV1.network.PacketSetTarget;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityTestSoldier extends EntityFighting {

	public EntityTestSoldier(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		boundingBox = new BoundingAABB(new Vector3d(-0.2, 0, -0.2), new Vector3d(0.2, 1, 0.2));
	}

	@Override
	public void init(World world) {
		setSprite(1, "Entities/peter.png");
		health = 20;
	}

	@Override
	public void updateServer(float time, ArrayList<PacketEntity> packets) {
		for (PacketEntity packet : packets) {
			if (packet instanceof PacketSetTarget) {
				PacketSetTarget setTargetPacket = (PacketSetTarget) packet;
				target = (EntityFighting) world.getEntityByID(setTargetPacket.targetID);
			}
		}

		//if (target != null) {
		/*Vector3d newPos = Vector3d.add(matrix.position, new Vector3d(Math.random() * 0.2 - 0.1, 0, Math.random() * 0.2 - 0.1));
		newPos.setY(world.land.getHeight(newPos));
		move(newPos);*/
		//}
	}

	@Override
	public void updateClient(float time, ArrayList<PacketEntity> packets) {

	}

	@Override
	public void load(DNFile chunkFile, String folder) {
		// TODO Auto-generated method stub
		super.load(chunkFile, folder);
	}

	@Override
	public void save(DNFile chunkFile, String folder) {
		// TODO Auto-generated method stub
		super.save(chunkFile, folder);
	}

	@Override
	public void click(int key) {
		// TODO Auto-generated method stub

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
	public void interact() {
		// TODO Auto-generated method stub

	}

	@Override
	public String getNameTag() {
		return "testSoldier";
	}
}
