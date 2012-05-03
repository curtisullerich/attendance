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

	private static final String JSPATH = "mobiledata";
	
	private static final String DATA_PARAMETER = "data";

	public enum Page implements IPathEnum {
		index;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if(!isLoggedIn(req, resp, User.Type.TA, User.Type.Director))
			return;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		isLoggedIn(req, resp, User.Type.TA, User.Type.Director);
		
		DataTrain train = DataTrain.getAndStartTrain();

		MobileDataController mdc = train.getMobileDataController();

		//data here is delimited by "&newline&". Those values in turn are delimited by "&split&"
		String data = req.getParameter(DATA_PARAMETER);

		mdc.pushMobileData(data);
		
	}

	@Override
	public String getJspPath() {
		return JSPATH;
	}

}
