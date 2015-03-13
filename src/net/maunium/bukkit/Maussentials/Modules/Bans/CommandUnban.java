package net.maunium.bukkit.Maussentials.Modules.Bans;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;

public class CommandUnban implements CommandExecutor {
	private Maussentials plugin;
	private MauBans host;
	
	public CommandUnban(Maussentials plugin, MauBans host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.bans.unban")) return true;
		if (args.length > 0) {
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
			
			host.unban(u);
			
			if (args.length > 1 && args[1].equalsIgnoreCase("silent")) return true;
			plugin.getServer().broadcast(plugin.translatePlain("bans.broadcast.unbanned", p.getName(), sender.getName()), "maussentials.bans.see.unban");
			return true;
		} else {
			sender.sendMessage(plugin.translateErr("bans.help.unban", label));
			return true;
		}
	}
}
