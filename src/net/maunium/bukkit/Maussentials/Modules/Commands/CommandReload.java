package net.maunium.bukkit.Maussentials.Modules.Commands;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredListener;

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
	private PluginManager pm;
	private boolean loaded = false;
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.reload")) return true;
		if (label.equalsIgnoreCase("maussentials")) {
			plugin.fullReload();
			sender.sendMessage(plugin.translateStd("plugin.restarted", plugin.getName()));
			return true;
		}
		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("module") && args[1].equalsIgnoreCase("list")) plugin.modules(sender);
			else if (args[0].equalsIgnoreCase("plugin") && args[1].equalsIgnoreCase("list")) CommandPlugins.plugins(plugin, sender);
			else return false;
			return true;
		} else if (args.length > 2) {
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
			} else if (args[0].equalsIgnoreCase("config")) {
				plugin.reloadConfig();
				sender.sendMessage(plugin.translateStd("config.reloaded"));
				return true;
			} else if (args[0].equalsIgnoreCase("plugin")) {
				if (args[1].equalsIgnoreCase("load")) {
					sender.sendMessage(plugin.translateErr("nyi"));
				} else if (args[1].equalsIgnoreCase("unload")) {
					sender.sendMessage(plugin.translateErr("nyi"));
				} else if (args[1].equalsIgnoreCase("reload")) {
					sender.sendMessage(plugin.translateErr("nyi"));
				} else if (args[1].equalsIgnoreCase("enable")) {
					Plugin p = getLoadedPlugin(args[2]);
					if (p == null) sender.sendMessage(plugin.translateErr("plugin.notfound", args[2]));
					if (!p.isEnabled()) {
						pm.enablePlugin(p);
						sender.sendMessage(plugin.translateStd("plugin.enabled", p.getName()));
					} else sender.sendMessage(plugin.translateErr("plugin.alreadyenabled"));
				} else if (args[1].equalsIgnoreCase("disable")) {
					Plugin p = getLoadedPlugin(args[2]);
					if (p == plugin) {
						sender.sendMessage(plugin.translateErr("plugin.disable.self"));
						return true;
					}
					if (p == null) sender.sendMessage(plugin.translateErr("plugin.notfound", args[2]));
					if (p.isEnabled()) {
						pm.disablePlugin(p);
						sender.sendMessage(plugin.translateStd("plugin.disabled", p.getName()));
					} else sender.sendMessage(plugin.translateErr("plugin.alreadydisabled"));
				} else if (args[1].equalsIgnoreCase("restart")) {
					Plugin p = getLoadedPlugin(args[2]);
					if (p == null) sender.sendMessage(plugin.translateErr("plugin.notfound", args[2]));
					if (p == plugin) plugin.fullReload();
					else {
						if (p.isEnabled()) pm.disablePlugin(p);
						pm.enablePlugin(p);
					}
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
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("plugin")) sender.sendMessage(plugin.translateErr("reloader.plugin.help", label));
			else if (args[0].equalsIgnoreCase("module")) sender.sendMessage(plugin.translateErr("reloader.module.help", label));
			else sender.sendMessage(plugin.translateErr("reloader.help", label));
		} else sender.sendMessage(plugin.translateErr("reloader.help", label));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	private boolean load(String name) {
		Plugin target = null;
		File pluginDir = plugin.getDataFolder().getParentFile();
		File pluginFile = new File(pluginDir, name + ".jar");
		
		if (!pluginFile.isFile()) {
			for (File f : pluginDir.listFiles()) {
				if (f.getName().endsWith(".jar")) {
					try {
						PluginDescriptionFile desc = plugin.getPluginLoader().getPluginDescription(f);
						if (desc.getName().equalsIgnoreCase(name)) {
							pluginFile = f;
							break;
						}
					} catch (Throwable t) {
						return false;
					}
				}
			}
		}
		
		try {
			target = Bukkit.getPluginManager().loadPlugin(pluginFile);
		} catch (Throwable t) {
			return false;
		}
		
		target.onLoad();
		Bukkit.getPluginManager().enablePlugin(target);
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private boolean unload(Plugin plugin) {
		String name = plugin.getName();
		PluginManager pluginManager = Bukkit.getPluginManager();
		SimpleCommandMap commandMap = null;
		List<Plugin> plugins = null;
		Map<String, Plugin> names = null;
		Map<String, Command> commands = null;
		Map<Event, SortedSet<RegisteredListener>> listeners = null;
		
		boolean reloadlisteners = true;
		
		if (pluginManager != null) {
			
			try {
				
				Field pluginsField = Bukkit.getPluginManager().getClass().getDeclaredField("plugins");
				pluginsField.setAccessible(true);
				plugins = (List<Plugin>) pluginsField.get(pluginManager);
				
				Field lookupNamesField = Bukkit.getPluginManager().getClass().getDeclaredField("lookupNames");
				lookupNamesField.setAccessible(true);
				names = (Map<String, Plugin>) lookupNamesField.get(pluginManager);
				
				try {
					Field listenersField = Bukkit.getPluginManager().getClass().getDeclaredField("listeners");
					listenersField.setAccessible(true);
					listeners = (Map<Event, SortedSet<RegisteredListener>>) listenersField.get(pluginManager);
				} catch (Exception e) {
					reloadlisteners = false;
				}
				
				Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
				commandMapField.setAccessible(true);
				commandMap = (SimpleCommandMap) commandMapField.get(pluginManager);
				
				Field knownCommandsField = SimpleCommandMap.class.getDeclaredField("knownCommands");
				knownCommandsField.setAccessible(true);
				commands = (Map<String, Command>) knownCommandsField.get(commandMap);
				
			} catch (Throwable t) {
				return false;
			}
		}
		
		pluginManager.disablePlugin(plugin);
		if (plugins != null && plugins.contains(plugin)) plugins.remove(plugin);
		if (names != null && names.containsKey(name)) names.remove(name);
		if (listeners != null && reloadlisteners) {
			for (SortedSet<RegisteredListener> set : listeners.values()) {
				for (Iterator<RegisteredListener> it = set.iterator(); it.hasNext();) {
					RegisteredListener value = it.next();
					if (value.getPlugin() == plugin) it.remove();
				}
			}
		}
		
		if (commandMap != null) {
			for (Iterator<Map.Entry<String, Command>> it = commands.entrySet().iterator(); it.hasNext();) {
				Map.Entry<String, Command> entry = it.next();
				if (entry.getValue() instanceof PluginCommand) {
					PluginCommand c = (PluginCommand) entry.getValue();
					if (c.getPlugin() == plugin) {
						c.unregister(commandMap);
						it.remove();
					}
				}
			}
		}
		
		ClassLoader cl = plugin.getClass().getClassLoader();
		
		if (cl instanceof URLClassLoader) {
			try {
				((URLClassLoader) cl).close();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		
		System.gc();
		return true;
		
	}
}
