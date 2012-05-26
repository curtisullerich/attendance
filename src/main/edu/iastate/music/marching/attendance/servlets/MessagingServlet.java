package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.MessagingController;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.User;

public class MessagingServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 676736626822747236L;

	private static final String SERVLET_JSP_PATH = "messaging";

	private enum Page {
		index, viewthread;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!isLoggedIn(req, resp, getServletUserTypes())) {
			resp.sendRedirect(AuthServlet.URL_LOGIN);
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);
		if (page == null) {
			show404(req, resp);
			return;
		}

		switch (page) {
		case index:
			showIndex(req, resp);
			break;
		case viewthread:
			showThread(req, resp);
			break;
		default:
			ErrorServlet.showError(req, resp, 404);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		if (!isLoggedIn(req, resp, getServletUserTypes())) {
			resp.sendRedirect(AuthServlet.URL_LOGIN);
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case viewthread:
				handleThread(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}
	}

	private void handleThread(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//there are two possibilities, so figure 
		//out which button was pressed and handle it
		String resolved = req.getParameter("resolved");
		String id = req.getParameter("id");
		String message = req.getParameter("Message");
		long longid = Long.parseLong(id);
		DataTrain train = DataTrain.getAndStartTrain();

		if (resolved != null && !resolved.equals("")) {//resolve or unresolve
			MessagingController mc = train.getMessagingController();
			MessageThread mt = mc.get(longid);
			
			if (resolved.equals("false")) {
				mt.setResolved(true);
				mc.update(mt);
			} else if (resolved.equals("true")) {
				mt.setResolved(false);
				mc.update(mt);
			} else {
				//?TODO
			}
		} else if (message != null && !message.equals("")) {//add a message
			MessagingController mc = train.getMessagingController();
			MessageThread mt = mc.get(longid);
			User sender = AuthController.getCurrentUser(req.getSession());
			mc.addMessage(mt, sender, message);
			
		} else {
			//?TODO 
		}
		showThread(req, resp, longid, train);
	}

	private void postMessageThread(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		long threadId = Long.parseLong(req.getParameter("Id"));
		String message = req.getParameter("Message");
		
		// TODO sanitize message?
		
		DataTrain train = DataTrain.getAndStartTrain();

		// TODO Handle exceptions maybe for invalid thread id's?
		MessageThread thread = train.getMessagingController().get(threadId);

		// TODO Verify currently logged in user is a participant in the
		// conversation,
		// or is a director
		
		train.getMessagingController().addMessage(thread, AuthController.getCurrentUser(req.getSession()), message);

		showThread(req, resp, threadId, train);
	}

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();

		// TODO Handle exceptions maybe for invalid thread id's?
		List<MessageThread> threads;
		if(AuthController.getCurrentUser(req.getSession()).getType().equals(User.Type.Director))
			threads = train.getMessagingController().getAll();
		else
			threads = train.getMessagingController().get(
				AuthController.getCurrentUser(req.getSession()));

		// TODO Verify currently logged in user is a participant in the
		// conversation,
		// or is a director

		PageBuilder builder = new PageBuilder(Page.index, SERVLET_JSP_PATH);

		builder.setAttribute("threads", threads);

		builder.passOffToJsp(req, resp);
	}

	private void showThread(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		long threadId = Long.parseLong(req.getParameter("id"));
		showThread(req, resp, threadId, DataTrain.getAndStartTrain());
	}

	private void showThread(HttpServletRequest req, HttpServletResponse resp,
			long threadId, DataTrain train) throws ServletException,
			IOException {

		// TODO Handle exceptions maybe for invalid thread id's?
		MessageThread thread = train.getMessagingController().get(threadId);

		// TODO Verify currently logged in user is a participant in the
		// conversation,
		// or is a director

		PageBuilder builder = new PageBuilder(Page.viewthread, SERVLET_JSP_PATH);

		builder.setAttribute("thread", thread);

		builder.passOffToJsp(req, resp);
	}

	/**
	 * This servlet can be used for multiple user types, this grabs the type of
	 * user this specific servlet instance is for
	 * 
	 * @return
	 */
	private User.Type getServletUserType() {
		String value = getInitParameter("userType");

		return User.Type.valueOf(value);
	}

	/**
	 * This servlet can be used for multiple user types, this grabs the type of
	 * user this specific servlet instance can be accessed by
	 * 
	 * @return
	 */
	private User.Type[] getServletUserTypes() {
		User.Type userType = getServletUserType();

		// Since a TA is also a student, we also allow them access to their
		// student forms
		if (userType == User.Type.Student)
			return new User.Type[] { User.Type.Student, User.Type.TA };
		else
			return new User.Type[] { userType };
	}

	/**
	 * Have to use a method since this servlet can be mapped from different
	 * paths
	 */
	private String getIndexURL() {
		return pageToUrl(Page.index, getInitParameter("path"));
	}

}
