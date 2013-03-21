package de.nerogar.gameV1.network;

import java.io.*;
import java.net.*;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class Client {
	private Socket socket;
	public boolean connected = false;
	SendThread sender;
	ReceiveThread receiver;

	public Client(Socket socket) {
		this.socket = socket;
		connected = true;
		init();
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
		receiver = new ReceiveThread(socket);

		sender.start();
		receiver.start();
	}

	public void sendPackage(DNFile file) {
		sender.sendPackage(file);
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

	public DNFile getData() {
		return receiver.getData();
	}
}
