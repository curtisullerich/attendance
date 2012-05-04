package edu.iastate.music.marching.attendance.controllers;

import java.util.Date;

import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.Event.Type;
import edu.iastate.music.marching.attendance.model.ModelFactory;

public class EventController extends AbstractController {

	DataTrain train;

	public EventController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public boolean create(Type type, Date start, Date end) {
		Event event = ModelFactory.newEvent(type, start, end);
		ObjectDatastore od = this.train.getDataStore();
		od.store(event);
		return true;
	}

	public Event createOrUpdate(Type type, Date start, Date end) {
		Event event = ModelFactory.newEvent(type, start, end);
		ObjectDatastore od = this.train.getDataStore();
		od.storeOrUpdate(event);
		return event;
	}

}
