package edu.iastate.music.marching.attendance.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;

public class AbsenceController extends AbstractController {

	private static final Logger log = Logger.getLogger(AbsenceController.class
			.getName());

	private DataTrain train;

	private static final Logger LOG = Logger.getLogger(AbsenceController.class
			.getName());

	public AbsenceController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public Absence createOrUpdateTardy(User student, Date time) {

		if (student == null) {
			throw new IllegalArgumentException(
					"Tried to create absence for null user");
		}

		Absence absence = ModelFactory.newAbsence(Absence.Type.Tardy, student);
		absence.setDatetime(time);
		// Associate with event
		if (!tryLink(absence)) {
			log.log(Level.WARNING, "Orphaned tardy being created at time: "
					+ time);
		}

		return storeAbsence(absence, student);
	}

	public List<Absence> getUnanchored() {
		List<Absence> absences = this.train.find(Absence.class)
				.addFilter(Absence.FIELD_EVENT, FilterOperator.EQUAL, null)
				.returnAll().now();
		return absences;
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
				if (contester.getDatetime().equals(current.getDatetime())) {
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
				if (contester.getDatetime().equals(current.getDatetime())) {
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

	/**
	 * This is deprecated because the start and end date are not sufficient to
	 * identify a single event. Type must also be specified. This is needed in
	 * the current implementation of the mobile app, though.
	 * 
	 * @param student
	 * @param start
	 * @param end
	 * @return
	 */
	@Deprecated
	public Absence createOrUpdateAbsence(User student, Date start, Date end) {

		if (student == null) {
			throw new IllegalArgumentException(
					"Tried to create absence for null user");
		}
		if (!end.after(start)) {
			// this should handle the case of equality
			throw new IllegalArgumentException(
					"End date was not after start date.");
		}

		Absence absence = ModelFactory
				.newAbsence(Absence.Type.Absence, student);
		absence.setStart(start);
		absence.setEnd(end);
		absence.setStatus(Absence.Status.Pending);

		// Associate with event
		if (!tryLink(absence)) {
			log.log(Level.WARNING,
					"Orphaned absence being created for timespan: " + start
							+ " to " + end);
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
			throw new IllegalArgumentException(
					"Tried to create absence for null user");
		}

		Absence absence = ModelFactory
				.newAbsence(Absence.Type.Absence, student);
		absence.setStatus(Absence.Status.Pending);

		if (e != null && e.getStart() != null && e.getEnd() != null) {
			absence.setEvent(e);
			absence.setStart(e.getStart());
			absence.setEnd(e.getEnd());
			// associated with this event for this student
		} else {
			log.log(Level.SEVERE,
					"Orphaned absence being created, bad event passed in, its id was "
							+ ((e == null) ? "null-event" : e.getId()));
		}

		return storeAbsence(absence, student);
	}

	public Absence createOrUpdateEarlyCheckout(User student, Date time) {

		if (student == null)
			throw new IllegalArgumentException(
					"Tried to create absence for null user");

		Absence absence = ModelFactory.newAbsence(Absence.Type.EarlyCheckOut,
				student);
		absence.setDatetime(time);
		absence.setStatus(Absence.Status.Pending);

		// Associate with event
		if (!tryLink(absence)) {
			log.log(Level.WARNING,
					"Orphaned early checkout being created for time: " + time);
		}

		return storeAbsence(absence, student);
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

		AbsenceController ac = train.getAbsenceController();
		List<Absence> conflicts = ac.getAll(linked, student);

		if (conflicts.size() > 2) {
			LOG.severe("Absence conflicting with more than two others, this indicates a possibly inconsistant database");
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

	/**
	 * This does not store any changes in the database!
	 * 
	 * Note that this is only valid for forms A, B, and C. Form D has separate
	 * validation that occurs when a Form D is approved AND verified.
	 * 
	 * @param absence
	 * @return
	 */
	private void checkForAutoApproval(Absence absence) {
		Event linked = absence.getEvent();
		if (absence.getStatus() != Absence.Status.Pending) {
			// only pending absences can be autoapproved
			return;
		}
		if (linked == null) {
			return;
			// throw new IllegalArgumentException(
			// "Can't validate an orphaned Absence.");
		}
		User student = absence.getStudent();
		if (student == null) {
			return;
			// throw new IllegalArgumentException(
			// "Can't validate Absence with null student");
		}

		List<Form> forms = train.getFormsController().get(student);

		for (Form form : forms) {
			checkForAutoApproval(absence, form);
		}
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
	private Absence checkForAutoApproval(Absence absence, Form form) {

		TimeZone timezone = this.train.getAppDataController().get()
				.getTimeZone();

		if (form.getStatus() != Form.Status.Approved) {
			// must be approved!
			return absence;
		}
		if (absence.getStatus() != Absence.Status.Pending) {
			// only pending absences can be autoapproved
			return absence;
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
		case A:
			// Performance absence request
			if (absence.getEvent().getType() != Event.Type.Performance) {
				// nope!
				return absence;
			} else {
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

				if (fmt.format(absence.getEvent().getDate()).equals(
						fmt.format(form.getStart()))) {
					// TODO
					// https://github.com/curtisullerich/attendance/issues/107
					// it wouldn't be hard to implement an AutoApproved
					// type for documentation purposes, since this and the form
					// controller are the only places it should happen
					absence.setStatus(Absence.Status.Approved);
				}
			}
			break;
		case B:
			Event e = absence.getEvent();
			if (e != null && e.getType() == Event.Type.Rehearsal) {

				Calendar dayOfAbsence = Calendar.getInstance(timezone);
				dayOfAbsence.setTime((Date) (absence.getStart().clone()));

				dayOfAbsence.set(Calendar.HOUR, 0);
				dayOfAbsence.set(Calendar.HOUR_OF_DAY, 0);
				dayOfAbsence.set(Calendar.MINUTE, 0);
				dayOfAbsence.set(Calendar.SECOND, 0);
				dayOfAbsence.set(Calendar.MILLISECOND, 0);

				// take the date from the absence and the time-of-day from the
				// form
				Calendar formTimeStart = Calendar.getInstance(timezone);
				formTimeStart.setTime(absence.getStart());
				Calendar formstarttmp = Calendar.getInstance(timezone);
				formstarttmp.setTime(form.getStart());
				formTimeStart.set(Calendar.HOUR,
						formstarttmp.get(Calendar.HOUR));
				formTimeStart.set(Calendar.HOUR_OF_DAY,
						formstarttmp.get(Calendar.HOUR_OF_DAY));
				formTimeStart.set(Calendar.MINUTE,
						formstarttmp.get(Calendar.MINUTE));
				formTimeStart.set(Calendar.SECOND,
						formstarttmp.get(Calendar.SECOND));
				formTimeStart.set(Calendar.MILLISECOND,
						formstarttmp.get(Calendar.MILLISECOND));
				// to include the buffer
				formTimeStart.add(Calendar.MINUTE, form.getMinutesToOrFrom()
						* -1);

				Calendar formTimeEnd = Calendar.getInstance(timezone);

				// this just sets the fields to lock into the necessary DATE.
				// For the current implementation, this should always be the
				// same
				// result, because we don't support multi-day events
				if (absence.getEnd() == null) {
					formTimeEnd.setTime(absence.getStart());
				} else {
					formTimeEnd.setTime(absence.getEnd());
				}

				// take the date from the absence and the time-of-day from the
				// form
				Calendar formendtmp = Calendar.getInstance(timezone);
				formendtmp.setTime(form.getEnd());
				formTimeEnd.set(Calendar.HOUR, formendtmp.get(Calendar.HOUR));
				formTimeEnd.set(Calendar.HOUR_OF_DAY,
						formendtmp.get(Calendar.HOUR_OF_DAY));
				formTimeEnd.set(Calendar.MINUTE,
						formendtmp.get(Calendar.MINUTE));
				formTimeEnd.set(Calendar.SECOND,
						formendtmp.get(Calendar.SECOND));
				formTimeEnd.set(Calendar.MILLISECOND,
						formendtmp.get(Calendar.MILLISECOND));
				// to include the buffer
				formTimeEnd.add(Calendar.MINUTE, form.getMinutesToOrFrom());

				Calendar formDateStart = Calendar.getInstance(timezone);
				formDateStart.setTime(form.getStart());
				formDateStart.set(Calendar.HOUR, 0);
				formDateStart.set(Calendar.HOUR_OF_DAY, 0);
				formDateStart.set(Calendar.MINUTE, 0);
				formDateStart.set(Calendar.SECOND, 0);
				formDateStart.set(Calendar.MILLISECOND, 0);

				Calendar formDateEnd = Calendar.getInstance(timezone);
				formDateEnd.setTime(form.getEnd());
				formDateEnd.set(Calendar.HOUR, 0);
				formDateEnd.set(Calendar.HOUR_OF_DAY, 0);
				formDateEnd.set(Calendar.MINUTE, 0);
				formDateEnd.set(Calendar.SECOND, 0);
				formDateEnd.set(Calendar.MILLISECOND, 0);

				// absence date must fall on a valid form date repetition (same
				// day of week)
				if (formTimeStart.get(Calendar.DAY_OF_WEEK) > 0
						&& formTimeStart.get(Calendar.DAY_OF_WEEK) < 8
						&& form.getDayAsInt() == formTimeStart
								.get(Calendar.DAY_OF_WEEK)
						&& !dayOfAbsence.getTime().after(formDateEnd.getTime())
						&& !dayOfAbsence.getTime().before(
								formDateStart.getTime())) {
					if (absence.getType() == Absence.Type.Absence) {
						if (formTimeEnd != null
								&& !formTimeStart.getTime().after(
										absence.getStart())
								&& !formTimeEnd.getTime().before(
										absence.getEnd())
								&& form.getAbsenceType() == Absence.Type.Absence) {
							absence.setStatus(Absence.Status.Approved);
						} else if (!formTimeEnd.getTime().before(
								absence.getStart())
								&& !formTimeStart.getTime().after(
										absence.getEnd())
								&& form.getAbsenceType() == Absence.Type.Absence) {
							// this case is specifically to cover the time when
							// a class conflict does not actually cover an
							// entire rehearsal, but it's close enough that
							// students don't go

							// in prose, this means that if there is any overlap
							// of the form B start and end (including buffer)
							// and the rehearsal, then the absence will be
							// approved
							absence.setStatus(Absence.Status.Approved);
						}
					} else if (absence.getType() == Absence.Type.Tardy) {

						if (formTimeEnd != null
								&& !absence.getDatetime().before(
										formTimeStart.getTime())
								&& !absence.getDatetime().after(
										formTimeEnd.getTime())
								&& (form.getAbsenceType() == Absence.Type.Absence || form
										.getAbsenceType() == Absence.Type.Tardy)) {
							absence.setStatus(Absence.Status.Approved);
						}

						// if event and form times overlap, and if absence time
						// falls within the event
						if (!absence.getEvent().getEnd()
								.before(absence.getDatetime())
								&& !absence.getEvent().getStart()
										.after(absence.getDatetime())
								&& !formTimeEnd.getTime().before(
										absence.getEvent().getStart())
								&& !formTimeStart.getTime().after(
										absence.getEvent().getEnd())
								&& form.getAbsenceType() == Absence.Type.Absence) {
							absence.setStatus(Absence.Status.Approved);
						}

					} else if (absence.getType() == Absence.Type.EarlyCheckOut) {

						if (formTimeEnd != null
								&& !absence.getDatetime().before(
										formTimeStart.getTime())
								&& !absence.getDatetime().after(// TODO
										formTimeEnd.getTime())
								&& (form.getAbsenceType() == Absence.Type.Absence || form
										.getAbsenceType() == Absence.Type.EarlyCheckOut)) {
							absence.setStatus(Absence.Status.Approved);
						}

						// if the time is during the event and the form is
						// approved, then approve the form
						if (!absence.getEvent().getEnd()
								.before(absence.getDatetime())
								&& !absence.getEvent().getStart()
										.after(absence.getDatetime())
								&& !formTimeEnd.getTime().before(
										absence.getEvent().getStart())
								&& !formTimeStart.getTime().after(
										absence.getEvent().getEnd())
								&& form.getAbsenceType() == Absence.Type.Absence) {
							absence.setStatus(Absence.Status.Approved);
						}

					}
				}
			}
			break;
		case C:
			if (absence.getEvent().getType() != Event.Type.Rehearsal) {
				// nope!
				return absence;
			} else {
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
				// This is a lot of date logic and is probably wrong somehow.
				// Test it extensively.
				if (fmt.format(absence.getEvent().getDate()).equals(
						fmt.format(form.getStart()))) {
					if (absence.getType() == Absence.Type.Absence) {
						// the dates matched above, so it's approved
						if (form.getAbsenceType() == Absence.Type.Absence) {
							absence.setStatus(Absence.Status.Approved);
						} else {
							// throw new IllegalArgumentException(
							// "Wrong form type!");
						}
					} else if (absence.getType() == Absence.Type.Tardy) {
						if (!absence.getDatetime().after(form.getEnd())
								&& !absence.getDatetime().before(
										form.getStart())) {

							// absence forms autoapprove tardies
							if (form.getAbsenceType() == Absence.Type.Tardy
									|| form.getAbsenceType() == Absence.Type.Absence) {
								absence.setStatus(Absence.Status.Approved);
							} else {
								// throw new IllegalArgumentException(
								// "Wrong form type!");
							}
						}
					} else if (absence.getType() == Absence.Type.EarlyCheckOut) {
						if (!absence.getDatetime().before(form.getStart())
								&& !absence.getDatetime().after(form.getEnd())) {

							// absence forms autoapprove earlycheckouts
							if (form.getAbsenceType() == Absence.Type.EarlyCheckOut
									|| form.getAbsenceType() == Absence.Type.Absence) {
								absence.setStatus(Absence.Status.Approved);
							} else {
								// throw new IllegalArgumentException(
								// "Wrong form type!");
							}
						}
					}
				}
			}
			break;
		case D:
			// this does not auto-approve here. It does that upon an update of a
			// Form D in the forms controller
			break;
		}
		return absence;
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
			checkForAutoApproval(resolvedAbsence);

			// Then do actual store
			this.train.getDataStore().storeOrUpdate(resolvedAbsence);
			// this.train.getDataStore().store(resolvedAbsence);

			// Finally check for side-effects caused by absence
			train.getUsersController().update(resolvedAbsence.getStudent());

			// Success.
			return resolvedAbsence;
		}
	}

	private boolean tryLink(Absence absence) {
		Date time = absence.getDatetime();
		Date start = absence.getStart();
		Date end = absence.getEnd();
		List<Event> events;

		switch (absence.getType()) {
		case Absence:
			// Associate with event
			// else the absence is orphaned
			events = train.getEventController().get(start, end);
			if (events.size() == 1) {
				absence.setEvent(events.get(0));
				// associated with this event for this student
				return true;
			} else {
				return false;
			}
		case EarlyCheckOut:
			// Associate with event
			events = this.train.getEventController().get(time);

			if (events.size() == 1) {
				absence.setEvent(events.get(0));
				// associated with this event for this student
				return true;
			} else {
				return false;
			}
		case Tardy:
			// Associate with event
			events = train.getEventController().get(time);
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

	private Absence storeAbsence(Absence absence, User student) {
		train.getDataStore().store(absence);

		// Then do some validation
		Absence resolvedAbsence = validateAbsence(absence);
		if (resolvedAbsence != null) {
			// And check for side-effects on the absence
			checkForAutoApproval(resolvedAbsence);

			// Then do actual store
			this.train.getDataStore().storeOrUpdate(resolvedAbsence);

			// Finally check for side-effects caused by absence
			train.getUsersController().updateUserGrade(student);

			// Done.
			return resolvedAbsence;
		} else {
			train.getDataStore().delete(absence);
		}

		// Invalid absence returns null because it doesn't store
		return null;
	}

	public List<Absence> get(User student) {
		List<Absence> absences = this.train
				.find(Absence.class)
				.addFilter(Absence.FIELD_STUDENT, FilterOperator.EQUAL, student)
				.returnAll().now();
		return absences;
	}

	public Absence get(long id) {
		return this.train.getDataStore().load(
				this.train.getTie(Absence.class, id));
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

	public Integer getCount(Absence.Type type) {

		RootFindCommand<Absence> find = this.train.find(Absence.class);
		find.addFilter(Absence.FIELD_TYPE, FilterOperator.EQUAL, type);

		return find.returnCount().now();
	}

	public Integer getCount() {
		return this.train.find(Absence.class).returnCount().now();
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

	public void remove(List<Absence> todie) {
		ObjectDatastore od = this.train.getDataStore();
		UserController uc = this.train.getUsersController();

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
		train.getUsersController().update(todie.getStudent());
	}

	void delete(User student) {
		List<Absence> absences = this.get(student);
		train.getDataStore().deleteAll(absences);
	}

	/**
	 * Method to be used by the Event Controller to update absences when an
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
	protected void linkAbsenceEvent(List<Event> events, Date eventStart,
			Date eventEnd) {
		for (Absence a : getUnanchored()) {
			// For absences both the start and the end have to be inside of the
			// event times, otherwise
			// im gonna use l33t h4x0r skills since the tardies and
			// earlycheckouts store their time in the
			// start field
			boolean checkBothTimes = a.getType() == Absence.Type.Absence;
			if (eventStart.compareTo(a.getStart()) > 0
					|| (checkBothTimes && a.getEnd().compareTo(eventEnd) > 0)
					|| (!checkBothTimes && a.getStart().compareTo(eventEnd) > 0)) {
				// Skip this one because the times were different so the event
				// deleted couldn't
				// have conflicted with the correct event for this absence
				continue;
			}
			Event linkedEvent = null;
			for (Event e : events) {
				// If the event times wrap around the absence time
				if (e.getStart().compareTo(a.getStart()) <= 0
						&& ((checkBothTimes && a.getEnd().compareTo(e.getEnd()) <= 0) || (!checkBothTimes && a
								.getStart().compareTo(e.getEnd()) <= 0))) {
					if (linkedEvent == null) {
						linkedEvent = e;
					} else {
						// Multiple events so we can't do anything about it
						linkedEvent = null;
						break;
					}
				}
			}
			if (linkedEvent != null) {
				a.setEvent(linkedEvent);
				train.getUsersController().update(a.getStudent());
				this.train.getDataStore().storeOrUpdate(a);
			}
		}
	}
}