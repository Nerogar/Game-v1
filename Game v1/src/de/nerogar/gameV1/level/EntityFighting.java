package de.nerogar.gameV1.level;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.Game;
import de.nerogar.gameV1.World;
import de.nerogar.gameV1.network.PacketSetTarget;
import de.nerogar.gameV1.physics.ObjectMatrix;

public abstract class EntityFighting extends Entity {

	public EntityFighting target;
	public int faction;
	public int health;

	public EntityFighting(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);

	}

	public void sendStartAttack(EntityFighting attackTarget) {
		PacketSetTarget setTargetpacket = new PacketSetTarget();
		setTargetpacket.targetID = attackTarget.id;
		setTargetpacket.entityID = id;
		world.client.sendPacket(setTargetpacket);
	}

	@Override
	public void load(DNNodePath folder) {
		super.load(folder);
	}

	@Override
	public void save(DNNodePath folder) {
		super.save(folder);
	}

	@Override
	public void click(int key) {

	}
}
