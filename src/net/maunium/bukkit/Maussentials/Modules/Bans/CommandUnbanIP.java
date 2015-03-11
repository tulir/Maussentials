package net.maunium.bukkit.Maussentials.Modules.Bans;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

public class CommandUnbanIP implements CommandExecutor {
	private Maussentials plugin;
	private MauBans host;
	
	public CommandUnbanIP(Maussentials plugin, MauBans host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandModule.checkPerms(sender, "maussentials.bans.unipban")) return true;
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
			
			if (!args[1].equalsIgnoreCase("silent"))
				plugin.getServer().broadcast(plugin.translatePlain("bans.broadcast.unipbanned", p.getName(), sender.getName()), "maussentials.bans.see.unipban");
			return true;
		} else {
			sender.sendMessage(plugin.translateErr("bans.help.unipban", label));
			return true;
		}
	}
}
