package edu.iastate.music.marching.attendance.util;

import java.util.LinkedList;
import java.util.List;

public class ValidationExceptions extends IllegalArgumentException {

	private LinkedList<String> errors;

	public ValidationExceptions() {
		super();

		this.errors = new LinkedList<String>();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2888806460619857762L;

	public List<String> getErrors() {
		return this.errors;
	}

	@Override
	public String getMessage() {
		StringBuilder sb = new StringBuilder();

		if (super.getMessage() != null) {
			sb.append(super.getMessage());
			sb.append('\n');
		}

		for (String s : getErrors()) {
			sb.append(s);
			sb.append('\n');
		}

		return sb.toString();
	}

}
