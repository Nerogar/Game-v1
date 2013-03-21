package de.nerogar.gameV1.network;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class ReceiveThread extends Thread{
	private Socket socket;
	private boolean connectionClosed = false;
	private LinkedList<DNFile> data = new LinkedList<DNFile>();
	
	public ReceiveThread(Socket socket) {
		setName("ConnectionThread");
		this.socket = socket;
		try {
			System.out.println(socket.getInputStream().getClass().getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public void run() {
		DataInputStream in;

		try {
			in = new DataInputStream(socket.getInputStream());
			byte[] buffer = null;

			while (!connectionClosed) {
				buffer = new byte[in.readInt()];
				System.out.println("TESTTESTTEST");
				in.read(buffer);
				DNFile newFile = new DNFile("");
				newFile.setFromArray(buffer);
				data.add(newFile);
			}

		} catch (Exception e) {
			e.printStackTrace();
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
	
	public void stopThread(){
		connectionClosed = true;
	}
}
