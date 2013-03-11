package de.nerogar.gameV1.network;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class ConnectionThread extends Thread {

	private Socket socket;
	private Server server;
	private boolean connectionClosed = false;
	private LinkedList<DNFile> data = new LinkedList<DNFile>();

	public ConnectionThread(Socket socket, Server server) {
		this.socket = socket;
		this.server = server;
	}

	@Override
	public void run() {
		DataInputStream in;
		DataOutputStream out;

		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			byte[] buffer = null;

			while (!connectionClosed) {
				buffer = new byte[in.readInt()];
				in.read(buffer);
				DNFile newFile = new DNFile("");
				newFile.setFromArray(buffer);
				data.add(newFile);
			}

		} catch (Exception e) {
			e.printStackTrace();
			server.removeClient(this);
		}
	}

	public DNFile getData() {
		if (data.size() > 0) {
			DNFile retFile = data.get(0);
			data.remove(0);
			return retFile;
		}
		return null;
	}
}
