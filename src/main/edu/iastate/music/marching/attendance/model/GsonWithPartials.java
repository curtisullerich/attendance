package edu.iastate.music.marching.attendance.model;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.mail.Message;

import com.google.code.twig.annotation.Id;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import edu.iastate.music.marching.attendance.model.store.Absence;
import edu.iastate.music.marching.attendance.model.store.AppData;
import edu.iastate.music.marching.attendance.model.store.Event;
import edu.iastate.music.marching.attendance.model.store.Form;
import edu.iastate.music.marching.attendance.model.store.MobileDataUpload;
import edu.iastate.music.marching.attendance.model.store.User;

public class GsonWithPartials {

	private static class InnerTypeAdapter implements JsonSerializer<Object>,
			JsonDeserializer<Object> {

		@Override
		public Object deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return getFieldGson().fromJson(json, typeOfT);
		}

		@Override
		public JsonElement serialize(Object src, Type typeOfSrc,
				JsonSerializationContext context)
				throws IllegalArgumentException {

			if (src == null) {
				return JsonNull.INSTANCE;
			}

			Class<?> clazz = src.getClass();
			String id = null;

			for (Field field : clazz.getDeclaredFields()) {
				try {
					field.setAccessible(true);
					if (field.isAnnotationPresent(Id.class)) {
						if (id != null) {
							throw new IllegalArgumentException(
									"Multiple Id fields on model object not allowed");
						}

						Object value = field.get(src);

						if (value == null) {
							throw new IllegalArgumentException(
									"Null Id field value not allowed");
						}

						id = value.toString();
					}
				} catch (IllegalArgumentException e) {
					// Skip field
				} catch (IllegalAccessException e) {
					// Skip field
				}
			}

			if (id == null) {
				// return getGson().toJsonTree(src, typeOfSrc);
				// return JsonNull.INSTANCE;
				throw new IllegalArgumentException(
						"No Id field found in model object of type "
								+ typeOfSrc.toString());
			}

			JsonObject object = new JsonObject();
			object.addProperty("id", id);
			return object;
		}
	}

	private static class OuterTypeAdapter implements JsonSerializer<Object>,
			JsonDeserializer<Object> {

		@Override
		public Object deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return getInnerGson().fromJson(json, typeOfT);
		}

		@Override
		public JsonElement serialize(Object src, Type typeOfSrc,
				JsonSerializationContext context) {

			if (src == null) {
				return JsonNull.INSTANCE;
			}

			Class<?> clazz = (Class<?>) typeOfSrc;
			JsonObject object = new JsonObject();

			for (Field field : clazz.getDeclaredFields()) {
				try {
					field.setAccessible(true);

					if (!Modifier.isStatic(field.getModifiers())) {
						object.add(
								field.getName(),
								getInnerGson().toJsonTree(field.get(src),
										field.getType()));
					}
				} catch (IllegalArgumentException e) {
					// Skip field
				} catch (IllegalAccessException e) {
					// Skip field
				}
			}

			return object;
		}
	}

	public static <T> T fromJson(Reader json, Class<T> clazz) {
		return getInstance().fromJson(json, clazz);
	}
	
	private static Gson getFieldGson() {
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(java.util.Date.class, new UTCDateAdaptor());
		return gson.create();
	}

	private static Gson getInnerGson() {
		GsonBuilder gson = new GsonBuilder();
		InnerTypeAdapter adapter = new InnerTypeAdapter();
		gson.registerTypeAdapter(Absence.class, adapter);
		gson.registerTypeAdapter(AppData.class, adapter);
		gson.registerTypeAdapter(Event.class, adapter);
		gson.registerTypeAdapter(Form.class, adapter);
		gson.registerTypeAdapter(Message.class, adapter);
		gson.registerTypeAdapter(MobileDataUpload.class, adapter);
		gson.registerTypeAdapter(User.class, adapter);
		return gson.create();
	}

	private static Gson getInstance() {
		GsonBuilder gson = new GsonBuilder();
		OuterTypeAdapter adapter = new OuterTypeAdapter();
		gson.registerTypeAdapter(Absence.class, adapter);
		gson.registerTypeAdapter(AppData.class, adapter);
		gson.registerTypeAdapter(Event.class, adapter);
		gson.registerTypeAdapter(Form.class, adapter);
		gson.registerTypeAdapter(Message.class, adapter);
		gson.registerTypeAdapter(MobileDataUpload.class, adapter);
		gson.registerTypeAdapter(User.class, adapter);
		return gson.create();
	}

	public static <T> void toJson(T src, Appendable writer) {
		getInstance().toJson(src, writer);
	}

	private static class UTCDateAdaptor implements JsonSerializer<Date>,
			JsonDeserializer<Date> {
		private final DateFormat defaultDateFormatUTC;

		UTCDateAdaptor() {
			this.defaultDateFormatUTC = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.US);
			this.defaultDateFormatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		}

		public JsonElement serialize(Date src, Type typeOfSrc,
				JsonSerializationContext context) {
			String dateFormatAsString = defaultDateFormatUTC.format(src);
			return new JsonPrimitive(dateFormatAsString);
		}

		public Date deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			if (!(json instanceof JsonPrimitive)) {
				throw new JsonParseException(
						"The date should be a string value");
			}
			Date date = deserializeToDate(json);
			if (typeOfT == Date.class) {
				return date;
			} else {
				throw new IllegalArgumentException(getClass()
						+ " cannot deserialize to " + typeOfT);
			}
		}

		private Date deserializeToDate(JsonElement json) {
			try {
				return defaultDateFormatUTC.parse(json.getAsString());
			} catch (ParseException e) {
				throw new JsonSyntaxException(json.getAsString(), e);
			}
		}

	}
}
