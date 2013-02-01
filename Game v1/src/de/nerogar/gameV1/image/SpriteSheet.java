package de.nerogar.gameV1.image;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL11.*;

import de.nerogar.gameV1.Vector2d;

public class SpriteSheet {

	public String name;
	public int id;
	private ArrayList<String> textures = new ArrayList<String>();
	private HashMap<String, Vector2d> TexturePositons = new HashMap<String, Vector2d>();
	private HashMap<String, Vector2d> TextureSizes = new HashMap<String, Vector2d>();

	public SpriteSheet() {
		/*int[] pixels = new int[64];
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = 0x00ff00;
		}

		IntBuffer texture = BufferUtils.createIntBuffer(8 * 8);
		texture.put(pixels);
		texture.rewind();

		glEnable(GL_TEXTURE_2D);

		IntBuffer buffer = BufferUtils.createIntBuffer(1);
		glGenTextures(buffer);
		int id = buffer.get(0);

		glBindTexture(GL_TEXTURE_2D, id);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		//glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, 8, 8, 0, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, texture);

		glBindTexture(GL_TEXTURE_2D, 0);

		TextureBank.instance.addTexture("test", id);*/
	}

	public void addTexture(String filename) {
		for (String tempFilename : textures) {
			if (tempFilename.equals(filename)) return;
		}
		textures.add(filename);
	}

	public void compile() {
		BufferedImage[] images = new BufferedImage[textures.size()];
		int biggestImage = 0;
		int imageSizeSum = 0;
		for (int i = 0; i < textures.size(); i++) {
			try {
				images[i] = ImageIO.read(new FileInputStream("res/" + textures.get(i)));
				if (images[i].getWidth() > biggestImage) biggestImage = images[i].getWidth();
				imageSizeSum += images[i].getHeight();
			} catch (IOException e) {
				System.err.println("could not load Texture: " + textures.get(i));
				e.printStackTrace();
			}
		}
		int[] pixels = new int[imageSizeSum * biggestImage];
		int yOffset = 0;
		for (int i = 0; i < images.length; i++) {

			for (int x = 0; x < images[i].getWidth(); x++) {
				for (int y = 0; y < images[i].getHeight(); y++) {
					pixels[((yOffset+y)*biggestImage)+x] = images[i].getRGB(x, y);
				}
			}
			yOffset+=images[i].getHeight();
		}
		
		IntBuffer composedTextuer = BufferUtils.createIntBuffer(imageSizeSum * biggestImage);
		composedTextuer.put(pixels);
		composedTextuer.rewind();
		
		/*BufferedImage image = new BufferedImage(biggestImage, imageSizeSum, BufferedImage.TYPE_INT_RGB);
		image.setRGB(0, 0, biggestImage, imageSizeSum, pixels, 0, biggestImage);
		try {
			FileOutputStream out = new FileOutputStream("res/terrain/TestDump.png");
			ImageIO.write(image, "PNG", out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}*/
	}
}
