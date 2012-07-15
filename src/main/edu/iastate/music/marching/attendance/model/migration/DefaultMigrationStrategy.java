package edu.iastate.music.marching.attendance.model.migration;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.transaction.NotSupportedException;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.code.twig.annotation.Entity;
import com.google.code.twig.standard.StandardObjectDatastore;
import com.google.code.twig.util.generic.GenericTypeReflector;

import edu.iastate.music.marching.attendance.model.ModelFactory;

/**
 * Copies all data on upgrade, throws on downgrade
 * 
 * @author stiner
 * 
 */
public class DefaultMigrationStrategy implements IMigrationStrategy {

	private int fromVersion;
	private int toVersion;
	private Map<Class<?>, Class<?>> typeMap;

	public DefaultMigrationStrategy(int fromVersion, int toVersion,
			Map<Class<?>, Class<?>> typeMap) {
		this.fromVersion = fromVersion;
		this.toVersion = toVersion;
		this.typeMap = typeMap;
	}

	@Override
	public int getFromVersion() {
		return fromVersion;
	}

	@Override
	public int getToVersion() {
		return toVersion;
	}

	@Override
	public void doUpgrade(StandardObjectDatastore datastore)
			throws MigrationException {

		// For each from type, find all instances of it in the datastore and
		// migrate them to the corresponding to type
		for (Entry<Class<?>, Class<?>> entry : this.typeMap.entrySet()) {
			QueryResultIterator<?> iterator = datastore.find(entry.getKey());

			while (iterator.hasNext()) {
				Object translated = upgrade(iterator.next(), entry.getKey(),
						entry.getValue(), entry.getKey(), entry.getValue());
				datastore.storeOrUpdate(translated);
			}
		}
	}

	@Override
	public void doDowngrade() throws MigrationException {
		throw new MigrationException("Not supported",
				new NotSupportedException());
	}

	protected Object upgrade(Object object, Class<?> fromType, Class<?> toType,
			Type fromGenericType, Type toGenericType) throws MigrationException {

		if (object == null) {
			return null;
		}

		if (fromType.isEnum())
			return upgradeEnum(object, fromType, toType);

		if (fromType.isAnnotationPresent(Entity.class)) {
			return upgradeEntity(object, fromType, toType);
		}

		if (List.class.isAssignableFrom(fromType)) {
			return upgradeList((List<?>) object, fromGenericType, toGenericType);
		}

		if (Set.class.isAssignableFrom(fromType)) {
			return upgradeSet((Set<?>) object, fromGenericType, toGenericType);
		}

		if (Map.class.isAssignableFrom(fromType)) {
			return upgradeMap((Map<?, ?>) object, fromGenericType,
					toGenericType);
		}

		return object;
	}

	private Object upgradeMap(Map<?, ?> map, Type fromType, Type toType)
			throws MigrationException {
		HashMap<Object, Object> newCollection;

		// handles the tricky task of finding what type of map we have
		Type exactFromType = GenericTypeReflector.getExactSuperType(fromType,
				Map.class);
		Type componentFromKeyType = ((ParameterizedType) exactFromType)
				.getActualTypeArguments()[0];
		Type componentFromValueType = ((ParameterizedType) exactFromType)
				.getActualTypeArguments()[1];

		Type exactToType = GenericTypeReflector.getExactSuperType(toType,
				Map.class);
		Type componentToKeyType = ((ParameterizedType) exactToType)
				.getActualTypeArguments()[0];
		Type componentToValueType = ((ParameterizedType) exactToType)
				.getActualTypeArguments()[1];

		newCollection = new HashMap<Object, Object>();

		for (Entry<?, ?> entry : map.entrySet()) {
			Object key = upgrade(entry.getKey(),
					(Class<?>) componentFromKeyType,
					(Class<?>) componentToKeyType, componentFromKeyType,
					componentToKeyType);
			Object value = upgrade(entry.getValue(),
					(Class<?>) componentFromValueType,
					(Class<?>) componentToValueType, componentFromValueType,
					componentToValueType);

			newCollection.put(key, value);
		}

		return newCollection;
	}

