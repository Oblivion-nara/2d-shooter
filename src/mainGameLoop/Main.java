package mainGameLoop;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;

import game.MainMenu;
import gamePlay.Map;
import helpers.InputHandler;
import helpers.ResourceLoader;
import helpers.Sound;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private boolean running, pause;
	private int FPS;
	private float SPF, deltas;
	private long currentTime, previousTime, deltaTime;
	private Graphics g;
	private Image BufferImage;

	public static InputHandler input;
	public static Random random;
	public static boolean sound;
	public static int width, height;
	public static float zeroXCoord, zeroYCoord, root2 = (float) Math.sqrt(2.0);

	private Map map;
	private MainMenu menu;

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
			update();
			draw();
			if(pause){
				menu.run(BufferImage);
				pause = false;
				currentTime =System.nanoTime() + 1000;
			}
			previousTime = currentTime;
		}
	}

	private void initialise() {

		setFrame();
		setVariables();
		setCursor();
		Sound.dayNNight.loop();
	}

	private void setVariables() {
		sound = false;
		running = true;
		random = new Random();
		previousTime = System.nanoTime();
		FPS = this.getGraphicsConfiguration().getDevice().getDisplayMode().getRefreshRate();
		SPF = 1f / FPS;
		g = this.getGraphics();
		width = this.getWidth();
		height = this.getHeight();
		input = new InputHandler(this);
		menu = new MainMenu(g);
		map = new Map();
	}

	private void setCursor() {
		Cursor cursor = this.getCursor();
		boolean fix = false;
		while (!fix) {
			try {
				cursor = Toolkit.getDefaultToolkit().createCustomCursor(ResourceLoader.getImage("cursor.png"),
						new Point(getX(), getY()), "c");
				fix = true;
			} catch (Exception e) {
				System.out.println("fail");
				e.printStackTrace();
			}
		}
		setCursor(cursor);
	}

	private void setFrame() {
		this.setTitle("Day And Night");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(InputHandler.screenSize);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);
		this.setVisible(true);
	}

	private void update() {

		map.update(deltas);

		if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
			input.artificialKeyReleased(KeyEvent.VK_ESCAPE);
			pause = true;
		}
	}

	private void draw() {

		BufferImage = this.createImage(width, height);
		Graphics BufferGraphics = BufferImage.getGraphics();
		// draw here

		map.draw(BufferGraphics);

		// to here
		drawScreenLayout(BufferGraphics);
		g.drawImage(BufferImage, 0, 0, width, height, null);

	}
	
	private void drawScreenLayout(Graphics g){

		g.drawLine(width/2, 0, width/2, height);
		g.drawLine(0, height/2, width, height/2);
		
	}

}
