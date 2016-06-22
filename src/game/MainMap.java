package game;

import java.awt.Graphics;
import java.util.ArrayList;

import entities.Player;
import gamePlay.GrassMap;
import gamePlay.Map;

public class MainMap {

	private ArrayList<Map> maps;
	private int currentMap;
	private boolean inMap;
	
	private Player player;
	
	public MainMap(Player player) {
		
		this.player = player;
		maps = new ArrayList<Map>();
		maps.add(new GrassMap(player));
		inMap = true;
		currentMap = 0;
		
	}

	public void update(float deltas) {
		if(inMap){
			maps.get(currentMap).update(deltas);
		}else{
			
		}
	}

	public void draw(Graphics bufferGraphics) {
		if(inMap){
			maps.get(currentMap).draw(bufferGraphics);
		}else{
			
		}
	}

	public void drawGUI(Graphics bufferGraphics) {
		if(inMap){
			maps.get(currentMap).drawGUI(bufferGraphics);
		}else{
			
		}
	}
	
	
	
}
