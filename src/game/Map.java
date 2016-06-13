package game;

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

public class Map {

	private BufferedImage background;
	private Enemy[] enemies;
	private Player player;
	private Rectangle[] enemyLocations;

	private ArrayList<Point2D.Double> trajectories;
	private ArrayList<Line2D.Double> shots;

	private int numOfEnemies = 20;
	private float healthRatio = 1;

	public Map() {
		init();
	}

	private void init() {

		player = new Player();

		enemies = new Enemy[numOfEnemies];
		for (int i = 0; i < numOfEnemies; i++) {
			enemies[i] = new Enemy();
		}
		trajectories = new ArrayList<Point2D.Double>();
		shots = new ArrayList<Line2D.Double>();
		background = new BufferedImage(Main.width, Main.height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = background.getGraphics();
		g.setColor(new Color(0xdd, 0xa3, 0x3d));
		g.fillRect(0, 0, Main.width, Main.height);
		g.setColor(new Color(0x04, 0xac, 0x1c));
		g.fillRect(0, 0, Main.width, 256);
		Image shrub = ResourceLoader.getImage("shrub.png");
		for (int i = 0; i < Main.random.nextInt(10)+5; i++) {
			g.drawImage(shrub, Main.random.nextInt(Main.width), Main.random.nextInt(Main.height), null);
		}
		Image house1 = ResourceLoader.getImage("house1.png");
		Image house2 = ResourceLoader.getImage("house2.png");
		int x = 0;
		while(x < Main.width){
			if(Main.random.nextBoolean()){
				g.drawImage(house1, x, 128, null);
				x+=256;
			}else{
				g.drawImage(house2, x, 0, null);
				x+=512;
			}
		}

	}

	public void shoot(Point mouseCoord) {
		double accuracy = 1.0 / player.accuracy;
		shots.add(new Line2D.Double(player.location.x, player.location.y, player.location.x, player.location.y));
		double speed = 20.0;
		trajectories.add(MathHelper.getPoint(new Point2D.Double(player.location.x, player.location.y),
				new Point2D.Double(mouseCoord.getX(), mouseCoord.getY()), speed, accuracy));

	}

	public void update() {

		if(Main.input.isMouseDown(MouseEvent.BUTTON1)){
			shoot(Main.input.getMousePositionOnScreen());
		}
		
		for (int i = 0; i < shots.size(); i++) {

			shots.get(i).setLine(shots.get(i).getX1() + trajectories.get(i).getX(),
					shots.get(i).getY1() + trajectories.get(i).getY(), shots.get(i).getX1(), shots.get(i).getY1());
		}
		Rectangle rect = new Rectangle(0, 0, Main.width, Main.height);
		for (int i = 0; i < shots.size(); i++) {
			if (!rect.contains(shots.get(i).getX2(), shots.get(i).getY2())) {
				shots.remove(i);
				trajectories.remove(i);
			}

		}
		for (Enemy enemy : enemies) {
			enemy.update();
		}
		player.update();
		
		healthRatio = (player.health / player.maxHealth) * 1000f;
		
	}

	public void draw(Graphics g) {
		g.drawImage(background, 0, 0, Main.width, Main.height, null);
		for(int i = 0; i< shots.size(); i++){
			g.drawLine((int)shots.get(i).getX1(),(int)shots.get(i).getY1(),(int)shots.get(i).getX2(),(int)shots.get(i).getY2());
		}
		for (Enemy enemy : enemies) {
			enemy.draw(g);
		}
		player.draw(g);
		g.setColor(Color.red);
		g.fillRect(Main.width/2 - 500, 50, 1000, 20);
		g.setColor(Color.green);
		g.fillRect(Main.width/2 + 500 - (int)healthRatio, 50, (int)healthRatio, 20);
	}

}
