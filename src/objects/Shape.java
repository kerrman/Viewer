package objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import math.*;

public class Shape{

	protected List<Triangle> tris = new ArrayList<>();
	protected static Dimension ScreenSize;
	// double[] zBuffer;// = new double[(int) (ScreenSize.getWidth() *
	// ScreenSize.getHeight())];
	private volatile Vertex center = new Vertex(0, 0, 0);
	private volatile Vertex lastLocation = new Vertex(0, 0, 0);
	volatile double rotXY = 0, rotXZ = 0, rotYZ = 0;
	protected volatile boolean hasChanged = false;
	public volatile Matrix3 rotation = new Matrix3(new double[] {1,0,0,0,1,0,0,0,1});

	public Shape(Dimension _screenSize) {
		ScreenSize = _screenSize;
		// zBuffer = new double[(int) (ScreenSize.getWidth() * ScreenSize.getHeight())];
		// TODO Auto-generated constructor stub
	}

	public List<Triangle> getTris() {
		return tris;
	}

	public void setTris(List<Triangle> tris) {
		this.tris = tris;
	}

	public Color getShade(Color color, double shade) {
		double redLinear = Math.pow(color.getRed(), 2.4) * shade;
		double greenLinear = Math.pow(color.getGreen(), 2.4) * shade;
		double blueLinear = Math.pow(color.getBlue(), 2.4) * shade;

		int red = (int) Math.pow(redLinear, 1 / 2.4);
		int green = (int) Math.pow(greenLinear, 1 / 2.4);
		int blue = (int) Math.pow(blueLinear, 1 / 2.4);

		return new Color(red, green, blue);
	}
	
	public void move(Vertex newLoc) {
		// tan = opp / adj
		double xy = Math.tan(newLoc.y/newLoc.x);
		double xz = Math.tan(newLoc.x/newLoc.z);
		double yz = Math.tan(newLoc.y/newLoc.z);
		move(newLoc,xy,xz,yz);
	}

	public void move(Vertex newLocation, double xy, double xz, double yz) {
		lastLocation = center;
		center.add(newLocation);
		rotXY+=xy;
		rotXZ+=xz;
		rotYZ+=yz;
		hasChanged=true;
	}

	protected void buildTris() {
		System.out.println("SHAPE.buildTris was called");
	}

	public synchronized void draw(BufferedImage img, double[] zBuffer) {
		System.out.println("Image width:"+img.getWidth()+" - Image Height"+img.getHeight());
		for (Triangle t : tris) {
			Vertex v1 = rotation.transform(t.v1);//t.v1.clone();
			Vertex v2 = rotation.transform(t.v2);//t.v2.clone();
			Vertex v3 = rotation.transform(t.v3);//t.v3.clone();
			v1.x += img.getWidth() / 2;
			v1.y += img.getHeight() / 2;
			v2.x += img.getWidth() / 2;
			v2.y += img.getHeight() / 2;
			v3.x += img.getWidth() / 2;
			v3.y += img.getHeight() / 2;
			

			double angleCos = Math.abs(t.getNorm().z);

            int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
            int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
            int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
            int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

			double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);
			//System.out.print("Start");
			for (int y = minY; y <= maxY; y++) {
				for (int x = minX; x <= maxX; x++) {
					double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
					double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
					double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
					if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
						double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
						int zIndex = y * img.getWidth() + x;
						if (zBuffer[zIndex] < depth) {
							//System.out.print("X:"+x+"-Y:"+y);
							img.setRGB(x, y, getShade(getColor(t,x,y), angleCos).getRGB());
							zBuffer[zIndex] = depth;
						}
					}
				}
			}//System.out.println("-END");

		}
	}
	protected
	Color getColor(Triangle tri,int x,int y) {
		return tri.getColor(x, y);
	}
	public void setRotationXY(double angle) {
		if(rotXY!=angle) {
		rotXY=angle;
		rotate();
		}
	}

	public void setRotationYZ(double angle) {
		if(rotYZ!=angle) {
		rotYZ=angle;
		rotate();
		}
	}

	public void setRotationXZ(double angle) {
		if(rotXZ!=angle) {
		rotXZ=angle;
		rotate();
		}
	}
	private void rotate() {
		hasChanged=true;
		rotation = Matrix3.rotateXY(rotXY).multiply(Matrix3.rotateXZ(rotXZ)).multiply(Matrix3.rotateYZ(rotYZ));
	}

	public void setScreenSize(Dimension screen) {
		this.ScreenSize=screen;
	}
}