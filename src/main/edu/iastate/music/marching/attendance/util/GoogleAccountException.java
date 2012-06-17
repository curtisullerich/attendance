package edu.iastate.music.marching.attendance.util;


public class GoogleAccountException extends IllegalArgumentException {

	private Type type;
	
	public GoogleAccountException(String message, Type type)
	{
		super(message);
		this.type = type;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5059826102465006401L;
	
	public enum Type {
		None, Invalid
	}
	public Type getType() {
		return this.type;
	}

}
