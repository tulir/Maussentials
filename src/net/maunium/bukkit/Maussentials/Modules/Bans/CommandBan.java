package net.maunium.bukkit.Maussentials.Modules.Bans;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;

public class CommandBan implements CommandExecutor {
	private Maussentials plugin;
	private MauBans host;
	
	public CommandBan(Maussentials plugin, MauBans host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 1) {
			sender.sendMessage(plugin.translateErr("ban.error.reasonmissing"));
			return true;
		} else if (args.length > 1) {
			UUID u = null;
			try {
				u = plugin.getPlayerData().getLatestUUIDByName(args[0]);
			} catch (SQLException e) {
				sender.sendMessage(plugin.translateErr("error.nevervisited", args[0]));
				e.printStackTrace();
				return true;
			}
			
			if (u == null) {
				sender.sendMessage(plugin.translateErr("error.nevervisited", args[0]));
				return true;
			}
			
			OfflinePlayer op = plugin.getServer().getOfflinePlayer(u);
			
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < args.length; i++) {
				sb.append(args[i]);
				sb.append(" ");
			}
			sb.deleteCharAt(sb.length() - 1);
			String reason = sb.toString();
			
			host.ban(u, (sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "CONSOLE", reason, -1);
			
			boolean silent = false;
			if (reason.endsWith(" silent")) {
				reason = reason.substring(reason.length() - 7, reason.length());
				silent = true;
			}
			
			if (!silent)
				plugin.getServer().broadcast(plugin.translatePlain("ban.broadcast.banned", op.getName(), reason, sender.getName()), "maussentials.bans.see.ban");
			return true;
		}
		return false;
	}
}
