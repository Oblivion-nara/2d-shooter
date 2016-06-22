package helpers;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.ImageIcon;

public class ResourceLoader {
	
	private static ResourceLoader rl = new ResourceLoader();
	private static BufferedReader reader = null;
	
	/**
	 * gets an image at the path specified
	 * @param path the path to the image
	 * @return the image or, if there is an exception, null
	 */
	public static Image getImage(String path){
		try{
			
			URL url = rl.getClass().getClassLoader().getResource("zResources/images/" + path + ".png");
			return new ImageIcon(url).getImage();
		}catch (Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferedImage getBufferedImage(String path) {
		try {
			URL url = rl.getClass().getClassLoader().getResource("zResources/images/" + path + ".png");
			return ImageIO.read(url);
		} catch (IOException e) {
			System.out.println("failed to load");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * loads an image and cuts it up based on the width and height of the sub-images needed and puts the resulting images into a 2D array
	 * @param path the path to the image
	 * @param spriteWidth the width of the sprite you would like to take from the image
	 * @param spriteHeight the height of the sprite you would like to take from the image
	 * @return an array of the sub-images taken from the image, size depending on the width and height of the sub-images
	 */
	public static Image[][] getPlayerSprites(String path, int spriteWidth, int spriteHeight){
		Image spriteSheet = ResourceLoader.getImage(path);
		BufferedImage BI = new BufferedImage(spriteSheet.getWidth(null), spriteSheet.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Image[][] sprites = new Image[spriteSheet.getWidth(null)/spriteWidth][spriteSheet.getHeight(null)/spriteHeight];
		Graphics2D bGr = BI.createGraphics();
		bGr.drawImage(spriteSheet, 0, 0, null);
		for(int i = 0; i <= sprites.length - 1; i++){
			for(int j = 0; j <= sprites[0].length - 1; j++){
				sprites[i][j] = BI.getSubimage(i * spriteWidth, j * spriteHeight, spriteWidth, spriteHeight);
			}
		}
		return sprites;
	}
	
	/**
	 * gets the sprites for objects with only a set looping animation, stores them in an array
	 * @param path the path to the images
	 * @param spriteWidth the width of the sub-images
	 * @param spriteHeight the height of the sub-images
	 * @return an array of the sub-images
	 */
	public static Image[] getBlockSprites(String path, int spriteWidth, int spriteHeight){
		Image spriteSheet = ResourceLoader.getImage(path);
		BufferedImage BI = new BufferedImage(spriteSheet.getWidth(null), spriteSheet.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Image[] sprites = new Image[spriteSheet.getWidth(null)/spriteWidth];
		Graphics2D bGr = BI.createGraphics();
		bGr.drawImage(spriteSheet, 0, 0, null);
		for(int i = 0; i <= sprites.length - 1; i++){
			sprites[i] = BI.getSubimage(i * spriteWidth, 0, spriteWidth, spriteHeight);
		}
		return sprites;
	}
	
	/**
	 * gets a .wav file from the path
	 * @param path the path to the sound file
	 * @return a sound file
	 */
	public static AudioInputStream getSound(String path){
		try {
			URL url = rl.getClass().getClassLoader().getResource("zResources/sound/" + path);
			return AudioSystem.getAudioInputStream(url);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * creates a BufferedReader to allow reading from a file
	 * @param path the path to the file
	 * @throws FileNotFoundException
	 */
	public static void openInputStream(String path) throws IOException{
		FileReader fr = new FileReader(path);
		reader = new BufferedReader(fr);
	}
	
	/**
	 * closes the BufferedReader
	 * @throws IOException
	 */
	public static void closeInputStream() throws IOException{
		reader.close();
	}
	
	/**
	 * gets a single line of text from a .txt file
	 * @return a line of text from the file as a String
	 * @throws IOException
	 */
	public static String getLineOfTextFromFile() throws IOException{
		return reader.readLine();
	}
	
	/**
	 * gets all the text from a file and puts each line in an index of an array
	 * @return an array containing strings
	 * @throws IOException
	 */
	public static String[] getAllTextFromFile() throws IOException{
		String[] strings = new String[(int) reader.lines().count()];
		for(int i = 0; i < reader.lines().count(); i++){
			strings[i] = reader.readLine();
		}
		return strings;
	}

}
