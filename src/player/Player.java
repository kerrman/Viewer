package player;

import java.awt.Dimension;
import java.awt.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.BitSet;

import graphics.Texture;
import math.Matrix3;
import objects.Shape;

public class Player implements KeyListener {

	public ArrayList<Player> peeps;
	private BitSet keys;
	// used to assign player types as well as track which player it is
	// negative values are administrative or GM
	protected int playerID = 0; // default ID as well as singlePlayer
	protected volatile double rotX = 0, rotY = 0;
	public volatile Matrix3 rotation = new Matrix3(new double[] { 1, 0, 0, 0, 1, 0, 0, 0, 1 });

	// used for cheats
	StringBuilder code = new StringBuilder();
	private Dimension ScreenSize;
	private int mapID = 0;

	public Player(Dimension screen) {
		peeps = new ArrayList<Player>();
		keys = new BitSet(256);
		setScreenSize(screen);
		init();
	}

	@Override
	public void keyPressed(final KeyEvent event) {
		int keyCode = event.getKeyCode();
		keys.set(keyCode);
		if (keyCode == KeyEvent.VK_ESCAPE) {
			System.exit(0);
		}
	}

	@Override
	public void keyReleased(final KeyEvent event) {
		int keyCode = event.getKeyCode();
		keys.clear(keyCode);
	}

	@Override
	public void keyTyped(KeyEvent k) {
		// TODO Auto-generated method stub
		//code.append(k.getKeyChar());
		//System.out.println(code.toString());
	}

	public void tick() {
		boolean rot = false;
		if (isKeyPressed(KeyEvent.VK_A)) {
			rotX -= 2;
			rot = true;
		}
		if (isKeyPressed(KeyEvent.VK_D)) {
			rotX += 2;
			rot = true;
		}
		if (isKeyPressed(KeyEvent.VK_W)) {
			rotY += 2;
			rot = true;
		}
		if (isKeyPressed(KeyEvent.VK_S)) {
			rotY -= 2;
			rot = true;
		}
		if (rot) {
			rotate();
		}
	}

	protected void init() {
		peeps.add(this);
	}

	public void setScreenSize(Dimension screen) {
		ScreenSize = screen;
	}

	public int getMapID() {
		return mapID;
	}

	public int getID() {
		return playerID;
	}

	public boolean isKeyPressed(final int keyCode) {
		return keys.get(keyCode);
	}

	private void rotate() {
		if (rotX >= 360) {
			rotX -= 360;
		}
		if (rotY >= 360) {
			rotY -= 360;
		}
		if (rotX < 0) {
			rotX += 360;
		}
		if (rotY < 0) {
			rotY += 360;
		}
		rotation = Matrix3.rotateXZ(rotX * Math.PI / 180)
				.multiply(Matrix3.rotateYZ(rotY * Math.PI / 180));
	}

	public int rotXpercent() {
		return (int) ((rotX*100)/360);
	}

	public int rotYpercent() {
		return (int) ((rotY*100)/360);
	}

	/**
	 * @return the screenSize
	 */
	public Dimension getScreenSize() {
		return ScreenSize;
	}
}
