package objects;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import math.*;

public class Shape {

	protected List<Triangle> tris = new ArrayList<>();
	Dimension ScreenSize;
	// double[] zBuffer;// = new double[(int) (ScreenSize.getWidth() *
	// ScreenSize.getHeight())];
	Vertex center = new Vertex(0, 0, 0);
	Vertex lastLocation = new Vertex(0, 0, 0);
	double rotXY = 0, rotXZ = 0, rotYZ = 0;
	private Matrix3 rotation;

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

	public void move(Vertex newLocation) {
		lastLocation = center;
		center = newLocation;
		buildTris();
	}

	protected void buildTris() {
		System.out.println("SHAPE.buildTris was called");
	}

	public void draw(BufferedImage img, double[] zBuffer) {
		System.out.println("Image width:"+img.getWidth()+" - Image Height"+img.getHeight());
		for (Triangle t : tris) {
			Vertex v1 = t.v1.clone();
			Vertex v2 = t.v2.clone();
			Vertex v3 = t.v3.clone();
			v1.x += img.getWidth() / 2;
			v1.y += img.getHeight() / 2;
			v2.x += img.getWidth() / 2;
			v2.y += img.getHeight() / 2;
			v3.x += img.getWidth() / 2;
			v3.y += img.getHeight() / 2;

			Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
			Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
			Vertex norm = new Vertex(ab.y * ac.z - ab.z * ac.y, ab.z * ac.x - ab.x * ac.z, ab.x * ac.y - ab.y * ac.x);
			double normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
			norm.x /= normalLength;
			norm.y /= normalLength;
			norm.z /= normalLength;

			double angleCos = Math.abs(norm.z);

			int minX = (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
			int maxX = (int) Math.min(img.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x))));
			int minY = (int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y))));
			int maxY = (int) Math.min(img.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y))));

			double triangleArea = (v1.y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - v1.x);

			for (int y = minY; y <= maxY; y++) {
				for (int x = minX; x <= maxX; x++) {
					double b1 = ((y - v3.y) * (v2.x - v3.x) + (v2.y - v3.y) * (v3.x - x)) / triangleArea;
					double b2 = ((y - v1.y) * (v3.x - v1.x) + (v3.y - v1.y) * (v1.x - x)) / triangleArea;
					double b3 = ((y - v2.y) * (v1.x - v2.x) + (v1.y - v2.y) * (v2.x - x)) / triangleArea;
					if (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) {
						double depth = b1 * v1.z + b2 * v2.z + b3 * v3.z;
						int zIndex = y * img.getWidth() + x;
						if (zBuffer[zIndex] < depth) {
							img.setRGB(x, y, getShade(getColor(t,x,y), angleCos).getRGB());
							zBuffer[zIndex] = depth;
						}
					}
				}
			}

		}
	}
	protected
	Color getColor(Triangle tri,int x,int y) {
		return tri.color;
	}
	public void setRotationXY(double angle) {
		if(rotXY!=angle) {
		rotXY=angle;
		rotate(Matrix3.rotateXY(angle));
		}
	}

	public void setRotationYZ(double angle) {
		if(rotYZ!=angle) {
		rotYZ=angle;
		rotate(Matrix3.rotateXY(angle));
		}
	}

	public void setRotationXZ(double angle) {
		if(rotYZ!=angle) {
		rotXZ=angle;
		rotate(Matrix3.rotateXY(angle));
		}
	}
	private void rotate(Matrix3 transform) {
		if(rotation==null) {
			rotation=transform;
		}else {
			rotation.multiply(transform);
		}
	}
}