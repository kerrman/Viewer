package math;

public class Vertex {

    public double x;
    public double y;
    public double z;

	public Vertex(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vertex clone() {
		return new Vertex(x,y,z);
	}
	public void add(Vertex newLocation) {
		this.x+=newLocation.x;		
		this.y+=newLocation.y;		
		this.y+=newLocation.y;		
	}
}