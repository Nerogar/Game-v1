package de.nerogar.gameV1;

import java.util.ArrayList;

public class Timer {
	public float delta;
	private double lastFrame;
	private int fps;
	private double lastFps;
	public int mfFps;
	private ArrayList<Event> events = new ArrayList<Event>();
	private long framecount;
	public boolean printFPS = false;

	//private ArrayList<String> eventNames = new ArrayList<String>();
	//public static Timer instance = new Timer();

	public Timer() {

	}

	public void init() {
		lastFrame = getTime();
		lastFps = getTime();
	}

	private void printFPS() {
		if (getTime() - lastFps > 1) {
			if (GameOptions.instance.getBoolOption("debug") && printFPS) System.out.println("FPS: " + fps);
			mfFps = fps;
			fps = 0;
			lastFps += 1;
		}
	}

	public void update() {
		fps++;
		framecount++;
		double time = getTime();
		delta = (float) (time - lastFrame);
		/*if (delta > 0.1f) {
			System.out.println("capped time delta");
			delta = 0.1f;
		}*/
		lastFrame = time;
		printFPS();
	}

	private double getTime() {
		//return (Sys.getTime() * 1000) / Sys.getTimerResolution();
		return System.nanoTime() / 1000000000D;
	}

	//EVENTS
	public void registerEvent(String name, long seconds) {
		if (indexOfEvent(name) != -1) return;
		events.add(new Event(name, seconds * 1000000000));
	}

	private int indexOfEvent(String name) {
		for (int i = 0; i < events.size(); i++) {
			if (name.equals(events.get(i).name)) return i;
		}
		return -1;
	}

	public boolean shellExecute(String name) {
		int index = indexOfEvent(name);
		if (index != -1) { return events.get(index).shellExecute(); }
		return false;
	}

	public long getFramecount() {
		return framecount;
	}

	private class Event {
		public long nanoSeconds;
		public long lastExecute;
		public String name;

		public Event(String name, long nanoSeconds) {
			this.nanoSeconds = nanoSeconds;
			this.lastExecute = System.nanoTime();
			this.name = name;
		}

		public boolean shellExecute() {
			if (System.nanoTime() - lastExecute > nanoSeconds) {
				lastExecute += nanoSeconds;
				return true;
			}
			return false;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Event) return (((Event) o).name.equals(name));
			return false;
		}
	}
}
