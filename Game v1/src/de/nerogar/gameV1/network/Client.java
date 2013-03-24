package de.nerogar.gameV1.network;

import java.io.*;
import java.net.*;

import de.nerogar.gameV1.Game;

public class Client {
	private Socket socket;
	public boolean connected = false;
	SendThread sender;
	ReceiveThread receiver;

	public static final int CLIENT = 0;
	public static final int SERVER_CLIENT = 1;
	public int clientType = CLIENT;
	private PacketConnectionInfo connectionInfo;
	public boolean connectionInfoReceived = false;

	public String closeMessage = null;

	public Client(Socket socket) {
		this.socket = socket;
		connected = true;
		init();
	}

	public void setServerClient() {
		clientType = SERVER_CLIENT;
	}

	public Client(String adress, int port) {
		try {
			socket = new Socket(adress, port);
			connected = true;
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
		}
	}

	public void sendPacket(Packet packet) {
		sender.sendPacket(packet);
	}

	public Packet getData() {
		return receiver.getData();
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

	}
}
