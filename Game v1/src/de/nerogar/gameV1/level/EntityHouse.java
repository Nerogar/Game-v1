package de.nerogar.gameV1.level;

import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.Vector3d;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public class EntityHouse extends Entity {

	public EntityHouse(Game game, ObjectMatrix matrix) {
		//super(game, matrix, data, "houses/cone");
		super(game, matrix, "houses/test1");
		//texture = "houses/test1.png";/*Textur name*/
		setObject("houses/test1", "houses/test1.png");
		boundingBox = new BoundingAABB(new Vector3d(-1, 0, -1), new Vector3d(1, 2, 1));
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
	public void saveProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	public void loadProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	public void click(int key) {
		if(key == 1){
			markToRemove = true;
		}

	}
}