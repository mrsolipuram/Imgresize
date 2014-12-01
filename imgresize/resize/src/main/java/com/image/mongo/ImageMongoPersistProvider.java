package com.image.mongo;

import java.util.Date;

import com.image.mongo.MongoDB.MongoCollections;
import com.image.persist.ImagePersist;
import com.image.resize.util.UploadHandler;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class ImageMongoPersistProvider implements ImagePersist {

	public void saveImage(String filePath) {
		try {
			DBObject obj = new BasicDBObject();
			obj.put("_id", UploadHandler.getPrimaryKey());
			obj.put("imagePath", filePath);
			obj.put("isVerified", false);
			obj.put("createdDate", new Date());
			DBCollection collection = MongoDB
					.getCollection(MongoCollections.IMAGES.getKey());
			collection.save(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public DBCursor getAllImages(boolean status, int size) {
		try {
			DBObject query = new BasicDBObject();
			query.put("isVerified", status);
			DBCollection collection = MongoDB
					.getCollection(MongoCollections.IMAGES.getKey());
			if (size == -1)
				return collection.find(query);
			return collection.find(query).limit(size);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateStatus(String[] imageIds, boolean status) {
		try {
			for (String id : imageIds) {
				DBObject query = new BasicDBObject();
				query.put("_id", id);
				DBObject field = new BasicDBObject();
				field.put("$set", new BasicDBObject().append("isVerified", status));
				DBCollection collection = MongoDB
						.getCollection(MongoCollections.IMAGES.getKey());
				collection.update(query, field);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
