package net.maunium.bukkit.Maussentials.Utils;

import org.bukkit.block.Block;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class MauUtils {
	public static MetadataValue getMetadata(Block b, String tag, Plugin owner) {
		for (MetadataValue mv : b.getMetadata(tag))
			if (mv.getOwningPlugin().equals(owner)) return mv;
		return null;
	}
}
