package net.maunium.bukkit.Maussentials.Utils.Deserialization;

import java.util.Map;

public class DeserializationUtils {
	/**
	 * Get a value from a map, cast it and return the casted object. This calls {@link #getValue(String, Map, Class, Object)} and gives {@code null} as the
	 * default value, meaning that all errors will be thrown instead of returning a default value.
	 * 
	 * @param key The key of the value to get.
	 * @param serialized The map containing the key and value.
	 * @param type The class to which the object should be casted to.
	 * @return The casted object.
	 * @throws DeserializationException If there is no valid object for the given key in the map.
	 */
	public static <T> T getValue(String key, Map<?, ?> serialized, Class<T> type) throws DeserializationException {
		return getValue(key, serialized, type, null);
	}
	
	/**
	 * Get a value from a map, cast it and return the casted object.
	 * 
	 * @param key The key of the value to get.
	 * @param serialized The map containing the key and value.
	 * @param type The class to which the object should be casted to.
	 * @param def The default value to return if something goes wrong. If null, errors will be thrown as deserialization exceptions.
	 * @return The casted object.
	 * @throws DeserializationException If there is no valid object for the given key in the map and a default value hasn't been specified.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getValue(String key, Map<?, ?> serialized, Class<T> type, T def) throws DeserializationException {
		boolean bypassErrors = def != null;
		// Check if the map contains the given key.
		if (!serialized.containsKey(key)) {
			// It does not contain the given key.
			// If errors should be bypassed, return the default value.
			if (bypassErrors) return def;
			// If errors should be thrown, create a DeserializationException and throw it.
			else throw new DeserializationException("There is no object in field \"" + key + "\".", serialized);
		}
		
		// Get the object in the map.
		Object o = serialized.get(key);
		// Check if it is an instance of the given type.
		if (!type.isInstance(o)) {
			// It is not.
			if (bypassErrors) return def;
			else throw new DeserializationException("The object in field \"" + key + "\" is not an instance of " + type.getSimpleName(), serialized);
		}
		
		T casted;
		try {
			// Try to cast the object to the given type.
			casted = (T) o;
		} catch (ClassCastException e) {
			// If class casting fails for some reason (even though the type has been checked already) throw a deserialization exception.
			throw new DeserializationException("Failed to cast object in field \"" + key + "\" to " + type.getSimpleName() + ".", e, serialized);
		} catch (Throwable t) {
			// If any other error occurs, throw a deserialization exception without a message.
			throw new DeserializationException(t, serialized);
		}
		
		// Make sure the casted object is not null.
		if (casted == null) {
			if (bypassErrors) return def;
			else throw new DeserializationException("There is no object in field \"" + key + "\".", serialized);
		}
		
		// Return the casted object.
		return casted;
	}
}
