package de.nerogar.gameV1.network;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class Client extends Thread {
	private LinkedList<DNFile> data = new LinkedList<DNFile>();
	private Socket socket;
	private String adress;
	private int port;
	private boolean running = true;
	private Object syncObject;
	public boolean connected = false;

	public Client(Object syncObject, String adress) {
		this.adress = adress;
		port = 4200;
		try {
			socket = new Socket(adress, port);
			connected = true;
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
		connected = false;
		running = false;
		stopWaiting();

		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e){
			e.printStackTrace();
		}
	}
}
