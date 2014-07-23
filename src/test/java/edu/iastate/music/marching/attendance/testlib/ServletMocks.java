package edu.iastate.music.marching.attendance.testlib;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.iastate.music.marching.attendance.model.store.User;

public class ServletMocks {

	public static <T extends HttpServlet> void doGet(Class<T> clazz,
			HttpServletRequest req, HttpServletResponse resp)
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		commonSetup(req, resp);

		when(req.getMethod()).thenReturn("GET");

		HttpServlet s = clazz.newInstance();
		s.service(req, resp);
	}

	public static <T extends HttpServlet> void doPost(Class<T> clazz,
			HttpServletRequest req, HttpServletResponse resp)
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		commonSetup(req, resp);

		when(req.getMethod()).thenReturn("POST");

		HttpServlet s = clazz.newInstance();
		s.service(req, resp);
	}

	public static HttpServletRequest setUserSession(HttpServletRequest req, User u) {
		HttpSession session = mock(HttpSession.class);
		when(session.getAttribute("authenticated_user")).thenReturn(u);
		
		when(req.getSession()).thenReturn(session);
		
		return req;
	}

	public static HttpServletRequest setPathInfo(HttpServletRequest req, String pathInfo) {
		when(req.getPathInfo()).thenReturn(pathInfo);
		return req;
	}

	public static void setPostedContent(HttpServletRequest req, String data)
			throws IOException {
		ByteArrayInputStream realStream = new ByteArrayInputStream(
				data.getBytes("UTF-8"));
		ServletInputStream mockStream = new MockServletInputStream(realStream);
		when(req.getInputStream()).thenReturn(mockStream);
	}

	private static void commonSetup(HttpServletRequest req,
			HttpServletResponse resp) {
		when(req.getHeader("User-Agent"))
				.thenReturn(
						"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5");
	}

}
