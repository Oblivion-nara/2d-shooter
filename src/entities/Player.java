package entities;

import java.awt.Color;
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
	public int health, accuracy, rateOfFire, damage, maxHealth, bulletSpeed, points;
	public float speed;
	
	private Image picture;
	private int playArea = 10;
	private float angle;
	private Point mouse;

	public Player() {

		picture = ResourceLoader.getImage("player.png");
		location = new Point2D.Double(Main.width/2, Main.height/2);
		hitBox = new Rectangle((Main.width/2), (Main.height/2), 32, 32);
		angle = 0;
		maxHealth = 1000;
		health = maxHealth;
		speed = 100f;
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

	public void moveUp(float deltas) {
//		if(location.y > 256){
			location.y -= speed * deltas;
//		}
	}

	public void moveDown(float deltas) {
//		if(location.y < (playArea-1)*Main.height/playArea){
			location.y += speed * deltas;
//		}
	}

	public void moveRight(float deltas) {
//		if(location.x < (playArea-1)*Main.width/playArea){
			location.x += speed * deltas;
//		}
	}

	public void moveLeft(float deltas) {
//		if(location.x > Main.width/playArea){
			location.x -= speed * deltas;
//		}
	}

	public void moveUR(float deltas) {
//		if (location.y <= 256) {
//			moveRight(deltas);
//		} else if (location.x >= (playArea-1)*Main.width/playArea) {
//			moveUp(deltas);
//		} else {
			location.x += (speed * deltas) / Main.root2;
			location.y -= (speed * deltas) / Main.root2;
//		}
	}

	public void moveUL(float deltas) {
//		if (location.y <= 256) {
//			moveLeft(deltas);
//		} else if (location.x <= Main.width/playArea) {
//			moveUp(deltas);
//		} else {
			location.x -= (speed * deltas) / Main.root2;
			location.y -= (speed * deltas) / Main.root2;
//		}
	}

	public void moveDR(float deltas) {
//		if (location.y >= (playArea-1)*Main.height/playArea) {
//			moveRight(deltas);
//		} else if (location.x >= (playArea-1)*Main.width/playArea) {
//			moveDown(deltas);
//		} else {
			location.x += (speed * deltas) / Main.root2;
			location.y += (speed * deltas) / Main.root2;
//		}
	}

	public void moveDL(float deltas) {
//		if (location.y >= (playArea-1)*Main.height/playArea) {
//			moveLeft(deltas);
//		} else if (location.x <= Main.width/playArea) {
//			moveDown(deltas);
//		} else {
			location.x -= (speed * deltas) / Main.root2;
			location.y += (speed * deltas) / Main.root2;
//		}
	}

	public void update(float deltas) {
		
		if (Main.input.isKeyDown(KeyEvent.VK_W)) {
			if (Main.input.isKeyDown(KeyEvent.VK_D)) {
				moveUR(deltas);
			} else if (Main.input.isKeyDown(KeyEvent.VK_A)) {
				moveUL(deltas);
			} else {
				moveUp(deltas);
			}
		} else if (Main.input.isKeyDown(KeyEvent.VK_S)) {
			if (Main.input.isKeyDown(KeyEvent.VK_D)) {
				moveDR(deltas);
			} else if (Main.input.isKeyDown(KeyEvent.VK_A)) {
				moveDL(deltas);
			} else {
				moveDown(deltas);
			}
		} else if (Main.input.isKeyDown(KeyEvent.VK_D)) {
			moveRight(deltas);
		} else if (Main.input.isKeyDown(KeyEvent.VK_A)) {
			moveLeft(deltas);
		}
		
		mouse = Main.input.getMousePositionOnScreen();
		angle = (float)MathHelper.getAngle(new Point((int)Main.width/2, (int)Main.height/2),mouse);
		hitBox.setLocation((int)location.x, (int)location.y);
		
	}

	public void draw(Graphics g) {

		Graphics2D g2 = (Graphics2D)g;

		g2.setColor(Color.blue);
		g2.drawRect( (int) location.x - 16, (int) location.y - 16, 32, 32);
		//-------------------------------------------------------------
		AffineTransform save = g2.getTransform();
		AffineTransform rotate = new AffineTransform();
		rotate.rotate(angle+(Math.PI/2),location.x,location.y);
		g2.setTransform(rotate);
		g2.drawImage(picture, (int) location.x-16, (int) location.y-16, null);
		g2.setTransform(save);
		
	}

	public void drawGUI(Graphics g){

		float healthRatio = (health * 1000f / maxHealth);
		g.setColor(Color.red);
		g.fillRect(Main.width / 2 - 500, 50, 1000, 20);
		g.setColor(Color.green);
		g.fillRect(Main.width / 2 + 500 - (int) healthRatio, 50, (int) healthRatio, 20);
	}
	
}
