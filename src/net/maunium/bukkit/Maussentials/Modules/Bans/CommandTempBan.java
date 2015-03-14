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

public class CommandTempBan implements CommandExecutor {
	private Maussentials plugin;
	private MauBans host;
	
	public CommandTempBan(Maussentials plugin, MauBans host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.bans.tempban")) return true;
		if (args.length == 1) {
			sender.sendMessage(plugin.translateErr("bans.error.reasonmissing"));
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
			
			String t = args[1];
			long time = -1;
			
			try {
				if (t.endsWith("ms")) time = Long.parseLong(t.substring(0, t.length() - 2));
				else {
					time = Long.parseLong(t.substring(0, t.length() - 1));
					if (t.endsWith("s")) time = time * 1000;
					else if (t.endsWith("m")) time = time * 1000 * 60;
					else if (t.endsWith("h")) time = time * 1000 * 60 * 60;
					else if (t.endsWith("d")) time = time * 1000 * 60 * 60 * 24;
					else if (t.endsWith("W")) time = time * 1000 * 60 * 60 * 24 * 7;
					else if (t.endsWith("M")) time = time * 1000 * 60 * 60 * 24 * 30;
					else if (t.endsWith("Y")) time = time * 1000 * 60 * 60 * 24 * 365;
					else if (t.endsWith("D")) time = time * 1000 * 60 * 60 * 24 * 365 * 10;
					else if (t.endsWith("C")) time = time * 1000 * 60 * 60 * 24 * 365 * 100;
					else {
						sender.sendMessage(plugin.translateErr("bans.error.invalidtime"));
						return true;
					}
				}
			} catch (Throwable e) {
				sender.sendMessage(plugin.translateErr("bans.error.invalidtime"));
				return true;
			}
			
			if (time < 1) {
				sender.sendMessage(plugin.translateErr("bans.error.invalidtime"));
				return true;
			}
			
			StringBuffer sb = new StringBuffer();
			boolean silent = false;
			for (int i = 2; i < args.length; i++) {
				if (i == args.length - 1 && args[i].equalsIgnoreCase("silent")) {
					silent = true;
					continue;
				}
				sb.append(args[i]);
				sb.append(" ");
			}
			sb.deleteCharAt(sb.length() - 1);
			String reason = sb.toString();
			
			host.ban(u, (sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "CONSOLE", reason, System.currentTimeMillis() + time);
			
			if (!silent)
				plugin.getServer().broadcast(
						plugin.translatePlain("bans.broadcast.tempbanned", p.getName(), reason, sender.getName(),
								DateUtils.getDurationBreakdown(time, DateUtils.MODE_FOR)), "maussentials.bans.see.ban");
			
			if (p.isOnline())
				p.getPlayer().kickPlayer(
						plugin.translatePlain("bans.ban.temporary", reason, sender.getName(), DateUtils.getDurationBreakdown(time, DateUtils.MODE_FOR),
								p.getUniqueId()));
			return true;
		} else {
			sender.sendMessage(plugin.translateErr("bans.help.tempban", label));
			return true;
		}
	}
}
