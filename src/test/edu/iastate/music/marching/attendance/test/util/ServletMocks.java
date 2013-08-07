package edu.iastate.music.marching.attendance.test.util;

import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	private static void commonSetup(HttpServletRequest req,
			HttpServletResponse resp) {
		when(req.getHeader("User-Agent"))
				.thenReturn(
						"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5");
	}

}
