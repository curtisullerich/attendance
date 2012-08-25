package edu.iastate.music.marching.attendance.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.iastate.music.marching.attendance.controllers.AuthController;
import edu.iastate.music.marching.attendance.controllers.DataTrain;
import edu.iastate.music.marching.attendance.controllers.MessagingController;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.util.PageBuilder;

public class MessagingServlet extends AbstractBaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 676736626822747236L;

	private static final String SERVLET_JSP_PATH = "messaging";

	private enum Page {
		index, viewthread, handle;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl(req));
			return;
		} else if (!isLoggedIn(req, resp, getServletUserTypes())) {
			resp.sendRedirect(ErrorServlet.getLoginFailedUrl(req));
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

	private void showThread(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		DataTrain train = DataTrain.getAndStartTrain();
		MessagingController mc = train.getMessagingController();
		
		String sid = req.getParameter("id");
		long id = -1;
		if (sid != null && !sid.equals("")) {
			try {
				 id = Long.parseLong(sid);
			} catch (NumberFormatException nfe) {
				//error
			}
		} else {
			//error
		}
		
		AuthController ac = train.getAuthController();
		User current = ac.getCurrentUser(req.getSession());
		
		MessageThread mt = mc.get(id);
		if (mt == null) {
			//error
		} else {
			if (mt.getAbsenceParent() != null) {
				if (current.getType() == User.Type.Director) {
					resp.sendRedirect("/director/viewabsence?absenceid=" + mt.getAbsenceParent().getId());
				} else {
					resp.sendRedirect("/student/viewabsence?absenceid=" + mt.getAbsenceParent().getId());
				}
			} 
			if (mt.getFormParent() != null) {
				if (current.getType() == User.Type.Director) {
					resp.sendRedirect("/director/forms/view?id=" + mt.getFormParent().getId());
				} else {
					resp.sendRedirect("/student/forms/view?id=" + mt.getFormParent().getId());
				}
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		if (!isLoggedIn(req, resp)) {
			resp.sendRedirect(AuthServlet.getLoginUrl());
			return;
		} else if (!isLoggedIn(req, resp, getServletUserTypes())) {
			resp.sendRedirect(ErrorServlet.getLoginFailedUrl(req));
			return;
		}

		Page page = parsePathInfo(req.getPathInfo(), Page.class);

		if (page == null)
			ErrorServlet.showError(req, resp, 404);
		else
			switch (page) {
			case handle:
				handleThread(req, resp);
				break;
			default:
				ErrorServlet.showError(req, resp, 404);
			}
	}

	public void handleThread(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// there are two possibilities, so figure
		// out which button was pressed and handle it
		String resolved = req.getParameter("resolved");
		String id = req.getParameter("id");
		String message = req.getParameter("Message");
		long longid = Long.parseLong(id);
		DataTrain train = DataTrain.getAndStartTrain();

		MessagingController mc = train.getMessagingController();
		MessageThread mt = mc.get(longid);

		User sender = train.getAuthController()
				.getCurrentUser(req.getSession());

		if (resolved != null && !resolved.equals("")) {// resolve or unresolve

			if (resolved.equals("false")) {
				mt.setResolved(true);
				mc.update(mt);
			} else if (resolved.equals("true")) {
				mt.setResolved(false);
				mc.update(mt);
			} else {
			}
		} else if (message != null && !message.equals("")) {// add a message

			mc.addMessage(mt, sender, message);

		} else {
		}

		if (mt != null) {
			if (mt.getFormParent() != null) {
				if (sender.getType() == User.Type.Director) {
					resp.sendRedirect("/director/form/view");
				} else {
					resp.sendRedirect("/student/form/view");
				}
			} else if (mt.getAbsenceParent() != null) {
				if (sender.getType() == User.Type.Director) {
					resp.sendRedirect("/director/viewabsence");
				} else {
					resp.sendRedirect("/student/viewabsence");
				}
			}
		} else {
			ErrorServlet.showError(req, resp, 404);
		}

		// showThread(req, resp, longid, train);
	}

	// private void postMessageThread(HttpServletRequest req,
	// HttpServletResponse resp) throws ServletException, IOException {
	// long threadId = Long.parseLong(req.getParameter("Id"));
	// String message = req.getParameter("Message");
	//
	// // TODO https://github.com/curtisullerich/attendance/issues/119
	// //sanitize message?
	//
	// DataTrain train = DataTrain.getAndStartTrain();
	//
	// //Handle exceptions maybe for invalid thread id's? -Didn't do because
	// this is unused
	// MessageThread thread = train.getMessagingController().get(threadId);
	//
	// //Verify currently logged in user is a participant in the
	// // conversation, or is a director
	// if (isPartOfConversation(thread,
	// train.getAuthController().getCurrentUser(req.getSession()))) {
	// train.getMessagingController().addMessage(thread,
	// train.getAuthController().getCurrentUser(req.getSession()), message);
	// showThread(req, resp, threadId, train);
	// }
	// }

	private void showIndex(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DataTrain train = DataTrain.getAndStartTrain();

		// Handle exceptions maybe for invalid thread id's?
		List<MessageThread> resolved;
		List<MessageThread> unresolved;
		if (train.getAuthController().getCurrentUser(req.getSession())
				.getType().equals(User.Type.Director)) {
			resolved = train.getMessagingController().get(true);
			unresolved = train.getMessagingController().get(false);
		} else {
			resolved = train.getMessagingController().get(
					train.getAuthController().getCurrentUser(req.getSession()),
					true);
			unresolved = train.getMessagingController().get(
					train.getAuthController().getCurrentUser(req.getSession()),
					false);
		}

		// Verify currently logged in user is a participant in the
		// conversation, or is a director
		User current = train.getAuthController().getCurrentUser(
				req.getSession());
		if (current.getType() != User.Type.Director) {
			List<MessageThread> tempResolved = new ArrayList<MessageThread>();
			List<MessageThread> tempUnresolved = new ArrayList<MessageThread>();

			for (MessageThread thread : resolved) {
				if (train.getMessagingController().isPartOfConversation(thread,
						current)) {
					tempResolved.add(thread);
				}
			}

			for (MessageThread thread : unresolved) {
				if (train.getMessagingController().isPartOfConversation(thread,
						current)) {
					tempUnresolved.add(thread);
				}
			}
			resolved = tempResolved;
			unresolved = tempUnresolved;
		}

		List<MessageThread> nonempty_resolved = new ArrayList<MessageThread>();
		List<MessageThread> nonempty_unresolved = new ArrayList<MessageThread>();

		if (resolved != null)
			for (MessageThread mt : resolved) {
				if (mt.getMessages() != null && mt.getMessages().size() > 0)
					nonempty_resolved.add(mt);
			}
		if (unresolved != null)
			for (MessageThread mt : unresolved) {
				if (mt.getMessages() != null && mt.getMessages().size() > 0)
					nonempty_unresolved.add(mt);
			}

		Collections.sort(nonempty_resolved,
				MessageThread.SORT_LATEST_MESSAGE_DESC);
		Collections.sort(nonempty_unresolved,
				MessageThread.SORT_LATEST_MESSAGE_DESC);

		PageBuilder builder = new PageBuilder(Page.index, SERVLET_JSP_PATH);

		builder.setAttribute("resolved", nonempty_resolved);
		builder.setAttribute("unresolved", nonempty_unresolved);

		builder.passOffToJsp(req, resp);
	}

	// private void showThread(HttpServletRequest req, HttpServletResponse resp)
	// throws ServletException, IOException {
	//
	// long threadId = Long.parseLong(req.getParameter("id"));
	// showThread(req, resp, threadId, DataTrain.getAndStartTrain());
	// }

	// private void showThread(HttpServletRequest req, HttpServletResponse resp,
	// long threadId, DataTrain train) throws ServletException,
	// IOException {
	// }

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
