package de.nerogar.gameV1.graphics;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import org.lwjgl.BufferUtils;

public class TextureBank {
	//private HashMap<String, Texture> textures;
	private HashMap<String, Integer> TextureIDs;
	
	public static TextureBank instance = new TextureBank();

	public TextureBank() {
		//textures = new HashMap<String, Texture>();
		TextureIDs = new HashMap<String, Integer>();
	}

	public void loadTexture(String filename) {
		if (TextureIDs.get(filename) == null) {
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
				BufferedImage image = ImageIO.read(new File("res/" + filename));
				IntBuffer buffer = BufferUtils.createIntBuffer(image.getWidth() * image.getHeight());
				int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
				buffer.put(pixels);
				buffer.rewind();
				addTexture(filename, buffer, image.getWidth(), image.getHeight());
				//System.out.println("loaded: "+filename);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			/*try {
				FileInputStream in = new FileInputStream("res/" + filename);
				Texture newTexture = TextureLoader.getTexture("PNG", in);
				textures.put(filename, newTexture);
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}*/
		}
	}

	public void addTexture(String name, IntBuffer intBuffer, int width, int height) {

		glEnable(GL_TEXTURE_2D);

		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		glGenTextures(buffer);
		int id = buffer.get(0);

		glBindTexture(GL_TEXTURE_2D, id);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, intBuffer);

		TextureIDs.put(name, id);
	}

	public void bindTexture(String textureName) {
		if (!TextureIDs.containsKey(textureName)) {
			loadTexture(textureName);
		}
		glBindTexture(GL_TEXTURE_2D, TextureIDs.get(textureName));
	}

	/*public Texture getTexture(String filename) {
		Texture retTexture = textures.get(filename);
		if (retTexture != null) { return retTexture; }

		loadTexture(filename);

		retTexture = textures.get(filename);

		return retTexture;
	}*/
}
