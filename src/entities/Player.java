package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import helpers.MathHelper;
import helpers.ResourceLoader;
import mainGameLoop.Main;

public class Player {

	public Point2D.Double location;
	public Rectangle hitBox;
	private Image picture;
	private int playArea = 8;
	private float angle;
	private Point mouse;
	public int health, speed, accuracy, rateOfFire, damage, maxHealth, bulletSpeed, points;

	public Player() {

		picture = ResourceLoader.getImage("player.png");
		location = new Point2D.Double(Main.width/2, Main.height/2);
		hitBox = new Rectangle((Main.width/2), (Main.height/2), 32, 32);
		angle = 0;
		maxHealth = 1000;
		health = maxHealth;
		speed = 3;
		bulletSpeed = 1000;
		accuracy = 50;
		rateOfFire = 5;
		damage = 10;
	}

	// 0,0 top-left
	// left +
	// up +
	public Point2D.Double getLocation() {
		return location;
	}

	public void damage(int damage){
		health -= damage;
	}
	
	public void setLocation(Point2D.Double location) {
		this.location = location;
	}

	public void moveUp() {
		if(location.y > 256){
			location.y -= speed;
		}
	}

	public void moveDown() {
		if(location.y < (playArea-1)*Main.height/playArea){
			location.y += speed;
		}
	}

	public void moveRight() {
		if(location.x < (playArea-1)*Main.width/playArea){
			location.x += speed;
		}
	}

	public void moveLeft() {
		if(location.x > Main.width/playArea){
			location.x -= speed;
		}
	}

	public void moveUR() {
		if (location.y <= 256) {
			moveRight();
		} else if (location.x >= (playArea-1)*Main.width/playArea) {
			moveUp();
		} else {
			location.x += (speed) / Main.root2;
			location.y -= (speed) / Main.root2;
		}
	}

	public void moveUL() {
		if (location.y <= 256) {
			moveLeft();
		} else if (location.x <= Main.width/playArea) {
			moveUp();
		} else {
			location.x -= (speed) / Main.root2;
			location.y -= (speed) / Main.root2;
		}
	}

	public void moveDR() {
		if (location.y >= (playArea-1)*Main.height/playArea) {
			moveRight();
		} else if (location.x >= (playArea-1)*Main.width/playArea) {
			moveDown();
		} else {
			location.x += (speed) / Main.root2;
			location.y += (speed) / Main.root2;
		}
	}

	public void moveDL() {
		if (location.y >= (playArea-1)*Main.height/playArea) {
			moveLeft();
		} else if (location.x <= Main.width/playArea) {
			moveDown();
		} else {
			location.x -= (speed) / Main.root2;
			location.y += (speed) / Main.root2;
		}
	}

	public void update() {

		if (Main.input.isKeyDown(KeyEvent.VK_W)) {
			if (Main.input.isKeyDown(KeyEvent.VK_D)) {
				moveUR();
			} else if (Main.input.isKeyDown(KeyEvent.VK_A)) {
				moveUL();
			} else {
				moveUp();
			}
		} else if (Main.input.isKeyDown(KeyEvent.VK_S)) {
			if (Main.input.isKeyDown(KeyEvent.VK_D)) {
				moveDR();
			} else if (Main.input.isKeyDown(KeyEvent.VK_A)) {
				moveDL();
			} else {
				moveDown();
			}
		} else if (Main.input.isKeyDown(KeyEvent.VK_D)) {
			moveRight();
		} else if (Main.input.isKeyDown(KeyEvent.VK_A)) {
			moveLeft();
		}
		
		mouse = Main.input.getMousePositionOnScreen();
		angle = (float)MathHelper.getAngle(new Point((int)location.x, (int)location.y),mouse);
		hitBox.setLocation((int)location.x, (int)location.y);
		
	}

	public void draw(Graphics g) {

		Graphics2D g2 = (Graphics2D)g;
		
		AffineTransform save = g2.getTransform();
		AffineTransform rotate = new AffineTransform();
		rotate.rotate(angle+(Math.PI/2),location.x,location.y);
		g2.setTransform(rotate);
		g2.drawImage(picture, (int) location.x-16, (int) location.y-16, null);
		g2.setTransform(save);
	}

}
