package net.maunium.bukkit.Maussentials.Modules.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

/**
 * The /maureload command
 * 
 * @author Tulir293
 * @since 0.1
 */
public class CommandReload implements CommandModule {
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.reload")) return true;
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("list")) plugin.modules(sender);
			else return false;
			return true;
		} else if (args.length > 1) {
			if (args[0].equalsIgnoreCase("load")) {
				byte b = plugin.loadModule(args[1]);
				if (b == 1) sender.sendMessage(plugin.translateStd("module.loaded", args[1]));
				else if (b == 0) sender.sendMessage(plugin.translateErr("module.alreadyloaded", args[1]));
				else if (b == -1) sender.sendMessage(plugin.translateErr("module.notfound", args[1]));
			} else if (args[0].equalsIgnoreCase("unload")) {
				byte b = plugin.unloadModule(args[1]);
				if (b == 1) sender.sendMessage(plugin.translateStd("module.unloaded", args[1]));
				else if (b == 0) sender.sendMessage(plugin.translateErr("module.alreadyunloaded", args[1]));
				else if (b == -1) sender.sendMessage(plugin.translateErr("module.notfound", args[1]));
			} else if (args[0].equalsIgnoreCase("reload")) {
				if (plugin.reloadModule(args[1])) sender.sendMessage(plugin.translateStd("module.reloaded", args[1]));
				else sender.sendMessage(plugin.translateErr("module.notfound", args[1]));
			} else return false;
			return true;
		} else return false;
	}
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
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
		sender.sendMessage(plugin.translateErr("module.help", label));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
