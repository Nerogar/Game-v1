package de.nerogar.gameV1.network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import de.nerogar.gameV1.Game;

public class Client {
	private Socket socket;
	public boolean connected = false;
	SendThread sender;
	ReceiveThread receiver;

	public static final int CLIENT = 0;
	public static final int SERVER_CLIENT = 1;
	public int clientType = CLIENT;
	private Server server;
	public PacketConnectionInfo connectionInfo;
	public boolean connectionInfoReceived = false;
	public boolean connectionInfoSent = false;

	public String closeMessage = "";

	public Client(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
		connected = true;
		clientType = SERVER_CLIENT;
		init();
	}

	public Client(String adress, int port) {
		try {
			socket = new Socket(adress, port);
			connected = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		init();
	}

	public void init() {
		sender = new SendThread(socket);
		receiver = new ReceiveThread(socket, this);

		sender.start();
		receiver.start();
	}

	public void setConnectionInfo(PacketConnectionInfo connectionInfo) {
		this.connectionInfo = connectionInfo;
		connectionInfoReceived = true;

		if (!connectionInfo.version.equals(Game.version)) {
			stopClient();
			closeMessage = "incorrect version number";
		} else if (!connectionInfoSent) {
			connectionInfoSent = true;
			sendPacket(new PacketConnectionInfo());
		}
	}

	public void sendPacket(Packet packet) {
		sender.sendPacket(packet);
	}

	public ArrayList<Packet> getData(int channel) {
		return receiver.getData(channel);
	}

	public String getAdress() {
		if (socket != null) { return socket.getInetAddress().getHostAddress() + ":" + socket.getPort(); }
		return "";
	}

	public void stopClient() {
		connected = false;

		receiver.stopThread();

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		sender.stopThread();
		if (clientType == SERVER_CLIENT) {
			server.removeClient(this);
		}
	}
}
