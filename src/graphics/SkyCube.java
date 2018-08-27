/**
 * 
 */
package graphics;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import math.Matrix3;
import math.Vertex;
import objects.Triangle;
import player.Player;

/**
 * @author Chris
 *
 */
public class SkyCube {

	// Custom SkyBox variables
	protected List<Triangle> skyBox = new ArrayList<>();
	private volatile List<Texture> sky;// = new Texture("/constellation2.jpg");
	protected int skyCubeSize = 0;
	// used for vertices of skybox
	private short l = -1, r = 1, top = -1, bot = 1, fd = 1, bk = -1;
	BufferedImage skyImage;

	/**
	 * 
	 */
	public SkyCube() {
		init();
		// TODO Auto-generated constructor stub
	}
	
	private void init() {

		sky = new ArrayList<Texture>();
		setSkyBoxTexture();
		// setSkyBox();
	}


	private void setSkyBox() {
		// Front Square - two triangles
		Vertex FTL = new Vertex(l, top, fd);
		Vertex FTR = new Vertex(r, top, fd);
		Vertex FBL = new Vertex(l, bot, fd);
		Vertex FBR = new Vertex(r, bot, fd);
		skyBox.add(new Triangle(FTR, FTL, FBL, 0));
		// skyBox.add(new Triangle(FBL, FBR, FTR, 6));
		// Back Square - two triangles
		Vertex BTL = new Vertex(l, top, bk);
		Vertex BTR = new Vertex(r, top, bk);
		Vertex BBL = new Vertex(l, bot, bk);
		Vertex BBR = new Vertex(r, bot, bk);
		skyBox.add(new Triangle(BTL, BTR, BBL, 1));
		// skyBox.add(new Triangle(BBL, BBR, BTR, 7));
		// Top square
		skyBox.add(new Triangle(FTR, FTL, BTL, 2));
		// skyBox.add(new Triangle(BTL, BTR, FTR, 8));
		// Bottom square
		skyBox.add(new Triangle(FBR, FBL, BBL, 3));
		// skyBox.add(new Triangle(BBL, BBR, FBR, 9));
		// Right square
		skyBox.add(new Triangle(FBR, BBR, FTR, 4));
		// skyBox.add(new Triangle(BBR, BTR, FTR, 10));
		// Left square
		skyBox.add(new Triangle(FBL, BBL, FTL, 5));
		// skyBox.add(new Triangle(BBL, BTL, FTL, 11));
	}

	private void setSkyBoxTexture() {
		sky.add(0, new Texture("/nebula/ft.jpg"));
		sky.add(1, new Texture("/nebula/bk.jpg"));
		sky.add(2, new Texture("/nebula/up.jpg"));
		sky.add(3, new Texture("/nebula/dn.jpg"));
		sky.add(4, new Texture("/nebula/rt.jpg"));
		sky.add(5, new Texture("/nebula/lt.jpg"));
		for (Texture skyT : sky) {
			skyCubeSize = Math.max(skyCubeSize, skyT.getCube());
			//System.out.println("CubeSize:"+skyCubeSize);
		}
		skyImage = new BufferedImage(skyCubeSize, skyCubeSize, BufferedImage.TYPE_INT_ARGB);
		for (Texture skyT : sky) {
			skyT.cube(skyCubeSize);
		}
		// Setting cube dimension to texture dimensions

		l *= skyCubeSize / 2;
		r *= skyCubeSize / 2;
		top *= skyCubeSize / 2;
		bot *= skyCubeSize / 2;
		fd *= skyCubeSize / 2;
		bk *= skyCubeSize / 2;
		//createSkyImg();
		setSkyBox();
		//System.out.println("SkyBox Textures Loaded and CUBED to:" + skyCubeSize);
	}

