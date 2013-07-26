package de.nerogar.gameV1.network;

import java.util.ArrayList;
import java.util.HashMap;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public abstract class Packet {

	protected DNFile data;
	public byte[] packedData;
	public int packetID;
	public int channel = DEFAULT_CHANNEL;
	public boolean packed = false;

	public static final int DEFAULT_CHANNEL = 0;
	public static final int LOBBY_CHANNEL = 1;
	public static final int WORLD_CHANNEL = 2;
	public static final int PLAYER_CHANNEL = 3;
	public static final int ENTITY_CHANNEL = 3;
	

	private static int biggestID = 0; //0 is reserved for PacketConnectionInfo

	protected static ArrayList<Class<? extends Packet>> packets = new ArrayList<Class<? extends Packet>>();
	protected static HashMap<Class<? extends Packet>, Integer> packetIdMap = new HashMap<Class<? extends Packet>, Integer>();

	public Packet() {
		setID();
	}

	public void setID() {
		packetID = packetIdMap.get(getClass());
	}

	public static Class<? extends Packet> getPacket(int id) {
		return packets.get(id);
	}

	public abstract void pack();

	public abstract void unpack();

	public DNFile getData() {
		return data;
	}

	public abstract String getName();

	private static void registerPacket(Class<? extends Packet> packet) {
		packetIdMap.put(packet, biggestID);

		packets.add(packet);
		biggestID++;
	}

	static {
		registerPacket(PacketConnectionInfo.class);//has to be at position 0

		registerPacket(PacketTestString.class);
		registerPacket(PacketMultiplayerLobbyInfo.class);
		registerPacket(PacketMultiplayerLobbyClient.class);
		registerPacket(PacketExitMultiplayerLobby.class);
		registerPacket(PacketChunkData.class);
		registerPacket(PacketExitGame.class);
		registerPacket(PacketBuildHouse.class);
		registerPacket(PacketSpawnEntity.class);
		registerPacket(PacketClickEntity.class);
		registerPacket(PacketRemoveEntity.class);
	}
}
