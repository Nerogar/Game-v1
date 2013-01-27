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
		/*int[] intarray = { 1, 2, 3, 4, 5, 6 };
		dnFile.addNode("test.1", 1l);
		dnFile.addNode("test.a.2", intarray);
		dnFile.addNode("3", '*');
		dnFile.addNode("test.4", 4);
		dnFile.addNode("test.5", "inhalt 1");
		dnFile.addNode("abc.def.ghi.6", new String[] { "inhalt 1", "inhalt 2" });*/

		byte[] testarray = { 0, 1 };

		dnFile.addNode("abc", testarray);
		dnFile.addNode("def", testarray);
		dnFile.addNode("0abc", testarray);

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
		System.out.println(dnFile.getByteArray("abc")[1]);
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
