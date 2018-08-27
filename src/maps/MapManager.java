/**
 * 
 */
package maps;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import player.Player;

/**
 * @author Chris
 *
 */
public class MapManager {
	
	List<Map> places;
	private final Map Stagging = new SolarSystem();

	public MapManager() {
		places = new ArrayList<Map>();
	}
	
	public void tick() {
		// TODO Auto-generated method stub
		for(Map m : places) {
			m.tick();
		}
	}
	
	public void playerStart(Player who,int where) {
		// TODO BUILD MORE LEVELS
		if(where<=-1) {
			addPlayerToPlace(who,Stagging);
		}else if(where==0) {
			// 0 means random start
			// TODO Create MAPS
			addPlayerToPlace(who,Stagging);
		}else {
			// greater than 0 means a specific map
			// Maps still need to be created
			addPlayerToPlace(who,Stagging);
		}
	}

	private void addPlayerToPlace(Player who, Map where) {
		// TODO Auto-generated method stub
		if(places.contains(where)) {
			where.addPlayer(who.getID());
			
		}else {
			places.add(where);
			where.addPlayer(who.getID());
		}
	}

	public void draw(Player player, int[] mainViewer, double[] zBuffer) {
		for(Player p : player.peeps) {
			boolean foundPlayer = false;
			for(Map m : places) {
				if(m.isPeepInThisPlace(player.getID())) {
					m.drawMap(mainViewer, player, zBuffer);
					foundPlayer=true;
				}
			}
			System.out.println("Player "+player.getID()+": Found "+foundPlayer);
		}
	}

}
