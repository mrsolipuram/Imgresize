/**
 * 
 */
package com.image.thread;

import java.awt.image.BufferedImage;

import com.google.gson.JsonObject;
import com.image.resize.util.ImageResizeUtil;

/**
 * @author madhava
 *
 */
public class ImageResizeThread extends Thread{
	
	private BufferedImage image;
	private String size;
	private String mimeType;
	private JsonObject json;
	private String filePrefix;
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public void setJson(JsonObject json) {
		this.json = json;
	}
	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}
	
	@Override
	public void run() {
		String path = uploadImage(image,size,mimeType);
		json.addProperty(size, path);
	}
	
	private String uploadImage(BufferedImage image, String sizes,String mimeType) {
		String size[] = sizes.split("x");
		image = ImageResizeUtil.createResizeImage(image, Integer.parseInt(size[0]), Integer.parseInt(size[1]));
		String fileName = filePrefix+"_"+sizes+'.'+mimeType;
		ImageResizeUtil.writeBufferedImage(image, mimeType, fileName,sizes);
		return sizes+"/"+fileName;
	}

}
