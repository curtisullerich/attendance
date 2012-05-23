package edu.iastate.music.marching.attendance.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.FindCommand.RootFindCommand;
import com.google.common.collect.Iterators;

import edu.iastate.music.marching.attendance.model.Absence;
import edu.iastate.music.marching.attendance.model.Event;
import edu.iastate.music.marching.attendance.model.MessageThread;
import edu.iastate.music.marching.attendance.model.ModelFactory;
import edu.iastate.music.marching.attendance.model.User;

public class AbsenceController extends AbstractController {

	private DataTrain train;

	public AbsenceController(DataTrain dataTrain) {
		this.train = dataTrain;
	}

	public Absence createOrUpdateTardy(User student, Date time) {

		if (student == null)
			throw new IllegalArgumentException(
					"Tried to create absence for null user");

		// TODO : Check for exact duplicates

		Absence absence = ModelFactory.newAbsence(Absence.Type.Tardy, student);
		absence.setDatetime(time);

		// Associate with event
		List<Event> events = train.getEventsController().get(time);

		if (events.size() == 1)
			absence.setEvent(events.get(0));
		// else the absence is orphaned

		return storeAbsence(absence);
	}

	public Absence createOrUpdateAbsence(User student, Date start, Date end) {

		if (student == null)
			throw new IllegalArgumentException(
					"Tried to create absence for null user");

		// TODO : Check for exact duplicates

		Absence absence = ModelFactory.newAbsence(Absence.Type.Tardy, student);
		absence.setStart(start);
		absence.setEnd(end);

		// Associate with event
		List<Event> events = train.getEventsController().get(start, end);

		if (events.size() == 1)
			absence.setEvent(events.get(0));
		// else the absence is orphaned

		return storeAbsence(absence);
	}

	public Absence createOrUpdateEarlyCheckout(User student, Date time) {

		if (student == null)
			throw new IllegalArgumentException(
					"Tried to create absence for null user");

		// TODO : Check for exact duplicates

		Absence absence = ModelFactory.newAbsence(Absence.Type.EarlyCheckOut,
				student);
		absence.setDatetime(time);

		// Associate with event
		List<Event> events = train.getEventsController().get(time);

		if (events.size() == 1)
			absence.setEvent(events.get(0));
		// else the absence is orphaned

		return storeAbsence(absence);
	}

	private Absence storeAbsence(Absence absence) {
		ObjectDatastore od = this.train.getDataStore();

		// First build an empty message thread and store it
		MessageThread messages = ModelFactory.newMessageThread();
		od.store(messages);
		absence.setMessageThread(messages);

		// Then do actual store
		od.store(absence);

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
	
	public List<Absence> get(Absence.Type... types) {

		RootFindCommand<Absence> find = this.train.getDataStore().find()
				.type(Absence.class);
		find.addFilter(Absence.FIELD_TYPE, FilterOperator.IN, Arrays.asList(types));

		return find.returnAll().now();
	}

	public List<Absence> getUnanchored() {
		return this.train.getDataStore().find().type(Absence.class)
				.addFilter(Absence.FIELD_STUDENT, FilterOperator.EQUAL, null)
				.returnAll().now();
	}

	public List<Absence> getAll() {
		return this.train.getDataStore().find().type(Absence.class).returnAll()
				.now();
	}

}
