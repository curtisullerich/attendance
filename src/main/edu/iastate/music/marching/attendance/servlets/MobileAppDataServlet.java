package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

		if (!isLoggedIn(req, resp, User.Type.TA, User.Type.Director))
			return;

		DataTrain train = DataTrain.getAndStartTrain();

		MobileDataController mdc = train.getMobileDataController();

		String classList = mdc.getClassList();

		resp.getOutputStream().print(classList);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp, User.Type.TA, User.Type.Director))
			return;
		
		// Default to success
		String error = "Success";

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

	}

}