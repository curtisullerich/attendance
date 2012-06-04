package edu.iastate.music.marching.attendance.test;

import static org.mockito.Mockito.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AbstractServletTest extends AbstractDataStoreTest {
	protected <T extends HttpServlet> void doGet(Class<T> clazz,
			HttpServletRequest req, HttpServletResponse resp)
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		commonSetup(req, resp);

		when(req.getMethod()).thenReturn("GET");

		HttpServlet s = clazz.newInstance();
		s.service(req, resp);
	}

	protected <T extends HttpServlet> void doPost(Class<T> clazz,
			HttpServletRequest req, HttpServletResponse resp)
			throws InstantiationException, IllegalAccessException,
			ServletException, IOException {

		commonSetup(req, resp);

		when(req.getMethod()).thenReturn("POST");

		HttpServlet s = clazz.newInstance();
		s.service(req, resp);
	}

	private void commonSetup(HttpServletRequest req, HttpServletResponse resp) {
		when(req.getHeader("User-Agent"))
				.thenReturn(
						"Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/536.5 (KHTML, like Gecko) Chrome/19.0.1084.52 Safari/536.5");
	}
}
