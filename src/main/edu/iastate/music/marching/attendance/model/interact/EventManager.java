package edu.iastate.music.marching.attendance.model.interact;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.Event.Type;
import edu.iastate.music.marching.attendance.model.store.ModelFactory;

public class EventManager extends AbstractManager {

	DataTrain train;

	private static final Logger log = Logger.getLogger(EventManager.class
			.getName());

	public EventManager(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	// public boolean create(Type type, DateTime start, DateTime end) {
	// Event event = ModelFactory.newEvent(type, start, end);
	// ObjectDatastore od = this.train.getDataStore();
	// od.store(event);
	// return true;
	// }

	// /**
	// * Checks if two DateTime ranges intersect at all.
	// *
	// * @param e1
	// * @param e2
	// * @return
	// */
	// private boolean overlaps(Event e1, Event e2) {
	// boolean a = e1.getStart().after(e2.getEnd());
	// boolean b = e1.getEnd().before(e2.getStart());
	// // if a and b are both true, then no instant in time is found in both
	// // DateTime ranges.
	// return !(a || b);
	// }

	public Event createOrUpdate(Type type, Interval interval) {
		DateTimeZone zone = this.train.appData().get().getTimeZone();
		
		Event event = null;

		List<Event> similarEvents = getExactlyAt(interval);
		// List<Event> overlapping = new ArrayList<Event>();
		for (Event e : similarEvents) {
			if (e.getType().equals(type)) {
				if (event != null) {
					// Already found an event for this time and type
					log.log(Level.WARNING, "Duplicate " + e.getType()
							+ "events found for time period " + interval);
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
		event = ModelFactory.newEvent(type, interval);

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
		AbsenceManager ac = train.absences();
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
					if (a.getInterval(zone).equals(e.getInterval(zone))) {
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
					if (e.getInterval(zone).contains(a.getCheckout(zone))) {
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
				// doing this every time is probably horrible for
				// performance. Another possible solution is keeping a list of
				// all students and updating each one before returning
				this.train.getDataStore().refresh(event);
				this.train.users().update(a.getStudent());
			}
		}

		this.train.getDataStore().refresh(event);
		return event;
	}

	public void delete(Event event, boolean deleteLinkedAbsences) {
		DateTimeZone zone = this.train.appData().get().getTimeZone();
		ObjectDatastore od = this.train.getDataStore();
		AbsenceManager ac = this.train.absences();

		List<Absence> todie = ac.getAll(event);
		if (deleteLinkedAbsences) {
			// Just remove all of them
			ac.remove(todie);
		} else {
			// Still need to unlink them
			for (Absence absence : todie) {
				absence.setEvent(null);
				// ac.updateAbsence(absence);
			}
		}

		Interval eventInterval = event.getInterval(zone);

		od.delete(event);

		// Can't upDateTime until we actually delete the event otherwise
		// the absence will just link to the same event
		if (!deleteLinkedAbsences) {
			for (Absence absence : todie) {
				ac.updateAbsence(absence);
			}
		}

		/*
		 * If the deleted event had conflicted with another one before then all
		 * the absences for that date/time range would be unanchored. Now that
		 * one is deleted we can try to link them all up again
		 */
		ac.tryLinkUnanchoredInInterval(eventInterval);
		// od.delete() returns void, so we don't really have anything to return
		// here, either
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

	/**
	 * Gets a list of events that contain the specified time
	 */
	public List<Event> getContains(DateTime time) {
		RootFindCommand<Event> find = this.train.getDataStore().find()
				.type(Event.class);

		Date d = time.toDate();

		find.addFilter(Event.FIELD_START, FilterOperator.LESS_THAN_OR_EQUAL, d);
		find.addFilter(Event.FIELD_END, FilterOperator.GREATER_THAN_OR_EQUAL, d);
		return find.returnAll().now();
	}

	public Integer getCount() {
		return this.train.getDataStore().find().type(Event.class).returnCount()
				.now();
	}

	/**
	 * Gets a list of events whose start and end coincide exactly with the given
	 * interval
	 */
	public List<Event> getExactlyAt(Interval interval) {
		RootFindCommand<Event> find = this.train.getDataStore().find()
				.type(Event.class);

		find.addFilter(Event.FIELD_START, FilterOperator.EQUAL, interval
				.getStart().toDate());
		find.addFilter(Event.FIELD_END, FilterOperator.EQUAL, interval.getEnd()
				.toDate());
		return find.returnAll().now();
	}

	public List<Event> readAll() {
		return this.train.getDataStore().find().type(Event.class).returnAll()
				.now();
	}

}
