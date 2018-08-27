package objects;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import math.Matrix3;

public class ObjectManager {

	Tetrahedron tet;// = new Tetrahedron(100,100,100,screen);
	Sphere roundthing;// = new Sphere(100,screen);
	List<Shape> sprites = new ArrayList<>();
	private Matrix3 rotation;
	private double rotXZ;
	private double rotYZ;
	private double rotXY;

	public ObjectManager(Dimension screen) {
		init(screen);
	}

	private void init(Dimension screen) {
		roundthing = new Sphere(100, screen);
		tet = new Tetrahedron(100, 100, 100, screen);
		sprites.add(roundthing);
	}

	/*public void repaint(Dimension screen) {

	}*/

	public synchronized void setRotationXY(double angle) {
		if (rotXY != angle) {
			rotXY = angle;
			for (Shape s : sprites) {
				s.setRotationXY(angle);
			}
		}
	}

	public synchronized void setRotationYZ(double angle) {
		if (rotYZ != angle) {
			rotYZ = angle;
			for (Shape s : sprites) {
				s.setRotationYZ(angle);
			}
		}
	}

	public synchronized void setRotationXZ(double angle) {
		if (rotXZ != angle) {
			rotXZ = angle;
			for (Shape s : sprites) {
				s.setRotationXZ(angle);
			}
		}
	}

	public synchronized void draw(BufferedImage img, double[] zBuffer) {
		for (Shape s : sprites) {
			System.out.println("Drawing:" + s.getClass().getName());
			s.draw(img, zBuffer);
		}
	}

	public void setScreenSize(Dimension screen) {
		for (Shape s : sprites) {
			s.setScreenSize(screen);
		}
	}

	public synchronized void tick() {
		// TODO Auto-generated method stub
		
	}
}
