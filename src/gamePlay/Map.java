package gamePlay;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.sun.glass.events.KeyEvent;

import entities.Enemy;
import entities.Player;
import helpers.MathHelper;
import helpers.ResourceLoader;
import helpers.Sound;
import mainGameLoop.Main;

public class Map {

	private BufferedImage background;
	private ArrayList<Enemy> enemies;
	private Player player;
	private ArrayList<Rectangle> enemyLocations;
	private ArrayList<Point2D.Double> enemyDrops;

	private ArrayList<Point2D.Double> trajectories;
	private ArrayList<Line2D.Double> shots;

	// base Wave stuff, wave 0 stats
	private float baseZombiesPerSecond = 0.5f;
	private long spawnTimer = 0;
	private int wave = 0, maxZombies = 20, baseZombiesPerRound = 5, baseZombieHealth = 91;
	private boolean waveEnded;
	public static boolean upgradesOpen;

	// current wave stuff
	private int zombiesPerRound = 0, zombieHealth = 0, zombiesSpawned = 0;
	private float zombiesPerSecond = 0;

	// shooting
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
		wave = 0;
		waveEnded = true;

	}

	private void spawn() {

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
		 * legacy if (waveDurationLeft > 0 && System.currentTimeMillis() >=
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

	public void startWaveX(int waveNumber) {
		wave = waveNumber;
		zombieHealth = baseZombieHealth + (10 * waveNumber);
		zombiesPerRound = baseZombiesPerRound + (5 * waveNumber);
		zombiesPerSecond = baseZombiesPerSecond + (0.1f * waveNumber);
		zombiesSpawned = 0;
		spawnTimer = System.currentTimeMillis() + 1000;
		waveEnded = false;
	}

	public void nextWave() {
		wave++;
		zombieHealth = Math.max(zombieHealth + 10, baseZombieHealth + 10);
		zombiesPerRound = Math.max(zombiesPerRound + 5, baseZombiesPerRound + 5);
		zombiesPerSecond = Math.max(zombiesPerSecond + 0.1f, baseZombiesPerSecond + 0.1f);
		zombiesSpawned = 0;
		spawnTimer = System.currentTimeMillis() + 1000;
		waveEnded = false;
	}

	private void shoot(Point mouseCoord) {
		if (System.currentTimeMillis() > shottimer) {
			double accuracy = 1.0 / player.accuracy;
			shots.add(new Line2D.Double(player.location.x, player.location.y, player.location.x, player.location.y));
			double speed = player.bulletSpeed;
			trajectories.add(MathHelper.getPoint(new Point2D.Double(player.location.x, player.location.y),
					new Point2D.Double(mouseCoord.getX(), mouseCoord.getY()), speed, accuracy));
			if (System.currentTimeMillis() > shotsound) {
				Sound.gunshot.play();
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

	private void enemyDamage() {
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
							genPlayerPoint(enemyLocations.get(i));
							enemyLocations.remove(i);
							enemies.remove(i);
							i--;
							break;
						}
					}
				}
			} else {
				enemyLocations.remove(i);
				enemies.remove(i);
				i--;
			}
		}
	}

	private void updateShots(float deltas) {
		for (int i = 0; i < shots.size(); i++) {

			shots.get(i).setLine(shots.get(i).getX1() + (trajectories.get(i).getX() * deltas),
					shots.get(i).getY1() + (trajectories.get(i).getY() * deltas), shots.get(i).getX1(),
					shots.get(i).getY1());
		}
		Rectangle rect = new Rectangle(0, 0, Main.width, Main.height);
		for (int i = 0; i < shots.size(); i++) {
			if (!rect.contains(shots.get(i).getX2(), shots.get(i).getY2())) {
				shots.remove(i);
				trajectories.remove(i);
			}

		}
	}

	private void afterWaveEvent() {
		if (Main.input.isKeyDown(KeyEvent.VK_SPACE)) {
			nextWave();
		} else if (Main.input.isKeyDown(KeyEvent.VK_Q) && !upgradesOpen) {
			Main.input.artificialKeyReleased(KeyEvent.VK_W);
			Main.input.artificialKeyReleased(KeyEvent.VK_A);
			Main.input.artificialKeyReleased(KeyEvent.VK_S);
			Main.input.artificialKeyReleased(KeyEvent.VK_D);
			Main.input.artificialKeyReleased(KeyEvent.VK_Q);
			upgradesOpen = true;
			Upgrades up = new Upgrades(player);
			up.repaint();
			up.setFocusable(true);
		}
	}

	private void genPlayerPoint(Rectangle deadZombie) {
		if (Main.random.nextDouble() > 0.6) {
			player.points++;
		}
	}

	public void update(float deltas) {
		player.update(deltas);
		updateShots(deltas);
		if (!waveEnded) {
			spawn();

			if (Main.input.isMouseDown(MouseEvent.BUTTON1)) {
				shoot(Main.input.getMousePositionOnScreen());
			}

			for (Enemy enemy : enemies) {
				enemy.update(deltas);
			}

			enemyDamage();
			playerDamage();
		} else {
			afterWaveEvent();
		}
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
		if (waveEnded) {
			String help1 = "Press 'Q' to open upgrades";
			String help2 = "Press 'SPACE' to start next wave";
			g.setFont(new Font("Verdana", Font.BOLD, Main.width/50));
			g.setColor(new Color(0,0,0,127));
			FontMetrics met = g.getFontMetrics();
			int l1 = met.stringWidth(help1);
			int l2 = met.stringWidth(help2);
			g.drawString(help1, Main.width/2 - l1/2, Main.height/2);
			g.drawString(help2, Main.width/2 - l2/2, 3*Main.height/4);
		}

	}

}
