/**
 * 
 */
package objects;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import graphics.Texture;
import math.Vertex;

/**
 * @author Chris
 *
 */
public class Sphere extends Tetrahedron {

	Texture holoDeck = new Texture("DEF", 8);
	//int minX = 0, maxX = 0, minY = 0, maxY = 0;
	private int pixX = 0;
	private int pixY = 0;
	private int radius = 0;
	private int[] pixels;

	/**
	 * 
	 */
	public Sphere(int _radius, Dimension _screenSize) {
		super(_radius, _radius, _radius, _screenSize);
		radius = _radius;
		for (int i = 0; i < 4; i++) {
			tris = inflate(tris);
		}
		// TODO setTexture();
	}

	public List<Triangle> inflate(List<Triangle> _tris) {
		System.out.print("Sphere.inflate()");
		List<Triangle> result = new ArrayList<>();
		for (Triangle t : _tris) {
			Vertex m1 = new Vertex((t.v1.x + t.v2.x) / 2, (t.v1.y + t.v2.y) / 2, (t.v1.z + t.v2.z) / 2);
			Vertex m2 = new Vertex((t.v2.x + t.v3.x) / 2, (t.v2.y + t.v3.y) / 2, (t.v2.z + t.v3.z) / 2);
			Vertex m3 = new Vertex((t.v1.x + t.v3.x) / 2, (t.v1.y + t.v3.y) / 2, (t.v1.z + t.v3.z) / 2);
			result.add(new Triangle(t.v1, m1, m3, t.color));
			result.add(new Triangle(t.v2, m1, m2, t.color));
			result.add(new Triangle(t.v3, m2, m3, t.color));
			result.add(new Triangle(m1, m2, m3, t.color));
		}
	/*	minX = (int) center.x;
		maxX = (int) center.x;
		minY = (int) center.y;
		maxY = (int) center.y;
	*/	for (Triangle t : result) {
			int tempXstart = 0, tempXend = 0, tempYstart = 0, tempYend = 0;
			for (Vertex v : new Vertex[] { t.v1, t.v2, t.v3 }) {
				double l = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z) / Math.sqrt(30000);
				v.x /= l;
				v.y /= l;
				v.z /= l;
				if (v == t.v1) {
					tempXstart = (int) t.v1.x;
					tempXend = (int) t.v1.x;
					tempYstart = (int) t.v1.y;
					tempXend = (int) t.v1.y;
				}
	/*			minX = (int) Math.min(minX, v.x);
				maxX = (int) Math.max(maxX, v.x);
				minY = (int) Math.min(minY, v.y);
				maxY = (int) Math.max(maxY, v.y);
	*/			tempXstart = (int) Math.ceil(Math.min(tempXstart, v.x));
				tempXend = (int) Math.floor(Math.max(tempXend, v.x));
				tempYstart = (int) Math.ceil(Math.min(tempYstart, v.y));
				tempYend = (int) Math.floor(Math.max(tempYstart, v.y));
			}
			t.setMinMax(tempYstart, tempXend, tempYstart, tempYend);

		}
		// double rad = maxX-minX;
		/*
		 * for (Triangle t : tris) { for (Vertex v : new Vertex[] { t.v1, t.v2, t.v3 })
		 * { int location = 0; for (int yDraw = t.xMin,y=0; yDraw < t.xMax; yDraw++,y++)
		 * { for (int xDraw = t.yMin,x=0; xDraw < t.yMax; xDraw++,x++) { double tempY=
		 * Math.sin(Math.abs((yDraw/maxY)*90))*Math.abs(maxY); double tempX=
		 * Math.cos(Math.abs((xDraw/maxX)*90))*Math.abs(maxX); int locX = (int)tempX &
		 * 0x08; int locY = (int)(tempY * radius)& 64; location = (locX + locY) ;
		 * System.out.println("locX:" + locX + " - locY:" + locY);
		 * System.out.println("xDraw:" + xDraw + " - tempX:" + tempX);
		 * System.out.println("yDraw:" + yDraw + " - tempY:" + tempY); Color tempColor =
		 * new Color(holoDeck.getPix(location)); t.setColor(tempColor, (x), (y)); } }
		 * 
		 * } }
		 */
		//System.out.println("minX:" + minX + " - maxX:" + maxX);
		//6System.out.println("minY:" + minY + " - maxY:" + maxY);
		return result;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see objects.Shape#getColor(objects.Triangle, int, int)
	 */
	
