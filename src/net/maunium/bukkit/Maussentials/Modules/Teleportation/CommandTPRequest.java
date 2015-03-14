package net.maunium.bukkit.Maussentials.Modules.Teleportation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;

public class CommandTPRequest implements CommandExecutor {
	private Maussentials plugin;
	private MauTPs host;
	
	public CommandTPRequest(Maussentials plugin, MauTPs host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return false;
	}
}
