package de.nerogar.gameV1.DNFileSystem;

import java.util.ArrayList;

public class DNNodePath {
	String name;
	private ArrayList<DNNodePath> paths = new ArrayList<DNNodePath>();
	private ArrayList<DNNode> nodes = new ArrayList<DNNode>();

	public DNNodePath(String name) {
		this.name = name;
	}

	public ArrayList<DNNodePath> getPaths() {
		return paths;
	}

	public ArrayList<DNNode> getNodes() {
		return nodes;
	}

	public DNNodePath getPath(String name) {

		for (int i = 0; i < paths.size(); i++) {
			if (paths.get(i).name.equals(name)) { return paths.get(i); }
		}

		return null;
	}

	public int getNodesSize() {
		return nodes.size();
	}

	public int getFoldersSize() {
		return paths.size();
	}

	public int getSize() {
		return nodes.size() + paths.size();
	}

	public DNNodePath getPath(int index) {
		return paths.get(index);
	}

	public DNNode getNode(String name) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).name.equals(name)) return nodes.get(i);
		}
		return null;
	}

	public DNNode getNodePath(String name) {
		String[] folders = name.split("\\.");
		DNNodePath path = this;

		for (int i = 0; i < folders.length - 1; i++) {
			if (path != null) {
				path = path.getPath(folders[i]);
			}
		}
		if (path != null) {
			return path.getNode(folders[folders.length - 1]);
		} else {
			return null;
		}

	}

	public DNNodePath getNodepath(String name) {
		String[] folders = name.split("\\.");
		DNNodePath path = this;

		for (int i = 0; i < folders.length - 1; i++) {
			path = path.getPath(folders[i]);
		}

		return path.getPath(folders[folders.length - 1]);
	}

	public void addNodePath(String name, byte typ, int length, Object value) {
		String[] nodes = name.split("\\.");

		DNNodePath path = this;
		for (int i = 0; i < nodes.length - 1; i++) {
			if (path.getPath(nodes[i]) == null) {
				path.addFolder(nodes[i]);
			}

			path = path.getPath(nodes[i]);

		}
		path.addNode(nodes[nodes.length - 1], typ, length, value);
	}

	public void addPathPath(String name) {
		String[] nodes = name.split("\\.");

		DNNodePath path = this;
		for (int i = 0; i < nodes.length; i++) {
			if (path.getPath(nodes[i]) == null) {
				path.addFolder(nodes[i]);
			}

			path = path.getPath(nodes[i]);
		}
	}

	public void addFolder(String name) {
		paths.add(new DNNodePath(name));
	}

	public void addNode(String newName, byte typ, int length, Object value) {
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i).name.equals(newName)) {
				nodes.get(i).value = value;
				return;
			}
		}
		nodes.add(new DNNode(newName, typ, length, value));
	}

	public int calcSize() {
		int size = 0;
		for (int i = 0; i < getNodes().size(); i++) {
			DNNode node = getNodes().get(i);
			size += node.calcSize();
		}

		for (int i = 0; i < getPaths().size(); i++) {
			DNNodePath path = getPaths().get(i);
			size += DNHelper.FOLDERSIZE;
			size += path.name.length() * 2;
			size += path.calcSize();
		}

		return size;
	}

}
