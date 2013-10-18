package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.network.PacketEntity;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityShrine extends Entity {

	public EntityShrine(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		boundingBox = new BoundingAABB(new Vector3d(-2, 0, -2), new Vector3d(2, 4, 2));
	}

	@Override
	public void init(World world) {
		setObject("entities/shrine 1", "entities/shrine 1.png");
		//setSprite(1, "houses/test1-1.png");
	}

	@Override
	public void saveProperties(DNFile chunkFile, String folder) {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadProperties(DNFile chunkFile, String folder) {
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
		return "Shrine";
	}

	@Override
	public void updateServer(float time, ArrayList<PacketEntity> packets) {

	}

	@Override
	public void updateClient(float time, ArrayList<PacketEntity> packets) {

	}

}