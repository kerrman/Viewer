/**
 * 
 */
package objects;

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
	
	Texture holoDeck = new Texture("DEF",8);

	/**
	 * 
	 */
	public Sphere(int radius,Dimension _screenSize) {
		super(radius,radius,radius,_screenSize);
		for (int i = 0; i < 4; i++) {
			tris = inflate(tris);
		}
	}

    public List<Triangle> inflate(List<Triangle> _tris) {
    	System.out.print("Sphere.inflate()");
        List<Triangle> result = new ArrayList<>();
        for (Triangle t : _tris) {
            Vertex m1 = new Vertex((t.v1.x + t.v2.x)/2, (t.v1.y + t.v2.y)/2, (t.v1.z + t.v2.z)/2);
            Vertex m2 = new Vertex((t.v2.x + t.v3.x)/2, (t.v2.y + t.v3.y)/2, (t.v2.z + t.v3.z)/2);
            Vertex m3 = new Vertex((t.v1.x + t.v3.x)/2, (t.v1.y + t.v3.y)/2, (t.v1.z + t.v3.z)/2);
            result.add(new Triangle(t.v1, m1, m3, t.color));
            result.add(new Triangle(t.v2, m1, m2, t.color));
            result.add(new Triangle(t.v3, m2, m3, t.color));
            result.add(new Triangle(m1, m2, m3, t.color));
        }
        for (Triangle t : result) {
            for (Vertex v : new Vertex[] { t.v1, t.v2, t.v3 }) {
                double l = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z) / Math.sqrt(30000);
                v.x /= l;
                v.y /= l;
                v.z /= l;
            }
        }
        return result;
    }


}
