package net.maunium.bukkit.Maussentials.Modules.Teleportation;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.MauCommandExecutor;
import net.maunium.bukkit.Maussentials.Utils.SerializableLocation;

public class CommandTP implements MauCommandExecutor {
	private Maussentials plugin;
	
	public CommandTP(Maussentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.tp")) return true;
		if (args.length > 0) {
			Player p = plugin.getServer().getPlayer(args[0]);
			if (p != null) {
				if (args.length > 1) {
					Player p2 = plugin.getServer().getPlayer(args[1]);
					if (p2 != null) {
						p.teleport(p2);
						p2.sendMessage(plugin.translateStd("tp.teleported.other.toplayer", p.getName(), p2.getName()));
						return true;
					} else if (args.length > 3) {
						double x, y, z;
						float yaw = p.getLocation().getYaw(), pitch = p.getLocation().getPitch();
						World w = null;
						try {
							x = Double.parseDouble(args[1]);
							y = Double.parseDouble(args[2]);
							z = Double.parseDouble(args[3]);
						} catch (NumberFormatException e) {
							return false;
						}
						if (args.length > 4) {
							try {
								yaw = Float.parseFloat(args[4]);
							} catch (NumberFormatException e) {}
							if (args.length > 5) {
								try {
									pitch = Float.parseFloat(args[5]);
								} catch (NumberFormatException e) {}
								
								if (args.length > 6) w = plugin.getServer().getWorld(args[6]);
							}
						}
						if (w == null) w = p.getWorld();
						
						Location l = new Location(w, x, y, z, yaw, pitch);
						p.teleport(l);
						sender.sendMessage(plugin.translateStd("tp.teleported.other.tolocation", p.getName(), new SerializableLocation(l).toReadableString()));
						return true;
					}
				} else if (sender instanceof Player) {
					Player p2 = (Player) sender;
					p2.teleport(p);
					sender.sendMessage(plugin.translateStd("tp.teleported.self.toplayer", p.getName()));
					return true;
				}
			} else if (sender instanceof Player) {
				p = (Player) sender;
				double x, y, z;
				float yaw = p.getLocation().getYaw(), pitch = p.getLocation().getPitch();
				World w = null;
				try {
					x = Double.parseDouble(args[0]);
					y = Double.parseDouble(args[1]);
					z = Double.parseDouble(args[2]);
				} catch (NumberFormatException e) {
					return false;
				}
				if (args.length > 3) {
					try {
						yaw = Float.parseFloat(args[3]);
					} catch (NumberFormatException e) {}
					if (args.length > 4) {
						try {
							pitch = Float.parseFloat(args[4]);
						} catch (NumberFormatException e) {}
						
						if (args.length > 5) w = plugin.getServer().getWorld(args[5]);
					}
				}
				if (w == null) w = p.getWorld();
				
				Location l = new Location(w, x, y, z, yaw, pitch);
				p.teleport(l);
				sender.sendMessage(plugin.translateStd("tp.teleported.self.tolocation", new SerializableLocation(l).toReadableString()));
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) sender.sendMessage(plugin.translateErr("tp.help.player", label));
		else sender.sendMessage(plugin.translateErr("tp.help.console", label));
	}
}
