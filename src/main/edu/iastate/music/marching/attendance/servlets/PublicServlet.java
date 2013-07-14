package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.beans.PageTemplateBean;
import edu.iastate.music.marching.attendance.model.interact.DataTrain;
import edu.iastate.music.marching.attendance.model.interact.FormManager;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.util.PageBuilder;

public class PublicServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9184644423443871525L;

	private enum Page {
		verify, bugreport, faq;
	}

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
		case verify:
			verifyFormD(req, resp);
			break;
		case faq:
			faq(req, resp);
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

	private void reportBug(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain datatrain = DataTrain.getAndStartTrain();

		String severity = req.getParameter("Severity");
		String description = req.getParameter("Description");
		String redir = req.getParameter("Redirect");
		User user = datatrain.getAuthManager().getCurrentUser(
				req.getSession());
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

	private void verifyFormD(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		FormManager fc = new FormManager(train);
		List<String> errors = new LinkedList<String>();
		String success_message = null;
		boolean isValid = true;
		String status = req.getParameter("s");
		long hashedId = 0;
		try {
			hashedId = Long.parseLong(req.getParameter("i"));
		} catch (NumberFormatException e) {
			errors.add("Unable to identify form.");
			isValid = false;
		}
		if (isValid) {
			Form f = fc.getByHashedId(hashedId);
			if (f != null) { // doesn't need to be pending
				if (status.equals("a")) {
					f.setEmailStatus(Form.Status.Approved);
					success_message = "Successfully approved the form.";
				} else {
					f.setEmailStatus(Form.Status.Denied);
					success_message = "Successfully denied the form.";
				}
				fc.update(f);
			} else {
				if (f == null)
					errors.add("Unable to identify form.");
				else {
					errors.add("Once a form is approved or denied it cannot be changed.");
					errors.add("If you made a mistake you should contact the directors.");
				}

			}
		}

		PageBuilder page = new PageBuilder(Page.verify, SERVLET_PATH);
		page.setPageTitle("Form D Validation");
		page.setAttribute("error_messages", errors);
		page.setAttribute("success_message", success_message);
		page.passOffToJsp(req, resp);
	}
}
