package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.MobileDataManager;
import edu.iastate.music.marching.attendance.model.store.User;

public class MobileAppDataServletV2 extends AbstractBaseServlet {

	private static final long serialVersionUID = -8060346926573522097L;

	private class ClassListResultV2 {
		@SuppressWarnings("unused")
		public ResultErrorType error;
		@SuppressWarnings("unused")
		public List<MobileDataManager.ClassListV2User> data;
	}

	public enum Page {
		index;
	}

	private enum ResultErrorType {
		success, login, exception, empty
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Page page = parsePathInfo(req.getPathInfo(), Page.class);
		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case index:
				getIndexV2(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}
	}

	private void getIndexV2(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		ClassListResultV2 result = new ClassListResultV2();

		// Check if correct user type is logged in
		if (!isLoggedIn(req, resp, User.Type.TA, User.Type.Director)) {
			result.error = ResultErrorType.login;
		} else {
			DataTrain train = DataTrain.depart();

			MobileDataManager mdc = train.mobileData();

			result.data = mdc.getClassListV2();
			result.error = ResultErrorType.success;
		}

		// Print out JSON of result
		resp.getOutputStream().print(new Gson().toJson(result));
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Page page = parsePathInfo(req.getPathInfo(), Page.class);
		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case index:
				postIndexV2(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}
	}

	private class UploadResult {
		@SuppressWarnings("unused")
		public ResultErrorType error;
		@SuppressWarnings("unused")
		public String message;
	}

	private void postIndexV2(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		UploadResult result = new UploadResult();

		// Check if correct user type is logged in
		if (!isLoggedIn(req, resp, User.Type.TA, User.Type.Director)) {
			result.error = ResultErrorType.login;
		} else {
			DataTrain train = DataTrain.depart();
			MobileDataManager mdc = train.mobileData();
			Reader r = new InputStreamReader(req.getInputStream());

			// Optimistically set a success type
			result.error = ResultErrorType.success;

			// TODO set result.error = ResultErrorType.empty if empty,

			try {
				result.message = mdc.pushMobileDataV2(r, train.auth()
						.getCurrentUser(req.getSession()));
				// TODO these exceptions might be different types for gson
			} catch (IllegalArgumentException e) {
				result.error = ResultErrorType.exception;
				result.message = e.getMessage();
			} catch (IllegalStateException e) {
				result.error = ResultErrorType.exception;
				result.message = e.getMessage();
			}
		}

		// Print out JSON of result
		resp.getOutputStream().print(new Gson().toJson(result));

	}
}