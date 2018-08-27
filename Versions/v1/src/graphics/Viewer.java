/**
 * 
 */
package graphics;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import math.Matrix3;
import math.Vertex;
import objects.Shape;
import objects.Sphere;
import objects.Tetrahedron;
import objects.Triangle;

/**
 * @author Chris
 *
 */
public class Viewer extends JFrame {
	Dimension screen = new Dimension(800,800);   
	
	Tetrahedron tet = new Tetrahedron(100,100,100,screen);
	Sphere roundthing = new Sphere(100,screen);
    List<Shape> sprites = new ArrayList<>();

	/**
	 * @throws HeadlessException
	 */
	public Viewer() throws HeadlessException {
init();
	}

	/**
	 * @param arg0
	 */
	public Viewer(GraphicsConfiguration arg0) {
		super(arg0);
init();
	}

	/**
	 * @param arg0
	 * @throws HeadlessException
	 */
	public Viewer(String arg0) throws HeadlessException {
		super(arg0);
init();
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public Viewer(String arg0, GraphicsConfiguration arg1) {
		super(arg0, arg1);
init();
	}
	
	public Viewer(Dimension screenSize) {
		screen=screenSize;
init();
	}

	private void init() {
		sprites.add(roundthing);
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        // slider to control horizontal rotation
        JSlider headingSlider = new JSlider(-180, 180, 0);
        pane.add(headingSlider, BorderLayout.SOUTH);

        // slider to control vertical rotation
        JSlider pitchSlider = new JSlider(SwingConstants.VERTICAL, -180, 180, 0);
        pane.add(pitchSlider, BorderLayout.EAST);
		JPanel renderPanel = new JPanel() {
            public void paintComponent(Graphics g) {
            	
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());

                double heading = Math.toRadians(headingSlider.getValue());
                double pitch = Math.toRadians(pitchSlider.getValue());
                roundthing.setRotationXZ(heading);
                roundthing.setRotationYZ(pitch);
                BufferedImage img = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                //int[] pixels = new int[img.getWidth()*img.getHeight()];
                //img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());

                double[] zBuffer = new double[img.getWidth() * img.getHeight()];
                // initialize array with extremely far away depths
                for (int q = 0; q < zBuffer.length; q++) {
                    zBuffer[q] = Double.NEGATIVE_INFINITY;
                }
                for (Shape s : sprites) {
                	System.out.println("Drawing:"+s.getClass().getName());
                	s.draw(img, zBuffer);
                }
                //img.setRGB(0, 0, (int)screen.getWidth(), (int)screen.getHeight(), pixels, 0, (int)screen.getWidth());
                
                g2.drawImage(img, 0, 0, null);
            }
        };
    pane.add(renderPanel, BorderLayout.CENTER);

    headingSlider.addChangeListener(e -> renderPanel.repaint());
    pitchSlider.addChangeListener(e -> renderPanel.repaint());
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(screen);
    setVisible(true);
	}

}
