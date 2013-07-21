package edu.iastate.music.marching.attendance.util;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class ServletUtil {

	public static InputStream getInputStream(HttpServletRequest req)
			throws FileUploadException, IOException {
		FileItemIterator iterator = new ServletFileUpload()
				.getItemIterator(req);

		while (iterator.hasNext()) {
			FileItemStream item = iterator.next();
			if (!item.isFormField()) {
				return item.openStream();
			}
		}
		
		return null;
	}
}
