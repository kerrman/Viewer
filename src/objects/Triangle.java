package objects;

import java.awt.Color;
import java.awt.Dimension;

import main.Startup;
import math.Vertex;

public class Triangle {
	public Vertex v1;
	public Vertex v2;
	public Vertex v3;
	public int color;
	private Vertex norm;
	private double normalLength;
	byte[] vertexCoordinant;
	int xMin;
	int xMax;
	int yMin;
	int yMax;

	public Triangle(Vertex v1, Vertex v2, Vertex v3, int color) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = color;
		vertexCoordinant = new byte[]{1,2,3};
		setNorm();
	}
	public Triangle(Vertex v1, Vertex v2, Vertex v3, byte[] vCord) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = 0;
		vertexCoordinant = vCord;
		setNorm();
	}

	public Triangle(Vertex v1, Vertex v2, Vertex v3, Color color) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = color.getRGB();
		vertexCoordinant = new byte[] {1,2,3};
		setNorm();
	}
	public Triangle move(Vertex newLocation) {
		return this;
	}

	private void setNorm() {
		Vertex ab = new Vertex(v2.x - v1.x, v2.y - v1.y, v2.z - v1.z);
		Vertex ac = new Vertex(v3.x - v1.x, v3.y - v1.y, v3.z - v1.z);
		norm = new Vertex(ab.y * ac.z - ab.z * ac.y, ab.z * ac.x - ab.x * ac.z, ab.x * ac.y - ab.y * ac.x);
		normalLength = Math.sqrt(norm.x * norm.x + norm.y * norm.y + norm.z * norm.z);
		norm.x /= normalLength;
		norm.y /= normalLength;
		norm.z /= normalLength;
	}

	public Vertex getNorm() {
		return norm;
	}

	public void setNorm(Vertex norm) {
		this.norm = norm;
	}

	public int[] getMinMax(Dimension _screenSize) {
		return new int[] { (int) Math.max(0, Math.ceil(Math.min(v1.x, Math.min(v2.x, v3.x)))),
				(int) Math.min(_screenSize.getWidth() - 1, Math.floor(Math.max(v1.x, Math.max(v2.x, v3.x)))),
				(int) Math.max(0, Math.ceil(Math.min(v1.y, Math.min(v2.y, v3.y)))),
				(int) Math.min(_screenSize.getHeight() - 1, Math.floor(Math.max(v1.y, Math.max(v2.y, v3.y)))) };
	}


	public void setMinMax(int _xMin, int _xMax, int _yMin, int _yMax) {
		xMin = _xMin;
		xMax = _xMax;
		yMin = _yMin;
		yMax = _yMax;
	}

	public Color getColor(int x, int y) {
		return new Color(color);
	}
	public int getID() {
		return color;
	}
	public Vertex getAngleV() {
		if(vertexCoordinant[1]==1) {
			return v1.clone();
		}
		else if(vertexCoordinant[1]==2) {
			return v2.clone();
		}
		else if(vertexCoordinant[1]==3) {
			return v3.clone();
		}else {
			System.out.println("Vertex assignment is incorrect");
			return v1.clone();
		}
	}
	public Vertex getDrawV()  {
		if(vertexCoordinant[0]==1) {
			return v1.clone();
		}
		else if(vertexCoordinant[0]==2) {
			return v2.clone();
		}
		else if(vertexCoordinant[0]==3) {
			return v3.clone();
		}else {
			System.out.println("Vertex assignment is incorrect");
			return v2.clone();
		}
	}
	public Vertex getV(byte b)  {
		if(vertexCoordinant[b]==1) {
			return v1.clone();
		}
		else if(vertexCoordinant[b]==2) {
			return v2.clone();
		}
		else if(vertexCoordinant[b]==3) {
			return v3.clone();
		}else {
			System.out.println("Vertex assignment is incorrect");
			return v3.clone();
		}
	}
}
