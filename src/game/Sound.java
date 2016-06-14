package game;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

import main.ResourceLoader;

public class Sound {

	public static final Sound dayNNight = new Sound("DayNNite-HQ-lowVol.wav");
	public static final Sound gunshot = new Sound("gunshot2.wav");

	private Clip clip;
	private AudioInputStream sound;

	public Sound(String filePath) {
		try {
			clip = AudioSystem.getClip();
			sound = ResourceLoader.getSound(filePath);
			clip.open(sound);
		} catch (LineUnavailableException e) {
			Main.sound = false;
			e.printStackTrace();
			clip = null;
		} catch (IOException e) {
			Main.sound = false;
			e.printStackTrace();
			clip = null;
		}
		
	}

	public void play() {
		if (Main.sound) {
			clip.setFramePosition(0);
			clip.start();
		}
	}

	public void stop() {
		clip.stop();
	}

	public void loop() {
		if (Main.sound) {
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		}
	}

}
