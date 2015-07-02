package net.maunium.bukkit.Maussentials.Modules.Chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;

public class CommandNick implements CommandExecutor {
	private Maussentials plugin;
	private MauChat host;
	
	public CommandNick(Maussentials plugin, MauChat host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
