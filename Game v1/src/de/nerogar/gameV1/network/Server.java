package de.nerogar.gameV1.network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import de.nerogar.gameV1.Game;

public class Server extends Thread {

	private ArrayList<Client> clients = new ArrayList<Client>();
	private ServerSocket serverSocket;
	private int port;
	private boolean running = true;

	public Server() {
		setName("ServerThread");
		port = 4200;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Faild to start Server Listener!");
		} finally {
			this.start();
		}
	}

	@Override
	public void run() {
		try {
			while (running) {
				Socket newClientSocket = serverSocket.accept();
				Client newClient = new Client(newClientSocket);
				clients.add(newClient);

				PacketConnectionInfo packetConnectionInfo = new PacketConnectionInfo();
				packetConnectionInfo.version = Game.version;

				newClient.sendPacket(packetConnectionInfo);
			}
		} catch (SocketException e) {
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Client> getClients() {
		return clients;
	}

	public void stopServer() {
		for (Client client : clients) {
			client.stopClient();
		}
		running = false;

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeClient(Client client) {
		client.stopClient();
		clients.remove(client);
	}
}
