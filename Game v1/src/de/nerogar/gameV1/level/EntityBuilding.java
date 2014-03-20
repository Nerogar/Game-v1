package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.gameV1.*;
import de.nerogar.gameV1.network.*;
import de.nerogar.gameV1.physics.ObjectMatrix;

public abstract class EntityBuilding extends EntityFighting {

	public Position size;
	public Position centerPosition;

	public GameResources resourceCost;

	public EntityBuilding(Game game, World world, ObjectMatrix matrix) {
		//super(game, matrix, data, "houses/cone");
		super(game, world, matrix);
	}

	@Override
	public void updateServer(float time, ArrayList<EntityPacket> packets) {
		super.updateServer(time, packets);
		for (EntityPacket packet : packets) {
			if (packet instanceof EntityPacketRemoveHouse) {
				remove();
			}
		}
	}
}