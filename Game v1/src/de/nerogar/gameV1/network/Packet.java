package de.nerogar.gameV1.network;

import java.util.ArrayList;
import java.util.HashMap;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public abstract class Packet {

	public DNFile data;
	public byte[] packedData;
	public int packetID;
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

	public void setData(DNFile data) {
		this.data = data;
	}

	public DNFile getData() {
		return data;
	}

	private static void registerPacket(Class<? extends Packet> packet) {
		packetIdMap.put(packet, biggestID);

		packets.add(packet);
		biggestID++;
	}

	static {
		registerPacket(PacketConnectionInfo.class);//has to be at position 0

		registerPacket(PacketTestString.class);

	}
}
