package net.maunium.bukkit.Maussentials.Modules.Bans;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.DateUtils;

public class CommandSBan implements CommandExecutor {
	private Maussentials plugin;
	private MauBans host;
	
	public CommandSBan(Maussentials plugin, MauBans host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.bans.ban")) return true;
		if (args.length == 1) {
			sender.sendMessage(plugin.translateErr("bans.error.stdban.typemissing"));
			return true;
		} else if (args.length > 1) {
			UUID u = null;
			try {
				u = plugin.getPlayerData().getLatestUUIDByName(args[0]);
			} catch (SQLException e) {
				sender.sendMessage(plugin.translateErr("bans.error.sql", args[0], e.getMessage()));
				e.printStackTrace();
				return true;
			}
			
			if (u == null) {
				sender.sendMessage(plugin.translateErr("bans.error.nevervisited.player", args[0]));
				return true;
			}
			
			OfflinePlayer p = plugin.getServer().getOfflinePlayer(u);
			
			boolean silent = false;
			if (args.length > 2) silent = true;
			
			StandardBan sb = host.getStandardBan(args[1]);
			
			if (sb == null) {
				sender.sendMessage(plugin.translateErr("bans.error.stdban.nosuchkey", args[1]));
				return true;
			}
			
			host.ban(u, sender instanceof Player ? ((Player) sender).getUniqueId().toString() : "CONSOLE", sb.getReason(),
					sb.isPermanent() ? -1 : System.currentTimeMillis() + sb.getTimeout());
			
			if (sb.isPermanent()) {
				if (!silent)
					plugin.getServer().broadcast(plugin.translatePlain("bans.broadcast.banned", p.getName(), sb.getReason(), sender.getName()),
							"maussentials.bans.see.ban");
				
				if (p.isOnline()) p.getPlayer().kickPlayer(plugin.translatePlain("bans.ban.permanent", sb.getReason(), sender.getName(), p.getUniqueId()));
			} else {
				if (!silent)
					plugin.getServer().broadcast(
							plugin.translatePlain("bans.broadcast.tempbanned", p.getName(), sb.getReason(), sender.getName(),
									DateUtils.getDurationBreakdown(sb.getTimeout(), DateUtils.MODE_FOR)), "maussentials.bans.see.ban");
				
				if (p.isOnline())
					p.getPlayer().kickPlayer(
							plugin.translatePlain("bans.ban.temporary", sb.getReason(), sender.getName(),
									DateUtils.getDurationBreakdown(sb.getTimeout(), DateUtils.MODE_FOR), p.getUniqueId()));
			}
			return true;
		} else {
			sender.sendMessage(plugin.translateErr("bans.help.stdban", label));
			return true;
		}
	}
}
