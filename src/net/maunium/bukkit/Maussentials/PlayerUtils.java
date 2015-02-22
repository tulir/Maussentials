package net.maunium.bukkit.Maussentials;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class PlayerUtils {
	public static List<Player> getPlayers(Location location, double radius) {
		List<Player> rtrn = new ArrayList<Player>();
		double radiusSquared = radius * radius;
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (!p.getLocation().getWorld().equals(location.getWorld())) continue;
			if (p.getLocation().distanceSquared(location) <= radiusSquared) rtrn.add(p);
		}
		return rtrn;
	}
	
	public static Player getPlayer(String name) {
		for (Player p : Bukkit.getServer().getOnlinePlayers())
			if (p.getName().equalsIgnoreCase(name)) return p;
		return null;
	}
	
	public static OfflinePlayer getOfflinePlayer(String name) {
		for (OfflinePlayer op : Bukkit.getServer().getOfflinePlayers())
			if (op.getName().equalsIgnoreCase(name)) return op;
		return null;
	}
	
	public static MetadataValue getMetadata(Player p, String tag, Plugin owner) {
		for (MetadataValue mv : p.getMetadata(tag))
			if (mv.getOwningPlugin().equals(owner)) return mv;
		return null;
	}
	
	public static boolean isOnline(UUID player) {
		Player p = Bukkit.getServer().getPlayer(player);
		if (p != null && p.isOnline()) return true;
		else return false;
	}
	
	public static boolean isOnline(String player) {
		return getPlayer(player) != null;
	}
}
