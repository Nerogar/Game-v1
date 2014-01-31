package de.nerogar.gameV1.level;

import de.nerogar.gameV1.*;
import de.nerogar.gameV1.physics.ObjectMatrix;

public abstract class EntityMobile extends EntityFighting {

	public int team = Player.TEAM_BLUE;

	public GameResources resourceCost = new GameResources(0, 0, 0);

	public EntityMobile(Game game, World world, ObjectMatrix matrix) {
		//super(game, matrix, data, "houses/cone");
		super(game, world, matrix);
	}

	@Override
	public abstract void init(World world);

	@Override
	public abstract void interact();

	@Override
	public abstract String getNameTag();

	@Override
	public abstract void click(int key);

	public void sendToTarget() {

	}
}