package de.nerogar.gameV1.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureBank {
	//private ArrayList<Texture> textures;
	//private ArrayList<String> textureNames;
	private HashMap<String, Texture> textures;

	public static TextureBank instance = new TextureBank();

	public TextureBank() {
		textures = new HashMap<String, Texture>();
	}

	public void loadTexture(String filename) {
		if (textures.get(filename) == null) {
			/*BufferedImage image = null;
			try {
				InputStream in=TextureBank.class.getResourceAsStream("/" + filename);
				image = ImageIO.read(in);
				image.coerceData(true);
				System.out.println(image.isAlphaPremultiplied());

			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				Texture newTexture = BufferedImageUtil.getTexture("PNG", image);
				textures.put(filename, newTexture);
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			
			try {
				Texture newTexture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/" + filename)));
				textures.put(filename, newTexture);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Texture getTexture(String filename) {

		Texture retTexture = textures.get(filename);
		if (retTexture != null) { return retTexture; }

		loadTexture(filename);

		retTexture = textures.get(filename);

		return retTexture;
	}
}
