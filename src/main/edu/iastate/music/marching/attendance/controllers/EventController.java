package edu.iastate.music.marching.attendance.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Event.Type;
import edu.iastate.music.marching.attendance.model.ModelFactory;

public class EventController extends AbstractController {

	DataTrain train;

	private static final Logger log = Logger.getLogger(EventController.class
			.getName());

	public EventController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	// public boolean create(Type type, Date start, Date end) {
	// Event event = ModelFactory.newEvent(type, start, end);
	// ObjectDatastore od = this.train.getDataStore();
	// od.store(event);
	// return true;
	// }

	// /**
	// * Checks if two date ranges intersect at all.
	// *
	// * @param e1
	// * @param e2
	// * @return
	// */
	// private boolean overlaps(Event e1, Event e2) {
	// boolean a = e1.getStart().after(e2.getEnd());
	// boolean b = e1.getEnd().before(e2.getStart());
	// // if a and b are both true, then no instant in time is found in both
	// // date ranges.
	// return !(a || b);
	// }

	public Event createOrUpdate(Type type, Date start, Date end) {
		Event event = null;

		List<Event> similarEvents = get(start, end);
		// List<Event> overlapping = new ArrayList<Event>();
		for (Event e : similarEvents) {
			if (e.getType().equals(type)) {
				if (event != null) {
					// Already found an event for this time and type
					log.log(Level.WARNING, "Duplicate " + e.getType()
							+ "events found for time period " + start + " to "
							+ end);
				}
				event = e;
			}
		}

		if (event != null) {
			// this means that we found an event already in the datastore that
			// matches all fields of this one. We don't need to add the new one.
			return event;
		}

		// Safe to create a new event
		event = ModelFactory.newEvent(type, start, end);

		// TODO https://github.com/curtisullerich/attendance/issues/63
		// Needs to be changed for events that span multiple days
		event.setDate(datetimeToJustDate(start));

		// store it, and then we'll check for linking and refresh it in the
		// database before returning
		this.train.getDataStore().store(event);

		// we need get the subset of events that have any overlap with the new
		// event so we can send them into the battle royale to try and anchor
		// absences
		// List<Event> all = getAll();
		// for (Event e : all) {
		// if (overlaps(event, e)) {
		// overlapping.add(e);
		// }
		// }
		AbsenceController ac = train.getAbsenceController();
		List<Absence> unanchored = ac.getUnanchored();

		List<Event> all = getAll();
		for (Absence a : unanchored) {
			boolean foundOne = false;
			Event one = null;
			for (Event e : all) {
				// tardies/earlycheckouts are easy.
				// absences.... need to match start and end times.
				switch (a.getType()) {
				case Absence:
					if (a.getStart().compareTo(e.getStart()) == 0
							&& a.getEnd().compareTo(e.getEnd()) == 0) {
						// match!
						if (foundOne) {
							// we can't link it, because there are multiple
							// choices
							one = null;
						} else {
							foundOne = true;
							one = e;
						}
					}
					break;
				case Tardy:
					// break omitted intentionally so we fall through to the EOC
					// logic!
				case EarlyCheckOut:
					if (!a.getDatetime().after(e.getEnd())
							&& !a.getDatetime().before(e.getStart())) {
						// must be within the event
						if (foundOne) {
							one = null;
						} else {
							foundOne = true;
							one = e;
						}
					}
					break;
				default:
					break;
				}
			}
			// we've compared this unanchored absence to all events now
			if (one != null) {
				a.setEvent(one);

				// LINK IT
				ac.updateAbsence(a);

				// TODO https://github.com/curtisullerich/attendance/issues/105
				//doing this every time is probably horrible for
				// performance. Another possible solution is keeping a list of
				// all students and updating each one before returning
				this.train.getDataStore().refresh(event);
				this.train.getUsersController().update(a.getStudent());
			}
		}

		this.train.getDataStore().refresh(event);
		return event;
	}

	public void delete(Event event, boolean deleteLinkedAbsences) {
		ObjectDatastore od = this.train.getDataStore();
		
		AbsenceController ac = train.getAbsenceController();

		List<Absence> todie = ac.getAll(event);
		if (deleteLinkedAbsences) {
			// Just remove all of them
			ac.remove(todie);
		} else {
			// Still need to unlink them
			for(Absence absence : todie)
			{
				absence.setEvent(null);
				//ac.updateAbsence(absence);
			}
		}
		
		Date start = event.getStart();
		Date end = event.getEnd();
		od.delete(event);
		
		//Can't update until we actually delete the event otherwise
		//the absence will just link to the same event
		if (!deleteLinkedAbsences) {
			for (Absence absence: todie) {
				ac.updateAbsence(absence);
			}
		}
		
		/* If the deleted event had conflicted with another one before then 
		*  all the absences for that date/time range would be unanchored.
		*  Now that one is deleted we can try to link them all up again
		*/
		ac.linkAbsenceEvent(getAll(), start, end);
		// od.delete() returns void, so we don't really have anything to return
		// here, either
	}

	public List<Event> readAll() {
		return this.train.getDataStore().find().type(Event.class).returnAll()
				.now();
	}

	public Integer getCount() {
		return this.train.getDataStore().find().type(Event.class).returnCount()
				.now();
	}

	/**
	 * Gets a list of events which are happening during given time
	 * 
	 * @param time
	 * @param end
	 * @return
	 */
	public List<Event> get(Date time) {

		// Build date for the day of the event

		// Due to limitations of the data store, we cannot filter on the start
		// and end fields,
		// so match date and them manually filter results
		RootFindCommand<Event> find = this.train.getDataStore().find()
				.type(Event.class);
		find.addFilter(Event.FIELD_DATE, FilterOperator.EQUAL,
				datetimeToJustDate(time));
		Iterator<Event> iter = find.now();

		List<Event> matchingEvents = new ArrayList<Event>();
		while (iter.hasNext()) {
			Event e = iter.next();
			if (e.getStart().compareTo(time) <= 0
					&& e.getEnd().compareTo(time) >= 0)
				matchingEvents.add(e);
		}

		return matchingEvents;
	}

	/**
	 * Gets a list of events which start and end exactly at the given times
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Event> get(Date start, Date end) {
		RootFindCommand<Event> find = this.train.getDataStore().find()
				.type(Event.class);

		find.addFilter(Event.FIELD_START, FilterOperator.EQUAL, start);
		find.addFilter(Event.FIELD_END, FilterOperator.EQUAL, end);
		return find.returnAll().now();
	}

	private Date datetimeToJustDate(Date datetime) {
		Calendar ctime = Calendar.getInstance();
		ctime.setTime(datetime);

		Calendar cdate = Calendar.getInstance();
		cdate.setTimeInMillis(0);
		cdate.set(ctime.get(Calendar.YEAR), ctime.get(Calendar.MONTH),
				ctime.get(Calendar.DATE));

		return cdate.getTime();
	}

	public Event get(long id) {
		ObjectDatastore od = this.train.getDataStore();
		Event e = od.load(this.train.getTie(Event.class, id));
		return e;
	}

	public List<Event> getAll() {
		ObjectDatastore od = this.train.getDataStore();
		return od.find().type(Event.class).returnAll().now();
	}

}
