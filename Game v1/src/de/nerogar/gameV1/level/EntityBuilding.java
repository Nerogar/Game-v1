package de.nerogar.gameV1.level;

import de.nerogar.gameV1.*;
import de.nerogar.gameV1.physics.BoundingAABB;
import de.nerogar.gameV1.physics.ObjectMatrix;

public abstract class EntityBuilding extends Entity {

	public Position size;
	public Position centerPosition;
	public int team = Player.TEAM_BLUE;

	public GameResources resourceCost = new GameResources(0, 0, 0);

	public EntityBuilding(Game game, World world, ObjectMatrix matrix) {
		//super(game, matrix, data, "houses/cone");
		super(game, world, matrix);
		boundingBox = new BoundingAABB(new Vector3d(0, 0, 0), new Vector3d(0, 0, 0));
	}

	@Override
	public abstract void init(World world);

	//setObject("houses/test1", "houses/test1-1.png");

	//public void update(float time) {
	//	matrix.getPosition().y = game.world.land.getHeight(matrix.getPosition().x, matrix.getPosition().z);
	//}

	public abstract void interact();

	@Override
	public abstract String getNameTag();

	@Override
	public abstract void click(int key);
	//if(key == 1)	markToRemove = true;

}