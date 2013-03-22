package de.nerogar.gameV1.network;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.LinkedList;

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
				in.read(buffer);

				Packet receivedPacket = Packet.getPacket(packetID).newInstance();
				receivedPacket.packedData = buffer;
				//hardcoded PacketID for connectionData (0)
				if (client.clientType == Client.CLIENT && !client.connectionInfoReceived && packetID == 0) {
					client.setConnectionInfo((PacketConnectionInfo) receivedPacket);
				} else {
					receivedPacket.unpack();
					data.add(receivedPacket);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			client.stopClient();
		}
	}

	public Packet getData() {
		if (data.size() > 0) {
			Packet retPacket = data.get(0);
			data.remove(0);
			return retPacket;
		}
		return null;
	}

	public void stopThread() {
		connectionClosed = true;
	}
}
