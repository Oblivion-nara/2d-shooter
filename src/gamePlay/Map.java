package gamePlay;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Enemy;
import entities.Player;

public class Map {

	protected BufferedImage background;
	protected ArrayList<Enemy> enemies;
	protected Player player;
	protected ArrayList<Rectangle> enemyLocations;
	protected ArrayList<Point2D.Double> enemyDrops;
	
	protected ArrayList<Point2D.Double> trajectories;
	protected ArrayList<Line2D.Double> shots;

	public static float zoomlevel;

	// base Wave stuff, wave 0 stats
	protected float baseZombiesPerSecond = 0.5f;
	protected long spawnTimer = 0;
	protected int wave = 0, maxZombies = 20, baseZombiesPerRound = 5, baseZombieHealth = 91;
	protected boolean waveEnded;

	// current wave stuff
	protected int zombiesPerRound = 0, zombieHealth = 0, zombiesSpawned = 0;
	protected float zombiesPerSecond = 0;

	// shooting will be in the gun class eventually
	protected long shotsound = 0;
	protected long shottimer = 100;
	
	public Map() { 
	}
	
	public void update(float deltas){
		
	}
	
	public void draw(Graphics g){
		
	}
	
	public void drawGUI(Graphics g){
		
	}
}















