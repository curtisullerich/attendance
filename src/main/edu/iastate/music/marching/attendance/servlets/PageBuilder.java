package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.App;
import edu.iastate.music.marching.attendance.beans.AuthBean;
import edu.iastate.music.marching.attendance.beans.PageTemplateBean;

public class PageBuilder {

	private static final String JSP_PATH_PRE = "/WEB-INF/";

	private static final String JSP_PATH_POST = ".jsp";

	private HttpServletRequest mRequest;

	private String mJSPPath;

	private ServletResponse mResponse;

	private PageTemplateBean mPageBean;

	private PageBuilder(String jsp_path) {
		// Save parameters
		mJSPPath = jsp_path;

		mPageBean = new PageTemplateBean(jsp_path);

		mPageBean.setTitle(App.getTitle());
	}

	public <T extends Enum<T>> PageBuilder(T page, String jsp_servlet_path) {
		this(JSP_PATH_PRE + jsp_servlet_path + "/" + page.name()
				+ JSP_PATH_POST);
	}

	public PageBuilder setPageTitle(String title) {
		mPageBean.setTitle(title + " - " + App.getTitle());

		return this;
	}

	public PageBuilder setAttribute(String name, Object value) {
		mRequest.setAttribute(name, value);

		return this;
	}

	public void passOffToJsp(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Insert page template data bean
		mPageBean.apply(request);

		// Insert authentication data bean
		AuthBean.getBean(request.getSession()).apply(request);

		// Do actual forward
		request.getRequestDispatcher(mJSPPath).forward(request, response);
	}

}
