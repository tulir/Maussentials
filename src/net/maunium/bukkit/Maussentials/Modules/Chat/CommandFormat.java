package net.maunium.bukkit.Maussentials.Modules.Chat;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.MauCommandExecutor;

public class CommandFormat implements MauCommandExecutor {
	private Maussentials plugin;
	private MauChat host;
	
	public CommandFormat(Maussentials plugin, MauChat host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 1) {
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < args.length; i++)
				sb.append(args[i] + " ");
			sb.deleteCharAt(sb.length() - 1);
			String format = sb.toString();
			
			UUID u;
			try {
				u = plugin.getPlayerData().getLatestUUIDByName(args[0]);
			} catch (SQLException e) {
				u = null;
			}
			
			if (u == null) {
				host.setGroupFormat(args[0], format);
				sender.sendMessage(plugin.translateStd("chat.format.group", args[0], format));
			} else {
				host.setPlayerFormat(u, format);
				sender.sendMessage(plugin.translateStd("chat.format.player", args[0], format));
			}
			
			return true;
		} else return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(plugin.translateErr("chat.format.usage", label));
	}
}
