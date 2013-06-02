package de.nerogar.gameV1.network;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import de.nerogar.gameV1.GameOptions;

public class SendThread extends Thread {

	private LinkedList<Packet> data = new LinkedList<Packet>();
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

				while (running || data.size() > 0) {
					if (data.size() == 0) startWaiting();

					synchronized (data) {
						while (data.size() > 0) {
							Packet packet = data.get(0);
							data.remove(0);

							if (!packet.packed) {
								packet.pack();
								packet.packed = true;
							}

							byte[] buffer = packet.packedData;
							out.writeInt(buffer.length);
							out.writeInt(packet.packetID);
							out.write(buffer);
							if (GameOptions.instance.getBoolOption("showNetworkTraffic")) System.out.println("sent packet: " + packet.getName() + " (" + buffer.length + " bytes)");
						}
					}
				}

				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
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

	public void sendPacket(Packet packet) {
		synchronized (data) {
			data.add(packet);
		}
		stopWaiting();
	}

	public void stopThread() {
		running = false;
		stopWaiting();
	}
}
