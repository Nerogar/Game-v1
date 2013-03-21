package de.nerogar.gameV1.network;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.LinkedList;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class SendThread extends Thread {

	private LinkedList<DNFile> data = new LinkedList<DNFile>();
	private Socket socket;
	private boolean running = true;
	private Object syncObject;

	public SendThread(Socket socket) {
		setName("NetworkSendThread");
		this.socket = socket;
		running = true;
		this.syncObject = new Object();
	}

	@Override
	public void run() {
		if (socket != null) {

			try {

				DataOutputStream out;
				out = new DataOutputStream(socket.getOutputStream());

				while (running) {
					startWaiting();

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

	public void sendPackage(DNFile file) {
		data.add(file);
		stopWaiting();
	}

	public void stopThread() {
		running = false;
		stopWaiting();
	}
}
