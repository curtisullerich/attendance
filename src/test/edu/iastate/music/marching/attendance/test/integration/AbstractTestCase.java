package edu.iastate.music.marching.attendance.test.integration;

import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;

import edu.iastate.music.marching.attendance.controllers.DataTrain;

public class AbstractTestCase {
	

	protected DataTrain getDataTrain() {
		return DataTrain.getAndStartTrain();
	}

	protected ObjectDatastore getObjectDataStore() {
		return new AnnotationObjectDatastore(false);
	}
	
}
