package edu.iastate.music.marching.attendance.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Form;
import edu.iastate.music.marching.attendance.model.MessageThread;
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
		List<Event> events = train.getEventController().get(time);
		absence.setStatus(Absence.Status.Pending);

		// else the absence is orphaned, because we can't know which one is
		// best. It'll show in unanchored and they'll have to fix it.
		if (events.size() == 1) {
			Event toLink = events.get(0);
			// now link
			absence.setEvent(toLink);

		} else {
			log.log(Level.WARNING, "Orphaned tardy being created at time: "
					+ time);
		}
		return storeAbsence(absence, student);
	}

	// TODO https://github.com/curtisullerich/attendance/issues/109
	// TEST THIS! probably by getting all and making sure there aren't any
	// other unanchoreds running around somewher
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
			// Anything beats an absence
			return false;
		case Tardy:
			switch (contester.getType()) {
			case Absence:
				// Absence always loses, unless it is approved
				if (contester.getStatus() != Absence.Status.Approved) {
					remove(contester);
					return true;
				} else {
					return false;
				}
			case Tardy:
				// Later tardy beats an earlier one
				if (!current.getDatetime().before(contester.getDatetime())) {
					if (contester.getStatus() != Absence.Status.Approved) {
						remove(contester);
						return true;
					} else {
						return false;
					}
				} else if (contester.getStatus() != Absence.Status.Approved) {
					return true;
				} else {
					return false;
				}
			case EarlyCheckOut:
				// if check IN time is after check OUT time,
				// you're going to have a bad time (completely absent)
				// Otherwise no conflict
				if (current.getDatetime().after(contester.getDatetime())) {
					// Create a new absence
					current.setType(Absence.Type.Absence);
					current.setStart(current.getEvent().getStart());
					current.setEnd(current.getEvent().getEnd());
					// remove the old EarlyCheckOut
					remove(contester);
				}
				return true;
			}
			break;
		case EarlyCheckOut:
			switch (contester.getType()) {
			case Absence:
				// Absence always loses, unless the absence is approved
				if (contester.getStatus() != Absence.Status.Approved) {
					remove(contester);
					return true;
				} else {
					return false;
				}
			case Tardy:
				// if check IN time is after check OUT time,
				// you're going to have a bad time (completely absent)
				// Otherwise no conflict
				if (contester.getDatetime().after(current.getDatetime())) {
					// Create a new absence
					current.setType(Absence.Type.Absence);
					current.setStart(current.getEvent().getStart());
					current.setEnd(current.getEvent().getEnd());
					// remove the old Tardy
					remove(contester);
				}
				// else if (contester.getStatus() == Absence.Status.Approved) {
				// return false;
				// }
				// else {
				return true;
				// }
			case EarlyCheckOut:
				// Earlier EarlyCheckOut beats a later one
				if (current.getStart().before(contester.getStart())) {
					remove(contester);
					return true;
				} else {
					// We aren't earlier, contester stays
					return false;
				}
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
		// else the absence is orphaned
		List<Event> events = train.getEventController().get(start, end);
		if (events.size() == 1) {
			absence.setEvent(events.get(0));
			// associated with this event for this student
		} else {
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
		List<Event> events = train.getEventController().get(time);

		if (events.size() == 1) {
			absence.setEvent(events.get(0));
			// associated with this event for this student
		} else {
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

		if (conflicts.size() > 2)
			LOG.severe("Absence conflicting with more than two others, this indicates a possibly inconsistant database");

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

			// absence date must fall on a valid form date repetition
			if (dateFallsOnRepetition(absence.getDatetime(), form.getStart())) {
				if (absence.getType() == Absence.Type.Absence) {
					if (!form.getStart().after(absence.getStart())
							&& !form.getEnd().before(absence.getEnd())
							&& form.getAbsenceType() == Absence.Type.Absence) {
						absence.setStatus(Absence.Status.Approved);
					}
				} else if (absence.getType() == Absence.Type.Tardy) {
					Calendar tardyTime = Calendar.getInstance();
					tardyTime.setTime(absence.getDatetime());

					Calendar formEnd = Calendar.getInstance();
					formEnd.setTime(form.getEnd());
					formEnd.add(Calendar.MINUTE, form.getMinutesToOrFrom());

					if (!absence.getDatetime().before(form.getStart())
							&& !absence.getDatetime().after(formEnd.getTime())
							&& (form.getAbsenceType() == Absence.Type.Absence || form
									.getAbsenceType() == Absence.Type.Tardy)) {
						absence.setStatus(Absence.Status.Approved);
					}
				} else if (absence.getType() == Absence.Type.EarlyCheckOut) {
					Calendar outTime = Calendar.getInstance();
					outTime.setTime(absence.getDatetime());

					Calendar formStart = Calendar.getInstance();
					formStart.setTime(form.getEnd());
					formStart.add(Calendar.MINUTE, form.getMinutesToOrFrom()
							* -1);

					if (!absence.getDatetime().before(formStart.getTime())
							&& !absence.getDatetime().before(form.getEnd())
							&& (form.getAbsenceType() == Absence.Type.Absence || form
									.getAbsenceType() == Absence.Type.EarlyCheckOut)) {
						absence.setStatus(Absence.Status.Approved);
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

	/**
	 * Used to check that the absence date falls on a weekly repetition of the
	 * form date. Needs to be tested.
	 * 
	 * @param absenceDate
	 * @param formDate
	 * @return
	 */
	private boolean dateFallsOnRepetition(Date absenceDate, Date formDate) {
		Calendar absenceCal = Calendar.getInstance();
		Calendar formCal = Calendar.getInstance();
		absenceCal.setTime(absenceDate);
		formCal.setTime(formDate);

		// TODO https://github.com/curtisullerich/attendance/issues/110
		// Handle changing year
		return (formCal.get(Calendar.DAY_OF_YEAR) - absenceCal
				.get(Calendar.DAY_OF_YEAR)) % 7 == 0;
	}

	public Absence updateAbsence(Absence absence) {

		Event linked = absence.getEvent();
		if (linked != null && absence.getType() == Absence.Type.Tardy) {
		}

		// Do some validation
		Absence resolvedAbsence = validateAbsence(absence);
		if (resolvedAbsence == null) {
			// Null resolved absence means remove from database

			try {
				this.train.getDataStore().delete(absence);
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

	private Absence storeAbsence(Absence absence, User student) {
		// First build an empty message thread and store it
		MessageThread messages = this.train.getMessagingController()
				.createMessageThread(student);
		absence.setMessageThread(messages);

		// Then do some validation
		Absence resolvedAbsence = validateAbsence(absence);
		if (resolvedAbsence != null) {
			// And check for side-effects on the absence
			checkForAutoApproval(resolvedAbsence);

			// Then do actual store
			this.train.getDataStore().store(resolvedAbsence);

			// Finally check for side-effects caused by absence
			train.getUsersController().updateUserGrade(student);

			// Done.
			return resolvedAbsence;
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

		HashSet<User> users = new HashSet<User>();
		for (Absence a : todie) {
			User u = a.getStudent();
			users.add(u);
		}
		od.deleteAll(todie);

		// Finally check for side-effects caused by absence
		for (User u : users) {
			train.getUsersController().updateUserGrade(u);
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
		MessagingController mc = this.train.getMessagingController();
		List<Absence> absences = this.get(student);

		for (Absence a : absences) {
			MessageThread mt = a.getMessageThread();

			if (mt != null) {
				mc.delete(mt);
			}
		}
		train.getDataStore().deleteAll(absences);
	}
}