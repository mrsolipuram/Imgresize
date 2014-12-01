package com.image.thread;

import com.image.persist.ImagePersist;

public class ImagePersistThread extends Thread{
	
	private ImagePersist imagePersist;
	private String filePath;
	
	
	
	public void setImagePersist(ImagePersist imagePersist) {
		this.imagePersist = imagePersist;
	}



	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}



	@Override
	public void run() {
		imagePersist.saveImage(filePath);
	}

}
