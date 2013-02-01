package de.nerogar.gameV1.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.InternalTextureLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class TextureBank {
	//private ArrayList<Texture> textures;
	//private ArrayList<String> textureNames;
	private HashMap<String, Texture> textures;
	private HashMap<String, Integer> TextureIDs;

	public static TextureBank instance = new TextureBank();

	public TextureBank() {
		textures = new HashMap<String, Texture>();
		TextureIDs = new HashMap<String, Integer>();
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
				FileInputStream in = new FileInputStream("res/" + filename);
				Texture newTexture = TextureLoader.getTexture("PNG", in);
				textures.put(filename, newTexture);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void addTexture(String textureName, int id) {
		TextureIDs.put(textureName, id);
	}

	public void bindTexture(String textureName){
		 glBindTexture(GL_TEXTURE_2D, TextureIDs.get(textureName));
	}
	
	public Texture getTexture(String filename) {

		Texture retTexture = textures.get(filename);
		if (retTexture != null) { return retTexture; }

		loadTexture(filename);

		retTexture = textures.get(filename);

		return retTexture;
	}
}
