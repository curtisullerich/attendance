package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.FormController;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.util.PageBuilder;

public class PublicServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9184644423443871525L;

	private enum Page {
		verify, bugreport;
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

	private void reportBug(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String severity = req.getParameter("Severity");
		String message = req.getParameter("Message");

		// TODO probably shouldn't have put that in the appdatacontroller
		DataTrain.getAndStartTrain().getAppDataController()
				.sendBugReportEmail(severity, message);
		PageBuilder page = new PageBuilder(Page.bugreport, SERVLET_PATH);
		page.setAttribute("success_message", "Bug report submitted. Thanks!");
		page.passOffToJsp(req, resp);
	}

	private void verifyFormD(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		FormController fc = new FormController(train);
		List<String> errors = new LinkedList<String>();
		List<String> successes = new LinkedList<String>();
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
			if (f != null && f.getStatus().isPending()) {
				if (status.equals("a")) {
					f.setStatus(Form.Status.Approved);

				} else {
					f.setStatus(Form.Status.Denied);
				}
				successes.add("Successfully updated form.");
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
		page.setAttribute("success_message", successes);
		page.passOffToJsp(req, resp);
	}
}
