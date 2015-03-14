package net.maunium.bukkit.Maussentials.Modules.Bans;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.DateUtils;

public class CommandTempBanIP implements CommandExecutor {
	private Maussentials plugin;
	private MauBans host;
	
	public CommandTempBanIP(Maussentials plugin, MauBans host) {
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
			host.ipban(ip, (sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "CONSOLE", reason, System.currentTimeMillis() + time);
			
			if (!silent)
				plugin.getServer().broadcast(
						plugin.translatePlain("bans.broadcast.tempipbanned" + (username != null ? ".byname" : ""), ip, reason, sender.getName(),
								DateUtils.getDurationBreakdown(time, DateUtils.MODE_FOR), username), "maussentials.bans.see.ipban");
			return true;
		} else {
			sender.sendMessage(plugin.translateErr("bans.help.tempipban", label));
			return true;
		}
	}
}
