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
		if (!plugin.checkPerms(sender, "maussentials.bans.ban")) return true;
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
			
			host.ban(u, (sender instanceof Player) ? ((Player) sender).getUniqueId().toString() : "CONSOLE", reason, -1);
			
			if (!silent)
				plugin.getServer()
						.broadcast(plugin.translatePlain("bans.broadcast.banned", p.getName(), reason, sender.getName()), "maussentials.bans.see.ban");
			
			if (p.isOnline()) p.getPlayer().kickPlayer(plugin.translatePlain("bans.ban.permanent", reason, sender.getName(), p.getUniqueId()));
			return true;
		} else {
			sender.sendMessage(plugin.translateErr("bans.help.ban", label));
			return true;
		}
	}
}
