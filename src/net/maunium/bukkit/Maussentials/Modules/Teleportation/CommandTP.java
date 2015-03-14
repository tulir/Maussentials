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
			Player p = null;
			double x = Double.NaN, y = Double.NaN, z = Double.NaN;
			float yaw = Float.NaN, pitch = Float.NaN;
			World w = null;
			
			for (String s : args) {
				if (s.startsWith("-")) {
					s = s.substring(1);
					if (s.startsWith("-")) {
						s = s.substring(1);
						if (s.equalsIgnoreCase("exact")) {
							if (sender.hasPermission("maussentials.tp.exact")) exact = true;
							else sender.sendMessage(plugin.translateErr("tp.no-perms", "exact"));
						} else if (s.equalsIgnoreCase("force")) {
							if (sender.hasPermission("maussentials.tp.force")) force = true;
							else sender.sendMessage(plugin.translateErr("tp.no-perms", "force"));
						} else if (s.equalsIgnoreCase("safe")) {
							if (sender.hasPermission("maussentials.tp.safe")) safe = true;
							else sender.sendMessage(plugin.translateErr("tp.no-perms", "safe"));
						}
					} else {
						if (s.equalsIgnoreCase("e")) {
							if (sender.hasPermission("maussentials.tp.exact")) exact = true;
							else sender.sendMessage(plugin.translateErr("tp.no-perms", "exact"));
						} else if (s.equalsIgnoreCase("f")) {
							if (sender.hasPermission("maussentials.tp.force")) force = true;
							else sender.sendMessage(plugin.translateErr("tp.no-perms", "force"));
						} else if (s.equalsIgnoreCase("s")) {
							if (sender.hasPermission("maussentials.tp.safe")) safe = true;
							else sender.sendMessage(plugin.translateErr("tp.no-perms", "safe"));
						}
					}
				} else {
					
				}
			}
		}
		return false;
	}
}
