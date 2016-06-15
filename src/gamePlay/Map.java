package gamePlay;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Enemy;
import entities.Player;
import helpers.MathHelper;
import helpers.ResourceLoader;
import mainGameLoop.Main;

public class Map {

	private BufferedImage background;
	private ArrayList<Enemy> enemies;
	private Player player;
	private ArrayList<Rectangle> enemyLocations;

	private ArrayList<Point2D.Double> trajectories;
	private ArrayList<Line2D.Double> shots;

	private float healthRatio;

	// base Wave stuff
	private static int wave = 0;
	private float baseZombiesPerSecond = 0.5f;
	private long spawnTimer = 0;
	private int baseMaxZombies = 20;
	private int baseZombiesPerRound = 20;
	private int baseZombieHealth = 100;
	private static boolean upgradesOpened = false;
	
	// current wave stuff
	private int maxZombies;
	private int zombiesPerRound;
	private int zombieHealth;
	private int zombiesSpawned;
	
	//shooting
	private long shotsound = 0;
	private long shottimer = 100;


	public Map() {
		init();
	}

	private void init() {

		player = new Player();

		enemies = new ArrayList<Enemy>();
		enemyLocations = new ArrayList<Rectangle>();
		trajectories = new ArrayList<Point2D.Double>();
		shots = new ArrayList<Line2D.Double>();
		background = new BufferedImage(Main.width, Main.height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = background.getGraphics();
		g.setColor(new Color(0xdd, 0xa3, 0x3d));
		g.fillRect(0, 0, Main.width, Main.height);
		g.setColor(new Color(0x04, 0xac, 0x1c));
		g.fillRect(0, 0, Main.width, 256);
		
		Image shrub = ResourceLoader.getImage("shrub.png");
		for (int i = 0; i < Main.random.nextInt(10) + 5; i++) {
			g.drawImage(shrub, Main.random.nextInt(Main.width), Main.random.nextInt(Main.height), null);
		}
		Image house1 = ResourceLoader.getImage("house1.png");
		Image house2 = ResourceLoader.getImage("house2.png");
		int x = 0;
		while (x < Main.width) {
			if (Main.random.nextBoolean()) {
				g.drawImage(house1, x, 128, null);
				x += 256;
			} else {
				g.drawImage(house2, x, 0, null);
				x += 512;
			}
		}
		wave = 1;

	}

	private void spawn() {
		
		int maxZombies = (int)Math.pow(baseMaxZombies,wave);
		
		if(zombiesSpawned >= maxZombies && System.currentTimeMillis() >= spawnTimer){
			Enemy e = new Enemy(player, zombieHealth);
			e.health = zombieHealth;
			enemies.add(e);
			enemyLocations.add(new Rectangle((int) enemies.get(enemies.indexOf(e)).getLocation().x - 16,
					(int) enemies.get(enemies.indexOf(e)).getLocation().y - 16, 32, 32));
			zombiesSpawned++;
			spawnTimer = System.currentTimeMillis() + (1000/zombiesPerRound);
			
		}
		
//		if (waveDurationLeft > 0 && System.currentTimeMillis() >= waveTimer) {
//			Enemy e = new Enemy(player);
//			e.health = zombieHealth;
//			enemies.add(e);
//			enemyLocations.add(new Rectangle((int) enemies.get(enemies.indexOf(e)).getLocation().x - 16,
//					(int) enemies.get(enemies.indexOf(e)).getLocation().y - 16, 32, 32));
//			waveTimer += 1000 / zombiesPerSecond + 1;
//			waveDurationLeft -= 1000 / zombiesPerSecond + 1;
//			if (waveDurationLeft < 0) {
//				if (zombiesPerSecond >= 5)
//					zombiesPerSecond *= 1.1;
//				else
//					zombiesPerSecond++;
//				zombieHealth *= 1.1;
//			}
//		} else if (waveDurationLeft <= 0 && !upgradesOpened && !enemies.stream().anyMatch(e -> e.health >= 0)) {
//			new Upgrades(player);
//			upgradesOpened = true;
//		}
	}

	public void startWaveX(int waveNumber){
		
	}
	
	public static void nextWave() {
		wave++;
		
		upgradesOpened = false;
	}

	private void shoot(Point mouseCoord) {
		if (System.currentTimeMillis() > shottimer) {
			double accuracy = 1.0 / player.accuracy;
			shots.add(new Line2D.Double(player.location.x, player.location.y, player.location.x, player.location.y));
			double speed = player.bulletSpeed ;
			trajectories.add(MathHelper.getPoint(new Point2D.Double(player.location.x, player.location.y),
					new Point2D.Double(mouseCoord.getX(), mouseCoord.getY()), speed, accuracy));
			if (System.currentTimeMillis() > shotsound) {
				// Sound.gunshot.play();
				shotsound = System.currentTimeMillis() + 500;
			}
			shottimer = System.currentTimeMillis() + (long) (1000.0 / player.rateOfFire);
		}
	}

	private void playerDamage() {

		for (int i = 0; i < enemies.size(); i++) {
			if (enemyLocations.get(i).intersects(player.hitBox)
					&& System.currentTimeMillis() > enemies.get(i).attackTimer) {
				player.damage(25);
				enemies.get(i).attackTimer = System.currentTimeMillis() + 1000;
			}
		}
	}
	
	private void enemyDamage(){
		for (int i = 0; i < enemies.size(); i++) {

			if (enemies.get(i).isAlive()) {
				enemyLocations.get(i).setLocation((int) enemies.get(i).getLocation().x - 16,
						(int) enemies.get(i).getLocation().y - 16);
				for (int j = 0; j < shots.size(); j++) {
					if (enemyLocations.get(i).contains(shots.get(j).getP1())
							|| enemyLocations.get(i).contains(shots.get(j).getP2())) {
						enemies.get(i).takeDamage(player.damage * 2);
						shots.remove(j);
						trajectories.remove(j);
						if (!enemies.get(i).isAlive()) {
							enemyLocations.remove(i);
							enemies.remove(i);
							i--;
							break;
						}
					}
				}
			}else{
				enemyLocations.remove(i);
				enemies.remove(i);
				i--;
			}
		}
	}

	private void updateShots(float deltas){
		for (int i = 0; i < shots.size(); i++) {

			shots.get(i).setLine(shots.get(i).getX1() + (trajectories.get(i).getX() * deltas),
					shots.get(i).getY1() + (trajectories.get(i).getY() * deltas), shots.get(i).getX1(), shots.get(i).getY1());
		}
		Rectangle rect = new Rectangle(0, 0, Main.width, Main.height);
		for (int i = 0; i < shots.size(); i++) {
			if (!rect.contains(shots.get(i).getX2(), shots.get(i).getY2())) {
				shots.remove(i);
				trajectories.remove(i);
			}

		}
	}
	
	public void update(float deltas) {
		playerDamage();
		player.update();
		updateShots(deltas);
		enemyDamage();
		spawn();
		
		if (Main.input.isMouseDown(MouseEvent.BUTTON1)) {
			shoot(Main.input.getMousePositionOnScreen());
		}
		
		for (Enemy enemy : enemies) {
			enemy.update(deltas);
		}

		healthRatio = (player.health * 1000f / player.maxHealth);

	}

	public void draw(Graphics g) {
		g.drawImage(background, 0, 0, Main.width, Main.height, null);
		for (int i = 0; i < shots.size(); i++) {
			g.drawLine((int) shots.get(i).getX1(), (int) shots.get(i).getY1(), (int) shots.get(i).getX2(),
					(int) shots.get(i).getY2());
		}
		for (Enemy enemy : enemies) {
			enemy.draw(g);
		}
		player.draw(g);
		g.setColor(Color.red);
		g.fillRect(Main.width / 2 - 500, 50, 1000, 20);
		g.setColor(Color.green);
		g.fillRect(Main.width / 2 + 500 - (int) healthRatio, 50, (int) healthRatio, 20);
	}

}