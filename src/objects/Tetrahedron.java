package objects;

import java.awt.Color;
import java.awt.Dimension;

import math.Vertex;

public class Tetrahedron extends Shape {

	public Tetrahedron(int _height,int _width,int _depth, Dimension _screenSize) {
		super(_screenSize);
		buildTris();
	}

	/* (non-Javadoc)
	 * @see objects.Shape#buildTris()
	 */
	@Override
	protected void buildTris() {

    	System.out.println("Tetrahedron.buildTris()");
		getTris().add(new Triangle(new Vertex(100, 100, 100), new Vertex(-100, -100, 100), new Vertex(-100, 100, -100),
				Color.WHITE));
		getTris().add(new Triangle(new Vertex(100, 100, 100), new Vertex(-100, -100, 100), new Vertex(100, -100, -100),
				Color.RED));
		getTris().add(new Triangle(new Vertex(-100, 100, -100), new Vertex(100, -100, -100), new Vertex(100, 100, 100),
				Color.GREEN));
		getTris().add(new Triangle(new Vertex(-100, 100, -100), new Vertex(100, -100, -100), new Vertex(-100, -100, 100),
				Color.BLUE));
	}

}
