package net.maunium.bukkit.Maussentials.Modules.Bans;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.MauCommandExecutor;

public class CommandBanHistory implements MauCommandExecutor {
	private Maussentials plugin;
	private MauBans host;
	
	public CommandBanHistory(Maussentials plugin, MauBans host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		
	}
}
