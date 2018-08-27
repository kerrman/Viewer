package graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class Texture {

	// public int[] pixels;
	private String loc;
	BufferedImage IMG;
	public volatile int WIDTH = 0, HEIGHT = 0;

	public Texture(String location, int size) {
		System.out.println("Creating Texture: " + location);
		loc = location;
		WIDTH = size;
		HEIGHT = size;
		load();
	}

	public Texture(String location) {
		System.out.println("Creating Texture: " + location);
		loc = location;
		load();
	}

	private void load() {
		System.out.println("Loading Pixels File");
		try {
			// BufferedImage image = new BufferedImage(WIDTH, HEIGHT,
			// BufferedImage.TYPE_INT_ARGB);
			if (loc != "DEF") {
				System.out.println(loc);
				IMG = ImageIO.read(getClass().getResourceAsStream(loc));
				int w = IMG.getWidth();
				if (WIDTH == 0) {
					WIDTH = w;
				}
				int h = IMG.getHeight();
				if (HEIGHT == 0) {
					HEIGHT = h;
				}
				// pixels = new int[WIDTH*HEIGHT];
				// System.out.println("Loading Image");
				// IMG.getRGB(0, 0, w, h, pixels, 0, w);
				// System.out.println("Pixels Loaded");
			} else {
				loadDefualt();
			}
		} catch (IOException e) {
			loadDefualt();
			e.printStackTrace();
		}
	}

	private void loadDefualt() {
		System.out.println("Loading DEFAULT Pixels");
		if (WIDTH == 0) {
			WIDTH = 8;
		}
		if (HEIGHT == 0) {
			HEIGHT = 8;
		}
		int[] pixels = new int[HEIGHT * WIDTH];
		int line = WIDTH / 8;
		int g = Color.GREEN.getRGB();
		int b = Color.BLACK.getRGB();
		for (int row = 0; row < WIDTH; row++) {
			for (int p = 0; p < line; p++) {
				for (int col = 0; col < 8; col++) {
					int tempInt = b;
					if (col == 0 || row % 8 == 0) {
						tempInt = g;
					}
					pixels[col + p * 8 + row * WIDTH] = tempInt;
				}
			}
		}
		IMG = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		IMG.setRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
		;
	}
	/*
	 * public int getPix(int location) { return pixels[location]; }
	 */

	public BufferedImage getIMG() {
		return IMG;
	}

	public int getPix(int x, int y) {
		return IMG.getRGB(x, y);
	}

	public int[] resize(int xDiff, int yDiff) {
		Image tmp = IMG.getScaledInstance(WIDTH + xDiff, HEIGHT + yDiff, IMG.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(WIDTH + xDiff, HEIGHT + yDiff, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		int[] ret = new int[(WIDTH + xDiff) * (HEIGHT + yDiff)];
		// System.out.println("Width: "+(WIDTH + xDiff)+" - HEIGHT: "+(HEIGHT + yDiff));
		dimg.getRGB(0, 0, WIDTH + xDiff, HEIGHT + yDiff, ret, 0, WIDTH + xDiff);
		return ret;
	}

	public int getCube() {
		return WIDTH;
	}

	public void cube(int skyCubeSize) {
		if (skyCubeSize != WIDTH && skyCubeSize != HEIGHT) {
			//System.out.println("RESIZE/nWidth:"+WIDTH+" - Height:"+HEIGHT);
			IMG = resize(skyCubeSize);
			WIDTH = skyCubeSize;
			HEIGHT = skyCubeSize;
			//System.out.println("NEW Width:"+WIDTH+" - Height:"+HEIGHT);
		}
	}

	private BufferedImage resize(int skyCubeSize) {
		Image tmp = IMG.getScaledInstance(skyCubeSize, skyCubeSize, IMG.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(skyCubeSize, skyCubeSize, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return dimg;
	}

}
