package edu.iastate.music.marching.attendance.test.integration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;

import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.servlets.DirectorServlet;
import edu.iastate.music.marching.attendance.test.AbstractServletTest;

public class ServletAccess extends AbstractServletTest {
	
	@Test
	public void DirectorServlet_Student_Test() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setStudentSession(req);

		doGet(DirectorServlet.class, req, resp);

		verifyLoginRedirect(req, resp, "director/index");
	}

	@Test
	public void DirectorServlet_TA_Test() throws ServletException, IOException,
			InstantiationException, IllegalAccessException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setTASession(req);

		doGet(DirectorServlet.class, req, resp);

		verifyLoginRedirect(req, resp, "director/index");
	}

	@Test
	public void DirectorServlet_TA_Test_NoExist() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setTASession(req);

		doGet(DirectorServlet.class, req, resp);

		verifyLoginRedirect(req, resp, "director/nonexistant");
	}

	@Test
	public void DirectorServlet_DirectorIndex_Test() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setDirectorSession(req);

		when(req.getPathInfo()).thenReturn("/index");

		RequestDispatcher dispatcher = setupForwardTo(req, resp,
				"/WEB-INF/director/index.jsp");

		doGet(DirectorServlet.class, req, resp);

		verifyForwardTo(dispatcher, req, resp);
	}

	@Test
	public void DirectorServlet_DirectorNoExist_Test() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setDirectorSession(req);

		when(req.getPathInfo()).thenReturn("/nonexistant");

		RequestDispatcher dispatcher = setupErrorRedirect(req, resp, 404);

		doGet(DirectorServlet.class, req, resp);

		verifyErrorRedirect(dispatcher, req, resp, 404);
	}

	private void verifyLoginRedirect(HttpServletRequest req,
			HttpServletResponse resp, String returnUrl)
			throws ServletException, IOException {
		verify(resp).sendRedirect("/auth/login");
	}

	private RequestDispatcher setupErrorRedirect(HttpServletRequest req,
			HttpServletResponse resp, int code) throws ServletException,
			IOException {
		RequestDispatcher dispatcher = mock(RequestDispatcher.class);
		when(req.getRequestDispatcher("/WEB-INF/error/index.jsp")).thenReturn(
				dispatcher);

		return dispatcher;
	}

	private void verifyErrorRedirect(RequestDispatcher dispatcher,
			HttpServletRequest req, HttpServletResponse resp, int code)
			throws ServletException, IOException {
		verify(dispatcher).forward(req, resp);
	}

	private RequestDispatcher setupForwardTo(HttpServletRequest req,
			HttpServletResponse resp, String jsp_path) throws ServletException,
			IOException {
		RequestDispatcher dispatcher = mock(RequestDispatcher.class);
		when(req.getRequestDispatcher(jsp_path)).thenReturn(dispatcher);
		return dispatcher;
	}

	private void verifyForwardTo(RequestDispatcher dispatcher,
			HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		verify(dispatcher).forward(req, resp);
	}

	private HttpServletRequest setStudentSession(HttpServletRequest req) {
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute("authenticated_user")).thenReturn(
				createStudent(getDataTrain().getUsersController(), "studenttt",
						121, "I am", "A Student", 10, "Being Silly",
						User.Section.AltoSax));
		when(req.getSession()).thenReturn(session);
		return req;
	}

	private HttpServletRequest setDirectorSession(HttpServletRequest req) {
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute("authenticated_user")).thenReturn(
				createDirector(getDataTrain().getUsersController(), "director",
						123, "I am", "The Director"));
		when(req.getSession()).thenReturn(session);
		return req;
	}

	private HttpServletRequest setTASession(HttpServletRequest req) {
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute("authenticated_user")).thenReturn(
				createTA(getDataTrain().getUsersController(), "studenttt", 121,
						"I am", "A Student", 10, "Being Silly",
						User.Section.AltoSax));
		when(req.getSession()).thenReturn(session);
		return req;
	}
}
