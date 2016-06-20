package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
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

	private boolean mouseOnResume, mouseOnSave, mouseOnSettings, mouseOnExit, click;
	private int width, height, section;

	public MainMenu(Graphics g) {
		this.g = g;
		width = Main.width;
		height = Main.height;
		section = height / 5;
		click = false;
	}

	public void run(Image buffer) {
		float[] matrix = new float[25];
		for (int i = 0; i < 25; i++)
			matrix[i] = 1.0f / 25.0f;
		BufferedImageOp op = new ConvolveOp(new Kernel(5, 5, matrix), ConvolveOp.EDGE_ZERO_FILL, null);
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
		Point mouse = Main.input.getMousePositionOnScreen();
		if (mouse.x >= 4 * width / 10 && mouse.x <= 6 * width / 10) {

			if (mouse.y >= 5 * section / 4 && mouse.y <= 7 * section / 4) {
				mouseOnResume = true;
				if (click) {
					if (!Main.input.isMouseDown(MouseEvent.BUTTON1)) {
						resume = true;
					}
				}
			} else {
				mouseOnResume = false;
			}
			if (mouse.y >= 9 * section / 4 && mouse.y <= 11 * section / 4) {
				mouseOnSettings = true;
			} else {
				mouseOnSettings = false;
			}
			if (mouse.y >= 13 * section / 4 && mouse.y <= 15 * section / 4) {
				mouseOnSave = true;
			} else {
				mouseOnSave = false;
			}
			if (mouse.y >= 17 * section / 4 && mouse.y <= 19 * section / 4) {
				mouseOnExit = true;
				if (click) {
					if (!Main.input.isMouseDown(MouseEvent.BUTTON1)) {
						System.exit(0);
					}
				}
			} else {
				mouseOnExit = false;
			}

		} else {
			mouseOnResume = false;
			mouseOnSave = false;
			mouseOnSettings = false;
			mouseOnExit = false;
		}
		click = Main.input.isMouseDown(MouseEvent.BUTTON1);
	}

	private void draw() {

		BufferedImage offImage = new BufferedImage(Main.width, Main.height, BufferedImage.TYPE_INT_ARGB);
		Graphics g2 = offImage.getGraphics();

		g2.drawImage(buffer, 0, 0, Main.width, Main.height, null);
		g2.setColor(new Color(255, 255, 255, 100));
		g2.fillRect(0, 0, Main.width, Main.height);

		overlay(g2);

		g.drawImage(offImage, 0, 0, Main.width, Main.height, null);

	}

	public void overlay(Graphics g) {

		Font title = new Font("Verdana", Font.BOLD, section);
		g.setFont(title);
		g.setColor(Color.black);
		FontMetrics met = g.getFontMetrics();
		g.drawString("PAUSE", width / 2 - (int) (met.stringWidth("PAUSE") / 2),
				section / 2 + met.getHeight() / 2 - met.getDescent());

		g.setColor(Color.blue);
		if (mouseOnResume) {
			g.setColor(Color.orange);
			g.fillRoundRect(4 * width / 10, 5 * section / 4, width / 5, section / 2, 50, 30);
			g.setColor(Color.blue);
		} else {
			g.fillRoundRect(4 * width / 10, 5 * section / 4, width / 5, section / 2, 50, 30);
		}
		if (mouseOnSettings) {
			g.setColor(Color.orange);
			g.fillRoundRect(4 * width / 10, 9 * section / 4, width / 5, section / 2, 50, 30);
			g.setColor(Color.blue);
		} else {
			g.fillRoundRect(4 * width / 10, 9 * section / 4, width / 5, section / 2, 50, 30);
		}
		if (mouseOnSave) {
			g.setColor(Color.orange);
			g.fillRoundRect(4 * width / 10, 13 * section / 4, width / 5, section / 2, 50, 30);
			g.setColor(Color.blue);
		} else {
			g.fillRoundRect(4 * width / 10, 13 * section / 4, width / 5, section / 2, 50, 30);
		}
		if (mouseOnExit) {
			g.setColor(Color.orange);
			g.fillRoundRect(4 * width / 10, 17 * section / 4, width / 5, section / 2, 50, 30);
			g.setColor(Color.blue);
		} else {
			g.fillRoundRect(4 * width / 10, 17 * section / 4, width / 5, section / 2, 50, 30);
		}
		g.setFont(new Font("Verdana", Font.ITALIC, 40));
		g.setColor(Color.white);
		met = g.getFontMetrics();
		g.drawString("RESUME", width / 2 - (int) (met.stringWidth("RESUME") / 2),
				3 * section / 2 + met.getHeight() / 2 - met.getDescent());
		g.drawString("SETTINGS", width / 2 - (int) (met.stringWidth("SETTINGS") / 2),
				5 * section / 2 + met.getHeight() / 2 - met.getDescent());
		g.drawString("SAVE", width / 2 - (int) (met.stringWidth("SAVE") / 2),
				7 * section / 2 + met.getHeight() / 2 - met.getDescent());
		g.drawString("EXIT", width / 2 - (int) (met.stringWidth("EXIT") / 2),
				9 * section / 2 + met.getHeight() / 2 - met.getDescent());

	}
}
