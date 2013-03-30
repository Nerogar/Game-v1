package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.DNFileSystem.DNFile;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityTestparticle extends EntityParticle {

	public float liveTime;

	public EntityTestparticle(Game game, ObjectMatrix matrix) {
		super(game, matrix, 1);
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
	public void update(float time) {
		//addForce(new Vector3d(0, 15, 0));
		liveTime -= time;
		opacity = liveTime;
		if (liveTime <= 0) markToRemove = true;
		super.update(time);
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
