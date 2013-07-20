package edu.iastate.music.marching.attendance.model.store;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.code.twig.annotation.AnnotationConfiguration;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.annotation.Version;
import com.google.code.twig.standard.StandardObjectDatastore;
import com.google.code.twig.util.generic.GenericTypeReflector;


public class AttendanceDatastore extends StandardObjectDatastore {

	public static final int VERSION = 1;
	public static final String MODEL_OBJECTS_PACKAGE = "edu.iastate.music.marching.attendance.model.store";

	public static String typeToKind(Type type) {
		String kind = typeToName(type);

		// replace . with _ and _ with __
		kind = kind.replace('.', ' ');
		kind = kind.replaceAll("_", "__");
		kind = kind.replace(' ', '_');

		int version = version(type);
		if (version > 0) {
			kind = "v" + version + "_" + kind;
		}
		return kind;
	}
	
	private static String typeToName(Type type) {
		Class<?> erased = GenericTypeReflector.erase(type);
		Entity annotation = erased.getAnnotation(Entity.class);
		if (annotation != null && annotation.kind().length() > 0) {
			return annotation.kind();
		}

		Class<?> clazz = GenericTypeReflector.erase(type);
		String kind = clazz.getName();
		return kind;
	}
	
	private static int version(java.lang.reflect.Type type) {
		Class<?> clazz = GenericTypeReflector.erase(type);
		if (clazz.isAnnotationPresent(Version.class)) {
			return clazz.getAnnotation(Version.class).value();
		} else {
			return AttendanceDatastore.VERSION;
		}
	}
	
	AttendanceDatastore() {
		super(new Configuration());
	}

	private static class Configuration implements
			com.google.code.twig.configuration.Configuration {

		private static AnnotationConfiguration inner = new AnnotationConfiguration(
				true, AttendanceDatastore.VERSION);

		@Override
		public int activationDepth(Field field, int depth) {
			return inner.activationDepth(field, depth);
		}

		@Override
		public String typeToKind(Type type) {
			return AttendanceDatastore.typeToKind(type);
		}

		private final static Pattern pattern = Pattern.compile("v(\\d+)_");

		@Override
		public Type kindToType(String kind) {
			Matcher matcher = pattern.matcher(kind);
			int version = 0;
			if (matcher.lookingAt()) {
				kind = kind.substring(matcher.end());
				version = new Integer(matcher.group(1));
			}

			// use space as a place holder as it cannot exist in property names
			kind = kind.replaceAll("__", " ");
			kind = kind.replace('_', '.');
			kind = kind.replace(' ', '_');
			return nameToType(kind, version);
		}

		protected Type nameToType(String name, int version) {
			if (version == 0
					&& DatastoreVersion.class.getAnnotation(Entity.class)
							.kind().equals(name)) {
				return DatastoreVersion.class;
			} else if (version == AttendanceDatastore.VERSION) {
				try {
					return Class.forName(name);
				} catch (ClassNotFoundException e) {
					try {
						return Class.forName(MODEL_OBJECTS_PACKAGE + "." + name);
					} catch (ClassNotFoundException e2) {
						throw new IllegalStateException(e);
					}
				}
			} else {
				// Legacy model type
				int simpleNameSplitPoint = name.lastIndexOf('.');

				String packageStr;
				String simpleName;
				if (simpleNameSplitPoint == -1) {
					packageStr = MODEL_OBJECTS_PACKAGE;
					simpleName = name;
				} else {
					packageStr = name.substring(0, simpleNameSplitPoint);
					simpleName = name.substring(simpleNameSplitPoint + 1);
				}

				try {
					return Class.forName(packageStr + ".legacy." + simpleName
							+ "_V" + version);
				} catch (ClassNotFoundException e) {
					throw new IllegalStateException(e);
				}
			}
		}

		@Override
		public long allocateIdsFor(Type type) {
			return inner.allocateIdsFor(type);
		}

		@Override
		public boolean id(Field field) {
			return inner.id(field);
		}

		@Override
		public boolean parent(Field field) {
			return inner.parent(field);
		}

		@Override
		public boolean child(Field field) {
			return inner.child(field);
		}

		@Override
		public boolean embed(Field field) {
			return inner.embed(field);
		}

		@Override
		public boolean entity(Field field) {
			return inner.entity(field);
		}

		@Override
		public boolean index(Field field) {
			return inner.index(field);
		}

		@Override
		public boolean store(Field field) {
			return inner.store(field);
		}

		@Override
		public boolean polymorphic(Field field) {
			return inner.polymorphic(field);
		}

		@Override
		public Type typeOf(Field field) {
			return inner.typeOf(field);
		}

		@Override
		public String name(Field field) {
			return inner.name(field);
		}

	}
}
