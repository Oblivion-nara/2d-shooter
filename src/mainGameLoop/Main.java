package mainGameLoop;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JFrame;

import entities.Player;
import game.MainMap;
import game.MainMenu;
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
	private Player player;

	public static InputHandler input;
	public static Random random;
	public static boolean sound;
	public static int width, height;
	public static float zeroXCoord, zeroYCoord, root2 = (float) Math.sqrt(2.0);

	private MainMap map;
	private MainMenu menu;

	public static void main(String args[]) {
		Main main = new Main();

		main.run();
	}

	public void run() {
		initialise();
		int loops = 0,loop = 0;
		long secondT = System.currentTimeMillis();
		System.out.println(Integer.MAX_VALUE);
		while (running) {
			currentTime = System.nanoTime();
			deltaTime = currentTime - previousTime;
			deltas = ((float) deltaTime / 1000000000f);
			update();
			draw();
			loops++;
			loop++;
			if(secondT < System.currentTimeMillis()){
				secondT += 1000;
				System.out.println(loops+", total:"+loop);
				loops = 0;
			}
			if (pause) {
				menu.run(BufferImage);
				pause = false;
				currentTime = System.nanoTime() + 1000;
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
		FPS = this.getGraphicsConfiguration().getDevice().getDisplayModes()[1].getRefreshRate();
		SPF = 1f / FPS;
		g = this.getGraphics();
		width = this.getWidth();
		height = this.getHeight();
		input = new InputHandler(this);
		player = new Player();
		menu = new MainMenu(g);
		map = new MainMap(player);
	}

	private void setCursor() {
		Cursor cursor = this.getCursor();
		boolean fix = false;
		while (!fix) {
			try {
				cursor = Toolkit.getDefaultToolkit().createCustomCursor(ResourceLoader.getImage("cursor"),
						new Point(16,16), "c");
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
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gds = ge.getScreenDevices();
//		
//		if(gds.length > 0){
//			gds[0].setFullScreenWindow(this);
//		}
		
		this.setVisible(true);
	}

	private void update() {

		map.update(deltas);

		if (input.isKeyDown(KeyEvent.VK_ESCAPE)) {
			System.exit(0);
			input.artificialKeyReleased(KeyEvent.VK_ESCAPE);
			pause = true;
		}
	}

	private void draw() {

		BufferImage = this.createImage(width, height);
		Graphics BufferGraphics = BufferImage.getGraphics();
		// draw here

		map.draw(BufferGraphics);
		map.drawGUI(BufferGraphics);

		//	to here
//		drawScreenLayout(BufferGraphics);

		g.drawImage(BufferImage, 0, 0, width, height, null);
	}

	private void drawScreenLayout(Graphics g) {

		g.drawLine(width / 2, 0, width / 2, height);
		g.drawLine(0, height / 2, width, height / 2);

	}
	
}
