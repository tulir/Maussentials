package net.maunium.bukkit.Maussentials;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

/**
 * A serializable location containing the x, y, z, yaw and pitch values and the UUID of the world.<br>
 * Can be serialized using the java serialization system or the bukkit configuration serialization system.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class SerializableLocation implements Serializable, ConfigurationSerializable {
	private static final long serialVersionUID = -3174227815222499224L;
	private final double x, y, z;
	private final float yaw, pitch;
	private final UUID world;
	
	/**
	 * Creates a SerializableLocation using the values of the given location.
	 * 
	 * @param l The location to use for values.
	 */
	public SerializableLocation(Location l) {
		this.x = l.getX();
		this.y = l.getY();
		this.z = l.getZ();
		this.yaw = l.getYaw();
		this.pitch = l.getPitch();
		this.world = l.getWorld().getUID();
	}
	
	public SerializableLocation(double x, double y, double z, float yaw, float pitch, UUID world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		this.world = world;
	}
	
	public SerializableLocation(double x, double y, double z, float yaw, float pitch, World world) {
		this(x, y, z, yaw, pitch, world.getUID());
	}
	
	public SerializableLocation(double x, double y, double z, World world) {
		this(x, y, z, 0, 0, world.getUID());
	}
	
	public SerializableLocation(double x, double y, double z, UUID world) {
		this(x, y, z, 0, 0, world);
	}
	
	/**
	 * Creates a SerializableLocation from a string.
	 * 
	 * @param s The string to get the values from.
	 * @return The parsed SerializableLocation
	 */
	public static SerializableLocation fromString(String s) {
		String[] ss = s.split(", ");
		double x = Double.parseDouble(ss[0]);
		double y = Double.parseDouble(ss[1]);
		double z = Double.parseDouble(ss[2]);
		float yaw = Float.parseFloat(ss[3]);
		float pitch = Float.parseFloat(ss[4]);
		UUID world = UUID.fromString(ss[5]);
		return new SerializableLocation(x, y, z, yaw, pitch, world);
	}
	
	/**
	 * @return A string containing the x, y, z, yaw, pitch values and the world UID.
	 */
	@Override
	public String toString() {
		return x + ", " + y + ", " + z + ", " + yaw + ", " + pitch + ", " + world.toString();
	}
	
	/**
	 * @return A human-readable string representation of this location. Contains the x, y and z values rounded to two decimals. If you want a string with the
	 *         world too, use {@link #toReadableString(Location)}.
	 */
	public String toReadableString() {
		return round(x, 2) + ", " + round(y, 2) + ", " + round(z, 2);
	}
	
	/**
	 * @param l The location from which to take the values from.
	 * @return A string containing the x, y, z, yaw, pitch values and the world name.
	 */
	public static String toString(Location l) {
		if (l != null) return l.getX() + ", " + l.getY() + ", " + l.getZ() + ", " + l.getYaw() + ", " + l.getPitch() + ", "
				+ (l.getWorld() == null ? "Null" : l.getWorld().getUID());
		else return "Null Location";
	}
	
	/**
	 * @param l The location from which to take the values from
	 * @return A human-readable string representation of this location. Contains the x, y and z values rounded to two decimals and the name of the world.
	 */
	public static String toReadableString(Location l) {
		if (l != null) return round(l.getX(), 2) + ", " + round(l.getY(), 2) + ", " + round(l.getZ(), 2) + " @ "
				+ (l.getWorld() == null ? "Null" : l.getWorld().getName());
		else return "Null Location";
	}
	
	/**
	 * Serializes a SerializedLocation to configuration. It is not recommended to use this manually, as it is intended for the Bukkit configuration
	 * serialization system.
	 * 
	 * @return The serialized map.
	 */
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> location = new HashMap<String, Object>();
		location.put("x", x);
		location.put("y", y);
		location.put("z", z);
		location.put("yaw", yaw);
		location.put("pitch", pitch);
		location.put("world", world.toString());
		return location;
	}
	
	/**
	 * Deserializes a SerializedLocation from configuration. It is not recommended to use this manually, as it is intended for the Bukkit configuration
	 * serialization system.
	 * 
	 * @param location The map containing the values.
	 */
	public SerializableLocation(Map<String, Object> location) {
		this.x = parseDouble(location.get("x"));
		this.y = parseDouble(location.get("y"));
		this.z = parseDouble(location.get("z"));
		this.yaw = parseFloat(location.get("yaw"));
		this.pitch = parseFloat(location.get("pitch"));
		this.world = UUID.fromString((String) location.get("uuid"));
	}
	
	/**
	 * Deserializes a SerializedLocation from configuration. It is not recommended to use this manually, as it is intended for the Bukkit configuration
	 * serialization system.
	 * 
	 * @param location The map containing the values.
	 */
	public SerializableLocation valueOf(Map<String, Object> location) {
		return new SerializableLocation(location);
	}
	
	/**
	 * Deserializes a SerializedLocation from configuration. It is not recommended to use this manually, as it is intended for the Bukkit configuration
	 * serialization system.
	 * 
	 * @param location The map containing the values.
	 */
	public SerializableLocation deserialize(Map<String, Object> location) {
		return new SerializableLocation(location);
	}
	
	private static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();
		
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	private double parseDouble(Object o) {
		if (o instanceof Double) return (Double) o;
		else return Double.parseDouble(o.toString());
	}
	
	private float parseFloat(Object o) {
		if (o instanceof Float) return (Float) o;
		else return Float.parseFloat(o.toString());
	}
}
