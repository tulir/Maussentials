package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

public class CommandSeen extends CommandModule {
	private Maussentials plugin;
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		reload();
	}
	
	@Override
	public void reload() {
		plugin.getCommand("mauseen").setExecutor(this);
		this.permission = "maussentials.seen";
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO: Seen command implementation
		return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(plugin.stag + plugin.translate("seen.help"));
	}
	
}
