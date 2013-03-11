package de.nerogar.gameV1.network;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class Client extends Thread {
	private LinkedList<DNFile> data = new LinkedList<DNFile>();
	private Socket socket;
	private int port;
	private boolean running = true;
	private Object syncObject;

	public Client(Object syncObject) {
		port = 4200;
		try {
			socket = new Socket("localhost", port);
		} catch (Exception e) {
			e.printStackTrace();
		}
		running = true;
		this.syncObject = syncObject;
	}

	@Override
	public void run() {
		if (socket != null) {

			try {
				while (running) {
					startWaiting();

					DataOutputStream out;
					out = new DataOutputStream(socket.getOutputStream());

					while (data.size() > 0) {
						byte[] buffer = data.get(0).getAsArray();
						data.remove(0);
						out.writeInt(buffer.length);
						out.write(buffer);

					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void startWaiting() {
		try {
			synchronized (syncObject) {
				syncObject.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void stopWaiting() {
		synchronized (syncObject) {
			syncObject.notify();
		}
	}

	public void startSending() {
		stopWaiting();
	}

	public void addPackage(DNFile file) {
		data.add(file);
	}

	public void stopClient() {
		running = false;
		stopWaiting();

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
