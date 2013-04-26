package de.nerogar.gameV1;

import java.util.ArrayList;

import de.nerogar.gameV1.DNFileSystem.DNFile;

public class ServerList {
	private DNFile file;

	public static ServerList instance = new ServerList();
	public String filename = "serverlist.lst";
	public ArrayList<String> adresses;
	public ArrayList<Integer> ports;

	public ServerList() {
		adresses = new ArrayList<String>();
		ports = new ArrayList<Integer>();
	}

	public void load() {
		file = new DNFile(filename);
		if (file.load()) {
			adresses = new ArrayList<String>();
			String[] adressesA = file.getStringArray("adresses");
			for (String s : adressesA) {
				adresses.add(s);
			}

			ports = new ArrayList<Integer>();
			int[] portsA = file.getIntArray("ports");
			for (int i : portsA) {
				ports.add(i);
			}
		}
	}

	public void save() {
		file = new DNFile(filename);

		String[] adressesA = new String[adresses.size()];
		for (int i = 0; i < adresses.size(); i++) {
			adressesA[i] = adresses.get(i);
		}

		int[] portsA = new int[ports.size()];
		for (int i = 0; i < ports.size(); i++) {
			portsA[i] = ports.get(i);
		}

		file.addNode("adresses", adressesA);
		file.addNode("ports", portsA);
		file.save();
	}

	public String[] getAsStringArray() {
		if (adresses != null && ports != null) {
			String[] retString = new String[adresses.size()];
			for (int i = 0; i < retString.length; i++) {
				retString[i] = adresses.get(i) + ":" + ports.get(i);
			}
			return retString;
		}
		return new String[] { "" };
	}

	public void addServer(String adress, int port) {
		adresses.add(adress);
		ports.add(port);
		save();
	}

	public void removeServer(String adress, int port) {
		for (int i = 0; i < adresses.size(); i++) {
			if (adresses.get(i).equals(adress) && ports.get(i).equals(port)) {
				adresses.remove(i);
				ports.remove(i);
				break;
			}
		}
		save();
	}

	public void removeServer(int index) {
		adresses.remove(index);
		ports.remove(index);
		save();
	}

	public String getAdress(int index) {
		return adresses.get(index);
	}

	public int getPort(int index) {
		return ports.get(index);
	}
}
