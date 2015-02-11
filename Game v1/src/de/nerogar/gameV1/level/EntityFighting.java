package de.nerogar.gameV1.level;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.*;
import de.nerogar.gameV1.ai.AIContainer;
import de.nerogar.gameV1.internalServer.Faction;
import de.nerogar.gameV1.network.*;
import de.nerogar.gameV1.physics.ObjectMatrix;

public abstract class EntityFighting extends Entity {

	public EntityFighting target;
	public Faction faction;
	public int health;
	public int energy;
	public float energyPulseCooldown;
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

	public void sendRemoveBuilding() {
		if (this instanceof EntityBuilding) {
			EntityPacketRemoveHouse removeHousePacket = new EntityPacketRemoveHouse();
			removeHousePacket.entityID = id;
			removeHousePacket.factionID = faction.id;
			world.client.sendPacket(removeHousePacket);
		}
	}

	@Override
	public void updateServer(float time, ArrayList<EntityPacket> packets) {
		energyPulseCooldown -= time;
		energy--;
	}

	@Override
	public void load(DNNodePath folder) {
		super.load(folder);
		if (world.serverWorld) {
			faction = Faction.getServerFaction(folder.getInt("fID"));
		} else {
			faction = Faction.getClientFaction(folder.getInt("fID"));
		}

		energy = folder.getInt("en");
		energyPulseCooldown = folder.getFloat("enC");
	}

	@Override
	public void save(DNNodePath folder) {
		super.save(folder);
		if (faction == null) {
			System.out.println("Faction null for: " + this);
		}

		folder.addInt("fID", faction.id);
		folder.addInt("en", energy);
		folder.addFloat("enC", energyPulseCooldown);

	}

	public boolean fillEnergy() {
		if (energyPulseCooldown <= 0 && energy != getMaxEnergy() && getMaxEnergy() >= 0) {
			energy = getMaxEnergy();
			energyPulseCooldown = 5f;
			return true;
		}

		return false;
	}

	public abstract int getMaxEnergy();
}
