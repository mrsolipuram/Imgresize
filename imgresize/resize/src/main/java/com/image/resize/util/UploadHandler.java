/**
 * 
 */
package com.image.resize.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.imageio.ImageIO;

import com.google.gson.JsonObject;
import com.image.mongo.ImageMongoPersistProvider;
import com.image.thread.ImagePersistThread;
import com.image.thread.ImageResizeThread;

/**
 * @author madhava
 * 
 */
public class UploadHandler {

	public static JsonObject uploadImage(InputStream imageInputStream,
			String mimeType, String filePrex) {
		JsonObject json = new JsonObject();
		try {
			BufferedImage image = ImageIO.read(imageInputStream);
			//String filePrex = getPrimaryKey();
			for (String size : ImageProperties.IMAGE_SIZES) {
				createUploadThread(image, json, size, mimeType,filePrex);
			}
			saveImageInToDB(filePrex+"."+mimeType);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

	public static void saveImageInToDB(String itemName) {
		ImagePersistThread it = new ImagePersistThread();
		it.setImagePersist(new ImageMongoPersistProvider());
		it.setFilePath(ImageProperties.ORG_IMG_DIR+"/"+itemName);
		it.start();
		
	}

	public static void createUploadThread(BufferedImage image, JsonObject json,
			String size, String mimeType,String filePrefix) {
		ImageResizeThread imageResizeThread = new ImageResizeThread();
		imageResizeThread.setImage(image);
		imageResizeThread.setJson(json);
		imageResizeThread.setSize(size);
		imageResizeThread.setMimeType(mimeType);
		imageResizeThread.setFilePrefix(filePrefix);
		imageResizeThread.start();
	}
	
	public static String getPrimaryKey() {
		UUID id = UUID.randomUUID();
		return id.toString();
	}

}
