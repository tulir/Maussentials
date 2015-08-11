package net.maunium.bukkit.Maussentials.Modules.Chat;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;

public class CommandFormat implements CommandExecutor {
	private Maussentials plugin;
	private MauChat host;
	
	public CommandFormat(Maussentials plugin, MauChat host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 2) {
			StringBuffer sb = new StringBuffer();
			for (int i = 2; i < args.length; i++)
				sb.append(args[i] + " ");
			sb.deleteCharAt(sb.length() - 1);
			String format = sb.toString();
			
			if (args[0].equalsIgnoreCase("group")) {
				host.setGroupFormat(args[1], format);
			} else if (args[0].equalsIgnoreCase("player")) {
				UUID u;
				try {
					u = plugin.getPlayerData().getLatestUUIDByName(args[1]);
				} catch (SQLException e) {
					e.printStackTrace();
					return true;
				}
				host.setPlayerFormat(u, format);
			} else return false;
			return true;
		} else return false;
	}
	
}
