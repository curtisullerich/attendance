package edu.iastate.music.marching.attendance.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.beans.AuthBean;
import edu.iastate.music.marching.attendance.beans.PageTemplateBean;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.store.AppData;

public class PageBuilder {

	private static final String JSP_PATH_PRE = "/WEB-INF/";

	private static final String JSP_PATH_POST = ".jsp";

	private static final String ATTR_SUCCESS_MESSAGE = "success_message";

	public static final String PARAM_SUCCESS_MESSAGE = "success_message";

	private static final String ATTR_REDIRECT_URL = "redirect_url";

	public static final String PARAM_REDIRECT_URL = "redirect";

	private String mJSPPath;

	private Map<String, Object> attribute_map;
	private PageTemplateBean mPageTemplateBean;

	private AppData mAppData;

	private DataTrain mDataTrain;

	private PageBuilder() {
		attribute_map = new HashMap<String, Object>();
	}

	private PageBuilder(String jsp_simple_path, DataTrain train) {
		this();

		// Save parameters
		mJSPPath = JSP_PATH_PRE + jsp_simple_path + JSP_PATH_POST;

		mDataTrain = train;

		mAppData = mDataTrain.getAppDataManager().get();

		mPageTemplateBean = new PageTemplateBean(jsp_simple_path, mAppData,
				mDataTrain.getAuthManager());

		mPageTemplateBean.setTitle(mAppData.getTitle());
	}

	public <T extends Enum<T>> PageBuilder(T page, String jsp_servlet_path) {
		this(jsp_servlet_path + "/" + page.name(), DataTrain.getAndStartTrain());
	}

	public <T extends Enum<T>> PageBuilder(T page, String jsp_servlet_path,
			DataTrain dataTrain) {
		this(jsp_servlet_path + "/" + page.name(), dataTrain);
	}

	private void initialPageSetup(HttpServletRequest req,
			HttpServletResponse resp) {
		// Copy over any success message or redirect url
		req.setAttribute(ATTR_SUCCESS_MESSAGE,
				req.getParameter((PARAM_SUCCESS_MESSAGE)));
		req.setAttribute(ATTR_REDIRECT_URL,
				req.getParameter((PARAM_REDIRECT_URL)));
	}

	public void passOffToJsp(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		// Set some default attributes
		initialPageSetup(req, resp);

		// Apply all saved attributes
		for (Entry<String, Object> entry : attribute_map.entrySet())
			req.setAttribute(entry.getKey(), entry.getValue());

		// Insert page template data bean
		mPageTemplateBean.apply(req);

		// Insert authentication data bean
		AuthBean.getBean(req.getSession(), mDataTrain).apply(req);

		// Do actual forward
		RequestDispatcher d = req.getRequestDispatcher(mJSPPath);
		d.forward(req, resp);
	}

	public PageBuilder setAttribute(String name, Object value) {
		attribute_map.put(name, value);

		return this;
	}

	public PageBuilder setPageTitle(String title) {
		mPageTemplateBean.setTitle(title + " - " + mAppData.getTitle());

		return this;
	}

}
