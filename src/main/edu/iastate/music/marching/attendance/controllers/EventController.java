package edu.iastate.music.marching.attendance.controllers;

import java.util.Date;
import java.util.List;

import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Event.Type;
import edu.iastate.music.marching.attendance.model.ModelFactory;

public class EventController extends AbstractController {

	DataTrain train;
	
	public EventController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

//	public boolean create(Type type, Date start, Date end) {
//		Event event = ModelFactory.newEvent(type, start, end);
//		ObjectDatastore od = this.train.getDataStore();
//		od.store(event);
//		return true;
//	}

	public Event createOrUpdate(Type type, Date start, Date end) {
		Event event = ModelFactory.newEvent(type, start, end);
		ObjectDatastore od = this.train.getDataStore();
		od.storeOrUpdate(event);
		return event;
	}
	
	public void delete(Event event) {
		ObjectDatastore od = this.train.getDataStore();
		od.delete(event);
		//od.delete() returns void, so we don't really have anything to return here, either
	}
	
	public List<Event> readAll() {
		return this.train.getDataStore().find().type(Event.class).returnAll().now();
	}

}
