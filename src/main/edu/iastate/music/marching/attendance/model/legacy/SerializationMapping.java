package edu.iastate.music.marching.attendance.model.legacy;

import java.io.ObjectStreamClass;
import java.util.HashMap;

public class SerializationMapping {

	private SerializationMapping() {

	}

	private HashMap<SerializationInfo, Class<?>> map;

	private void setup() {
		map = new HashMap<SerializationMapping.SerializationInfo, Class<?>>();

		map.put(new SerializationInfo(
				"edu.iastate.music.marching.attendance.model.User",
				1421557192976557705L), User_V0.class);
		map.put(new SerializationInfo(
				"edu.iastate.music.marching.attendance.model.User$Type",
				0L), User_V0.Type.class);
		map.put(new SerializationInfo(
				"edu.iastate.music.marching.attendance.model.User$Grade",
				0L), User_V0.Grade.class);
		map.put(new SerializationInfo(
				"edu.iastate.music.marching.attendance.model.User$Section",
				0L), User_V0.Section.class);
		map.put(new SerializationInfo(
				"edu.iastate.music.marching.attendance.model.Message",
				-7053270580375162117L), Message_V0.class);
	}

	public static ObjectStreamClass get(ObjectStreamClass desc)
			throws ClassNotFoundException {
		SerializationMapping mapping = new SerializationMapping();

		mapping.setup();

		SerializationInfo si = new SerializationInfo(desc.getName(),
				desc.getSerialVersionUID());

		if (mapping.map.containsKey(si)) {
			return ObjectStreamClass.lookup(mapping.map.get(si));
		} else {
			throw new ClassNotFoundException("Could not map class " + si.name
					+ " with serial version " + si.serialVersionUID);
		}
	}

	public static class SerializationInfo {

		public SerializationInfo(String name, long uid) {
			this.name = name;
			this.serialVersionUID = uid;
		}

		public String name;
		public long serialVersionUID;

		@Override
		public int hashCode() {
			return new String(this.name).hashCode()
					^ new Long(this.serialVersionUID).hashCode();
		}

		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;

			if (o instanceof SerializationInfo) {
				SerializationInfo si = (SerializationInfo) o;

				return this.name.equals(si.name)
						&& this.serialVersionUID == si.serialVersionUID;
			}
			return false;
		}
	}

}
