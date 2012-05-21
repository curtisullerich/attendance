package edu.iastate.music.marching.attendance.model;

import com.google.code.twig.annotation.AnnotationObjectDatastore;
import com.google.code.twig.conversion.CombinedConverter;
import com.google.code.twig.conversion.SpecificConverter;

class ObjectDatastore extends AnnotationObjectDatastore {

	@Override
	protected CombinedConverter createTypeConverter() {
		// start with the default converter which we will add to
		CombinedConverter converter = super.createTypeConverter();

		// register a new converter for storing null Events as plain object
		converter.append(new SpecificConverter<Event, Object>() {

			@Override
			public Object convert(Event source) {
				if (source == null)
					return new Object();
				else
					return source;
			}
		});

		// register the reverse converter for reading instances back again
		converter.append(new SpecificConverter<Object, Event>() {
			@Override
			public Event convert(Object source) {
				if (source instanceof Event)
					return (Event) source;
				else
					return null;
			}
		});

		return converter;
	}
}