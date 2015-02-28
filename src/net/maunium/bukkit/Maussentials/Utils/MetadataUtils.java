package net.maunium.bukkit.Maussentials.Utils;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.plugin.Plugin;

public class MetadataUtils {
	public static MetadataValue getMetadata(Metadatable m, String tag, Plugin owner) {
		for (MetadataValue mv : m.getMetadata(tag))
			if (mv.getOwningPlugin().equals(owner)) return mv;
		return null;
	}
	
	public static void setFixedMetadata(Metadatable m, String tag, Object value, Plugin owner) {
		m.setMetadata(tag, new FixedMetadataValue(owner, value));
	}
	
	public static void removeMetadata(Metadatable m, String tag, Plugin owner){
		m.removeMetadata(tag, owner);
	}
}
