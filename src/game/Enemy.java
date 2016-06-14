package game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class Enemy {

	private Point2D.Double location;
	private Image picture;
	public int health;
	public float speed = 2f, angle;
	private Player p;
	public long attackTimer;

	public Enemy(Player p) {
		this.p = p;
		health = 100;
		picture = ResourceLoader.getImage("enemy.png");
		location = new Point2D.Double(Main.random.nextInt(Main.width), Main.random.nextInt(Main.height - 256) + 256);
		speed += Main.random.nextGaussian() * 0.25;
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
		if (Main.random.nextDouble() > 0.8) {
			p.points++;
		}
		return false;
	}

	public void update() {

		angle = (float) MathHelper.getAngle(new Point((int) location.x, (int) location.y),
				new Point((int) p.location.x, (int) p.location.y));
		Point2D.Double gain = MathHelper.getPoint(location, p.location, speed, 0);
		location.setLocation(location.x + gain.x, location.y + gain.y);

	}

	public void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		AffineTransform save = g2.getTransform();
		AffineTransform rotate = new AffineTransform();
		rotate.rotate(angle + (Math.PI / 2), location.x, location.y);
		g2.setTransform(rotate);
		g2.drawImage(picture, (int) location.x - 16, (int) location.y - 16, null);
		g2.setTransform(save);
	}

}
