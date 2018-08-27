/**
 * 
 */
package main;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.List;

import graphics.Viewer;
import objects.Sphere;
import objects.Tetrahedron;
import objects.Triangle;

/**
 * @author Chris
 *
 */
public class Startup {

	public static Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

    Tetrahedron tet = new Tetrahedron(100,100,100,ScreenSize);


	/**
	 * 
	 */
	public Startup() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Viewer window = new Viewer(ScreenSize);
	}

}