	/*
	protected void setTexture(int x, int y) {
		int loc = 0;
		int lengthX=0,lengthY=0;
		
		// TODO fix - change to radius after constructor builds based on radius
		if(maxX>=0) {
			lengthX=maxX-minX;
		}else {
			lengthX=minX-maxX;
		}
		radius = (int) (lengthX * Math.PI * 2);
		/*
		if(maxX>=0) {
			lengthX=maxX-minX;
		}else {
			lengthX=minX-maxX;
		}*/
	/*System.out.println("lengthX:"+lengthX+" - lengthY:"+lengthY);
		pixels=new int[lengthX*lengthY];
		int newX = (int) (Math.sin((x / maxX) * 90)) & holoDeck.SIZE;
		int newY = (int) (Math.cos(y / maxY)) & holoDeck.SIZE;
		loc = (int) (newX + newY * holoDeck.SIZE);
		pixels[x + y * radius] = holoDeck.getPix(loc);
	}*/

	/*
	 * Vertex v1 = t.v1.clone(); Vertex v2 = t.v2.clone(); Vertex v3 = t.v3.clone();
	 * v1.x += ScreenSize.getWidth() / 2; v1.y += ScreenSize.getHeight() / 2; v2.x
	 * += ScreenSize.getWidth() / 2; v2.y += ScreenSize.getHeight() / 2; v3.x +=
	 * ScreenSize.getWidth() / 2; v3.y += ScreenSize.getHeight() / 2;
	 * 
	 * minX = (int) Math.min(minX, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x))));
	 * maxX = (int) Math.max(maxX, Math.floor(Math.max(v1.x, Math.max(v2.x,
	 * v3.x)))); minY = (int) Math.min(minY, Math.ceil(Math.min(v1.y, Math.min(v2.y,
	 * v3.y)))); maxY = (int) Math.max(maxY, Math.floor(Math.max(v1.y,
	 * Math.max(v2.y, v3.y))));
	 * 
	 * pixX = Math.abs(maxX-minX); pixY = Math.abs(maxY-minY);
	 */
	/*public int findColor(int _x, int _y) {
		int rad = 0;
		int y = 0;
		short xNeg = 1, yNeg = 1;
		int ret = 0;
		if (_x < 0) {
			xNeg = -1;
			_x = Math.abs(_x);
		}
		if (_y < 0) {
			yNeg = -1;
			_y = Math.abs(_x);
		}
		rad = ((int) Math.sqrt(Math.pow(_x, 2) + Math.pow(_y, 2))) & 0x8;
		y = _y & 0x8;
		ret = holoDeck.getPix(rad + y * 7);
		return ret;

	}*/
	/*
	 * private void setTexture() { for (Triangle t : tris) {
	 * System.out.println("New Triangle"); double triangleArea = (t.v1.y - t.v3.y) *
	 * (t.v2.x - t.v3.x) + (t.v2.y - t.v3.y) * (t.v3.x - t.v1.x); //
	 * System.out.print("Start"); for (int yDraw = t.yMin, y = 0; yDraw <= t.yMax;
	 * yDraw++, y++) { for (int xDraw = t.xMin, x = 0; xDraw <= t.xMax; x++,
	 * xDraw++) { double b1 = ((y - t.v3.y) * (t.v2.x - t.v3.x) + (t.v2.y - t.v3.y)
	 * * (t.v3.x - x)) / triangleArea; double b2 = ((y - t.v1.y) * (t.v3.x - t.v1.x)
	 * + (t.v3.y - t.v1.y) * (t.v1.x - x)) / triangleArea; double b3 = ((y - t.v2.y)
	 * * (t.v1.x - t.v2.x) + (t.v1.y - t.v2.y) * (t.v2.x - x)) / triangleArea; if
	 * (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1) { double
	 * depth = b1 * t.v1.z + b2 * t.v2.z + b3 * t.v3.z; int zIndex = y *
	 * t.pixelWidth + x; System.out.println("zBuffer.length:" + zBuffer.length);
	 * System.out.println("xDraw:" + xDraw + " - yDraw:" + yDraw); if
	 * (zBuffer[zIndex] < depth) { Color tempColor = new Color(findColor(x, y));
	 * System.out.print("X:"+x+"-Y:"+y); t.setColor(tempColor, x, y);
	 * zBuffer[zIndex] = depth; } } } } } }
	 */

}
