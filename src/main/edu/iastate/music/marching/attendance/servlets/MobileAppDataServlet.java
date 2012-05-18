package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.MobileDataController;
import edu.iastate.music.marching.attendance.model.User;

public class MobileAppDataServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3138258973922548889L;

	private static final String SERVLET_PATH = "mobiledata";

	private static final String DATA_PARAMETER = "data";

	public enum Page {
		index;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ClassListResult result = new ClassListResult();

		// Check if correct user type is logged in
		if (!isLoggedIn(req, resp, User.Type.TA, User.Type.Director)) {
			result.error = ResultErrorType.login;
		} else {
			DataTrain train = DataTrain.getAndStartTrain();

			MobileDataController mdc = train.getMobileDataController();

			result.data = mdc.getClassList();
			result.error = ResultErrorType.success;
		}

		// Print out JSON of result
		resp.getOutputStream().print(new Gson().toJson(result));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		UploadResult result = new UploadResult();

		// Check if correct user type is logged in
		if (!isLoggedIn(req, resp, User.Type.TA, User.Type.Director)) {
			result.error = ResultErrorType.login;
		} else {

			DataTrain train = DataTrain.getAndStartTrain();

			MobileDataController mdc = train.getMobileDataController();

			// data here is the posted content delimited by "&newline&". Those values in turn are
			// delimited by "&split&"	
			String data;
			try {
				data = new java.util.Scanner(req.getInputStream()).useDelimiter("\\A").next();
		    } catch (java.util.NoSuchElementException e) {
		    	data = "";
		    }

			// Optimistically set a success type
			result.error = ResultErrorType.success;
			
			try {
				result.message = mdc.pushMobileData(data, AuthController.getCurrentUser(req.getSession()));
			} catch (IllegalArgumentException e) {
				result.error = ResultErrorType.exception;
				result.message = e.getMessage();
			}

		}

		// Print out JSON of result
		resp.getOutputStream().print(new Gson().toJson(result));

	}

	private enum ResultErrorType {
		success, login, exception
	}

	private class UploadResult {
		public ResultErrorType error;
		public String message;
	}

	private class ClassListResult {
		public ResultErrorType error;
		public String data;
	}
}