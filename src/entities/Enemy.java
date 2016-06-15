package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import helpers.MathHelper;
import helpers.ResourceLoader;
import mainGameLoop.Main;

public class Enemy {

	private Point2D.Double location;
	private Image livingZombie;
	public Image deadZombie, deadZombieWithReward;
	public int health;
	public float speed = 90f, angle;
	private Player p;
	public long attackTimer;

	public Enemy(Player p, int health) {
		this.p = p;
		this.health = health;
		livingZombie = ResourceLoader.getImage("liveZombie.png");
		deadZombie = ResourceLoader.getImage("deadZombieNoReward.png");
		deadZombieWithReward = ResourceLoader.getImage("deadZombieWithReward.png");
		location = new Point2D.Double(Main.random.nextInt(Main.width), Main.random.nextInt(Main.height - 256) + 256);
		this.speed = (float) (speed + Main.random.nextGaussian() * 20);
		attackTimer = System.currentTimeMillis() + 1000;
	}

	public Point2D.Double getLocation() {
		return location;
	}

	public void takeDamage(int damage) {
		health -= damage;
	}

	public boolean isAlive() {
		if (health >= 0) {
			return true;
		}
		return false;
	}

	public void update(float deltas) {

		angle = (float) MathHelper.getAngle(new Point((int) location.x, (int) location.y),
				new Point((int) p.location.x, (int) p.location.y));
		float speed = this.speed * deltas;
		Point2D.Double gain = MathHelper.getPoint(location, p.location, speed, 0);
		location.setLocation(location.x + gain.x, location.y + gain.y);

	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(Color.orange);
		g2.drawRect( (int) location.x - 16, (int) location.y - 16, 32, 32);
		//-------------------------------------------------------------
		AffineTransform save = g2.getTransform();
		AffineTransform rotate = new AffineTransform();
		rotate.rotate(angle + (Math.PI / 2), location.x, location.y);
		g2.setTransform(rotate);
		g2.drawImage(livingZombie, (int) location.x - 16, (int) location.y - 16, null);
		g2.setTransform(save);
	}

}
