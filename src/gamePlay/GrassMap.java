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

public class GrassMap extends Map {

	public GrassMap(Player p) {
		super();
		init(p);
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

	// will be in gun class
	private void shoot(Point mouseCoord) {
		if (System.currentTimeMillis() > shottimer) {
			double accuracy = 1.0 / player.accuracy;
			shots.add(new Line2D.Double(player.location.x, player.location.y, player.location.x, player.location.y));
			double speed = player.bulletSpeed;

			trajectories.add(MathHelper.getPoint(new Point2D.Double(player.location.x, player.location.y),
					new Point2D.Double((mouseCoord.getX() - actualX) / zoomlevel,
							(mouseCoord.getY() - actualY) / zoomlevel),
					speed, accuracy));
			if (System.currentTimeMillis() > shotsound) {
				Sound.gunshot.play();
				shotsound = System.currentTimeMillis() + 500;
			}
			shottimer = System.currentTimeMillis() + (long) (1000.0 / player.rateOfFire);
		}
	}

	// checks if the player is damaged
	private void playerDamage() {

		for (int i = 0; i < enemies.size(); i++) {
			if (enemyLocations.get(i).intersects(player.hitBox)
					&& System.currentTimeMillis() > enemies.get(i).attackTimer) {
				player.damage(25);
				enemies.get(i).attackTimer = System.currentTimeMillis() + 1000;
			}
		}
	}

	// checks if the enemy is damaged
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

	// will be in gun class
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
		} else if (Main.input.isKeyDown(KeyEvent.VK_Q) && !Upgrades.upgradesOpen) {
			Main.input.artificialKeyReleased(KeyEvent.VK_W);
			Main.input.artificialKeyReleased(KeyEvent.VK_A);
			Main.input.artificialKeyReleased(KeyEvent.VK_S);
			Main.input.artificialKeyReleased(KeyEvent.VK_D);
			Main.input.artificialKeyReleased(KeyEvent.VK_Q);
			Upgrades.upgradesOpen = true;
			Upgrades up = new Upgrades(player);
			up.repaint();
			up.setFocusable(true);
		}
	}

	public void update(float deltas) {
		player.update(deltas, zoomlevel, actualX, actualY);
		updateShots(deltas);
		if (!waveEnded) {
			spawn();

			if (Main.input.isMouseDown(MouseEvent.BUTTON1)) {
				shoot(Main.input.getMousePositionRelativeToComponent());
			}

			for (Enemy enemy : enemies) {
				enemy.update(deltas);
			}

			enemyDamage();
			playerDamage();
		} else {
			afterWaveEvent();
		}
		zoom(deltas);
	}

	public void draw(Graphics g2) {

		BufferedImage buffer = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = buffer.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, buffer.getWidth(), buffer.getHeight());
		int x = buffer.getWidth();// image size
		int y = buffer.getHeight();
		int halfW = 3 * Main.width / 2;// buffer size
		int halfH = 3 * Main.height / 2;
		g.drawImage(background, halfW - x / 2, halfH - y / 2, x, y, null);
		for (int i = 0; i < shots.size(); i++) {
			g.drawLine((int) shots.get(i).getX1(), (int) shots.get(i).getY1(), (int) shots.get(i).getX2(),
					(int) shots.get(i).getY2());
		}
		for (Enemy enemy : enemies) {
			enemy.draw(g);
		}
		player.draw(g);

		int xRelativeToPlayer = (int) ((-zoomlevel * player.location.x) + Main.width / 2); 
		// screen x without correction
		int yRelativeToPlayer = (int) ((-zoomlevel * player.location.y) + Main.height / 2); 
		// screen y without correction
		int width = (int) (3 * Main.width * zoomlevel); // width of image
		int height = (int) (3 * Main.height * zoomlevel); // height of image
		actualX = Math.max(0, Math.min(x - width, xRelativeToPlayer)); 
		// screen x with correction
		actualY = Math.max(0, Math.min(y - height, yRelativeToPlayer)); 
		// screen y with correction

		g2.drawImage(buffer, actualX, actualY, actualX + width, actualY + height, 0, 0, buffer.getWidth(),
				buffer.getHeight(), null);
	}

	public void drawGUI(Graphics g2) {
		if (waveEnded) {
			String help1 = "Press 'Q' to open upgrades";
			String help2 = "Press 'SPACE' to start next wave";
			g2.setFont(new Font("Verdana", Font.BOLD, Main.width / 50));
			g2.setColor(new Color(0, 0, 0, 127));
			FontMetrics met = g2.getFontMetrics();
			int l1 = met.stringWidth(help1);
			int l2 = met.stringWidth(help2);
			g2.drawString(help1, Main.width / 2 - l1 / 2, 3 * Main.height / 5);
			g2.drawString(help2, Main.width / 2 - l2 / 2, 3 * Main.height / 4);
		}
		player.drawGUI(g2);
	}

}

























