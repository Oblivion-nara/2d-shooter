package gamePlay;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Enemy;
import entities.Player;
import helpers.ResourceLoader;
import mainGameLoop.Main;

public abstract class Map {

	protected BufferedImage background;
	protected ArrayList<Enemy> enemies;
	protected Player player;
	protected ArrayList<Rectangle> enemyLocations;
	protected ArrayList<Point2D.Double> enemyDrops;
	
	protected ArrayList<Point2D.Double> trajectories;
	protected ArrayList<Line2D.Double> shots;

	protected static float zoomlevel;
	protected int actualX, actualY;

	// base Wave stuff, wave 0 status
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

	protected void init(Player p) {

		player = p;
		player.location = new Point2D.Double(Main.width * 3 / 2, Main.height * 3 / 2);

		zoomlevel = 1;
		enemies = new ArrayList<Enemy>();
		enemyLocations = new ArrayList<Rectangle>();
		trajectories = new ArrayList<Point2D.Double>();
		shots = new ArrayList<Line2D.Double>();
		
		Image ground = ResourceLoader.getImage("trainingGround");
		
		background = new BufferedImage(ground.getWidth(null), ground.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = background.getGraphics();
		
		g.drawImage(ground,0,0,Main.width * 3, Main.height * 3,0,0,ground.getWidth(null), ground.getHeight(null), null);
		
//		g.setColor(new Color(0xdd, 0xa3, 0x3d));
//		g.fillRect(0, 0, Main.width, Main.height);
//		g.setColor(new Color(0x04, 0xac, 0x1c));
//		g.fillRect(0, 0, Main.width, 256);

//		Image shrub = ResourceLoader.getImage("shrub");
//		for (int i = 0; i < Main.random.nextInt(10) + 5; i++) {
//			g.drawImage(shrub, Main.random.nextInt(Main.width), Main.random.nextInt(Main.height), null);
//		}
//		Image house1 = ResourceLoader.getImage("house1");
//		Image house2 = ResourceLoader.getImage("house2");
//		int x = 0;
//		while (x < Main.width) {
//			if (Main.random.nextBoolean()) {
//				g.drawImage(house1, x, 128, null);
//				x += 256;
//			} else {
//				g.drawImage(house2, x, 0, null);
//				x += 512;
//			}
//		}
		wave = 0;
		waveEnded = true;

	}
	
	protected void spawn() {

		if (zombiesSpawned < zombiesPerRound && System.currentTimeMillis() >= spawnTimer
				&& enemies.size() < maxZombies) {
			Enemy e = new Enemy(player, zombieHealth);
			e.health = zombieHealth;
			enemies.add(e);
			enemyLocations.add(new Rectangle((int) enemies.get(enemies.indexOf(e)).getLocation().x - 16,
					(int) enemies.get(enemies.indexOf(e)).getLocation().y - 16, 32, 32));
			zombiesSpawned++;
			spawnTimer = System.currentTimeMillis() + (long) (1000f / zombiesPerSecond);

		}
		if (zombiesSpawned >= zombiesPerRound && enemies.size() == 0) {
			waveEnded = true;
		}
		/**
		 * legacy 
		 * if (waveDurationLeft > 0 && System.currentTimeMillis() >=
		 * waveTimer) { Enemy e = new Enemy(player); e.health = zombieHealth;
		 * enemies.add(e); enemyLocations.add(new Rectangle((int)
		 * enemies.get(enemies.indexOf(e)).getLocation().x - 16, (int)
		 * enemies.get(enemies.indexOf(e)).getLocation().y - 16, 32, 32));
		 * waveTimer += 1000 / zombiesPerSecond + 1; waveDurationLeft -= 1000 /
		 * zombiesPerSecond + 1; if (waveDurationLeft < 0) { if
		 * (zombiesPerSecond >= 5) zombiesPerSecond *= 1.1; else
		 * zombiesPerSecond++; zombieHealth *= 1.1; } } else if
		 * (waveDurationLeft <= 0 && !upgradesOpened &&
		 * !enemies.stream().anyMatch(e -> e.health >= 0)) { new
		 * Upgrades(player); upgradesOpened = true; }
		 */
	}
	
	protected void genPlayerPoint(Rectangle deadZombie) {
		if (Main.random.nextDouble() > 0.6) {
			player.points++;
		}
	}

	protected void zoom(float deltas) {

		if (Main.input.hasMouseWheelMoved()) {
			zoomlevel -= Main.input.getMouseWheelRotation() * deltas*2;
		}
		if (zoomlevel > 2) {
			zoomlevel = 2f;
		}
		if (zoomlevel < 0.5) {
			zoomlevel = 0.5f;
		}
		if (Main.input.isMouseDown(MouseEvent.BUTTON2)) {
			zoomlevel = 1;
		}
	}
	
	public void update(float deltas){
		
	}
	
	public void draw(Graphics g){
		
	}
	
	public void drawGUI(Graphics g){
		
	}
}















