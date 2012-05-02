package edu.iastate.music.marching.attendance.servlets;

import edu.iastate.music.marching.attendance.servlets.AbstractBaseServlet.IPathEnum;

public class PathInfo<T> {
	
	private T mPathEnum;

	public PathInfo(T pathEnum) {
		mPathEnum = pathEnum;
	}

	public T getEnum()
	{
		return mPathEnum;
	}

}
