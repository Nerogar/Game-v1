package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityShrine extends Entity {

	public EntityShrine(Game game, ObjectMatrix matrix) {
		super(game, matrix, "entities/shrine 1");
		texture = "entities/shrine 1.png";/*Texture name*/
		boundingBox = new BoundingAABB(new Vector3d(-2, 0, -2), new Vector3d(2, 4, 2));
	}

	@Override
	public void saveProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadProperties() {
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

}