package edu.iastate.music.marching.attendance.model.interact;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.Lang;
import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.ModelFactory;
import edu.iastate.music.marching.attendance.model.store.User;

public class AbsenceManager extends AbstractManager {

	private DataTrain train;

	private static final Logger LOG = Logger.getLogger(AbsenceManager.class
			.getName());

	public AbsenceManager(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	/**
	 * This does not store any changes in the database!
	 * 
	 * Note that this is only valid for forms A, B, and C. Form D has separate
	 * validation that occurs when a Form D is approved AND verified.
	 * 
	 * @param absence
	 * @return
	 */
	private boolean shouldBeAutoApproved(Absence absence) {
		Event linked = absence.getEvent();
		User student = absence.getStudent();

		if (absence.getStatus() != Absence.Status.Pending) {
			// only pending absences can be auto-approved
			return false;
		}
		if (linked == null) {
			// Can only auto-approve linked absences
			return false;
		}

		if (student == null) {
			// Has to have a student associated
			return false;
		}

		// True if approved by any form
		for (Form form : train.forms().get(student)) {
			if (shouldBeAutoApproved(absence, form)) {
				return true;
			}
		}

		return false;
	}

	Interval getAbsenceInterval(Absence absence, Event event, DateTimeZone zone) {
		// this assumes that a tardy is the /first/ checkin
		// and that an eco is the /last/ checkout
		switch (absence.getType()) {
		case Absence:
			// should be the whole thing
			Interval i = absence.getInterval(zone).overlap(
					event.getInterval(zone));
			if (i == null) {
				return null;
			}
			return i;
		case EarlyCheckOut:
			DateTime checkout = absence.getCheckout(zone);
			if (!event.getInterval(zone).contains(checkout)) {
				throw new IllegalArgumentException();
			}
			return new Interval(checkout, event.getInterval(zone).getEnd());
		case Tardy:
			DateTime checkin = absence.getCheckin(zone);
			if (!event.getInterval(zone).contains(checkin)) {
				throw new IllegalArgumentException();
			}
			return new Interval(event.getInterval(zone).getStart(), checkin);
		default:
			throw new UnsupportedOperationException();
		}

	}

	Interval getEffectiveFormInterval(Absence absence, Form form,
			DateTimeZone zone) {
		int formDayOfWeek = form.getDayOfWeek().DayOfWeek;
		DateMidnight formDayOnAbsenceWeek;
		switch (absence.getType()) {
		case Absence:

			DateTime aStart = absence.getInterval(zone).getStart();
			DateTime aEnd = absence.getInterval(zone).getEnd();

			if (aStart.getDayOfWeek() != aEnd.getDayOfWeek()) {
				LOG.warning("BLERG");
			}

			formDayOnAbsenceWeek = aStart.withDayOfWeek(formDayOfWeek)
					.toDateMidnight();
			break;
		case Tardy:
			DateTime checkin = absence.getCheckin(zone);
			formDayOnAbsenceWeek = checkin.withDayOfWeek(formDayOfWeek)
					.toDateMidnight();
			break;
		case EarlyCheckOut:
			DateTime checkout = absence.getCheckout(zone);
			formDayOnAbsenceWeek = checkout.withDayOfWeek(formDayOfWeek)
					.toDateMidnight();
			break;
		default:
			throw new UnsupportedOperationException();
		}

		int minutesToOrFrom = form.getMinutesToOrFrom();
		DateTime effectiveStartTime = form.getStartTime()
				.toDateTime(formDayOnAbsenceWeek).minusMinutes(minutesToOrFrom);
		DateTime effectiveEndTime = form.getEndTime()
				.toDateTime(formDayOnAbsenceWeek).plusMinutes(minutesToOrFrom);

		Interval effectiveFormInterval = new Interval(effectiveStartTime,
				effectiveEndTime);
		return effectiveFormInterval;

	}

	/**
	 * This does not store any changes in the database!
	 * 
	 * Note that this is only valid for forms A, B, and C. Form D has separate
	 * validation that occurs when a Form D is approved AND verified.
	 * 
	 * @param absence
	 * @param form
	 * @return
	 */
	private boolean shouldBeAutoApproved(Absence absence, Form form) {

		DateTimeZone zone = this.train.appData().get().getTimeZone();

		if (form.getStatus() != Form.Status.Approved) {
			// form must be approved!
			return false;
		}
		if (absence.getStatus() != Absence.Status.Pending) {
			// only pending absences can be auto-approved
			return false;
		}

		if (form.getStudent() == null || absence.getStudent() == null) {
			throw new IllegalArgumentException(
					"Student was null in the absence or form.");
		}

		if (absence.getEvent() == null) {
			throw new IllegalArgumentException("Absence had a null event.");
		}

		if (!form.getStudent().equals(absence.getStudent())) {
			throw new IllegalArgumentException(
					"Can't check absence against a form from another student.");
		}

		switch (form.getType()) {
		case PerformanceAbsence:
			// Performance absence request
			if (absence.getEvent().getType() != Event.Type.Performance) {
				// nope!
				return false;
			} else {
				// TODO use Absence.isContainedIn(...)?
				if (form.getInterval(zone).contains(
						absence.getEvent().getInterval(zone))) {
					return true;
				}
			}
			break;
		case ClassConflict:
			Event e = absence.getEvent();

			if (e.getType() != Event.Type.Rehearsal) {
				return false;
			}

			Interval effectiveFormInterval = getEffectiveFormInterval(absence,
					form, zone);
			return Absence.isContainedIn(absence, effectiveFormInterval);
		case TimeWorked:
			// this does not auto-approve here. It does that upon an upDateTime
			// of a
			// Form D in the forms controller
			break;
		}

		return false;
	}

	/**
	 * This is deprecated because the start and end DateTime are not sufficient
	 * to identify a single event. Type must also be specified. This is needed
	 * in the current implementation of the mobile app, though.
	 * 
	 * @param student
	 * @param start
	 * @param end
	 * @return
	 */
	@Deprecated
	public Absence createOrUpdateAbsence(User student, Interval interval) {

		if (student == null) {
			throw new IllegalArgumentException(Lang.ERROR_ABSENCE_FOR_NULL_USER);
		}

		Absence absence = ModelFactory
				.newAbsence(Absence.Type.Absence, student);
		absence.setInterval(interval);
		absence.setStatus(Absence.Status.Pending);

		// Associate with event
		if (!tryLink(absence)) {
			LOG.log(Level.WARNING,
					"Orphaned absence being created for timespan: "
							+ interval.getStart() + " to " + interval.getEnd());
		}

		return storeAbsence(absence, student);
	}

	/**
	 * 
	 * @param student
	 * @param e
	 * @return
	 */
	public Absence createOrUpdateAbsence(User student, Event e) {

		if (student == null) {
			throw new IllegalArgumentException(Lang.ERROR_ABSENCE_FOR_NULL_USER);
		}

		DateTimeZone zone = this.train.appData().get().getTimeZone();

		Absence absence = ModelFactory
				.newAbsence(Absence.Type.Absence, student);
		absence.setStatus(Absence.Status.Pending);

		if (e != null) {
			absence.setEvent(e);
			absence.setInterval(e.getInterval(zone));
			// associated with this event for this student
		} else {
			LOG.log(Level.SEVERE,
					"Orphaned absence being created, bad event passed in, its id was "
							+ ((e == null) ? "null-event" : e.getId()));
		}

		return storeAbsence(absence, student);
	}

	public Absence createOrUpdateEarlyCheckout(User student, DateTime time) {

		if (student == null)
			throw new IllegalArgumentException(Lang.ERROR_ABSENCE_FOR_NULL_USER);

		Absence absence = ModelFactory.newAbsence(Absence.Type.EarlyCheckOut,
				student);
		absence.setCheckout(time);
		absence.setStatus(Absence.Status.Pending);

		// Associate with event
		if (!tryLink(absence)) {
			LOG.log(Level.WARNING,
					"Orphaned early checkout being created for time: " + time);
		}

		return storeAbsence(absence, student);
	}

	public Absence createOrUpdateTardy(User student, DateTime time) {

		if (student == null) {
			throw new IllegalArgumentException(Lang.ERROR_ABSENCE_FOR_NULL_USER);
		}

		Absence absence = ModelFactory.newAbsence(Absence.Type.Tardy, student);
		absence.setCheckin(time);
		// Associate with event
		if (!tryLink(absence)) {
			LOG.log(Level.WARNING, "Orphaned tardy being created at time: "
					+ time);
		}

		return storeAbsence(absence, student);
	}

	void delete(User student) {
		List<Absence> absences = this.get(student);
		train.getDataStore().deleteAll(absences);
	}

	public List<Absence> get(Absence.Type... types) {

		if (types == null || types.length == 0)
			throw new IllegalArgumentException(
					"Must pass at least one type to get by type");

		RootFindCommand<Absence> find = this.train.find(Absence.class);
		find.addFilter(Absence.FIELD_TYPE, FilterOperator.IN,
				Arrays.asList(types));

		return find.returnAll().now();
	}

	public Absence get(long id) {
		return this.train.getDataStore().load(
				this.train.getTie(Absence.class, id));
	}

	public List<Absence> get(User student) {
		List<Absence> absences = this.train
				.find(Absence.class)
				.addFilter(Absence.FIELD_STUDENT, FilterOperator.EQUAL, student)
				.returnAll().now();
		return absences;
	}

	public List<Absence> getAll() {
		return this.train.find(Absence.class).returnAll().now();
	}

	/**
	 * Returns a list of all Absences that have the given Event associate with
	 * them.
	 * 
	 * @param associated
	 * @return
	 */
	public List<Absence> getAll(Event event) {

		return this.train.find(Absence.class)
				.addFilter(Absence.FIELD_EVENT, FilterOperator.EQUAL, event)
				.returnAll().now();
	}

	public List<Absence> getAll(Event event, User student) {

		return this.train
				.find(Absence.class)
				.addFilter(Absence.FIELD_STUDENT, FilterOperator.EQUAL, student)
				.addFilter(Absence.FIELD_EVENT, FilterOperator.EQUAL, event)
				.returnAll().now();
	}

	public Integer getCount() {
		return this.train.find(Absence.class).returnCount().now();
	}

	public Integer getCount(Absence.Type type) {

		RootFindCommand<Absence> find = this.train.find(Absence.class);
		find.addFilter(Absence.FIELD_TYPE, FilterOperator.EQUAL, type);

		return find.returnCount().now();
	}

	public List<Absence> getUnanchored() {
		List<Absence> absences = this.train.find(Absence.class)
				.addFilter(Absence.FIELD_EVENT, FilterOperator.EQUAL, null)
				.returnAll().now();
		return absences;
	}

	/**
	 * Note that if the parameter is not in the database, this will not throw an
	 * exception!
	 * 
	 * @param todie
	 */
	public void remove(Absence todie) {
		ObjectDatastore od = this.train.getDataStore();

		od.delete(todie);

		// Finally check for side-effects caused by absence
		// this also checks the students grade
		train.users().update(todie.getStudent());
	}

	public void remove(List<Absence> todie) {
		ObjectDatastore od = this.train.getDataStore();
		UserManager uc = this.train.users();

		HashSet<User> users = new HashSet<User>();
		for (Absence a : todie) {
			User u = a.getStudent();
			users.add(u);
		}
		od.deleteAll(todie);

		// Finally check for side-effects caused by absence
		for (User u : users) {
			uc.update(u);
		}
	}

	/**
	 * Note! This method causes destructive edits to the database that cannot be
	 * fixed after releasing a reference to both parameters!
	 * 
	 * Given two absences, it checks to make sure that they're linked to the
	 * same event and that they have the same student, then applies the ruleset
	 * that dictates which absence takes precedence and returns it after
	 * removing the conflicting one.
	 * 
	 * The Absence returned will still need to be stored in the database!
	 * 
	 * @param current
	 *            New absence, which is being resolved. Could be modified
	 *            slightly during the resolving process, so if was already in
	 *            the database it should be updated
	 * @param contester
	 *            An existing absence in the database, which could be removed
	 *            from the database if the current one overrides it
	 * 
	 * @return True if current absence is non-conflicting, or could be modified
	 *         to be non-conflicting, otherwise returns false to indicate the
	 *         current absence should not be stored in the database
	 */
	private boolean resolveConflict(Absence current, Absence contester) {

		if (current.equals(contester)) {
			return true;
			// not a conflict
		}

		if (!contester.getEvent().equals(current.getEvent())) {
			// not a conflict
			return true;
		}
		if (!contester.getStudent().equals(current.getStudent())) {
			// not a conflict
			return true;
		}

		DateTimeZone zone = train.appData().get().getTimeZone();

		// Basic tenants:
		// - Anything beats an absence
		// - Later tardy beats an earlier one
		// - Earlier EarlyCheckOut beats a later one
		// - An EarlyCheckOut before and Tardy after an event
		// combines into a new super-powerful being, an absence

		switch (current.getType()) {
		case Absence:
			switch (contester.getType()) {
			case Absence:
				return false;
			case Tardy:
				return false;
			case EarlyCheckOut:
				// No conflict
				return true;
			}
			break;
		case Tardy:
			switch (contester.getType()) {
			case Absence:
				// Absence loses to tardy
				remove(contester);
				return true;
			case Tardy:
				if (contester.getCheckin(zone).equals(current.getCheckin(zone))) {
					if (contester.getStatus() == Absence.Status.Approved) {
						return false;
					} else {
						remove(contester);
						return true;
					}
				}
				// both allowed
				return true;
			case EarlyCheckOut:
				// No conflict
				return true;
			}
			break;
		case EarlyCheckOut:
			switch (contester.getType()) {
			case Absence:
				return true;
			case Tardy:
				return true;
			case EarlyCheckOut:
				if (contester.getCheckout(zone).equals(
						current.getCheckout(zone))) {
					if (contester.getStatus() == Absence.Status.Approved) {
						return false;
					} else {
						remove(contester);
						return true;
					}
				}
				return true;
			}
			break;
		}

		// should never reach this point!
		throw new IllegalArgumentException(
				"Types of absences were somehow wrong.");
	}

	private Absence storeAbsence(Absence absence, User student) {
		train.getDataStore().store(absence);

		// Then do some validation
		Absence resolvedAbsence = validateAbsence(absence);
		if (resolvedAbsence != null) {
			if (shouldBeAutoApproved(resolvedAbsence)) {
				resolvedAbsence.setStatus(Absence.Status.Approved);
			}

			// Then do actual store
			this.train.getDataStore().storeOrUpdate(resolvedAbsence);

			// Finally check for side-effects caused by absence
			train.users().update(student);

			// Done.
			return resolvedAbsence;
		} else {
			train.getDataStore().delete(absence);
		}

		// Invalid absence returns null because it doesn't store
		return null;
	}

	private boolean tryLink(Absence absence) {
		DateTimeZone zone = train.appData().get().getTimeZone();
		List<Event> events;

		switch (absence.getType()) {
		case Absence:
			Interval interval = absence.getInterval(zone);

			// Associate with event
			// else the absence is orphaned
			events = train.events().getExactlyAt(interval);
			if (events.size() == 1) {
				absence.setEvent(events.get(0));
				// associated with this event for this student
				return true;
			} else {
				return false;
			}
		case EarlyCheckOut:
			DateTime checkout = absence.getCheckout(zone);

			// Associate with event
			events = this.train.events().getContaining(checkout, 2);

			if (events.size() == 1) {
				absence.setEvent(events.get(0));
				// associated with this event for this student
				return true;
			} else {
				return false;
			}
		case Tardy:
			DateTime checkin = absence.getCheckin(zone);

			// Associate with event
			events = train.events().getContaining(checkin, 2);
			absence.setStatus(Absence.Status.Pending);

			// else the absence is orphaned, because we can't know which one is
			// best. It'll show in unanchored and they'll have to fix it.
			if (events.size() == 1) {
				Event toLink = events.get(0);
				// now link
				absence.setEvent(toLink);

				return true;
			} else {
				return false;
			}
		default:
			throw new UnsupportedOperationException();
		}
	}

	/**
	 * Method to be used by the Event Controller to upDateTime absences when an
	 * event is deleted. This will try to link together unanchored absences and
	 * events
	 * 
	 * @param events
	 *            a list of all the events in the database
	 * @param eventStart
	 *            the starting time for the event deleted
	 * @param eventEnd
	 *            the ending time for the event just deleted
	 */
	public void tryLinkUnanchoredInInterval(Interval eventInterval) {
		for (Absence a : getUnanchored()) {
			if (Absence.isContainedIn(a, eventInterval)) {
				if (tryLink(a)) {
					// Then do actual store
					this.train.getDataStore().storeOrUpdate(a);

					// Finally check for side-effects caused by absence
					train.users().update(a.getStudent());
				}
			}
		}
	}

	public Absence updateAbsence(Absence absence) {

		// See if we can link up an event now
		Event linked = absence.getEvent();
		if (linked == null) {
			tryLink(absence);
		}

		// Do some validation
		Absence resolvedAbsence = validateAbsence(absence);
		if (resolvedAbsence == null) {
			// Null resolved absence means remove from database

			try {
				remove(absence);
			} catch (IllegalArgumentException e) {
				LOG.severe("Attempted to delete absence that wasn't associated.");
			}

			// Current absence has been invalidated somehow and removed from the
			// database, so return null to indicate that
			return null;
		} else {
			// And check for side-effects on the absence
			if (shouldBeAutoApproved(resolvedAbsence)) {
				resolvedAbsence.setStatus(Absence.Status.Approved);
			}

			// Then do actual store
			this.train.getDataStore().storeOrUpdate(resolvedAbsence);
			// this.train.getDataStore().store(resolvedAbsence);

			// Finally check for side-effects caused by absence
			train.users().update(resolvedAbsence.getStudent());

			// Success.
			return resolvedAbsence;
		}
	}

	/**
	 * This method ensures that conflicts with existing absences are resolved by
	 * removing the offending absence(s) and/or slightly modifing the return
	 * absence copy
	 * 
	 * This will return the updated Absence WITHOUT PUTTING IT IN THE DATABASE.
	 * Its internal calls to resolveConflict(), however, will potentially remove
	 * conflicts from the database.
	 * 
	 * @param absence
	 * @param student
	 * @return A copy of the absence, which may be slight different from the
	 *         passed in absence, if it was modified to resolve conflicts, or
	 *         null if the absence is not valid to be stored
	 */
	private Absence validateAbsence(Absence absence) {
		// Force a copy to work on
		// absence = ModelFactory.copyAbsence(absence);

		// If no linked event, its not possible to have conflicts
		Event linked = absence.getEvent();
		if (linked == null) {
			return absence;
		}

		User student = absence.getStudent();
		if (student == null) {
			LOG.severe("Can't validate Absence with null student");
			return null;
		}

		AbsenceManager ac = train.absences();
		List<Absence> conflicts = ac.getAll(linked, student);

		if (conflicts.size() > 2) {
			LOG.severe("Absence conflicting with more than two others, this indicates a possibly inconsistent database");
		}

		// this loop should remove any conflicting absences and leave us with
		// the correct absence to store once we're done
		for (Absence other : conflicts) {
			// If we are unable to resolve contacts, don't store
			if (!resolveConflict(absence, other))
				return null;
		}

		return absence;
	}
}