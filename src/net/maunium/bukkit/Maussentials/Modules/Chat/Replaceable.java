package net.maunium.bukkit.Maussentials.Modules.Chat;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import net.maunium.bukkit.Maussentials.Utils.Deserialization.DeserializationException;
import net.maunium.bukkit.Maussentials.Utils.Deserialization.DeserializationUtils;

/**
 * An object containing data for chat filtering.
 * 
 * @author Tulir293
 * @since 0.3
 */
@SerializableAs("MauChatReplaceable")
public class Replaceable implements Comparable<Replaceable>, ConfigurationSerializable {
	private int priority = 0;
	private Type type = Type.STRING;
	private String toReplace = null, replaceWith = null;
	
	/**
	 * Create a Replaceable with the given values.
	 */
	public Replaceable(String toReplace, String replaceWith, Type type, int priority) {
		if (toReplace == null || replaceWith == null || type == null) throw new IllegalArgumentException("Null arguments!");
		this.toReplace = toReplace;
		this.replaceWith = replaceWith;
		this.type = type;
		this.priority = priority;
	}
	
	/**
	 * Apply this Replaceable to the given String.
	 * 
	 * @param s The String to apply this Replaceable to.
	 * @return A String with this Replaceable replaced.
	 */
	public String replaceIn(String s) {
		switch (type) {
			case REGEX:
				return s.replaceAll(toReplace, replaceWith);
			case STRING:
				return s.replace(toReplace, replaceWith);
			default:
				return s;
		}
	}
	
	/**
	 * An enum storing the type of a Replaceable.
	 * 
	 * @author Tulir293
	 * @since 0.3
	 */
	public static enum Type {
		STRING, REGEX;
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> serialized = new HashMap<String, Object>();
		
		serialized.put("priority", priority);
		serialized.put("type", type.toString());
		serialized.put("to-replace", toReplace);
		serialized.put("replace-with", replaceWith);
		
		return serialized;
	}
	
	/**
	 * Deserialize the given map into a Replaceable.
	 * 
	 * @param serialized The map containing the serialized data.
	 * @throws DeserializationException If deserialization fails.
	 */
	public Replaceable(Map<String, Object> serialized) throws DeserializationException {
		priority = DeserializationUtils.getValue("priority", serialized, int.class, 0);
		type = DeserializationUtils.getValue("type", serialized, Type.class, Type.STRING);
		toReplace = DeserializationUtils.getValue("to-replace", serialized, String.class);
		replaceWith = DeserializationUtils.getValue("replace-with", serialized, String.class);
	}
	
	/**
	 * Deserialize the given map into a Replaceable. This simply calls the constructor
	 * {@link #Replaceable(Map)}, so it is usually better to use the constructor.
	 * 
	 * @param serialized The map containing the serialized data.
	 * @throws DeserializationException If deserialization fails.
	 */
	public static Replaceable valueOf(Map<String, Object> serialized) throws DeserializationException {
		return new Replaceable(serialized);
	}
	
	/**
	 * Deserialize the given map into a Replaceable. This simply calls the constructor
	 * {@link #Replaceable(Map)}, so it is usually better to use the constructor.
	 * 
	 * @param serialized The map containing the serialized data.
	 * @throws DeserializationException If deserialization fails.
	 */
	public static Replaceable deserialize(Map<String, Object> serialized) throws DeserializationException {
		return new Replaceable(serialized);
	}
	
	/**
	 * Compares the priorities of this and the given Replaceable.
	 */
	@Override
	public int compareTo(Replaceable o) {
		return Integer.compare(priority, o.priority);
	}
}
