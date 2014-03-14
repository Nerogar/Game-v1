package de.nerogar.gameV1.level;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.*;
import de.nerogar.gameV1.ai.AIContainer;
import de.nerogar.gameV1.internalServer.Faction;
import de.nerogar.gameV1.network.PacketSetTarget;
import de.nerogar.gameV1.physics.ObjectMatrix;

public abstract class EntityFighting extends Entity {

	public EntityFighting target;
	public Faction faction;
	public int health;
	public int energy;
	public float moveSpeed = 1;
	public AIContainer aiContainer;

	public EntityFighting(Game game, World world, ObjectMatrix matrix) {
		super(game, world, matrix);
		aiContainer = new AIContainer();
	}

	public void sendStartAttack(EntityFighting attackTarget) {
		PacketSetTarget setTargetpacket = new PacketSetTarget();
		setTargetpacket.targetID = attackTarget.id;
		setTargetpacket.entityID = id;
		world.client.sendPacket(setTargetpacket);
	}

	public void sendStartMoving(Vector3d targetPosition) {
		if (this instanceof EntityMobile) {
			PacketSetTarget setTargetpacket = new PacketSetTarget();
			setTargetpacket.targetPosition = targetPosition;
			setTargetpacket.type = PacketSetTarget.TYPE_FOLLOW;
			setTargetpacket.entityID = id;
			world.client.sendPacket(setTargetpacket);
		}
	}

	@Override
	public void load(DNNodePath folder) {
		super.load(folder);
		if(world.serverWorld){
			faction = Faction.getServerFaction(folder.getInt("fID"));
		}else{
			faction = Faction.getClientFaction(folder.getInt("fID"));
		}
	}

	@Override
	public void save(DNNodePath folder) {
		super.save(folder);
		if (faction == null) {
			System.out.println("Faction null for: " + this);
		}

		folder.addInt("fID", faction.id);

	}

	@Override
	public void click(int key) {

	}

	public abstract int getMaxEnergy();
}
