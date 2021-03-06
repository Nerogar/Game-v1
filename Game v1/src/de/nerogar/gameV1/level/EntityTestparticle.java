package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.network.EntityPacket;
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
		setSprite(0.3f, "entities/testParticle/texture.png");
	}

	@Override
	public void updateServer(float time, ArrayList<EntityPacket> packets) {
		super.updateServer(time, packets);
		//this is a client entity
	}

	@Override
	public void updateClient(float time, ArrayList<EntityPacket> packets) {
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
	public String getNameTag() {
		return "testPartikel";
	}
}
