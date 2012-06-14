package edu.iastate.music.marching.attendance.controllers;

import java.util.Arrays;
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

		if (events.size() == 1) {
			Event toLink = events.get(0);
			// before, linking, need to check other absences that are linked to
			// this event for this user for states of conflict
			List<Absence> absences = getAll(events.get(0), student);

			// now link
			absence.setEvent(toLink);

			// well, stack overflow says this works :)
			if (absence.getStart().getTime() - toLink.getStart().getTime() >= 30 * 60 * 1000) {
				absence.setType(Absence.Type.Absence);
			}

			// this should not include the absence we're adding now
			if (absences.size() > 0) {
				// conflict
				for (Absence a : absences) {
					// so after we're done with all these, whatever absence
					// should be stored in the case of conflicts
					//
					// All intermediate absences that WERE in conflict are
					// removed inside this method!
					Absence temp = resolveConflict(a, absence);
					if (temp != null) {
						absence = temp;
					}
				}
			} else {
				// we're good. Store it.
			}
			return storeAbsence(absence, student);
		} else {
			// else the absence is orphaned, because we can't know which one is
			// best. It'll show in unanchored and they'll have to fix it.
			return storeAbsence(absence,student);
		}
	}

	/**
	 * Given two absences, it checks to make sure that they're linked to the
	 * same event and have the same student, then applies the ruleset that
	 * dictates which absence takes precedence and returns it.
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
				remove(two);
				return one;
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

		if (student == null)
			throw new IllegalArgumentException(
					"Tried to create absence for null user");

		// TODO : Check for exact duplicates

		Absence absence = ModelFactory
				.newAbsence(Absence.Type.Absence, student);
		absence.setStart(start);
		absence.setEnd(end);

		// Associate with event
		List<Event> events = train.getEventController().get(start, end);
		if (events.size() == 1)
			absence.setEvent(events.get(0));
		// else the absence is orphaned
		absence.setStatus(Absence.Status.Pending);
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

		if (events.size() == 1)
			absence.setEvent(events.get(0));
		// else the absence is orphaned

		return storeAbsence(absence, student);
	}

	public void updateAbsence(Absence absence) {
		this.train.getDataStore().update(absence);
	}
	
	public Absence autoApprove(Absence absence) {
		//TODO check against forms for auto approval.
		
		FormController fc = this.train.getFormsController();
		if (absence != null ) {
			if (absence.getStudent() != null) {
				List<Form> forms = fc.get(absence.getStudent());
				for (Form f :forms) {
					autoApprove(absence, f);
				}
			} else {
				throw new IllegalArgumentException("Student was null.");
			}
		} else {
			throw new IllegalArgumentException("Absence was null.");
		}
		return absence;
	}
	
	private Absence autoApprove(Absence absence, Form form) {
		if (form.getStatus() == Form.Status.Approved){
			switch (form.getType()) {
			case A:
				
				break;
			case B:
				
				break;
			case C:
				
				break;
			case D:
				
				break;
			}
		} else {
			//does not apply!
		}
		//TODO check if the form meets a condition that approves this absence
		return absence;
	}

	private Absence storeAbsence(Absence absence, User student) {
		ObjectDatastore od = this.train.getDataStore();

		// First build an empty message thread and store it
		MessageThread messages = ModelFactory.newMessageThread();
		od.store(messages);
		absence.setMessageThread(messages);

		//check against forms to see if this can be autoapproved
		autoApprove(absence);
		
		// Then do actual store
		od.store(absence);
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
