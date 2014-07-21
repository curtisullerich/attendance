package edu.iastate.music.marching.attendance.util;

import java.lang.reflect.Type;

import org.joda.time.DateTime;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateTimeDeserializer implements JsonSerializer<DateTime>,
		JsonDeserializer<DateTime> {
	// No need for an InstanceCreator since DateTime provides a no-args
	// constructor
	@Override
	public JsonElement serialize(DateTime src, Type srcType,
			JsonSerializationContext context) {
		return new JsonPrimitive(src.toString());
	}

	/*
	 * other possible options:
	 * http://stackoverflow.com/questions/6873020/gson-date-format
	 * https://github.com/gkopff/gson-jodatime-serialisers
	 * http://stackoverflow.com
	 * /questions/14996663/is-there-a-standard-implementation
	 * -for-a-gson-joda-time-serialiser
	 */
	@Override
	public DateTime deserialize(JsonElement json, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		return new DateTime(json.getAsString());
	}
}