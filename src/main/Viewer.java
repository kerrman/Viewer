/**
 * 
 */
package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import maps.MapManager;
import math.Matrix3;
import math.Vertex;
import objects.ObjectManager;
import player.Player;
import player.SinglePlayer;
import protoArea.*;

/**
 * @author Chris
 *
 */
public class Viewer extends JFrame implements Runnable{

	// Draw area for objects and player HUD
	private volatile int[] mainViewer;
	//volatile BufferedImage img; 
	// TODO designate HUD and other player areas
	// Main texture for background
	// volatile Texture sky;
	// Section of sky to base load img with - smaller section of sky, what is
	// actually visibale based on rotation
	// volatile BufferedImage backDrop;
	volatile double[] zBuffer;


	// thread control
	public volatile Thread thread; // used to turn Startup into a thread
	private volatile boolean running;

	// TODO build sprites and more complex objects and manager
	protected static volatile ObjectManager obj; // manages objects
	// LogicManager logic;

	// TODO build TCP player
	protected static volatile Player player; // Get player(s) data like keys - need to build multi-player TCP
												// server/client player object
	
	// TODO build maps
	protected static volatile MapManager map;

	// Screen Dimensions for game hardware - multi-player will need screen
	// dimensions per player connect
	public static final Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

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
	
	private void blankCanvas() {
		BufferedImage img = new BufferedImage((int) ScreenSize.getWidth(), (int) ScreenSize.getHeight(), BufferedImage.TYPE_INT_RGB);
		mainViewer = new int[img.getHeight()*img.getWidth()];
		img.getRGB(0, 0, (int)ScreenSize.getWidth(),(int)ScreenSize.getHeight(), mainViewer, 0, (int)ScreenSize.getWidth());
		zBuffer = new double[img.getWidth() * img.getHeight()];

		// initialize array with extremely far away depths
		for (int q = 0; q < zBuffer.length; q++) {
			zBuffer[q] = Double.NEGATIVE_INFINITY;
		}
	}


	private void init() {
		System.out.println("Viewer#init() has been called");
		obj = new ObjectManager(ScreenSize);
		player = new SinglePlayer(ScreenSize);
		addKeyListener(player);
		map = new MapManager();
		map.playerStart(player, -1);
		thread = new Thread(this);
		Container pane = getContentPane();
		pane.setLayout(new BorderLayout());
		blankCanvas();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setBackground(Color.black);
		setAlwaysOnTop(true);
		setUndecorated(true);
		setPreferredSize(ScreenSize);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("3D Engine");
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		start();
	}

	public synchronized void draw(Player player, ObjectManager objects) {
		blankCanvas();
		map.draw(player,mainViewer,zBuffer);
		//repaint();
	}

	public synchronized void updateGraphics() {
		// TODO Single Player repaint() multi player UPD.send()
		this.repaint();
	}

	public BufferedImage deepCopy(BufferedImage bi) {
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;// 60 times per second
		double delta = 0;
		requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta = delta + ((now - lastTime) / ns);
			lastTime = now;
			while (delta >= 1)// Make sure update is only happening 60 times a second
			{
				// handles all of the logic - restricted time
				player.tick();
				obj.tick();
				map.tick();
				draw(player,obj);
				delta--;
				render();
			}
			// TODO replace single player view with multicast UDP
			//updateGraphics();// displays to the screen unrestricted time
		}
	}

	private synchronized void start() {
		running = true;
		thread.start();
	}

	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Viewer();
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			return;
		}
		BufferedImage img = new BufferedImage((int) ScreenSize.getWidth(),(int)ScreenSize.getHeight(),BufferedImage.TYPE_INT_RGB);
		img.setRGB(0, 0,(int) ScreenSize.getWidth(),(int)ScreenSize.getHeight(),  mainViewer, 0, (int) ScreenSize.getWidth());
		Graphics g = bs.getDrawGraphics();
		g.drawImage(img, 0, 0, img.getWidth(), img.getHeight(), null);
		bs.show();
	}

}
