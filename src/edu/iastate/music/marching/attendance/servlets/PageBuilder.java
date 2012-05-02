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

	private HttpServletRequest mRequest;

	private String mJSPPath;

	private ServletResponse mResponse;

	private PageTemplateBean mPageBean;

	private PageBuilder(String jsp_path, HttpServletRequest req,
			ServletResponse resp) {
		// Save parameters
		mJSPPath = jsp_path;
		mRequest = req;
		mResponse = resp;

		mPageBean = new PageTemplateBean(jsp_path);

		mPageBean.setTitle(App.getTitle());
	}

	public <T extends Enum<T>> PageBuilder(AbstractBaseServlet servlet, T page,
			HttpServletRequest req, HttpServletResponse resp) {
		this(servlet.getJspPath() + "/" + page.name(), req, resp);

	}

	public void show() throws ServletException, IOException {

		// Insert page template data bean
		mPageBean.apply(mRequest);

		// Insert authentication data bean
		new AuthBean(mRequest.getSession()).apply(mRequest);

		// Do actual forward
		mRequest.getRequestDispatcher("/WEB-INF/" + mJSPPath + ".jsp").forward(
				mRequest, mResponse);
	}

	public PageBuilder setPageTitle(String title) {
		mPageBean.setTitle(title + " - " + App.getTitle());

		return this;
	}

	public PageBuilder setAttribute(String name, Object value) {
		mRequest.setAttribute(name, value);

		return this;
	}

}
