package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.network.PacketEntity;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityTestparticle extends EntityParticle {

	public float liveTime;

	public EntityTestparticle(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix, 1);
		saveEntity = false;
		liveTime = (float) (Math.random() * 1);
		opacity = liveTime;
	}

	@Override
	public void init(World world) {
		//setObject("houses/cone", "houses/cone.png");
		setSprite(0.3f, "particleTest.png");
	}

	@Override
	public void updateServer(float time, ArrayList<PacketEntity> packets) {
		//this is a client entity
	}

	@Override
	public void updateClient(float time, ArrayList<PacketEntity> packets) {
		//addForce(new Vector3d(0, 15, 0));
		liveTime -= time;
		opacity = liveTime;
		if (liveTime <= 0) remove();
		super.updateClient(time, packets);
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
	public void interact() {
		return;
	}

	@Override
	public void click(int key) {
		return;
	}

	@Override
	public String getNameTag() {
		return "Testpartikel";
	}
}
