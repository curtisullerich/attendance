package edu.iastate.music.marching.attendance.model;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

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
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonWithPartials {
	
	public static <T> void toJson(T src, Appendable writer) {
		getInstance().toJson(src, writer);
	}
	
	public static <T> T fromJson(Reader json, Class<T> clazz) {
		return getInstance().fromJson(json, clazz);
	}

	private static Gson getInstance() {
		GsonBuilder gson = new GsonBuilder();
		OuterTypeAdapter adapter = new OuterTypeAdapter();
		gson.registerTypeAdapter(Absence.class, adapter);
		gson.registerTypeAdapter(AppData.class, adapter);
		gson.registerTypeAdapter(DatastoreVersion.class, adapter);
		gson.registerTypeAdapter(Event.class, adapter);
		gson.registerTypeAdapter(Form.class, adapter);
		gson.registerTypeAdapter(Message.class, adapter);
		gson.registerTypeAdapter(MobileDataUpload.class, adapter);
		gson.registerTypeAdapter(User.class, adapter);
		return gson.create();
	}

	private static Gson getInnerGson() {
		GsonBuilder gson = new GsonBuilder();
		InnerTypeAdapter adapter = new InnerTypeAdapter();
		gson.registerTypeAdapter(Absence.class, adapter);
		gson.registerTypeAdapter(AppData.class, adapter);
		gson.registerTypeAdapter(DatastoreVersion.class, adapter);
		gson.registerTypeAdapter(Event.class, adapter);
		gson.registerTypeAdapter(Form.class, adapter);
		gson.registerTypeAdapter(Message.class, adapter);
		gson.registerTypeAdapter(MobileDataUpload.class, adapter);
		gson.registerTypeAdapter(User.class, adapter);
		return gson.create();
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

	private static class InnerTypeAdapter implements JsonSerializer<Object>,
			JsonDeserializer<Object> {

		@Override
		public Object deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			// TODO Auto-generated method stub
			return new Gson().fromJson(json, typeOfT);
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
}
