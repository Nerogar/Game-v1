package de.nerogar.gameV1.DNFileSystem;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveTest {

	DNFile dnFile;

	public void init() {
		dnFile = new DNFile("saves/dnTest1.abc");
		System.out.println("--init fertig");
	}

	public void addIrgendwas() {
		dnFile = new DNFile("");

		dnFile.addNode("playernames", "Nerogar");

		System.out.println("playernames" + dnFile.getString("playernames"));

		System.out.println("--add fertig");
	}

	public void save() {
		dnFile.save();
		System.out.println("--save fertig");
	}

	public void laod() {
		dnFile.load();
		System.out.println("--laod fertig");
	}

	public void auslesen() {
		String[] test = dnFile.getStringArray("playernames");
		for (String s : test) {
			System.out.println(s);
		}
		System.out.println("size: " + dnFile.toByteArray().length);
		System.out.println();
		System.out.println("--lesen fertig");
	}

	public void stringSaveTest() {
		try {
			File file = new File("saves/string.a");
			FileOutputStream f = new FileOutputStream(file);
			DataOutputStream out = new DataOutputStream(f);

			String teststring = "";
			for (int i = 0; i < 256; i++) {
				teststring = teststring + "*";
			}

			out.writeUTF(teststring);
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stringLoadTest() {
		try {
			File file = new File("saves/string.a");
			FileInputStream f = new FileInputStream(file);
			DataInputStream in = new DataInputStream(f);

			byte[] buffer = new byte[in.available()];

			in.read(buffer);

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SaveTest st = new SaveTest();
		st.init();
		st.addIrgendwas();
		st.save();
		st.laod();
		st.auslesen();
	}
}
