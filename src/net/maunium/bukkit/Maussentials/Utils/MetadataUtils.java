package net.maunium.bukkit.Maussentials.Utils;

import java.util.concurrent.Callable;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

/**
 * Three simple metadata methods that work for all Metadatable objects.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class MetadataUtils {
	/**
	 * Get metadata value that has the given tag and is owned by the given owner from the given metadatable.
	 * 
	 * @param m The metadatable object to get the metadata from.
	 * @param tag The metadata tag to get the metadata using.
	 * @param owner The owner of the metadata.
	 * @return A metadata value object, or null if none found with the given parameters.
	 */
	public static MetadataValue getMetadata(Metadatable m, String tag, Plugin owner) {
		for (MetadataValue mv : m.getMetadata(tag))
			if (mv.getOwningPlugin().equals(owner)) return mv;
		return null;
	}
	
	/**
	 * Set fixed metadata for given metadatable with given tag, value and owner
	 * 
	 * @param m The metadatable to set the given metadata value to.
	 * @param tag The tag to set the metadata value under.
	 * @param value The value to set to the tag.
	 * @param owner The owner of the metadata value.
	 */
	public static void setFixedMetadata(Metadatable m, String tag, Object value, Plugin owner) {
		m.setMetadata(tag, new FixedMetadataValue(owner, value));
	}
	
	/**
	 * Set lazy (weak reference) metadata for given metadatable with given tag, value and owner
	 * 
	 * @param m The metadatable to set the given metadata value to.
	 * @param tag The tag to set the metadata value under.
	 * @param value The value to set to the tag.
	 * @param owner The owner of the metadata value.
	 */
	public static void setLazyMetadata(Metadatable m, String tag, Object value, Plugin owner) {
		m.setMetadata(tag, new LazyMetadataValue(owner, new Callable<Object>() {
			@Override
			public Object call() throws Exception {
				return value;
			}
		}));
	}
	
	/**
	 * Remove metadata with given key owned by given owner from given metadatable.
	 * 
	 * @param m The metadatable to remove the metadata value from.
	 * @param tag The tag of the metadata value to remove.
	 * @param owner The owner of the metadata value.
	 */
	public static void removeMetadata(Metadatable m, String tag, Plugin owner) {
		m.removeMetadata(tag, owner);
	}
}
