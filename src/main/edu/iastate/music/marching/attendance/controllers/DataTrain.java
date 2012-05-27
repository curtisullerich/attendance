package edu.iastate.music.marching.attendance.controllers;

import java.util.concurrent.Future;

import com.google.appengine.api.datastore.Transaction;
import com.google.code.twig.annotation.AnnotationObjectDatastore;
import com.google.code.twig.standard.StandardObjectDatastore;

import edu.iastate.music.marching.attendance.model.ModelFactory;

public class DataTrain {

	private AnnotationObjectDatastore datastore = null;
	/**
	 * Current transaction
	 */
	private Track track = null;

	public static DataTrain getAndStartTrain() {
		return new DataTrain();
	}

	public DataTrain() {
		datastore = ModelFactory.newObjectDatastore();
	}

	public AbsenceController getAbsencesController() {
		return new AbsenceController(this);
	}

	public AppDataController getAppDataController() {
		return new AppDataController(this);
	}

	public AuthController getAuthController() {
		return new AuthController(this);
	}

	public EventController getEventsController() {
		return new EventController(this);
	}

	public FormController getFormsController() {
		return new FormController(this);
	}

	public MobileDataController getMobileDataController() {
		return new MobileDataController(this);
	}

	public UserController getUsersController() {
		return new UserController(this);
	}
	
	public MessagingController getMessagingController() {
		return new MessagingController(this);
	}

	StandardObjectDatastore getDataStore() {
		return this.datastore;
	}

	/**
	 * Starts a new data store transaction
	 * 
	 * Note that only one transaction can be active at a time
	 */
	public Track switchTracks() {

		this.track = new Track(getDataStore().beginTransaction());
		return this.track;
	}

	/**
	 * Non-recursive begin transaction
	 * 
	 * Starts a new data store transaction if none is running, or simply reuses
	 * the existing one in a wrapper if it is active.
	 * 
	 * This is because only one transaction can be running at a time
	 * 
	 * @return Internal transaction for use in a controller. Must either be
	 *         committed to or canceled within the controller and never be
	 *         passed out of the controller
	 */
	Track switchTracksInternal() {
		if (this.track != null && this.track.isActive())
			return new InternalTrack(this.track);
		else
			return switchTracks();
	}

	/**
	 * Transaction wrapper to keep in the train theme
	 * 
	 */
	public class Track {

		private Transaction transaction;

		protected Track(Transaction t) {
			this.transaction = t;
		}

		/**
		 * Commits this transaction to the datastore
		 */
		public void bendIronBack() {
			this.transaction.commit();
		}

		/**
		 * Commits this transaction to the datastore asynchronously
		 */
		public Future<Void> bendIronBackAsync() {
			return this.transaction.commitAsync();
		}

		/**
		 * 
		 * @return The application id for the Transaction.
		 */
		public String getApp() {
			return this.transaction.getApp();
		}

		/**
		 * 
		 * @return The globally unique identifier for the Transaction.
		 */
		public String getId() {
			return this.transaction.getId();
		}

		public boolean isActive() {
			return this.transaction.isActive();
		}

		/**
		 * End this transaction without committing any of the changes made
		 */
		public void derail() {
			this.transaction.rollback();
		}

		/**
		 * End this transaction asynchronously without committing any of the
		 * changes made
		 */
		public Future<Void> derailAsync() {
			return this.transaction.rollbackAsync();
		}

		protected Transaction getTransaction() {
			return this.transaction;
		}

	}

	/**
	 * Transaction wrapper to keep in the train theme
	 * 
	 * For internal use in controllers when the outside code has already started
	 * a transaction when the controller tries to start one
	 * 
	 * Thus to deal with this nested transaction problem, we just linearize
	 * everything and let the outside code call the actual commit to commit
	 * everything.
	 * 
	 */
	private class InternalTrack extends Track {

		protected InternalTrack(Track t) {
			super(t.getTransaction());
		}

		/**
		 * Does nothing, this is a nested transaction
		 */
		public void bendIronBack() {
			// Do nothing, this is a nested transaction
		}

		/**
		 * Does nothing, this is a nested transaction
		 */
		public Future<Void> bendIronBackAsync() {
			// Do nothing, this is a nested transaction
			return null;
		}

	}

}
