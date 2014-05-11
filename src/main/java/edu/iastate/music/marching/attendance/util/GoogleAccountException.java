package edu.iastate.music.marching.attendance.util;

public class GoogleAccountException extends IllegalArgumentException {

	public enum Type {
		None, Invalid
	}

	private Type type;

	/**
	 * 
	 */
	private static final long serialVersionUID = 5059826102465006401L;

	public GoogleAccountException(String message, Type type) {
		super(message);
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

}