	private Object upgradeSet(Set<?> set, Type fromType, Type toType)
			throws MigrationException {
		HashSet<Object> newCollection;

		// handles the tricky task of finding what type of set we have
		Type exactFromType = GenericTypeReflector.getExactSuperType(fromType,
				Set.class);
		Type componentFromType = ((ParameterizedType) exactFromType)
				.getActualTypeArguments()[0];
		Type exactToType = GenericTypeReflector.getExactSuperType(toType,
				Set.class);
		Type componentToType = ((ParameterizedType) exactToType)
				.getActualTypeArguments()[0];

		newCollection = new HashSet<Object>();

		for (Object item : set) {
			Object transformed = upgrade(item, (Class<?>) componentFromType,
					(Class<?>) componentToType, componentFromType,
					componentToType);

			newCollection.add(transformed);
		}

		return newCollection;
	}

	private Object upgradeList(List<?> list, Type fromType, Type toType)
			throws MigrationException {
		List<Object> newCollection;

		// handles the tricky task of finding what type of list we have
		Type exactFromType = GenericTypeReflector.getExactSuperType(fromType,
				List.class);
		Type componentFromType = ((ParameterizedType) exactFromType)
				.getActualTypeArguments()[0];
		Type exactToType = GenericTypeReflector.getExactSuperType(toType,
				List.class);
		Type componentToType = ((ParameterizedType) exactToType)
				.getActualTypeArguments()[0];

		newCollection = new ArrayList<Object>();

		for (Object item : list) {
			Object transformed = upgrade(item, (Class<?>) componentFromType,
					(Class<?>) componentToType, componentFromType,
					componentToType);

			newCollection.add(transformed);
		}

		return newCollection;
	}

	protected Object upgradeEntity(Object object, Class<?> fromType,
			Class<?> toType) throws MigrationException {
		Object newObject;

		try {
			newObject = ModelFactory.newInstance(toType);
		} catch (InstantiationException e) {
			throw new MigrationException("Could not create new model object", e);
		} catch (IllegalAccessException e) {
			throw new MigrationException("Could not create new model object", e);
		}

		// Iterate all properties on the new object and attempt to copy
		// their value in
		for (Field toField : toType.getDeclaredFields()) {

			// Skip static of final fields
			if (Modifier.isStatic(toField.getModifiers())
					|| Modifier.isFinal(toField.getModifiers())) {
				continue;
			}

			// Find corresponding field on from type
			boolean found = false;
			for (Field fromField : fromType.getDeclaredFields()) {
				if (fromField.getName().equals(toField.getName())) {
					try {
						toField.setAccessible(true);
						fromField.setAccessible(true);
						Object value = upgrade(fromField.get(object),
								fromField.getType(), toField.getType(),
								fromField.getGenericType(),
								toField.getGenericType());
						toField.set(newObject, value);
					} catch (IllegalArgumentException e) {
						throw new MigrationException(
								"Could not set field value", e);
					} catch (IllegalAccessException e) {
						throw new MigrationException(
								"Could not set field value", e);
					}
					found = true;
					break;
				}
			}

			if (!found) {
				throw new MigrationException(
						"Field "
								+ toField.getName()
								+ " did not have a corresponding field on the from model type");
			}
		}

		return newObject;
	}

	protected Object upgradeEnum(Object object, Class<?> fromType,
			Class<?> toType) throws MigrationException {
		for (Object constant : toType.getEnumConstants()) {
			if (constant.toString().equals(object.toString())) {
				return constant;
			}
		}
		throw new MigrationException(
				"Could not transform enum field value of type: "
						+ fromType.toString());
	}

	protected Object downgrade(Object object) throws MigrationException {
		throw new MigrationException("Not supported",
				new NotSupportedException());
	}
}
