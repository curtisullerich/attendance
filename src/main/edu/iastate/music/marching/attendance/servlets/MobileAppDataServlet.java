package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.MobileDataManager;
import edu.iastate.music.marching.attendance.model.store.User;

public class MobileAppDataServlet extends AbstractBaseServlet {

	private class ClassListResult {
		@SuppressWarnings("unused")
		public ResultErrorType error;
		@SuppressWarnings("unused")
		public String data;
	}

	public enum Page {
		index;
	}

	private enum ResultErrorType {
		success, login, exception, empty
	}

	private class UploadResult {
		@SuppressWarnings("unused")
		public ResultErrorType error;
		@SuppressWarnings("unused")
		public String message;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3138258973922548889L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		ClassListResult result = new ClassListResult();

		// Check if correct user type is logged in
		if (!isLoggedIn(req, resp, User.Type.TA, User.Type.Director)) {
			result.error = ResultErrorType.login;
		} else {
			DataTrain train = DataTrain.getAndStartTrain();

			MobileDataManager mdc = train.getMobileDataManager();

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

			MobileDataManager mdc = train.getMobileDataManager();

			// data here is the posted content delimited by "&newline&". Those
			// values in turn are
			// delimited by "&split&"
			String data;
			try {
				data = new java.util.Scanner(req.getInputStream())
						.useDelimiter("\\A").next();
			} catch (java.util.NoSuchElementException e) {
				data = "";
			}

			if (data.trim().equals("")) {
				// Empty data uploaded
				result.error = ResultErrorType.empty;
			} else {
				// Optimistically set a success type
				result.error = ResultErrorType.success;

				try {
					result.message = mdc.pushMobileData(data, train
							.getAuthManager().getCurrentUser(req.getSession()));
				} catch (IllegalArgumentException e) {
					result.error = ResultErrorType.exception;
					result.message = e.getMessage();
				} catch (IllegalStateException e) {
					result.error = ResultErrorType.exception;
					result.message = e.getMessage();
				}
			}

		}

		// Print out JSON of result
		resp.getOutputStream().print(new Gson().toJson(result));

	}
}