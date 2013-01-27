package de.nerogar.gameV1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class GameOptions {

	public final float GRAVITY = 9.81F;

	private String filename = "options.txt";
	private Properties options = new Properties();
	public static GameOptions instance = new GameOptions();

	public GameOptions() {
		load();
	}

	public String getOption(String option) {
		String s = options.getProperty(option);
		if (s != null) return s;
		else return "";
	}

	public void setOption(String option, String value) {
		options.put(option, value);
	}
	
	public void setOption(String option, boolean value) {
		options.put(option, String.valueOf(value));
	}

	public int getIntOption(String option) {
		String s = options.getProperty(option);
		int i = 0;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return i;
	}

	public void setIntOption(String option, int value) {
		options.put(option, String.valueOf(value));
	}

	public double getDoubleOption(String option) {
		String s = options.getProperty(option);
		double d = 0;
		try {
			d = Double.parseDouble(s);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return d;
	}

	public void setDoubleOption(String option, double value) {
		options.put(option, String.valueOf(value));
	}

	public boolean getBoolOption(String option) {
		String s = options.getProperty(option);
		if (s != null) return s.equals("true");
		else return false;
	}

	public void setBoolOption(String option, boolean value) {
		options.put(option, String.valueOf(value));
	}

	public void switchBoolOption(String option) {
		if (options.getProperty(option).equals("true")) options.put(option, "false");
		else options.put(option, "true");
	}

	private void load() {
		if (new File(filename).exists()) {
			try {
				FileInputStream fis = new FileInputStream(filename);
				options.load(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			generateOptionsFile();
			save();
		}
	}

	public void save() {
		try {
			options.store(new FileOutputStream(filename), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void generateOptionsFile() {
		options.put("width", "800");
		options.put("height", "600");
		options.put("fullscreen", "false");
		options.put("debug", "false");
		options.put("fov", "80");
		options.put("fps", "60");
		options.put("vSync", "false");
		options.put("loaddistance", "10");
		options.put("renderdistance", "5");
		options.put("showAABBs", "false");
	}
}
