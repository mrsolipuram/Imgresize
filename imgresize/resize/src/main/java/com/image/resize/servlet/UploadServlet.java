package com.image.resize.servlet;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.imgscalr.Scalr;

import com.google.gson.JsonObject;
import com.image.resize.util.ImageProperties;
import com.image.resize.util.ImageResizeUtil;
import com.image.resize.util.UploadHandler;

public class UploadServlet extends HttpServlet {

	int i = 0;

	@Override
	public void init() throws ServletException {
		super.init();
		File fileOrg = new File(ImageProperties.IMAGES_DIR,
				ImageProperties.ORG_IMG_DIR);
		if (fileOrg.mkdirs())
			System.out.println("Org img directory created");
		for (String size : ImageProperties.IMAGE_SIZES) {
			File sizeDir = new File(ImageProperties.IMAGES_DIR, size);
			sizeDir.mkdirs();
		}
		System.out.println("all directories created");

	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String path = request.getContextPath() + request.getServletPath();

		String fileName = request.getRequestURI().substring(path.length() + 1);
		if (fileName != null && !fileName.isEmpty()) {
			File file = new File(ImageProperties.IMAGES_DIR + fileName);
			if (file.exists() && isResourceModified(request, response, file)) {
				int bytes = 0;
				ServletOutputStream op = response.getOutputStream();
				response.setContentType(getMimeType(file));
				response.setHeader("Content-Disposition", "inline; filename=\""
						+ file.getName() + "\"");
				String width = request.getParameter("width");
				String height = request.getParameter("height");
				if (width != null && height != null) {
					BufferedImage image = ImageResizeUtil.createResizeImage(
							ImageIO.read(file), Integer.parseInt(width),
							Integer.parseInt(height));
					ImageIO.write(image, getSuffix(file.getName()), op);
				} else {
					response.setContentLength((int) file.length());
					byte[] bbuf = new byte[1024];
					DataInputStream in = new DataInputStream(
							new FileInputStream(file));
					while ((in != null) && ((bytes = in.read(bbuf)) != -1)) {
						op.write(bbuf, 0, bytes);
					}

					in.close();
				}

				op.flush();
				op.close();
			}
		} else if (request.getParameter("delfile") != null
				&& !request.getParameter("delfile").isEmpty()) {
			File file = new File("/home/madhava/key_backup/"
					+ request.getParameter("delfile"));
			if (file.exists()) {
				file.delete(); // TODO:check and report success
			}
		} else if (request.getParameter("getthumb") != null
				&& !request.getParameter("getthumb").isEmpty()) {
			File file = new File("/home/madhava/key_backup/"
					+ request.getParameter("getthumb"));
			if (file.exists()) {
				System.out.println(file.getAbsolutePath());
				String mimetype = getMimeType(file);
				if (mimetype.endsWith("png") || mimetype.endsWith("jpeg")
						|| mimetype.endsWith("jpg") || mimetype.endsWith("gif")) {
					BufferedImage im = ImageIO.read(file);
					if (im != null) {
						BufferedImage thumb = Scalr.resize(im, 75);
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						if (mimetype.endsWith("png")) {
							ImageIO.write(thumb, "PNG", os);
							response.setContentType("image/png");
						} else if (mimetype.endsWith("jpeg")) {
							ImageIO.write(thumb, "jpg", os);
							response.setContentType("image/jpeg");
						} else if (mimetype.endsWith("jpg")) {
							ImageIO.write(thumb, "jpg", os);
							response.setContentType("image/jpeg");
						} else {
							ImageIO.write(thumb, "GIF", os);
							response.setContentType("image/gif");
						}
						ServletOutputStream srvos = response.getOutputStream();
						response.setContentLength(os.size());
						response.setHeader("Content-Disposition",
								"inline; filename=\"" + file.getName() + "\"");
						os.writeTo(srvos);
						srvos.flush();
						srvos.close();
					}
				}
			} // TODO: check and report success
		} else {
			PrintWriter writer = response.getWriter();
			writer.write("call POST with multipart form data");
		}
	}

	private boolean isResourceModified(HttpServletRequest request,
			HttpServletResponse response, File file) {
		long modifiedSince = request.getDateHeader("If-Modified-Since");
		if (modifiedSince != -1) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(file.lastModified());
			response.addDateHeader("If-Modified-Since", cal.getTimeInMillis());
			response.addDateHeader("Last-Modified", cal.getTimeInMillis());
			cal.add(Calendar.MONTH, 2);
			response.addDateHeader("Expires", cal.getTimeInMillis());
			return true;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		if (!ServletFileUpload.isMultipartContent(request)) {
			throw new IllegalArgumentException(
					"Request is not multipart, please 'multipart/form-data' enctype for your form.");
		}

		ServletFileUpload uploadHandler = new ServletFileUpload(
				new DiskFileItemFactory());
		PrintWriter writer = response.getWriter();
		response.setContentType("application/json");
		JsonObject jsono = null;
		try {
			List<FileItem> items = uploadHandler.parseRequest(request);
			for (FileItem item : items) {
				if (!item.isFormField()) {
					String mimeType = getSuffix(item.getName());
					String prefix = UploadHandler.getPrimaryKey();
					File file = new File(ImageProperties.IMAGES_DIR
							+ ImageProperties.ORG_IMG_DIR
							+ ImageProperties.FOLDER_SEPARATOR, prefix + "."
							+ mimeType);
					item.write(file);
					jsono = UploadHandler.uploadImage(
							new FileInputStream(file), mimeType, prefix);
					jsono.addProperty("imgPath", ImageProperties.ORG_IMG_DIR+ImageProperties.FOLDER_SEPARATOR+prefix+"."+mimeType);
					jsono.addProperty("name", item.getName());
				}
			}
		} catch (FileUploadException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			writer.write(jsono.toString());
			writer.close();
		}

	}

	private String getMimeType(File file) {
		String mimetype = "";
		if (file.exists()) {
			if (getSuffix(file.getName()).equalsIgnoreCase("png")) {
				mimetype = "image/png";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("jpg")) {
				mimetype = "image/jpg";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("jpeg")) {
				mimetype = "image/jpeg";
			} else if (getSuffix(file.getName()).equalsIgnoreCase("gif")) {
				mimetype = "image/gif";
			} else {
				javax.activation.MimetypesFileTypeMap mtMap = new javax.activation.MimetypesFileTypeMap();
				mimetype = mtMap.getContentType(file);
			}
		}
		return mimetype;
	}

	private String getSuffix(String filename) {
		String suffix = "";
		int pos = filename.lastIndexOf('.');
		if (pos > 0 && pos < filename.length() - 1) {
			suffix = filename.substring(pos + 1);
		}
		return suffix;
	}
}
