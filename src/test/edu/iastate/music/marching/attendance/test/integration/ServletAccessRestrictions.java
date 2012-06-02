package edu.iastate.music.marching.attendance.test.integration;

import static org.mockito.Mockito.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

import edu.iastate.music.marching.attendance.servlets.AuthServlet;

public class ServletAccessRestrictions {
	
	@Test
	public void AuthServletTest()
	{
		HttpServletRequest req = mock(HttpServletRequest.class);
		HttpServletResponse resp = mock(HttpServletResponse.class);
		
		AuthServlet servlet = new AuthServlet();
		
		//servlet.service(arg0, arg1);
		
		// TODO
		
	}

}
