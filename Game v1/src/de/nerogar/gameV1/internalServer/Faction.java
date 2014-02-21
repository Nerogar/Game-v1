package de.nerogar.gameV1.internalServer;

import java.util.ArrayList;

import de.nerogar.gameV1.network.Client;

public class Faction {

	public static Faction factionNone;

	public static Faction factionBlue;
	public static Faction factionRed;
	public static Faction factionGreen;
	public static Faction factionYellow;

	private static ArrayList<Faction> factions;

	public int id;
	public Client client;

	public Faction(int id) {
		this.id = id;
	}

	public static Faction getFaction(int factionID) {
		if (factionID == -1) return factionNone;
		return factions.get(factionID);
	}

	public static int getMaxFactionCount() {
		return factions.size();
	}

	static {
		factionBlue = new Faction(-1);

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
