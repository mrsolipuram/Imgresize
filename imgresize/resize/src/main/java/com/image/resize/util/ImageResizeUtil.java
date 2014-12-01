/**
 * 
 */
package com.image.resize.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;

/**
 * @author madhava
 * 
 */
public class ImageResizeUtil {
	public static BufferedImage createResizeImage(BufferedImage img, int width,
			int height) {
		// Create quickly, then smooth and brighten it.
		// img = resize(img, Method.AUTOMATIC, 125, OP_ANTIALIAS, OP_BRIGHTER);
		img = Scalr.resize(img, Scalr.Method.ULTRA_QUALITY, width, height);
		// Let's add a little border before we return result.
		// return pad(img, 4);
		return img;
	}

	public static void writeBufferedImage(BufferedImage img, String type,
			String fileName, String directory) {
		try {
			ImageIO.write(img, type, new File(ImageProperties.IMAGES_DIR
					+ directory + "/", fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static void createImage(String fileName){ try { InputStream is =
	 * new FileInputStream(fileName); createThumbnail(is); } catch
	 * (FileNotFoundException e) { e.printStackTrace(); } }
	 */

	/*
	 * public static void main(String[] args) {
	 * createImage("/home/madhava/Downloads/SimpleApple.png");
	 * System.out.println("done!"); }
	 */
}
