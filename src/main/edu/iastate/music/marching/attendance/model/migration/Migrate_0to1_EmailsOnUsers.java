package edu.iastate.music.marching.attendance.model.migration;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.datastore.Email;
import com.google.code.twig.standard.StandardObjectDatastore;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.AppData;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.Message;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.MobileDataUpload;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;
import edu.iastate.music.marching.attendance.model.legacy.Absence_V0;
import edu.iastate.music.marching.attendance.model.legacy.AppData_V0;
import edu.iastate.music.marching.attendance.model.legacy.Event_V0;
import edu.iastate.music.marching.attendance.model.legacy.Form_V0;
import edu.iastate.music.marching.attendance.model.legacy.MessageThread_V0;
import edu.iastate.music.marching.attendance.model.legacy.Message_V0;
import edu.iastate.music.marching.attendance.model.legacy.MobileDataUpload_V0;
import edu.iastate.music.marching.attendance.model.legacy.User_V0;

public class Migrate_0to1_EmailsOnUsers extends DefaultMigrationStrategy {

	public static final int FROM_VERSION = 0;

	public static final int TO_VERSION = 1;

	private static final Map<Class<?>, Class<?>> TYPE_MAP = new HashMap<Class<?>, Class<?>>();

	static {
		TYPE_MAP.put(Absence_V0.class, Absence.class);
		TYPE_MAP.put(AppData_V0.class, AppData.class);
		TYPE_MAP.put(Event_V0.class, Event.class);
		TYPE_MAP.put(Form_V0.class, Form.class);
		TYPE_MAP.put(MessageThread_V0.class, MessageThread.class);
		TYPE_MAP.put(MobileDataUpload_V0.class, MobileDataUpload.class);
		TYPE_MAP.put(User_V0.class, User.class);
		TYPE_MAP.put(Message_V0.class, Message.class);
	}

	public Migrate_0to1_EmailsOnUsers() {
		super(FROM_VERSION, TO_VERSION, TYPE_MAP);
	}

	@Override
	protected Object upgrade(Object object, Class<?> fromType, Class<?> toType,
			Type fromGenericType, Type toGenericType, StandardObjectDatastore datastore) throws MigrationException {

		if (object == null)
			return null;

		// handle users specially
		if (object instanceof User_V0)
			return upgrade((User_V0) object, datastore);

		// also app data, so we don't get multiple copies
		if (object instanceof AppData_V0)
			return upgrade((AppData_V0) object);

		return super.upgrade(object, fromType, toType, fromGenericType,
				toGenericType, datastore);
	}

	private AppData upgrade(AppData_V0 oldAppData) {
		AppData appData = ModelFactory.newAppData();

		appData.setDatastoreVersion(1);

		appData.setDirectorRegistered(oldAppData.isDirectorRegistered());
		appData.setFormSubmissionCutoff(oldAppData.getFormSubmissionCutoff());
		appData.setHashedMobilePassword(oldAppData.getHashedMobilePassword());
		appData.setTimeWorkedEmails(oldAppData.getTimeWorkedEmails());
		appData.setTimeZone(oldAppData.getTimeZone());
		appData.setTitle(oldAppData.getTitle());

		return appData;
	}

	private User upgrade(User_V0 oldUser, StandardObjectDatastore datastore) {
		
		if(null == oldUser.getType())
		{
			// Have not yet activated this instance
			datastore.activate(oldUser);
			
			if(null == oldUser.getType())
			{
				throw new IllegalStateException("Activation error");
			}
		}
		
		User.Type type = User.Type.valueOf(oldUser.getType().name());
		Email email = new Email(oldUser.getGoogleUser().getEmail());

		User user = ModelFactory
				.newUser(type, email, oldUser.getUniversityID());
		user.setFirstName(oldUser.getFirstName());
		user.setLastName(oldUser.getLastName());
		if(oldUser.getGrade() == null)
		{
			user.setGrade(null);
		} else {
			user.setGrade(User.Grade.valueOf(oldUser.getGrade().name()));
		}
		user.setMajor(oldUser.getMajor());
		user.setMinutesAvailable(oldUser.getMinutesAvailable());
		user.setRank(oldUser.getRank());
		user.setSection(User.Section.valueOf(oldUser.getSection().name()));
		user.setShowApproved(oldUser.isShowApproved());
		user.setYear(oldUser.getYear());

		return user;
	}
}
