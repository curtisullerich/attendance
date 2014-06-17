package edu.iastate.music.marching.attendance.testlib;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletInputStream;

public class MockServletInputStream extends ServletInputStream {

	private InputStream mocked;

	public MockServletInputStream(InputStream from) {
		mocked = from;
	}

	@Override
	public int read() throws IOException {
		return mocked.read();
	}

}
