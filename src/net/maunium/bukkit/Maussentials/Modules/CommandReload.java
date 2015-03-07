package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

/**
 * The /maureload command
 * 
 * @author Tulir293
 * @since 0.1
 */
public class CommandReload extends CommandModule {
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 2) {
			if (args[0].equalsIgnoreCase("module")) {
				if (args[1].equalsIgnoreCase("load")) {
					byte b = plugin.loadModule(args[2]);
					if (b == 1) sender.sendMessage(plugin.stag + plugin.translate("module.loaded", args[2]));
					else if (b == 0) sender.sendMessage(plugin.errtag + plugin.translate("module.alreadyloaded", args[2]));
					else if (b == -1) sender.sendMessage(plugin.errtag + plugin.translate("module.notfound", args[2]));
				} else if (args[1].equalsIgnoreCase("unload")) {
					byte b = plugin.unloadModule(args[2]);
					if (b == 1) sender.sendMessage(plugin.stag + plugin.translate("module.unloaded", args[2]));
					else if (b == 0) sender.sendMessage(plugin.errtag + plugin.translate("module.alreadyunloaded", args[2]));
					else if (b == -1) sender.sendMessage(plugin.errtag + plugin.translate("module.notfound", args[2]));
				} else if (args[1].equalsIgnoreCase("reload")) {
					if (plugin.reloadModule(args[2])) sender.sendMessage(plugin.stag + plugin.translate("module.reloaded", args[2]));
					else sender.sendMessage(plugin.errtag + plugin.translate("module.notfound", args[2]));
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
		this.permission = "maussentials.reload";
		plugin.getCommand("maureload").setExecutor(this);
		loaded = true;
	}
	
	@Override
	public void unload() {
		plugin.getCommand("maureload").setExecutor(plugin);
		plugin = null;
		loaded = false;
	}
	
	@Override
	public void help(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(plugin.stag + plugin.translate("plugin.help", label));
		sender.sendMessage(plugin.stag + plugin.translate("module.help", label));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
