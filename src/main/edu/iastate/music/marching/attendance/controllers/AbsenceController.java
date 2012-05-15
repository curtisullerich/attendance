package edu.iastate.music.marching.attendance.controllers;

import java.util.Date;
import java.util.List;

import com.google.code.twig.ObjectDatastore;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;

public class AbsenceController extends AbstractController {

	private DataTrain train;

	public AbsenceController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public Absence createOrUpdateTardy(User student, Date time) {
		
		if(student == null)
			throw new IllegalArgumentException("Tried to create absence for null user");
		
		Absence absence = ModelFactory.newAbsence(Absence.Type.Tardy, student);
		absence.setDatetime(time);

		// Associate with event
		List<Event> events = train.getEventsController().get(time);

		if (events.size() == 1)
			absence.setEvent(events.get(0));
		// else the absence is orphaned

		ObjectDatastore od = this.train.getDataStore();
		od.storeOrUpdate(absence);
		return absence;
	}

	public Absence createOrUpdateAbsence(User student, Date start, Date end) {
		
		if(student == null)
			throw new IllegalArgumentException("Tried to create absence for null user");
		
		Absence absence = ModelFactory.newAbsence(Absence.Type.Tardy, student);
		absence.setStart(start);
		absence.setEnd(end);

		// Associate with event
		List<Event> events = train.getEventsController().get(start, end);

		if (events.size() == 1)
			absence.setEvent(events.get(0));
		// else the absence is orphaned

		ObjectDatastore od = this.train.getDataStore();
		od.storeOrUpdate(absence);
		return absence;
	}

	public Absence createOrUpdateEarlyCheckout(User student, Date time) {
		
		if(student == null)
			throw new IllegalArgumentException("Tried to create absence for null user");
		
		Absence absence = ModelFactory.newAbsence(Absence.Type.EarlyCheckOut, student);
		absence.setDatetime(time);

		// Associate with event
		List<Event> events = train.getEventsController().get(time);

		if (events.size() == 1)
			absence.setEvent(events.get(0));
		// else the absence is orphaned

		ObjectDatastore od = this.train.getDataStore();
		od.storeOrUpdate(absence);
		return absence;
	}

}
