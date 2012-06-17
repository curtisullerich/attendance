package edu.iastate.music.marching.attendance.controllers;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

	private DataTrain train;

	public AbsenceController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public Absence createOrUpdateTardy(User student, Date time) {

		if (student == null) {
			throw new IllegalArgumentException(
					"Tried to create absence for null user");
		}
		// TODO : Check for exact duplicates

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

			// well, stack overflow says this works :)
			// this changes the type from Tardy to Absence if there's a tardy
			// that's 30 or more minutes late. Per request from Mr. Staub.
			if (absence.getStart().getTime() - toLink.getStart().getTime() >= 30 * 60 * 1000) {
				absence.setType(Absence.Type.Absence);
			}
		}
		return storeAbsence(absence, student);
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
	 * If these two absences are not in conflict, it returns null;
	 * 
	 * The Absence returned will still need to be stored in the database!
	 * 
	 * @param one
	 * @param two
	 * @return
	 */
	private Absence resolveConflict(Absence one, Absence two) {
		if (!one.getEvent().equals(two.getEvent())) {
			// not a conflict
			return null;
		}
		if (!one.getStudent().equals(two.getStudent())) {
			// not a conflict
			return null;
		}
		switch (one.getType()) {
		case Absence:
			switch (two.getType()) {
			case Absence:
				remove(one);
				return two;
			case Tardy:
				remove(one);
				return two;
			case EarlyCheckOut:
				remove(one);
				return two;
			}
			break;
		case Tardy:
			switch (two.getType()) {
			case Absence:
				remove(two);
				return one;
			case Tardy:
				if (one.getStart().before(two.getStart())) {
					remove(one);
					return two;
				} else {
					remove(two);
					return one;
				}
			case EarlyCheckOut:
				// if check IN time is before check OUT time, there's no
				// conflict
				if (one.getStart().before(two.getStart())) {
					// no conflict
					return null;
				} else {
					remove(two);
					one.setType(Absence.Type.Absence);
					return one;
				}
			}
			break;
		case EarlyCheckOut:
			switch (two.getType()) {
			case Absence:
				remove(two);
				return one;
			case Tardy:
				// if check OUT time is after check IN time, there's no conflict
				if (one.getStart().after(two.getStart())) {
					return null;
				} else {
					remove(one);
					// TODO, does this leave everything else intact? Do I need
					// to change the start and end times as well?
					two.setType(Absence.Type.Absence);
					return two;
				}
			case EarlyCheckOut:
				if (one.getStart().before(two.getStart())) {
					remove(two);
					return one;
				} else {
					remove(one);
					return two;
				}
			}
			break;
		}
		// should never reach this point!
		throw new IllegalArgumentException(
				"Types of absences were somehow wrong.");
	}

	public Absence createOrUpdateAbsence(User student, Date start, Date end) {

		if (student == null) {
			throw new IllegalArgumentException(
					"Tried to create absence for null user");
		}
		// TODO:Check for exact duplicates--whoever put this here, did you mean
		// duplicate dates? If so, click here and press ctrl+D. repeat above
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
		}

		return storeAbsence(absence, student);
	}

	public Absence createOrUpdateEarlyCheckout(User student, Date time) {

		if (student == null)
			throw new IllegalArgumentException(
					"Tried to create absence for null user");

		// TODO : Check for exact duplicates

		Absence absence = ModelFactory.newAbsence(Absence.Type.EarlyCheckOut,
				student);
		absence.setDatetime(time);
		absence.setStatus(Absence.Status.Pending);

		// Associate with event
		List<Event> events = train.getEventController().get(time);

		if (events.size() == 1) {
			absence.setEvent(events.get(0));
			// associated with this event for this student
		}
		// else the absence is orphaned

		return storeAbsence(absence, student);
	}

	/**
	 * "Validate" isn't the right word, but this method checks things: It
	 * ensures that conflicts with existing absences are resolved by removing
	 * the offending absence(s). It checks for forms that may auto-approve this
	 * absence. If any other all-Absence checks would be necessary, add them to
	 * this method and DOCUMENT THEM.
	 * 
	 * This will return the updated Absence WITHOUT PUTTING IT IN THE DATABASE.
	 * Its internal calls to resolveConflict(), however, will potentially remove
	 * conflicts from the database.
	 * 
	 * @param absence
	 * @param student
	 */
	private Absence validateAbsence(Absence absence) {
		Event linked = absence.getEvent();
		if (linked == null) {
			return absence;
		}
		User student = absence.getStudent();
		if (student == null) {
			throw new IllegalArgumentException(
					"Can't validate Absence with null student");
		}

		AbsenceController ac = train.getAbsenceController();
		List<Absence> conflicts = ac.getAll(linked, student);

		// this loop should remove any conflicting absences and leave us with
		// the correct absence to store once we're done
		for (Absence a : conflicts) {
			Absence temp = resolveConflict(a, absence);

			// one case that will occur sometimes is where there are two
			// absences for the same user for the same event that are both
			// valid. (A tardy and an early check out.) In this case,
			// resolveConflict() will return null. We don't reassign the
			// reference here then, so we still have a reference to the new
			// Absence. This ensures that we don't end up throwing it away by
			// accident.
			if (temp != null) {
				absence = temp;
			}
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
	private Absence checkForAutoApproval(Absence absence) {
		Event linked = absence.getEvent();
		if (linked == null) {
			throw new IllegalArgumentException(
					"Can't validate an orphaned Absence.");
		}
		User student = absence.getStudent();
		if (student == null) {
			throw new IllegalArgumentException(
					"Can't validate Absence with null student");
		}

		List<Form> forms = train.getFormsController().get(student);

		for (Form form : forms) {
			checkForAutoApproval(absence, form);
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
	 * @param form
	 * @return
	 */
	private Absence checkForAutoApproval(Absence absence, Form form) {

		if (form.getStatus() != Form.Status.Approved) {
			// must be approved!
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
				// TODO this approves all three types of absences, even though
				// the form assumes a full absence. That cool?
				// TODO is this the best way to compare that two dates are the
				// same?
				SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
				// TODO verify that event.getDate() gets the date of the event
				// and that form.getStart() always gets the day on which the
				// form is applicable. This is relevant for the other types as
				// well.
				if (fmt.format(absence.getEvent().getDate()).equals(
						fmt.format(form.getStart()))) {
					// TODO it wouldn't be hard to implement an AutoApproved
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
							&& !form.getEnd().before(absence.getEnd())) {
						absence.setStatus(Absence.Status.Approved);
					}
				} else if (absence.getType() == Absence.Type.Tardy) {
					Calendar tardyTime = Calendar.getInstance();
					tardyTime.setTime(absence.getDatetime());

					Calendar formEnd = Calendar.getInstance();
					formEnd.setTime(form.getEnd());
					formEnd.add(Calendar.MINUTE, form.getMinutesToOrFrom());

					if (!absence.getDatetime().before(form.getStart())
							&& !absence.getDatetime().after(formEnd.getTime())) {
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
							&& !absence.getDatetime().before(form.getEnd())) {
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
				SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
				// This is a lot of date logic and is probably wrong somehow.
				// Test it extensively.
				if (fmt.format(absence.getEvent().getDate()).equals(
						fmt.format(form.getStart()))) {
					if (absence.getType() == Absence.Type.Absence) {
						// the dates matched above, so it's approved
						absence.setStatus(Absence.Status.Approved);
					} else if (absence.getType() == Absence.Type.Tardy) {
						if (!absence.getDatetime().after(form.getEnd())
								&& !absence.getDatetime().before(
										form.getStart())) {
							absence.setStatus(Absence.Status.Approved);
						}
					} else if (absence.getType() == Absence.Type.EarlyCheckOut) {
						if (!absence.getDatetime().before(form.getStart())
								&& !absence.getDatetime().after(form.getEnd())) {
							absence.setStatus(Absence.Status.Approved);
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

		// TODO this logic assumes that both dates are in the same year!
		return (formCal.get(Calendar.DAY_OF_YEAR) - absenceCal
				.get(Calendar.DAY_OF_YEAR)) % 7 == 0;
	}

	// TODO I don't like that this is public. There's no validation going on
	// here. We could call validateAbsence(absence) but that would throw an
	// exception if you're storing an orphaned or student-less Absence
	// (currently, at least)
	public void updateAbsence(Absence absence) {
		absence = validateAbsence(absence);
		absence = checkForAutoApproval(absence);
		this.train.getDataStore().update(absence);
	}

	private Absence storeAbsence(Absence absence, User student) {
		ObjectDatastore od = this.train.getDataStore();

		// First build an empty message thread and store it
		MessageThread messages = ModelFactory.newMessageThread();
		od.store(messages);
		absence.setMessageThread(messages);

		// Then do actual store
		od.store(absence);
		absence = validateAbsence(absence);
		absence = checkForAutoApproval(absence);

		train.getUsersController().updateUserGrade(student);

		return absence;
	}

	public List<Absence> get(User student) {
		return this.train
				.getDataStore()
				.find()
				.type(Absence.class)
				.addFilter(Absence.FIELD_STUDENT, FilterOperator.EQUAL, student)
				.returnAll().now();
	}

	public Absence get(long id) {
		return this.train.getDataStore().load(Absence.class, id);
	}

	public List<Absence> get(Absence.Type... types) {

		RootFindCommand<Absence> find = this.train.getDataStore().find()
				.type(Absence.class);
		find.addFilter(Absence.FIELD_TYPE, FilterOperator.IN,
				Arrays.asList(types));

		return find.returnAll().now();
	}

	public Integer getCount(Absence.Type type) {

		RootFindCommand<Absence> find = this.train.getDataStore().find()
				.type(Absence.class);
		find.addFilter(Absence.FIELD_TYPE, FilterOperator.EQUAL, type);

		return find.returnCount().now();
	}

	// TODO doesn't work
	public List<Absence> getUnanchored() {
		return this.train.getDataStore().find().type(Absence.class)
				.addFilter(Absence.FIELD_STUDENT, FilterOperator.EQUAL, null)
				.returnAll().now();
	}

	public List<Absence> getAll() {
		return this.train.getDataStore().find().type(Absence.class).returnAll()
				.now();
	}

	/**
	 * Returns a list of all Absences that have the given Event associate with
	 * them.
	 * 
	 * @param associated
	 * @return
	 */
	public List<Absence> getAll(Event event) {

		return this.train.getDataStore().find().type(Absence.class)
				.addFilter(Absence.FIELD_EVENT, FilterOperator.EQUAL, event)
				.returnAll().now();
	}

	public List<Absence> getAll(Event event, User student) {

		return this.train
				.getDataStore()
				.find()
				.type(Absence.class)
				.addFilter(Absence.FIELD_STUDENT, FilterOperator.EQUAL, student)
				.addFilter(Absence.FIELD_EVENT, FilterOperator.EQUAL, event)
				.returnAll().now();
	}

	public void remove(List<Absence> todie) {
		ObjectDatastore od = this.train.getDataStore();
		od.deleteAll(todie);
	}

	public void remove(Absence todie) {
		ObjectDatastore od = this.train.getDataStore();
		od.delete(todie);
	}
}