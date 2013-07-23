package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.beans.PageTemplateBean;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.util.PageBuilder;

public class PublicServlet extends AbstractBaseServlet {

	private enum Page {
		bugreport, faq;
	}

	private static final long serialVersionUID = 9184644423443871525L;

	private static final String SERVLET_PATH = "public";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Page page = parsePathInfo(req.getPathInfo(), Page.class);
		if (page == null) {
			show404(req, resp);
			return;
		}

		switch (page) {
		case faq:
			faq(req, resp);
			break;
		default:
			ErrorServlet.showError(req, resp, 404);
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Page page = parsePathInfo(req.getPathInfo(), Page.class);
		if (page == null) {
			show404(req, resp);
			return;
		}

		switch (page) {
		case bugreport:
			reportBug(req, resp);
			break;
		default:
			ErrorServlet.showError(req, resp, 404);
		}

	}

	private void faq(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PageBuilder page = new PageBuilder(Page.faq, SERVLET_PATH);
		page.setPageTitle("Attendance FAQ");
		page.passOffToJsp(req, resp);
	}

	private void reportBug(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain datatrain = DataTrain.getAndStartTrain();

		String severity = req.getParameter("Severity");
		String description = req.getParameter("Description");
		String redir = req.getParameter("Redirect");
		User user = datatrain.getAuthManager().getCurrentUser(req.getSession());
		String userAgent = req.getHeader("User-Agent");
		String error_messages = req.getParameter("error_messages");
		String field_values = req.getParameter("FieldValues");
		String success_message = req.getParameter("success_message");
		boolean mobileSite = PageTemplateBean.onMobileSite(req.getSession());

		if (error_messages != null && !error_messages.equals("[]")) {
			description = "\nError Messages: " + error_messages + "\n"
					+ description + "\n";
		}

		if (field_values != null) {
			description = "\nForm Field Values: " + field_values + "\n"
					+ description + "\n";
		}

		if (success_message != null && !success_message.equals("")) {
			description = "\nSuccess Message: " + success_message + "\n"
					+ description + "\n";
		}

		datatrain.getDataManager().sendBugReportEmail(user, severity, redir,
				userAgent, mobileSite, description);

		String append = "?";
		if (redir.contains("?")) {
			append = "&";
		}
		if (redir.equals("")) {
			redir = "/";
		}

		resp.sendRedirect(redir + append
				+ "success_message=Bug+report+submitted+successfully.+Thanks!");
	}
}
