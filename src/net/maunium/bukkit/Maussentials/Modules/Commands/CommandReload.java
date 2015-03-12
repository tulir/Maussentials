package net.maunium.bukkit.Maussentials.Modules.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

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
	private PluginManager pm;
	private boolean loaded = false;
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (!checkPerms(sender, "maussentials.reload")) return true;
		if (args.length > 2) {
			if (args[0].equalsIgnoreCase("module")) {
				if (args[1].equalsIgnoreCase("load")) {
					byte b = plugin.loadModule(args[2]);
					if (b == 1) sender.sendMessage(plugin.translateStd("module.loaded", args[2]));
					else if (b == 0) sender.sendMessage(plugin.translateErr("module.alreadyloaded", args[2]));
					else if (b == -1) sender.sendMessage(plugin.translateErr("module.notfound", args[2]));
				} else if (args[1].equalsIgnoreCase("unload")) {
					byte b = plugin.unloadModule(args[2]);
					if (b == 1) sender.sendMessage(plugin.translateStd("module.unloaded", args[2]));
					else if (b == 0) sender.sendMessage(plugin.translateErr("module.alreadyunloaded", args[2]));
					else if (b == -1) sender.sendMessage(plugin.translateErr("module.notfound", args[2]));
				} else if (args[1].equalsIgnoreCase("reload")) {
					if (plugin.reloadModule(args[2])) sender.sendMessage(plugin.translateStd("module.reloaded", args[2]));
					else sender.sendMessage(plugin.translateErr("module.notfound", args[2]));
				} else return false;
				return true;
			} else if (args[0].equalsIgnoreCase("plugin")) {
				if (args[1].equalsIgnoreCase("load")) {
					sender.sendMessage(plugin.translateErr("nyi"));
				} else if (args[1].equalsIgnoreCase("unload")) {
					sender.sendMessage(plugin.translateErr("nyi"));
				} else if (args[1].equalsIgnoreCase("reload")) {
					sender.sendMessage(plugin.translateErr("nyi"));
				} else if (args[1].equalsIgnoreCase("enable")) {
					Plugin p = getLoadedPlugin(args[1]);
					if (p == null) sender.sendMessage(plugin.translateErr("plugin.notfound", args[1]));
					if (!p.isEnabled()) {
						pm.enablePlugin(p);
						sender.sendMessage(plugin.translateStd("plugin.enabled", p.getName()));
					} else sender.sendMessage(plugin.translateErr("plugin.alreadyenabled"));
				} else if (args[1].equalsIgnoreCase("disable")) {
					Plugin p = getLoadedPlugin(args[1]);
					if (p == null) sender.sendMessage(plugin.translateErr("plugin.notfound", args[1]));
					if (p.isEnabled()) {
						pm.disablePlugin(p);
						sender.sendMessage(plugin.translateStd("plugin.disabled", p.getName()));
					} else sender.sendMessage(plugin.translateErr("plugin.alreadydisabled"));
				} else if (args[1].equalsIgnoreCase("restart")) {
					Plugin p = getLoadedPlugin(args[1]);
					if (p == null) sender.sendMessage(plugin.translateErr("plugin.notfound", args[1]));
					if (p.isEnabled()) pm.disablePlugin(p);
					pm.enablePlugin(p);
					sender.sendMessage(plugin.translateStd("plugin.restarted", p.getName()));
				} else return false;
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
		plugin.getCommand("maureload").setExecutor(this);
		pm = plugin.getServer().getPluginManager();
		loaded = true;
	}
	
	@Override
	public void unload() {
		plugin.getCommand("maureload").setExecutor(plugin);
		plugin = null;
		pm = null;
		loaded = false;
	}
	
	@Override
	public void help(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(plugin.translateErr("reloader.help", label));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
