package com.image.persist;

import com.mongodb.DBCursor;

public interface ImagePersist {
	public void saveImage(String path);
	
	public DBCursor getAllImages(boolean status, int size);

	public void updateStatus(String[] imageIds, boolean status);
}