	private void createSkyImg() {
		// TODO Auto-generated method stub
		skyImage = new BufferedImage(skyCubeSize * 3, skyCubeSize * 2, BufferedImage.TYPE_INT_ARGB);
		/*
		 * sky.add(0, new Texture("/nebula/ft.jpg")); sky.add(1, new
		 * Texture("/nebula/bk.jpg")); sky.add(2, new Texture("/nebula/up.jpg"));
		 * sky.add(3, new Texture("/nebula/dn.jpg")); sky.add(4, new
		 * Texture("/nebula/rt.jpg")); sky.add(5, new Texture("/nebula/lt.jpg"));
		 */
		int pix = 0, xDraw = 0, yDraw = 0;
		byte xTex = 0, yTex = 0;
		byte[][] tex = new byte[][] { { 5, 0, 4 }, { 1, 2, 3 } };
		for (int x = 0; x < (skyCubeSize * 3); x++) {
			if (xDraw > skyCubeSize) {
				xDraw -= skyCubeSize;
			}
			if (x >= (skyCubeSize * 2)) {
				xTex = 2;
			} else if (x >= (skyCubeSize)) {
				xTex = 1;
			} else {
				xTex = 0;
			}
			for (int y = 0; y < (skyCubeSize * 2); y++) {
				if (yDraw > skyCubeSize) {
					yDraw -= skyCubeSize;
				}
				if (x >= (skyCubeSize)) {
					yTex = 1;
				} else {
					yTex = 0;
				}
				pix = sky.get(tex[yTex][xTex]).getPix(xDraw, yDraw);
				skyImage.setRGB(x, y, pix);
			}
		}
	}


