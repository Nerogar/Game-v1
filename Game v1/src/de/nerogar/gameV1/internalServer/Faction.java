package de.nerogar.gameV1.internalServer;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.network.Client;

public class Faction {

	//factions
	public static Faction factionNoneServer; // factionless
	public static Faction factionBlueServer;
	public static Faction factionRedServer;
	public static Faction factionGreenServer;
	public static Faction factionYellowServer;
	private static ArrayList<Faction> factionsServer;

	public static Faction factionNoneClient; // factionless
	public static Faction factionBlueClient;
	public static Faction factionRedClient;
	public static Faction factionGreenClient;
	public static Faction factionYellowClient;
	private static ArrayList<Faction> factionsClient;
	//end factions

	private static final int TOWER_BUILDING_RADIUS = 10;

	public int id;
	public Client client;
	public boolean serverFaction;

	public int maxUnits;
	public ArrayList<EntityFighting> factionEntities;

	public Faction(int id) {
		this.id = id;
		maxUnits = 5;
		factionEntities = new ArrayList<EntityFighting>();
	}

	public void recalcFactionEntities(EntityList entityList) {
		factionEntities.clear();

		for (Entity e : entityList.entities.values()) {
			if (e instanceof EntityFighting) {
				EntityFighting entityFighting = (EntityFighting) e;
				if (entityFighting.faction.id == id) {
					factionEntities.add(entityFighting);
				}
			}
		}

		for (Entity e : entityList.newEntities) {
			if (e instanceof EntityFighting) {
				EntityFighting entityFighting = (EntityFighting) e;
				if (entityFighting.faction.id == id) {
					factionEntities.add(entityFighting);
				}
			}
		}
	}

	public int getUnitCount() {
		int count = 0;

		for (EntityFighting ef : factionEntities) {
			if (ef instanceof EntityMobile) {
				count++;
			}
		}

		return count;
	}

	public int getMaxUnitCount() {
		return maxUnits;
	}

	public boolean isEntityInVillageRange(EntityFighting target) {
		if (target instanceof EntityEnergyTower) return true;

		for (EntityFighting ef : factionEntities) {
			if (ef.isDistanceSmaller(target, TOWER_BUILDING_RADIUS)) { return true; }
		}
		return false;
	}

	public void save(DNNodePath folder) {

	}

	public void load(DNNodePath folder) {

	}

	public static Faction getServerFaction(int factionID) {
		if (factionID == -1) return factionNoneServer;
		return factionsServer.get(factionID);
	}

	public static Faction getClientFaction(int factionID) {
		if (factionID == -1) return factionNoneClient;
		return factionsClient.get(factionID);
	}

	public static Faction[] getClientFactions(int[] factionIDs) {
		Faction[] factions = new Faction[factionIDs.length];

		for (int i = 0; i < factionIDs.length; i++) {
			factions[i] = getClientFaction(factionIDs[i]);
		}

		return factions;
	}

	public static Faction[] getServerFactions(int[] factionIDs) {
		Faction[] factions = new Faction[factionIDs.length];

		for (int i = 0; i < factionIDs.length; i++) {
			factions[i] = getServerFaction(factionIDs[i]);
		}

		return factions;
	}

	public static int getMaxFactionCount() {
		return factionsServer.size();
	}

	static {
		//server
		factionNoneServer = new Faction(-1);
		factionBlueServer = new Faction(0);
		factionRedServer = new Faction(1);
		factionGreenServer = new Faction(2);
		factionYellowServer = new Faction(3);

		factionsServer = new ArrayList<Faction>();
		factionsServer.add(factionBlueServer);
		factionsServer.add(factionRedServer);
		factionsServer.add(factionGreenServer);
		factionsServer.add(factionYellowServer);

		factionNoneServer.serverFaction = true;
		factionBlueServer.serverFaction = true;
		factionRedServer.serverFaction = true;
		factionGreenServer.serverFaction = true;
		factionYellowServer.serverFaction = true;

		//client
		factionNoneClient = new Faction(-1);
		factionBlueClient = new Faction(0);
		factionRedClient = new Faction(1);
		factionGreenClient = new Faction(2);
		factionYellowClient = new Faction(3);

		factionsClient = new ArrayList<Faction>();
		factionsClient.add(factionBlueClient);
		factionsClient.add(factionRedClient);
		factionsClient.add(factionGreenClient);
		factionsClient.add(factionYellowClient);
	}
}
