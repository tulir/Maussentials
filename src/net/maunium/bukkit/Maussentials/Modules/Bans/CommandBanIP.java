package net.maunium.bukkit.Maussentials.Modules.Bans;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;

public class CommandBanIP implements CommandExecutor {
	private Maussentials plugin;
	private MauBans host;
	
	public CommandBanIP(Maussentials plugin, MauBans host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.bans.ipban")) return true;
		if (args.length == 1) {
			sender.sendMessage(plugin.translateErr("bans.error.reasonmissing"));
			return true;
		} else if (args.length > 1) {
			String ip, username = null;
			
			if (args[0].contains(".")) ip = args[0];
			else {
				try {
					UUID u = plugin.getPlayerData().getLatestUUIDByName(args[0]);
					
					if (u != null) {
						ip = plugin.getPlayerData().getLatestIPByUUID(u);
						username = plugin.getPlayerData().getNameByUUID(u);
					} else {
						sender.sendMessage(plugin.translateErr("bans.error.nevervisited.ip", args[0]));
						return true;
					}
				} catch (SQLException e) {
					sender.sendMessage(plugin.translateErr("bans.error.sql", args[0], e.getMessage()));
					e.printStackTrace();
					return true;
				}
			}
			
			if (ip == null) {
				sender.sendMessage(plugin.translateErr("bans.error.nevervisited.ip", args[0]));
				return true;
			}
			
			StringBuffer sb = new StringBuffer();
			boolean silent = false;
			for (int i = 1; i < args.length; i++) {
				if (i == args.length - 1 && args[i].equalsIgnoreCase("silent")) {
					silent = true;
					continue;
				}
				sb.append(args[i]);
				sb.append(" ");
			}
			sb.deleteCharAt(sb.length() - 1);
			String reason = sb.toString();
			
			host.ipban(ip, (sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "CONSOLE", reason, -1);
			
			if (!silent)
				plugin.getServer().broadcast(
						plugin.translatePlain("bans.broadcast.ipbanned" + (username != null ? ".byname" : ""), ip, reason, sender.getName(), username),
						"maussentials.bans.see.ipban");
			return true;
		} else {
			sender.sendMessage(plugin.translateErr("bans.help.ipban", label));
			return true;
		}
	}
}