	private void drawSkyBox(BufferedImage canvas, Player who, double[] buffer) {
		// TODO SKYBOX
		// draw cube to cube dimensions - capture output
		// then resize
		BufferedImage result = new BufferedImage(skyCubeSize, skyCubeSize, BufferedImage.TYPE_INT_ARGB);
		double[] zBuffer = new double[skyCubeSize * skyCubeSize];
		for (int q = 0; q < zBuffer.length; q++) {
			zBuffer[q] = Double.NEGATIVE_INFINITY;
		}
		for (Triangle t : skyBox) {
			// Rotate Cube to what player is seeing
			// Also clone vertex - we don't want to change the actual location
			Vertex v1 = who.rotation.transform(t.v1.clone());// t.v1.clone();
			Vertex v2 = who.rotation.transform(t.v2.clone());// t.v2.clone();
			Vertex v3 = who.rotation.transform(t.v3.clone());// t.v3.clone();
			// TODO is it better to call a method and get VAR or just create local VAR?
			v1.x += skyCubeSize / 2;
			v1.y += skyCubeSize / 2;
			v1.z += skyCubeSize / 2;
			v2.x += skyCubeSize / 2;
			v2.y += skyCubeSize / 2;
			v2.z += skyCubeSize / 2;
			v3.x += skyCubeSize / 2;
			v3.y += skyCubeSize / 2;
			v3.z += skyCubeSize / 2;
			// System.out.println("V1.x:"+v1.x+" y:"+v1.y+" z:"+v1.z);
			// int xDiff = canvas.getWidth() - sky.get(t.getID()).WIDTH;
			// int yDiff = canvas.getHeight() - sky.get(t.getID()).HEIGHT;
			// System.out.println("Texture to draw is: " + t.getID());
			// int[] pix = sky.get(t.getID()).resize(xDiff, yDiff);

			int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
			int maxX = (int) Math.min(skyCubeSize - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
			int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
			int maxY = (int) Math.min(skyCubeSize - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

			double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
			// System.out.print("Start");
			for (int y = minY; y <= maxY; y++) {
				for (int x = minX; x <= maxX; x++) {
					double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
					double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
					double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
					if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
						double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
						int zIndex = y * skyCubeSize + x;
						if (zBuffer[zIndex] < depth) {
							// System.out.print("X:"+x+"-Y:"+y);
							result.setRGB(x, y, sky.get(t.getID()).getPix(x, y));
							// canvas.setRGB(x, y, 255);//new Color(t.getID()*200).getRGB());//pix[x + y *
							// canvas.getWidth()]);
							zBuffer[zIndex] = depth;
						}
					}
				}
			} // System.out.println("-END");

		}
	}
	// for testing of code purposes
		private void drawSkyBoxOutline(int[] mainViewer, Player who) {
			BufferedImage img = new BufferedImage((int)who.getScreenSize().getWidth(),(int)who.getScreenSize().getHeight(),BufferedImage.TYPE_INT_ARGB);
			img.setRGB(0, 0, (int)who.getScreenSize().getWidth(), (int)who.getScreenSize().getHeight(), mainViewer, 0, (int)who.getScreenSize().getWidth());
			Graphics2D g2 = img.createGraphics();
			g2.setColor(Color.BLUE);
			g2.drawString("TEST: SKYBOX outline", 25, 25);
			g2.translate(img.getWidth() / 2, img.getHeight() / 2);
			Path2D pathB = new Path2D.Double();
			int left = -skyCubeSize / 2;
			int right = +skyCubeSize / 2;
			int down = -skyCubeSize / 2;
			int up = +skyCubeSize / 2;
			//System.out.println("L:"+left);
			pathB.moveTo(left, up);
			pathB.lineTo(right, up);
			pathB.lineTo(right, down);
			pathB.lineTo(left, down);
			pathB.closePath();
			g2.draw(pathB);
			g2.setColor(Color.WHITE);
			for (Triangle t : skyBox) {
				Vertex v1 = who.rotation.transform(t.getDrawV().clone());// t.v1.clone();
				Vertex v2 = who.rotation.transform(t.getAngleV().clone());// t.v2.clone();
				Vertex v3 = who.rotation.transform(t.getV((byte) (2)).clone());// t.v3.clone();
				Path2D path = new Path2D.Double();
				path.moveTo(v1.x, v1.y);
				path.lineTo(v2.x, v2.y);
				path.lineTo(v3.x, v3.y);
				path.closePath();
				g2.draw(path);

				// Only needed if a conditional statement is added above
				// This resets to our default color for line draw
				// g2.setColor(Color.WHITE);
			}
			img.getRGB(0, 0, (int)who.getScreenSize().getWidth(),(int)who.getScreenSize().getHeight(), mainViewer, 0, (int)who.getScreenSize().getWidth());
			g2.dispose();
			// drawSkyBox(tmp, who);
		}

		// for testing of code purposes
		private void drawSkyBox(BufferedImage tmp, Player who) {
			double playerBoundsWidth = (int) (who.getScreenSize().getWidth()),
					playerBoundsHeight = (int) (who.getScreenSize().getHeight());
			double screenRatio = playerBoundsWidth / playerBoundsHeight;
			// System.out.println("Ratio:" + screenRatio);
			if (playerBoundsHeight > playerBoundsWidth) {
				playerBoundsHeight = (int) (skyCubeSize * .9);
				playerBoundsWidth = (int) (playerBoundsHeight * screenRatio);
			} else {
				screenRatio = playerBoundsHeight / playerBoundsWidth;
				// System.out.println("Ratio:" + screenRatio);
				playerBoundsWidth = (int) (skyCubeSize * .9);
				playerBoundsHeight = (int) (playerBoundsWidth * screenRatio);
			}
			Graphics2D g2 = tmp.createGraphics();
			g2.setColor(Color.RED);
			g2.translate(tmp.getWidth() / 2, tmp.getHeight() / 2);
			Path2D pathB = new Path2D.Double();
			int left = (int) (-playerBoundsWidth / 2);
			int right = (int) (+playerBoundsWidth / 2);
			int down = (int) (-playerBoundsHeight / 2);
			int up = (int) (+playerBoundsHeight / 2);
			pathB.moveTo(left, up);
			pathB.lineTo(right, up);
			pathB.lineTo(right, down);
			pathB.lineTo(left, down);
			pathB.closePath();
			g2.draw(pathB);
			g2.dispose();
			boolean[] done = { false, false, false, false, false, false };
			int tmpSIZE = tmp.getWidth() * tmp.getHeight();
			for (Triangle t : skyBox) {
				int toPrint = t.getID();
				if (toPrint == 1) {
					// sSystem.out.println("draw SkyBox OUTLINE sample x:" + t.v1.x);
					Vertex v1 = who.rotation.transform(t.getDrawV().clone());// t.v1.clone();
					Vertex v2 = who.rotation.transform(t.getAngleV().clone());// t.v2.clone();
					Vertex v3 = who.rotation.transform(t.getV((byte) (2)).clone());// t.v3.clone();
					// Centering cube draw to middle of skyImage
					/*
					 * v1.x += skyCubeSize / 2; v1.y += skyCubeSize / 2; v1.z += skyCubeSize / 2;
					 * v2.x += skyCubeSize / 2; v2.y += skyCubeSize / 2; v2.z += skyCubeSize / 2;
					 * v3.x += skyCubeSize / 2; v3.y += skyCubeSize / 2; v3.z += skyCubeSize / 2;
					 */
					// int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x,
					// v3.x))));
					// int maxX = (int) Math.min(skyCubeSize - 1, Math.floor(Math.max(v1.x,
					// Math.max(v2.x, v3.x))));
					// int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y,
					// v3.y))));
					// int maxY = (int) Math.min(skyCubeSize - 1, Math.floor(Math.max(v1.y,
					// Math.max(v2.y, v3.y))));

					if (!done[toPrint]) {
						double rise = (v2.y - v1.y), run = (v2.x - v1.x);
						double riseEdge = (v2.y - v3.y), runEdge = (v2.x - v3.x);
						if (Math.abs(rise) < 3 || Math.abs(run) < 3 || Math.abs(riseEdge) < 3 || Math.abs(runEdge) < 3) {
							// TODO skyBox draw less than three - do we need to do anything here?
						} else {
							double m = rise / run;
							double mEdge = riseEdge / runEdge;
							// y - k = m (x - h)
							double b = v2.y - (m * v2.x);
							double bEdge = v2.y - (mEdge * v2.x);
							int dist = (int) Math.sqrt(Math.pow((v2.y - v1.y), 2) + Math.pow((v2.x - v1.x), 2));
							int distEdge = (int) Math.sqrt(Math.pow((v2.y - v3.y), 2) + Math.pow((v2.x - v3.x), 2));
							System.out.println("Dist:" + dist + " - SkyCubeDist:" + skyCubeSize + "\ny = " + m + " x + " + b
									+ " - For (" + v2.x + "," + v2.y + "),(" + v1.x + "," + v1.y + ")" + "\nDistEdge:"
									+ distEdge + " \ny = " + mEdge + " x + " + bEdge + " - For (" + v2.x + "," + v3.y
									+ "),(" + v1.x + "," + v3.y + ")");
							// x=(y-b)/m
							// int x = 0, y = 0;
							int r = 50;
							int g = 0;
							int bl = 125;
							int yStart = (int) Math.max((tmp.getHeight() / 2) + left, v2.y);
							int yEnd = (int) Math.min((tmp.getHeight() / 2) + right, (v2.y + rise));
							byte mod = (byte) (Math.abs(rise) / rise);
							for (int y = yStart, yTrack = 0; yTrack <= Math.abs(rise); y += mod, yTrack++) {
								double xEdge = ((+y) - bEdge) / mEdge;
								// double yEdge=((xEdge*mEdge)+bEdge);
								// System.out.println("xEdge|x: "+xEdge+"|"+v2.x+" - yEdge|y: "+yEdge+"|"+v2.y);
								int xEnd = (int) Math.min(((tmp.getWidth() / 2)), (xEdge + run));
								for (int x = (int) xEdge; x < xEnd; x++) {
									int yDraw = (int) (((xEdge + x) * m) + b);
									int xDraw = (int) ((yDraw - b) / m);
									xDraw += (tmp.getWidth() / 2);
									yDraw += (tmp.getHeight() / 2);
									System.out.println(
											"xDraw|x: " + xDraw + "|" + v2.x + " - yDraw|y: " + yDraw + "|" + v2.y);
									tmp.setRGB(xDraw, yDraw, new Color(r, g, bl).getRGB());
								}
								r += 50;
								g += 100;
								bl += 75;
								if (r >= 255) {
									r -= 255;
								}
								if (g >= 255) {
									g -= 255;
								}
								if (bl >= 255) {
									bl -= 255;
								}
							}
						}
						done[toPrint] = true;
					}
				}
			}
		}

		private void testNotWorking(BufferedImage canvas, Player who, double[] buffer){
			for (Triangle t : skyBox) {
				int toPrint = t.getID();
				if (toPrint <= 5) {
					// sSystem.out.println("draw SkyBox OUTLINE sample x:" + t.v1.x);
					Vertex v1 = t.getDrawV().clone();// t.v1.clone();
					Vertex v2 = t.getAngleV().clone();// t.v2.clone();
					Vertex v3 = t.getV((byte) (2)).clone();// t.v3.clone();

					v1 = who.rotation.transform(v1);
					v2 = who.rotation.transform(v2);
					v3 = who.rotation.transform(v3);

					int xMod = (int) (Math.abs((v1.x - v2.x)) / (v1.x - v2.x));
					int yMod = (int) (Math.abs((v3.y - v2.y)) / (v3.y - v2.y));
					double distX = Math.min(skyCubeSize, (int) Math.sqrt(Math.pow((v1.x - v2.x), 2)));
					double distY = Math.min(skyCubeSize, (int) Math.sqrt(Math.pow((v3.y - v2.y), 2)));
					System.out.println("xMod | yMod : " + xMod + "|" + yMod);
					if (xMod != 0 && yMod != 0 && distX >= 3 && distY >= 3) {
						int xStart = (int) (v2.x);
						int yStart = (int) (v2.y);
						int zStart = (int) v2.z;

						// double zAvg = ((v2.z + v1.z) / 2 + (v2.z + v3.z) / 2) / 2;
						// int dist = (int) Math.sqrt(Math.pow((v2.y - v1.y), 2) + Math.pow((v2.x -
						// v1.x), 2));
						int xzRatio = (int) ((v2.z - v1.z) / (v2.x - v1.x));
						double yDiff = Math.abs(skyCubeSize / distY);
						double xDiff = Math.abs(skyCubeSize / distX);
						if (yDiff < 1) {
							yDiff = 1;
							System.out.println("v2.y - v3.y : " + v2.y + " - " + v3.y);
							System.out.println("skyCubeSize | distX | distY : " + skyCubeSize + "|" + distX + "|" + distY);
							// System.exit(0);
						}
						if (xDiff < 1) {
							xDiff = 1;
							System.out.println("v2.x - v1.x : " + v2.x + " - " + v1.x);
							System.out.println("skyCubeSize | distX | distY : " + skyCubeSize + "|" + distX + "|" + distY);
						}
						// int distEdge = (int) Math.sqrt(Math.pow((v2.y - v3.y), 2) + Math.pow((v2.x -
						// v3.x), 2));
						int yzRatio = (int) ((v2.z - v3.z) / (v2.y - v3.y));
						/*
						 * v1.x += canvas.getWidth() / 2; v1.y += canvas.getHeight() / 2; //v1.z +=
						 * skyCubeSize / 2; v2.x += canvas.getWidth() / 2; v2.y += canvas.getHeight() /
						 * 2; //v2.z += skyCubeSize / 2; v3.x += canvas.getWidth() / 2; v3.y +=
						 * canvas.getHeight() / 2; //v3.z += skyCubeSize / 2;
						 */
						for (int yPix = 0; yPix < distY && yPix >= 0; yPix += yDiff) {
							int y = yStart + ((yPix) * yMod);
							int z = y * yzRatio + zStart;
							// System.out.println("y:"+y);
							int rgb = 0;
							for (int xPix = 0; xPix < distX && xPix >= 0; xPix += xDiff) {
								int x = xStart + ((xPix) * xMod);
								z += x * xzRatio;
								// System.out.println("x:"+x);
								// Vertex vDraw = who.rotation.transform(new Vertex(x, y, z));
								// TODO buffer add
								int xPos = (int) (x + (canvas.getWidth() / 2));
								int yPos = (int) (y + (canvas.getHeight() / 2));
								// BELOW MIGHT NEED <= TO BECOME <
								if (xPos >= 0 && xPos < canvas.getWidth() && yPos >= 0 && yPos < canvas.getHeight()) {
									// System.out.println("xPos | yPos - " + xPos + "|" + yPos);
									rgb = sky.get(t.getID()).getPix(xPix, yPix);
									canvas.setRGB(xPos, yPos, rgb);
								}
							}
						}
					}
				}
			}
		}

		private void testKindaWorks(BufferedImage canvas, Player who, double[] buffer) {
			for (Triangle t : skyBox) {
				int toPrint = t.getID();
				if (toPrint <= 5) {
					// sSystem.out.println("draw SkyBox OUTLINE sample x:" + t.v1.x);
					Vertex v1 = t.getDrawV().clone();// t.v1.clone();
					Vertex v2 = t.getAngleV().clone();// t.v2.clone();
					Vertex v3 = t.getV((byte) (2)).clone();// t.v3.clone();
					int xStart = (int) (v2.x);
					int yStart = (int) (v2.y);
					int zStart = (int) v2.z;
					/*
					 * v1 = who.rotation.transform(v1); v2 = who.rotation.transform(v2); v3 =
					 * who.rotation.transform(v3);
					 */
					// double zAvg = ((v2.z + v1.z) / 2 + (v2.z + v3.z) / 2) / 2;
					// int dist = (int) Math.sqrt(Math.pow((v2.y - v1.y), 2) + Math.pow((v2.x -
					// v1.x), 2));
					// int distY = (int) Math.sqrt(Math.pow((v2.y - v1.y), 2));
					int xzRatio = (int) ((v2.z - v1.z) / (v2.x - v1.x));
					// int distX = (int) Math.sqrt(Math.pow((v2.x - v3.x), 2));
					// int distEdge = (int) Math.sqrt(Math.pow((v2.y - v3.y), 2) + Math.pow((v2.x -
					// v3.x), 2));
					int yzRatio = (int) ((v2.z - v3.z) / (v2.y - v3.y));
					int xMod = (int) (Math.abs((v1.x - v2.x)) / (v1.x - v2.x));
					int yMod = (int) (Math.abs((v3.y - v2.y)) / (v3.y - v2.y));
					System.out.println("xMod | yMod : " + xMod + "|" + yMod);
					/*
					 * v1.x += canvas.getWidth() / 2; v1.y += canvas.getHeight() / 2; //v1.z +=
					 * skyCubeSize / 2; v2.x += canvas.getWidth() / 2; v2.y += canvas.getHeight() /
					 * 2; //v2.z += skyCubeSize / 2; v3.x += canvas.getWidth() / 2; v3.y +=
					 * canvas.getHeight() / 2; //v3.z += skyCubeSize / 2;
					 */
					int r = 50;
					int g = 0;
					int bl = 125;
					for (int yPix = 0; yPix < skyCubeSize; yPix++) {
						int y = yStart + (yPix * yMod);
						int z = y * yzRatio + zStart;
						// System.out.println("y:"+y);
						int rgb = r << 16 + g << 8 + bl;
						for (int xPix = 0; xPix < skyCubeSize; xPix++) {
							int x = xStart + (xPix * xMod);
							z += x * xzRatio;
							// System.out.println("x:"+x);
							Vertex vDraw = who.rotation.transform(new Vertex(x, y, z));
							// TODO buffer add
							int xPos = (int) (vDraw.x + (canvas.getWidth() / 2));
							int yPos = (int) (vDraw.y + (canvas.getHeight() / 2));
							// BELOW MIGHT NEED <= TO BECOME <
							if (xPos >= 0 && xPos < canvas.getWidth() && yPos >= 0 && yPos < canvas.getHeight()) {
								// System.out.println("xPos | yPos - " + xPos + "|" + yPos);
								rgb = sky.get(t.getID()).getPix(xPix, yPix);
								canvas.setRGB(xPos, yPos, rgb);
							}
						}

						r += 50;
						g += 100;
						bl += 75;
						if (r >= 255) {
							r -= 255;
						}
						if (g >= 255) {
							g -= 255;
						}
						if (bl >= 255) {
							bl -= 255;
						}
					}
				}
			}
		}

		private void test(int[] mainViewer, Player who, double[] buffer) {
			int width = (int) who.getScreenSize().getWidth();
			int height = (int) who.getScreenSize().getHeight();
			for (Triangle t : skyBox) {
				int toPrint = t.getID();
				if (toPrint <= 5) {
					// sSystem.out.println("draw SkyBox OUTLINE sample x:" + t.v1.x);
					Vertex v1 = t.getDrawV().clone();// t.v1.clone();
					Vertex v2 = t.getAngleV().clone();// t.v2.clone();
					Vertex v3 = t.getV((byte) (2)).clone();// t.v3.clone();
					Matrix3 fixer = new Matrix3(new double[] { 1, 0, 0, 0, 1, 0, 0, 0, 1 });
					int xMod = (int) (Math.abs((v1.x - v2.x)) / (v1.x - v2.x));
					int yMod = (int) (Math.abs((v3.y - v2.y)) / (v3.y - v2.y));
					int xStart = (int) (v2.x);
					int yStart = (int) (v2.y);
					int zStart = (int) v2.z;
					int xzRatio = (int) ((v1.z - v2.z) / (v1.x - v2.x));
					int yzRatio = (int) ((v3.z - v2.z) / (v3.y - v2.y));
					int xRot = 0, yRot = 0;
					System.out.println("xMod | yMod : " + xMod + "|" + yMod);
					if (xMod == 0 || yMod == 0) {
						xRot = xMod == 0 ? 90 : 0;
						yRot = yMod == 0 ? 90 : 0;
						fixer = fixer.rotateXZ(xRot).rotateYZ(yRot);
						Vertex v1tmp = fixer.transform(v1);
						Vertex v2tmp = fixer.transform(v2);
						Vertex v3tmp = fixer.transform(v3);
						xStart = (int) (v2tmp.x);
						yStart = (int) (v2tmp.y);
						zStart = (int) v2tmp.z;
						xzRatio = (int) ((v1tmp.z - v2tmp.z) / (v1tmp.x - v2tmp.x));
						yzRatio = (int) ((v3tmp.z - v2tmp.z) / (v3tmp.y - v2tmp.y));
						xMod = (int) (Math.abs((v1tmp.x - v2tmp.x)) / (v1tmp.x - v2tmp.x));
						yMod = (int) (Math.abs((v3tmp.y - v2tmp.y)) / (v3tmp.y - v2tmp.y));
						//fixer = fixer.UNrotateXZ(xRot).UNrotateYZ(yRot);
						System.out.println("NEW MODS >> xMod | yMod : " + xMod + "|" + yMod +"\nxrot:"+xRot+"yrot:"+yRot);
					}
					System.out.println("v1.x - v2.x : " + v1.x + " - " + v2.x);
					System.out.println("v3.y - v2.y : " + v3.y + " - " + v2.y);
					v1 = who.rotation.transform(v1);
					v2 = who.rotation.transform(v2);
					v3 = who.rotation.transform(v3);
					// TODO calculate avg z and decide if it is in front of player to even draw
					double distX = Math.min(skyCubeSize, (int) Math.sqrt(Math.pow((v1.x - v2.x), 2)));
					double distY = Math.min(skyCubeSize, (int) Math.sqrt(Math.pow((v3.y - v2.y), 2)));
					System.out.println("xMod | yMod : " + xMod + "|" + yMod);
					if (distX >= 3 && distY >= 3) {
						Matrix3 rotation;
						if (xRot != 0 || yRot != 0) {
							rotation = who.rotation.multiply(fixer);
						} else {
							rotation = who.rotation;
						}
						double yDiff = Math.abs(skyCubeSize / distY);
						double xDiff = Math.abs(skyCubeSize / distX);
						for (int yPix = 0; yPix < skyCubeSize; yPix += yDiff) {
							int y = yStart + (yPix * yMod);
							int z = y * yzRatio + zStart;
							int rgb = 255 << t.getID() == 0 ? 16 : t.getID() * 3;
							for (int xPix = 0; xPix < skyCubeSize; xPix += xDiff) {
								int x = xStart + (xPix * xMod);
								z += x * xzRatio;
								// System.out.println("x:"+x);
								Vertex vDraw;
								if (xRot != 0 || yRot != 0) {
									vDraw = rotation.UNrotateXZ(xRot).UNrotateYZ(yRot).transform(new Vertex(x, y, z));
								} else {
									vDraw = rotation.transform(new Vertex(x, y, z));
								}
								// TODO buffer add
								int xPos = (int) (vDraw.x + (width / 2));
								int yPos = (int) (vDraw.y + (height / 2));
								// BELOW MIGHT NEED <= TO BECOME <
								if (xPos >= 0 && xPos < width && yPos >= 0 && yPos < height) {
									if (buffer[xPos + yPos * width] < vDraw.z) {
										buffer[xPos + yPos * width] = vDraw.z;
										// System.out.println("xPos | yPos - " + xPos + "|" + yPos);
										// rgb = sky.get(t.getID()).getPix(xPix, yPix);
										mainViewer[xPos+ yPos*width] =rgb;
									}
								}
							}
						}
					}
				}
			}
		}

		public void draw(int[] mainViewer, Player who, double[] buffer) {
			//test(mainViewer,who,buffer);
			drawSkyBoxOutline(mainViewer, who);
		}



}
