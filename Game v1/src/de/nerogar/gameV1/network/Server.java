package de.nerogar.gameV1.network;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class Server extends Thread {

	private ArrayList<ConnectionThread> clients = new ArrayList<ConnectionThread>();
	private ServerSocket serverSocket;
	private int port;
	private boolean running = true;

	public Server() {
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

				Socket newClient = serverSocket.accept();
				ConnectionThread newThread = new ConnectionThread(newClient, this);
				clients.add(newThread);
				newThread.start();

			}
		} catch (SocketException e) {
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ConnectionThread> getClients() {
		return clients;
	}

	public void stopServer() {
		for (ConnectionThread thread : clients) {
			thread.interrupt();
		}
		running = false;

		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeClient(ConnectionThread connectionThread) {
		clients.remove(connectionThread);
	}
}
