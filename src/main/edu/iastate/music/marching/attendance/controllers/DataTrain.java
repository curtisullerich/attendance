package edu.iastate.music.marching.attendance.controllers;

import java.io.Serializable;
import java.util.Collections;
import java.util.concurrent.Future;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.standard.StandardObjectDatastore;

import edu.iastate.music.marching.attendance.model.ModelFactory;

public class DataTrain {

	private StandardObjectDatastore datastore = null;

	private Cache cache = null;

	/**
	 * Current transaction
	 */
	private Track track = null;

	public static DataTrain getAndStartTrain() {
		DataTrain train = new DataTrain();

		// Force version model object to
		// be created and associated
		train.getVersionController().getCurrent();

		return train;
	}

	public DataTrain() {
		datastore = ModelFactory.newObjectDatastore();
	}

	public AbsenceController getAbsenceController() {
		return new AbsenceController(this);
	}

	public AppDataController getAppDataController() {
		return new AppDataController(this);
	}

	public AuthController getAuthController() {
		return new AuthController(this);
	}

	public DataController getDataController() {
		return new DataController(this);
	}

	public EventController getEventController() {
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

	public VersionController getVersionController() {
		return new VersionController(this);
	}

	public MigrationController getMigrationController() {
		return new MigrationController(this);
	}

	StandardObjectDatastore getDataStore() {
		return this.datastore;
	}

	@SuppressWarnings("unchecked")
	<CachedType> CachedType loadFromCache(Class<CachedType> clazz, int id) {
		Cache cache = getMemCache();

		if (cache != null)
			return (CachedType) cache.get(new CacheKey(clazz, id));
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	<CachedType> boolean updateCache(int id, CachedType object) {

		if (object == null) {
			return false;
		}

		Class<?> clazz = object.getClass();

		Cache cache = getMemCache();

		if (cache != null) {
			cache.put(new CacheKey(clazz, id), object);

			return true;
		}

		return false;
	}

	private static class CacheKey implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8879640511900898415L;

		public CacheKey(Class<?> clazz, int id) {
			this.clazz = clazz.getName();
			this.id = id;
		}

		public String clazz;
		public int id;
	}

	private Cache getMemCache() {
		if (this.cache == null) {
			try {
				CacheFactory cacheFactory = CacheManager.getInstance()
						.getCacheFactory();
				this.cache = cacheFactory.createCache(Collections.emptyMap());
			} catch (CacheException e) {
				return null;
			}
		}

		return this.cache;
	}

	// For implementing transaction support
	// Object getAncestor() {
	// return getVersionController().getCurrent();
	// }

	<T> RootFindCommand<T> find(Class<T> type) {
		return getDataStore().find().type(type);
	}

	Key getTie(java.lang.reflect.Type type, long id) {
		return new KeyFactory.Builder(this.datastore.getConfiguration()
				.typeToKind(type), id).getKey();
	}

	Key getTie(java.lang.reflect.Type type, String id) {
		return new KeyFactory.Builder(this.datastore.getConfiguration()
				.typeToKind(type), id).getKey();
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
