package net.maunium.bukkit.Maussentials.Utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

/**
 * A basic 3d selection container
 * 
 * @author Tulir293
 * @since 0.1
 */
@SerializableAs("MauArea")
public class Area implements Serializable, ConfigurationSerializable {
	private static final long serialVersionUID = 6042751882876629285L;
	public static final String MINMAX_SPLITTER = " | ", NUMSPLITTER = ", ";
	private final int minX, minY, minZ, maxX, maxY, maxZ;
	
	/**
	 * Create an area with the coordinates of the given locations.
	 * 
	 * @param c1 The first corner.
	 * @param c2 The second corner.
	 */
	public Area(Location c1, Location c2) {
		this(c1.getBlockX(), c1.getBlockY(), c1.getBlockZ(), c2.getBlockX(), c2.getBlockY(), c2.getBlockZ());
	}
	
	/**
	 * Creates an area with the given coordinates.
	 * 
	 * @param x1 The X coordinate of the first corner.
	 * @param y1 The Y coordinate of the first corner.
	 * @param z1 The Z coordinate of the first corner.
	 * @param x2 The X coordinate of the second corner.
	 * @param y2 The Y coordinate of the second corner.
	 * @param z2 The Z coordinate of the second corner.
	 */
	public Area(int x1, int y1, int z1, int x2, int y2, int z2) {
		if (x1 < x2 || x1 == x2) {
			minX = x1;
			maxX = x2;
		} else {
			minX = x2;
			maxX = x1;
		}
		
		if (y1 < y2 || y1 == y2) {
			minY = y1 < 0 ? 0 : y1;
			maxY = y2 > 255 ? 255 : y2;
		} else {
			minY = y2 < 0 ? 0 : y1;
			maxY = y1 > 255 ? 255 : y2;
		}
		
		if (z1 < z2 || z1 == z2) {
			minZ = z1;
			maxZ = z2;
		} else {
			minZ = z2;
			maxZ = z1;
		}
	}
	
	/**
	 * Creates an area using the given serialized map. This should not be used directly, as it is intended for the
	 * Bukkit YAML configuration serialization system.
	 * 
	 * @param serialized The serialized map.
	 * @throws AreaFormatException If deserialization fails.
	 */
	public Area(Map<String, Object> serialized) {
		try {
			minX = Integer.parseInt(serialized.get("min-x").toString());
			minY = Integer.parseInt(serialized.get("min-y").toString());
			minZ = Integer.parseInt(serialized.get("min-z").toString());
			maxX = Integer.parseInt(serialized.get("max-x").toString());
			maxY = Integer.parseInt(serialized.get("max-y").toString());
			maxZ = Integer.parseInt(serialized.get("max-z").toString());
		} catch (NumberFormatException | NullPointerException e) {
			throw new AreaFormatException("Couldn't deserialize given map.", e);
		}
	}
	
	/**
	 * A textual representation of this Area in the following format:<br>
	 * <code>minX, minY, minZ | maxX, maxY, maxZ</code>
	 */
	@Override
	public String toString() {
		return minX + NUMSPLITTER + minY + NUMSPLITTER + minZ + MINMAX_SPLITTER + maxX + NUMSPLITTER + maxY + NUMSPLITTER + maxZ;
	}
	
	/**
	 * Parses an Area from a String.
	 * 
	 * @param s The String to parse an Area from.
	 * @return The parsed Area.
	 * @throws AreaFormatException If parsing fails
	 */
	public static Area fromString(String s) {
		String[] ss;
		try {
			ss = s.split(MINMAX_SPLITTER, 2);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new AreaFormatException("String doesn't contain the Min-Max splitter char", e);
		}
		
		String[] min, max;
		try {
			min = ss[0].split(NUMSPLITTER, 3);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new AreaFormatException("Min string doesn't contain the Number splitter char", e);
		}
		
		try {
			max = ss[1].split(NUMSPLITTER, 3);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new AreaFormatException("Min string doesn't contain the Number splitter char", e);
		}
		
		int minX, minY, minZ, maxX, maxY, maxZ;
		
		String parsing = "";
		try {
			parsing = "Min X";
			minX = Integer.parseInt(min[0]);
			parsing = "Min Y";
			minY = Integer.parseInt(min[1]);
			parsing = "Min Z";
			minZ = Integer.parseInt(min[2]);
			parsing = "Max X";
			maxX = Integer.parseInt(max[0]);
			parsing = "Max Y";
			maxY = Integer.parseInt(max[1]);
			parsing = "Max Z";
			maxZ = Integer.parseInt(max[2]);
		} catch (NumberFormatException e) {
			throw new AreaFormatException("Number format exception while parsing " + parsing, e);
		}
		
		return new Area(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	/**
	 * Get the corner with the smaller values as a Location in the given world.
	 * 
	 * @param w The world to use for the location.
	 * @return The location.
	 */
	public Location getMinCorner(World w) {
		return new Location(w, minX, minY, minZ);
	}
	
	/**
	 * Get the corner with the larger values as a Location in the given world.
	 * 
	 * @param w The world to use for the location.
	 * @return The location.
	 */
	public Location getMaxCorner(World w) {
		return new Location(w, maxX, maxY, maxZ);
	}
	
	/**
	 * Check if a location is in this Area.
	 * 
	 * @param l The location to check.
	 * @return True if the given location is in this Area, false otherwise.
	 */
	public boolean isInArea(Location l) {
		return isInArea(l.getBlockX(), l.getBlockY(), l.getBlockZ());
	}
	
	/**
	 * Check if the given coordinates are in this Area.
	 * 
	 * @param x The X coordinate.
	 * @param y The Y coordinate.
	 * @param z The Z coordinate.
	 * @return True if the given coordinates are in this Area, false otherwise.
	 */
	public boolean isInArea(int x, int y, int z) {
		return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
	}
	
	/**
	 * @return The smaller X value of this Area.
	 */
	public int getMinX() {
		return minX;
	}
	
	/**
	 * @return The smaller Y value of this Area.
	 */
	public int getMinY() {
		return minY;
	}
	
	/**
	 * @return The smaller Z value of this Area.
	 */
	public int getMinZ() {
		return minZ;
	}
	
	/**
	 * @return The larger X value of this Area.
	 */
	public int getMaxX() {
		return maxX;
	}
	
	/**
	 * @return The larger Y value of this Area.
	 */
	public int getMaxY() {
		return maxY;
	}
	
	/**
	 * @return The larger Z value of this Area.
	 */
	public int getMaxZ() {
		return maxZ;
	}
	
	/**
	 * An exception that is thrown when failing to parse or deserialize an Area.
	 * 
	 * @author Tulir293
	 * @since 0.1
	 */
	public static class AreaFormatException extends IllegalArgumentException {
		private static final long serialVersionUID = 4080468767898580267L;
		private AreaFormatException(String msg, Throwable cause) {
			super(msg, cause);
		}
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("min-x", minX);
		map.put("min-y", minY);
		map.put("min-z", minZ);
		map.put("max-x", maxX);
		map.put("max-y", maxY);
		map.put("max-z", maxZ);
		
		return map;
	}
	
	public static Area deserialize(Map<String, Object> serialized) {
		return new Area(serialized);
	}
	
	public static Area valueOf(Map<String, Object> serialized) {
		return new Area(serialized);
	}
}
