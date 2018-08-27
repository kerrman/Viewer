/**
 * 
 */
package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import graphics.*;
import objects.*;
import player.Player;
import player.SinglePlayer;

/**
 * @author Chris
 *
 */
public class Startup implements Runnable{
	
	//thread control
	public volatile Thread thread; // used to turn Startup into a thread
	private volatile boolean running;
	
	// TODO build sprites and more complex objects and manager
	protected static volatile ObjectManager obj; //manages objects
	//LogicManager logic;

	//TODO build UDP viewer
	protected static volatile Viewer window; // Draws pixel for player(s) screen(s) - need to build multi-player UDP screens
	
	//TODO build TCP player
	protected static volatile Player player; // Get player(s) data like keys - need to build multi-player TCP server/client player object
	
	//Screen Dimensions for game hardware - multi-player will need screen dimensions per player connect
	public static final Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * 
	 */
	public Startup() {
		// TODO Auto-generated constructor stub
		init();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Startup();
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		final double ns = 1000000000.0 / 60.0;// 60 times per second
		double delta = 0;
		window.requestFocus();
		while (running) {
			long now = System.nanoTime();
			delta = delta + ((now - lastTime) / ns);
			lastTime = now;
			while (delta >= 1)// Make sure update is only happening 60 times a second
			{
				// handles all of the logic - restricted time
				player.tick();
				obj.tick();
				window.draw(player,obj);
				delta--;
			}
			// TODO replace single player view with multicast UDP
			window.updateGraphics();// displays to the screen unrestricted time
		}
	}
	
	private void init() {
		obj = new ObjectManager(ScreenSize);
		window = new Viewer();
		player = new SinglePlayer(ScreenSize);
		System.out.println("Startup() has been called");
		thread = new Thread(this);
		window.addKeyListener(player);
		window.setSize(ScreenSize);
		window.setResizable(false);
		window.setTitle("3D Engine");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setBackground(Color.black);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		start();
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

}
