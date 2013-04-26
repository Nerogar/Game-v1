package de.nerogar.gameV1.network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

import de.nerogar.gameV1.GameOptions;

public class Server extends Thread {

	private ArrayList<Client> clients = new ArrayList<Client>();
	private ServerSocket serverSocket;
	public int port;
	private boolean running = true;

	public Server() {
		setName("ServerThread");
		port = GameOptions.instance.STANDARDPORT;
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
				Client newClient = new Client(newClientSocket, this);
				clients.add(newClient);

				PacketConnectionInfo packetConnectionInfo = new PacketConnectionInfo();
				newClient.connectionInfoSent = true;
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

	public void broadcastData(Packet packet) {
		for (int i = 0; i < clients.size(); i++) {
			clients.get(i).sendPacket(packet);
		}
	}

	public void stopServer() {
		synchronized (clients) {
			while (clients.size() > 0) {
				clients.get(0).stopClient();
			}
		}
		running = false;

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeClient(Client client) {
		synchronized (clients) {
			clients.remove(client);
		}
	}
}
