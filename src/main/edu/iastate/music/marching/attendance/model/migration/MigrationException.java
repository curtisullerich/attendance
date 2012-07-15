package edu.iastate.music.marching.attendance.model.migration;

public class MigrationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7046794757850805221L;

	public MigrationException(String message, Exception cause) {
		super(message, cause);
	}

	public MigrationException(String message) {
		super(message);
	}

}
