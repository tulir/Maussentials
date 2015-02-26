package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

/**
 * A module for private messaging between players.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class PrivateMessaging extends CommandModule {
	private Maussentials plugin;
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void reload() {
		this.plugin.getCommand("maumessage").setExecutor(this);
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		
		return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		
	}
	
}
