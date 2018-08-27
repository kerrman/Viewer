package graphics;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Texture {

	public int[] pixels;
	private String loc;
	public final int SIZE, WIDTH, HEIGHT;

	public Texture(String location, int size) {
		System.out.println("Creating Texture: " + location);
		loc = location;
		SIZE = size;
		WIDTH = SIZE;
		HEIGHT = SIZE;
		pixels = new int[SIZE * SIZE];
		load();
	}

	private void load() {
		System.out.println("Loading Pixels File");
		try {
			// BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			// BufferedImage.TYPE_INT_ARGB);
			if (loc != "DEF") {
				BufferedImage image = ImageIO.read(getClass().getResourceAsStream(loc));
				int w = image.getWidth();
				int h = image.getHeight();
				System.out.println("Loading Image");
				image.getRGB(0, 0, w, h, pixels, 0, w);
				System.out.println("Pixels Loaded");
			}else {
				loadDefualt();
			}
		} catch (IOException e) {
			loadDefualt();
			e.printStackTrace();
		}
	}

	private void loadDefualt() {
		System.out.println("Loading DEFAULT Pixels");
		int line = SIZE / 8;
		int g = Color.GREEN.getRGB();
		int b = Color.BLACK.getRGB();
		for (int row = 0; row < SIZE; row++) {
			for (int p = 0; p < line; p++) {
				for (int col = 0; col < 8; col++) {
					int tempInt = b;
					if (col == 0 || row % 8 == 0) {
						tempInt = g;
					}
					pixels[col + p * 8 + row * SIZE] = tempInt;
				}
			}
		}
	}

}
