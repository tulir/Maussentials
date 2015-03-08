package net.maunium.bukkit.Maussentials.Modules.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

/**
 * The /mauseen command
 * 
 * @author Tulir293
 * @since 0.1
 */
public class CommandSeen extends CommandModule {
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getCommand("mauseen").setExecutor(this);
		this.permission = "maussentials.seen";
		loaded = true;
	}
	
	@Override
	public void unload() {
		plugin.getCommand("mauseen").setExecutor(plugin);
		plugin = null;
		loaded = false;
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO: Seen command implementation
		return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(plugin.translateErr("seen.help"));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
