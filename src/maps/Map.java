/**
 * 
 */
package maps;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import graphics.*;
import math.*;
import objects.Triangle;
import player.Player;

/**
 * @author Chris
 *
 */
public class Map {

	SkyCube sky;

	private volatile List<Integer> peeps;

	public Map() {
		init();
	}

	private void init() {
		peeps = new ArrayList<Integer>();
		sky = new SkyCube();
	}
	public void drawMap(int[] mainViewer, Player who, double[] buffer) {
		drawEnvironment(mainViewer, who, buffer);
			sky.draw(mainViewer, who,buffer);
	}
/*
	private int getSkyColor(int whichMap, int x, int y) {
		// TODO Auto-generated method stub
		int ret = sky.get(whichMap).getPix(x, y);
		return 0;
	}
*/
	// draw non object things (i.e. for both) - Planet has terrain or buildings -
	// SolarSystems have random comets
	private void drawEnvironment(int[] mainViewer, Player who, double[] buffer) {
		// TODO MAP#drawEnvironmental(canvas,who)
		// Base map has no environmental draw this method is created as an interface or
		// place holder and is to be overridden by the classes that extend
	}

	public void tick() {
		// TODO MAP#tick() - create environmental list
		// For now base map has no tick - environmental list needs creating and those
		// can then be acted upon
	}

	public void addPlayer(int id) {
		// TODO Auto-generated method stub
		peeps.add(id);
	}

	public boolean isPeepInThisPlace(int id) {
		if (peeps.contains(id)) {
			return true;
		}
		return false;
	}

	}
