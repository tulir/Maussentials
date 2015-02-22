package net.maunium.bukkit.Maussentials;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class MauUtils {
	public static final Charset utf8 = Charset.forName("UTF-8");
	
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
	
	public static MetadataValue getMetadata(Block b, String tag, Plugin owner) {
		for (MetadataValue mv : b.getMetadata(tag))
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
	
	public static Location parseLocation(String s) {
		String[] ss = s.split(", ");
		double x = Double.parseDouble(ss[0]);
		double y = Double.parseDouble(ss[1]);
		double z = Double.parseDouble(ss[2]);
		float yaw = Float.parseFloat(ss[3]);
		float pitch = Float.parseFloat(ss[4]);
		World w = Bukkit.getWorld(ss[5]);
		return new Location(w, x, y, z, yaw, pitch);
	}
	
	public static String toString(Location l) {
		if (l != null) return l.getX() + ", " + l.getY() + ", " + l.getZ() + ", " + l.getYaw() + ", " + l.getPitch() + ", "
				+ (l.getWorld() == null ? "Null" : l.getWorld().getName());
		else return "Null Location";
	}
	
	public static String toReadableString(Location l) {
		if (l != null) return round(l.getX(), 2) + ", " + round(l.getY(), 2) + ", " + round(l.getZ(), 2) + " @ "
				+ (l.getWorld() == null ? "Null" : l.getWorld().getName());
		else return "Null Location";
	}
	
	private static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();
		
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
}
