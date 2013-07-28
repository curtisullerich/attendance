package edu.iastate.music.marching.attendance.test.integration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;

import edu.iastate.music.marching.attendance.model.store.User;
import edu.iastate.music.marching.attendance.servlets.DirectorServlet;
import edu.iastate.music.marching.attendance.servlets.MobileAppDataServlet;
import edu.iastate.music.marching.attendance.test.AbstractDatastoreTest;
import edu.iastate.music.marching.attendance.test.util.ServletMocks;
import edu.iastate.music.marching.attendance.test.util.Users;

public class ServletAccess extends AbstractDatastoreTest {

	@Test
	public void DirectorServlet_Student_ClassList_Test()
			throws ServletException, IOException, InstantiationException,
			IllegalAccessException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setDirectorSession(req);

		when(req.getPathInfo()).thenReturn("/classlist");

		ServletOutputStream sos = mock(ServletOutputStream.class);

		when(resp.getOutputStream()).thenReturn(sos);

		ServletMocks.doGet(MobileAppDataServlet.class, req, resp);

		// Verify data is written to the servlet output stream
		verify(sos).print("{\"error\":\"success\",\"data\":\"\"}");
	}

	@Test
	public void MobileDataServlet_TA_ClassList_Test() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setDirectorSession(req);

		when(req.getPathInfo()).thenReturn("/classlist");

		ServletOutputStream sos = mock(ServletOutputStream.class);

		when(resp.getOutputStream()).thenReturn(sos);

		ServletMocks.doGet(MobileAppDataServlet.class, req, resp);
	}

	@Test
	public void MobileDataServlet_Director_ClassList_Test()
			throws ServletException, IOException, InstantiationException,
			IllegalAccessException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setDirectorSession(req);

		when(req.getPathInfo()).thenReturn("/classlist");

		ServletOutputStream sos = mock(ServletOutputStream.class);

		when(resp.getOutputStream()).thenReturn(sos);

		ServletMocks.doGet(MobileAppDataServlet.class, req, resp);
	}

	@Test
	public void DirectorServlet_Student_Test() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setStudentSession(req);

		ServletMocks.doGet(DirectorServlet.class, req, resp);

		verifyUnauthorizedRedirect(req, resp, "director/index");
	}

	@Test
	public void DirectorServlet_TA_Test() throws ServletException, IOException,
			InstantiationException, IllegalAccessException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setTASession(req);

		ServletMocks.doGet(DirectorServlet.class, req, resp);

		verifyUnauthorizedRedirect(req, resp, "director/index");
	}

	@Test
	public void DirectorServlet_TA_Test_NoExist() throws ServletException,
			IOException, InstantiationException, IllegalAccessException {
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);

		setTASession(req);

		ServletMocks.doGet(DirectorServlet.class, req, resp);

		verifyUnauthorizedRedirect(req, resp, "director/nonexistant");
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

		ServletMocks.doGet(DirectorServlet.class, req, resp);

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

		ServletMocks.doGet(DirectorServlet.class, req, resp);

		verifyErrorRedirect(dispatcher, req, resp, 404);
	}

	private void verifyUnauthorizedRedirect(HttpServletRequest req,
			HttpServletResponse resp, String returnUrl)
			throws ServletException, IOException {
		verify(resp).sendRedirect("/error/unauthorized");
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
				Users.createStudent(getDataTrain().getUsersManager(),
						"studenttt", "123456789", "I am", "A Student", 10,
						"Being Silly", User.Section.AltoSax));
		when(req.getSession()).thenReturn(session);
		return req;
	}

	private HttpServletRequest setDirectorSession(HttpServletRequest req) {
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute("authenticated_user")).thenReturn(
				Users.createDirector(getDataTrain().getUsersManager(),
						"director", "I am", "The Director"));
		when(req.getSession()).thenReturn(session);
		return req;
	}

	private HttpServletRequest setTASession(HttpServletRequest req) {
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute("authenticated_user")).thenReturn(
				Users.createTA(getDataTrain().getUsersManager(), "ta",
						"123456789", "I am", "A TA", 10, "Being Silly",
						User.Section.AltoSax));
		when(req.getSession()).thenReturn(session);
		return req;
	}
}
