package edu.iastate.music.marching.attendance.beans;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

public class PageTemplateBean implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1624959847216686921L;

	/**
	 * Attribute name used when applying this bean to a request as an attribute
	 * in method apply
	 */
	public static final String ATTRIBUTE_NAME = "pagetemplate";

	private String title;

	private HttpServletRequest mRequest;

	private String mJSPPath;

	private String mPathInfo;

	/** No-arg constructor always for a bean 
	 * @param mJSPPath */
	public PageTemplateBean(String jsp_path) {
		mJSPPath = jsp_path;
	}
	
	public String getJspath() {
		return mJSPPath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void apply(ServletRequest request) {
		if (request != null)
			request.setAttribute(ATTRIBUTE_NAME, this);
	}
}
