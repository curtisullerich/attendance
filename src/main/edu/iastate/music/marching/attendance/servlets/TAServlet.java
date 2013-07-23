package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.UserManager;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.util.PageBuilder;

public class TAServlet extends AbstractBaseServlet {

	private enum Page {
		index, setranks;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -9160409137196393008L;

	private static final String SERVLET_PATH = "ta";

	public static final String INDEX_URL = pageToUrl(Page.index, SERVLET_PATH);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl(req));
			return;
		} else if (!isLoggedIn(req, resp, User.Type.TA)) {
			resp.sendRedirect(ErrorServlet.getLoginFailedUrl(req));
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case index:
				showIndex(req, resp);
				break;
			case setranks:
				showSetRanks(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl());
			return;
		} else if (!isLoggedIn(req, resp, User.Type.TA)) {
			resp.sendRedirect(ErrorServlet.getLoginFailedUrl(req));
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case setranks:
				postSetRanks(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}

	}

	private String getIndexURL() {
		return pageToUrl(Page.index, getInitParameter("path"));
	}

	private void postSetRanks(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();

		UserManager uc = train.getUsersManager();

		List<User> students = uc.get(User.Type.Student);

		List<String> errors = new LinkedList<String>();

		for (User s : students) {
			try {
				String rank = req.getParameter(s.getId());
				if (rank != null && !rank.equals("")) {
					s.setRank(rank);
					uc.update(s);
				} else if (rank == null) {
					throw new Exception("User not listed");
				}
			} catch (Exception e) {
				errors.add(e.getMessage() + ": " + s.getId()
						+ " - rank not updated");
			}
		}

		if (errors.size() == 0) {
			String url = getIndexURL() + "?success_message="
					+ URLEncoder.encode("All ranks set successfully", "UTF-8");
			url = resp.encodeRedirectURL(url);
			resp.sendRedirect(url);
		} else {
			PageBuilder page = new PageBuilder(Page.setranks, SERVLET_PATH);

			page.setAttribute("error_messages", errors);

			page.setAttribute("students", students);

			page.setPageTitle("Set Ranks");

			page.passOffToJsp(req, resp);

		}

	}

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PageBuilder page = new PageBuilder(Page.index, SERVLET_PATH);

		page.setAttribute("success_message",
				req.getParameter("success_message"));

		page.setPageTitle("Staff");
		page.setAttribute("StatusMessage", DataTrain.getAndStartTrain()
				.getAppDataManager().get().getStatusMessage());

		page.passOffToJsp(req, resp);
	}

	private void showSetRanks(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		DataTrain train = DataTrain.getAndStartTrain();
		PageBuilder page = new PageBuilder(Page.setranks, SERVLET_PATH);

		List<User> students = train.getUsersManager().get(User.Type.Student);
		page.setAttribute("students", students);

		page.setPageTitle("Set Ranks");

		page.passOffToJsp(req, resp);
	}

}
