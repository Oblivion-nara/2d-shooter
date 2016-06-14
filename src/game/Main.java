package game;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private static boolean running;
	private static int FPS;
	private static float SPF;
	private static long currentTime, previousTime, deltaTime;

	public static InputHandler input;
	public static Random random;
	public static boolean sound;
	public static int width, height;
	public static float ratio, tilesW, tilesH, widthpx, heightpx, zeroXCoord, zeroYCoord,
			root2 = (float) Math.sqrt(2.0);
	private static float deltas;
	
	private Map map;

	public static void main(String args[]) {
		Main main = new Main();

		main.run();
	}

	public void run() {
		initialise();
		while (running) {
			currentTime = System.nanoTime();
			deltaTime = currentTime - previousTime;
			deltas = ((float) deltaTime / 1000000000f);
			update(deltas);
			draw();
			previousTime = currentTime;
		}
	}

	public void initialise() {
		sound = false;
		running = true;
		random = new Random();
		previousTime = System.nanoTime();
		FPS = this.getGraphicsConfiguration().getDevice().getDisplayMode().getRefreshRate();
		SPF = 1f / FPS;
		this.setTitle("Day And Night");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(InputHandler.screenSize);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.setVisible(true);
		Cursor cursor = this.getCursor();
		boolean fix = false;
		while(!fix){
			try{
				cursor = Toolkit.getDefaultToolkit().createCustomCursor(ResourceLoader.getImage("cursor.png"), new Point(getX(), getY()), "c");
				fix = true;
			}catch(Exception e){
				System.out.println("fail");
				e.printStackTrace();
			}
		}
		setCursor(cursor);
		input = new InputHandler(this);
		width = this.getWidth();
		height = this.getHeight();
		ratio = (float) width / (float) height;
		int tiles = 16;
		if (height > width) {
			tilesH = tiles;
			tilesW = ratio * (float) tiles;
		} else {
			tilesW = tiles;
			tilesH = (float) tiles / ratio;
		}
		heightpx = (float) height / tilesH;
		widthpx = (float) width / tilesW;
		map = new Map();
//		Sound.dayNNight.loop();
	}

	

	private void update(float deltaTime) {

		map.update();

		if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
			System.exit(0);
		}
	}

	public void draw() {

		Graphics g = this.getGraphics();
		Image offImage = this.createImage(width, height);
		Graphics offGraphics = offImage.getGraphics();
		//draw here
		
		map.draw(offGraphics);
		
		//to here
		g.drawImage(offImage, 0, 0, width, height, null);

	}

}
