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

import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Event.Type;
import edu.iastate.music.marching.attendance.model.ModelFactory;

public class EventController extends AbstractController {

	DataTrain train;
	
	private static final Logger log = Logger
			.getLogger(EventController.class.getName());

	public EventController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	// public boolean create(Type type, Date start, Date end) {
	// Event event = ModelFactory.newEvent(type, start, end);
	// ObjectDatastore od = this.train.getDataStore();
	// od.store(event);
	// return true;
	// }

	public Event createOrUpdate(Type type, Date start, Date end) {
		Event event = null;

		List<Event> similarEvents = get(start, end);

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
		
		if(event != null)
		{
			return event;
		}
		
		// Safe to create a new event
		event = ModelFactory.newEvent(type, start, end);

		// TODO https://github.com/curtisullerich/attendance/issues/63
		// Needs to be changed for events that span multiple days
		event.setDate(datetimeToJustDate(start));

		this.train.getDataStore().store(event);
		
		return event;
	}

	public void delete(Event event) {
		ObjectDatastore od = this.train.getDataStore();
		od.delete(event);
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
