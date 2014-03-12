package de.nerogar.gameV1.internalServer;

import java.util.ArrayList;

import de.nerogar.DNFileSystem.DNNodePath;
import de.nerogar.gameV1.level.*;
import de.nerogar.gameV1.network.Client;

public class Faction {

	//factions
	public static Faction factionNone; // factionless
	public static Faction factionBlue;
	public static Faction factionRed;
	public static Faction factionGreen;
	public static Faction factionYellow;
	private static ArrayList<Faction> factions;
	//end factions

	private static final int TOWER_BUILDING_RADIUS = 10;

	public int id;
	public Client client;

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
				if (entityFighting.faction == this) {
					factionEntities.add(entityFighting);
				}
			}
		}

		for (Entity e : entityList.newEntities) {
			if (e instanceof EntityFighting) {
				EntityFighting entityFighting = (EntityFighting) e;
				if (entityFighting.faction == this) {
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

	public boolean isEntityInTowerRange(EntityFighting target) {

		for (EntityFighting ef : factionEntities) {
			if (ef.isDistanceSmaller(target, TOWER_BUILDING_RADIUS)) { return true; }
		}
		return false;
	}

	public void save(DNNodePath folder) {

	}

	public void load(DNNodePath folder) {

	}

	public static Faction getFaction(int factionID) {
		if (factionID == -1) return factionNone;
		return factions.get(factionID);
	}

	public static int getMaxFactionCount() {
		return factions.size();
	}

	static {
		factionNone = new Faction(-1);

		factionBlue = new Faction(0);
		factionRed = new Faction(1);
		factionGreen = new Faction(2);
		factionYellow = new Faction(3);

		factions = new ArrayList<Faction>();
		factions.add(factionBlue);
		factions.add(factionRed);
		factions.add(factionGreen);
		factions.add(factionYellow);
	}
}
