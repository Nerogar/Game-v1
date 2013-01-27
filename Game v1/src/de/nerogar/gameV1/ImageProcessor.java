package de.nerogar.gameV1;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageProcessor {
	private final String filename = "font.png";
	private BufferedImage image;
	private int[] pixels;
	private int h, w;

	private void loadImage() {
		try {
			image = ImageIO.read(ImageProcessor.class.getResourceAsStream("/" + filename));
			System.out.println("preMult: " + image.isAlphaPremultiplied());
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		w = image.getWidth();
		h = image.getHeight();

		pixels = image.getRGB(0, 0, w, h, null, 0, w);
	}

	private void processImage() {
		for (int i = 0; i < pixels.length; i++) {
			int pixel = pixels[i];
			int alpha = ((pixel & 0xff000000) >>> 24);

			if (alpha != 0x00) {

				//System.out.println(alpha + ": " + Integer.toHexString(pixel));
				pixel = 0xffff00ff;
			}

			pixels[i] = pixel;

		}
	}

	private void saveImage() {
		image.setRGB(0, 0, w, h, pixels, 0, w);
		try {
			ImageIO.write(image, "PNG", new FileOutputStream("res/new_" + filename));

			System.out.println("saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ImageProcessor ip = new ImageProcessor();

		ip.loadImage();
		ip.processImage();
		ip.saveImage();
	}
}
