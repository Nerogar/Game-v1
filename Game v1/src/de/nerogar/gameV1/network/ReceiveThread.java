package de.nerogar.gameV1.network;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import de.nerogar.gameV1.GameOptions;

public class ReceiveThread extends Thread {
	private Socket socket;
	private Client client;
	private boolean connectionClosed = false;
	private LinkedList<Packet> data = new LinkedList<Packet>();

	public ReceiveThread(Socket socket, Client client) {
		setName("ReceiveThread");
		this.socket = socket;
		this.client = client;
	}

	@Override
	public void run() {
		DataInputStream in;

		try {
			in = new DataInputStream(socket.getInputStream());
			byte[] buffer = null;

			while (!connectionClosed) {
				buffer = new byte[in.readInt()];
				int packetID = in.readInt();

				int receivedBytes = 0;

				while (receivedBytes < buffer.length) {
					receivedBytes += in.read(buffer, receivedBytes, buffer.length - receivedBytes);
				}
				Packet receivedPacket = Packet.getPacket(packetID).newInstance();
				if (GameOptions.instance.getBoolOption("showNetworkTraffic")) System.out.println("received packet: " + receivedPacket.packetID + " (" + receivedBytes + "/" + buffer.length + " bytes)");
				receivedPacket.packedData = buffer;
				receivedPacket.unpack();

				//hardcoded PacketID for connectionData (0)
				if (!client.connectionInfoReceived && packetID == 0) {
					client.setConnectionInfo((PacketConnectionInfo) receivedPacket);
				} else {
					synchronized (data) {
						data.add(receivedPacket);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			client.stopClient();
		}
	}

	public ArrayList<Packet> getData(int channel) {
		synchronized (data) {
			if (data.size() > 0) {
				ArrayList<Packet> retList = new ArrayList<Packet>();
				for (int i = 0; i < data.size(); i++) {
					if (data.get(i).channel == channel) {
						retList.add(data.get(i));
						data.remove(i);
						i--;
					}
				}
				return retList;
			}
		}

		return null;
	}

	public void stopThread() {
		connectionClosed = true;
	}
}
