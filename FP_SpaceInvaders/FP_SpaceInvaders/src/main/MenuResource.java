package main;

import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.imageio.ImageIO;

public class MenuResource {
	public static final BufferedImage logo_img = readImage("logo");
	public static final BufferedImage play_img = readImage("play");
	public static final BufferedImage exit_img = readImage("exit");
	public static final BufferedImage play_imgHvr = readImage("playHover");
	public static final BufferedImage exit_imgHvr = readImage("exitHover");
	public static final BufferedImage controls_img = readImage("controls");
	public static final BufferedImage health = readImage("health");
	public static final BufferedImage bar = readImage("healthbar");
	public static final BufferedImage score = readImage("score");
	public static final BufferedImage bg = readImage("background");
	public static final BufferedImage hg = readImage("highscore");
	public static final BufferedImage lv = readImage("level");

	/**Reads an image file
	 * @param fileName - The name of the file to read*/
	private static BufferedImage readImage(String fileName) {
		BufferedImage img = null;
	
		try {
			img = ImageIO.read(new File("images/"+fileName+".png"));
		} catch (IOException e) {
			System.out.println("[ResourceManager]: Exception when loading images");
		}
		
		return img;
	}
	
}
