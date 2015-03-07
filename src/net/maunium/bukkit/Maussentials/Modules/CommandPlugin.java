package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

/**
 * The /mauplugin command
 * 
 * @author Tulir293
 * @since 0.1
 */
public class CommandPlugin extends CommandModule {
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 2) {
			if (args[0].equalsIgnoreCase("module")) {
				if (args[1].equalsIgnoreCase("load")) {
					plugin.loadModule(args[2]);
				} else if (args[1].equalsIgnoreCase("unload")) {
					
				} else if (args[1].equalsIgnoreCase("reload")) {
					
				} else return false;
				return true;
			} else if (args[0].equalsIgnoreCase("plugin")) {
				Plugin p = getLoadedPlugin(args[1]);
				// TODO: Loading, reloading, unloading, restarting, enabling and disabling plugins and modules.
				// TODO: Check for non-loaded plugins.
				
				if (p == null) sender.sendMessage(plugin.errtag + plugin.translate("plugin.notfound", args[1]));
				return true;
			} else return false;
		} else return false;
	}
	
	private Plugin getLoadedPlugin(String name) {
		for (Plugin pp : plugin.getServer().getPluginManager().getPlugins())
			if (pp.getName().equalsIgnoreCase(name)) return pp;
		return null;
	}
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		this.permission = "maussentials.plugin";
		plugin.getCommand("mauplugin").setExecutor(this);
		loaded = true;
	}
	
	@Override
	public void unload() {
		plugin.getCommand("mauplugin").setExecutor(plugin);
		plugin = null;
		loaded = false;
	}
	
	@Override
	public void help(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(plugin.stag + plugin.translate("plugin.help", label));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
