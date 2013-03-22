package de.nerogar.gameV1.network;

import java.io.*;
import java.net.*;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class Client {
	private Socket socket;
	public boolean connected = false;
	SendThread sender;
	ReceiveThread receiver;

	private static final int CLIENT = 0;
	private static final int SERVER_CLIENT = 1;
	private int clientType = CLIENT;
	private PacketConnectionInfo connectionInfo;
	public boolean connectionInfoReceived = false;

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
	}

	public void sendPacket(Packet packet) {
		sender.sendPacket(packet);
	}

	public Packet getData() {
		return receiver.getData();
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
