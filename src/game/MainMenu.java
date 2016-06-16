package game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import com.sun.glass.events.KeyEvent;

import mainGameLoop.Main;

public class MainMenu {

	private Graphics g;
	private Image buffer;
	private boolean resume;

	public MainMenu(Graphics g) {
		this.g = g;
	}

	public void run(Image buffer) {
		float[] matrix = new float[25];
		for (int i = 0; i < 25; i++)
			matrix[i] = 1.0f/25.0f;
		BufferedImageOp op = new ConvolveOp(new Kernel(5, 5, matrix),ConvolveOp.EDGE_ZERO_FILL,null);
		this.buffer = op.filter((BufferedImage) buffer, (BufferedImage) this.buffer);
		resume = false;
		while (!resume) {
			update();
			draw();
		}
	}

	private void update() {

		if (Main.input.isKeyDown(KeyEvent.VK_ESCAPE)) {
			Main.input.artificialKeyReleased(KeyEvent.VK_ESCAPE);
			resume = true;
		}

	}

	private void draw() {

		BufferedImage offImage = new BufferedImage(Main.width, Main.height, BufferedImage.TYPE_INT_ARGB);
		Graphics g2 = offImage.getGraphics();
		
		g2.drawImage(buffer, 0, 0, Main.width, Main.height, null);
		g2.setColor(new Color(255,255,255,100));
		g2.fillRect(0, 0, Main.width, Main.height);
		
		g.drawImage(offImage, 0, 0, Main.width, Main.height, null);

	}

}
