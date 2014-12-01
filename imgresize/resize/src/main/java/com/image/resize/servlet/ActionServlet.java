/**
 * 
 */
package com.image.resize.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.image.mongo.ImageMongoPersistProvider;
import com.image.persist.ImagePersist;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

/**
 * @author madhava
 * 
 */
public class ActionServlet extends HttpServlet {

	public ImagePersist imagePersist = new ImageMongoPersistProvider();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DBCursor cursor = imagePersist.getAllImages(false,10);
		pushResponse(resp, JSON.serialize(cursor));
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String[] imagesId = req.getParameterValues("imageIds");
		imagePersist.updateStatus(imagesId,true);
		//System.out.println(imgesId.length);
		DBObject obj = new BasicDBObject();
		obj.put("status", "success");
		obj.put("images", imagePersist.getAllImages(false, 10));
		pushResponse(resp, JSON.serialize(obj));
	}

	private void pushResponse(HttpServletResponse res, String data)
			throws IOException {
		PrintWriter out = res.getWriter();
		out.write(data);
		out.flush();
		out.close();
	}
}
