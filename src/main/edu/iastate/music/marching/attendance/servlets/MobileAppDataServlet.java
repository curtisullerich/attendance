package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

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
			result.error = ResultError.login;
		} else {
			DataTrain train = DataTrain.getAndStartTrain();

			MobileDataController mdc = train.getMobileDataController();

			result.data = mdc.getClassList();
			result.error = ResultError.success;
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
			result.error = ResultError.login;
		} else {

			DataTrain train = DataTrain.getAndStartTrain();

			MobileDataController mdc = train.getMobileDataController();

			// data here is delimited by "&newline&". Those values in turn are
			// delimited by "&split&"
			String data = req.getParameter(DATA_PARAMETER);

			try {
				mdc.pushMobileData(data);
			} catch (IllegalArgumentException e) {
			}

			// TODO Error handling

			result.error = ResultError.success;
			result.message = "";

		}

		// Print out JSON of result
		resp.getOutputStream().print(new Gson().toJson(result));

	}

	private enum ResultError {
		success, login
	}

	private class UploadResult {
		public ResultError error;
		public String message;
	}

	private class ClassListResult {
		public ResultError error;
		public String data;
	}
}