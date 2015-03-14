package net.maunium.bukkit.Maussentials.Modules.Teleportation;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;

public class CommandTP implements CommandExecutor {
	private Maussentials plugin;
	private MauTPs host;
	
	public CommandTP(Maussentials plugin, MauTPs host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			boolean exact = false, force = false, safe = false;
			int nextArg = 0;
			Player p1 = null, p2 = null, relTo = null;
			double x = Double.NaN, y = Double.NaN, z = Double.NaN;
			float yaw = Float.NaN, pitch = Float.NaN;
			World w = null;
			
			for (String s : args) {
				if (nextArg == 1) {
					Player p = gplayer(sender, s);
					if (p != null) relTo = p;
					nextArg = 0;
				} else if (nextArg == 2) {
					Player p = gplayer(sender, s);
					if (p != null) p1 = p;
					nextArg = 0;
				} else if (nextArg == 3) {
					Player p = gplayer(sender, s);
					if (p != null) p2 = p;
					nextArg = 0;
				} else if (nextArg == 10) {
					double d = gdouble(sender, s);
					if (d != Double.NaN) z = d;
					nextArg = 0;
				} else if (nextArg == 11) {
					double d = gdouble(sender, s);
					if (d != Double.NaN) y = d;
					nextArg = 0;
				} else if (nextArg == 12) {
					double d = gdouble(sender, s);
					if (d != Double.NaN) z = d;
					nextArg = 0;
				} else if (nextArg == 13) {
					float f = gfloat(sender, s);
					if (f != Float.NaN) yaw = f;
					nextArg = 0;
				} else if (nextArg == 14) {
					float f = gfloat(sender, s);
					if (f != Float.NaN) pitch = f;
					nextArg = 0;
				} else if (nextArg == 15) {
					World ww = gworld(sender, s);
					if (ww != null) w = ww;
					nextArg = 0;
				} else if (s.startsWith("-")) {
					s = s.substring(1);
					if (s.startsWith("-")) {
						s = s.substring(1);
						if (s.equalsIgnoreCase("exact")) exact = true;
						else if (s.equalsIgnoreCase("force")) force = true;
						else if (s.equalsIgnoreCase("safe")) safe = true;
						else if (s.equalsIgnoreCase("relative")) nextArg = 1;
						else if (s.equalsIgnoreCase("player1")) nextArg = 2;
						else if (s.equalsIgnoreCase("player2")) nextArg = 3;
						else if (s.equalsIgnoreCase("xpos")) nextArg = 10;
						else if (s.equalsIgnoreCase("ypos")) nextArg = 11;
						else if (s.equalsIgnoreCase("zpos")) nextArg = 12;
						else if (s.equalsIgnoreCase("yaw")) nextArg = 13;
						else if (s.equalsIgnoreCase("pitch")) nextArg = 14;
						else if (s.equalsIgnoreCase("world")) nextArg = 15;
					} else {
						if (s.equalsIgnoreCase("e")) exact = true;
						else if (s.equalsIgnoreCase("f")) force = true;
						else if (s.equalsIgnoreCase("s")) safe = true;
						else if (s.equalsIgnoreCase("r")) nextArg = 1;
						else if (s.equalsIgnoreCase("p1")) nextArg = 2;
						else if (s.equalsIgnoreCase("p2")) nextArg = 3;
						else if (s.equalsIgnoreCase("x")) nextArg = 10;
						else if (s.equalsIgnoreCase("y")) nextArg = 11;
						else if (s.equalsIgnoreCase("z")) nextArg = 12;
						else if (s.equalsIgnoreCase("w")) nextArg = 15;
					}
				} else {
					Player p = plugin.getServer().getPlayer(s);
					if (p != null) {
						if (p1 == null) p1 = p;
						else if (p2 == null) p2 = p;
						else plugin.translateErr("tp.args.too-many-players");
					} else {
						World ww = plugin.getServer().getWorld(s);
						if (ww != null) {
							if (w == null) w = ww;
							else plugin.translateErr("tp.args.too-many-worlds");
						} else {
							// TODO: X, Y, Z, Yaw, Pitch
						}
					}
				}
			}
		}
		return false;
	}
	
	private float gfloat(CommandSender sender, String s) {
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			sender.sendMessage(plugin.translateErr("tp.args.nfe", s));
			return Float.NaN;
		}
	}
	
	private double gdouble(CommandSender sender, String s) {
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			sender.sendMessage(plugin.translateErr("tp.args.nfe", s));
			return Double.NaN;
		}
	}
	
	private Player gplayer(CommandSender sender, String s) {
		Player p = plugin.getServer().getPlayer(s);
		if (p != null) return p;
		else sender.sendMessage(plugin.translateErr("tp.args.pnf", s));
		return null;
	}
	
	private World gworld(CommandSender sender, String s) {
		World p = plugin.getServer().getWorld(s);
		if (p != null) return p;
		else sender.sendMessage(plugin.translateErr("tp.args.wnf", s));
		return null;
	}
}
