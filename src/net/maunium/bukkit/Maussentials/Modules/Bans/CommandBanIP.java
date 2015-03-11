package net.maunium.bukkit.Maussentials.Modules.Bans;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

public class CommandBanIP implements CommandExecutor {
	private Maussentials plugin;
	private MauBans host;
	
	public CommandBanIP(Maussentials plugin, MauBans host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandModule.checkPerms(sender, "maussentials.bans.ipban")) return true;
		if (args.length == 1) {
			sender.sendMessage(plugin.translateErr("ban.error.reasonmissing"));
			return true;
		} else if (args.length > 1) {
			String ip;
			
			if (args[0].contains(".")) ip = args[0];
			else {
				try {
					UUID u = plugin.getPlayerData().getLatestUUIDByName(args[0]);
					
					if (u != null) ip = plugin.getPlayerData().getLatestIPByUUID(u);
					else {
						sender.sendMessage(plugin.translateErr("ban.error.nevervisited", args[0]));
						return true;
					}
				} catch (SQLException e) {
					sender.sendMessage(plugin.translateErr("ban.error.sql", args[0], e.getMessage()));
					e.printStackTrace();
					return true;
				}
			}
			
			if (ip == null) {
				sender.sendMessage(plugin.translateErr("ban.error.nevervisited", args[0]));
				return true;
			}
			
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < args.length; i++) {
				sb.append(args[i]);
				sb.append(" ");
			}
			sb.deleteCharAt(sb.length() - 1);
			String reason = sb.toString();
			
			host.ipban(ip, (sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "CONSOLE", reason, -1);
			
			boolean silent = false;
			if (reason.endsWith(" silent")) {
				reason = reason.substring(reason.length() - 7, reason.length());
				silent = true;
			}
			
			if (!silent)
				plugin.getServer().broadcast(plugin.translatePlain("ban.broadcast.ipbanned", ip, reason, sender.getName()), "maussentials.bans.see.ipban");
			return true;
		}
		return false;
	}
}
