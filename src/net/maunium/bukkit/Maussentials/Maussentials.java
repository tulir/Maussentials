package net.maunium.bukkit.Maussentials;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lib.PatPeter.SQLibrary.Database;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.maunium.bukkit.Maussentials.Modules.BasicMessages;
import net.maunium.bukkit.Maussentials.Modules.DatabaseHandler;
import net.maunium.bukkit.Maussentials.Modules.DelayedActionListeners;
import net.maunium.bukkit.Maussentials.Modules.Godmode;
import net.maunium.bukkit.Maussentials.Modules.Language;
import net.maunium.bukkit.Maussentials.Modules.PlayerData;
import net.maunium.bukkit.Maussentials.Modules.PrivateMessaging;
import net.maunium.bukkit.Maussentials.Modules.SignEditor;
import net.maunium.bukkit.Maussentials.Modules.Bans.MauBans;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandKill;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandPlainSay;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandPlugins;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandReload;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandSeen;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandSpy;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandUUID;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

public class Maussentials extends JavaPlugin {
	// Instances of all modules, including those that have separately saved instances below.
	private Map<String, MauModule> modules = new HashMap<String, MauModule>();
	/*
	 * Instances of modules that need to be accessed from other modules. Other than these, all modules are mostly separate from eachother excluding public
	 * functionality modules such as the delayed actions system.
	 */
	private DatabaseHandler dbh;
	private Language lang;
	private MauBans bans;
	private PlayerData pd;
	// Singleton instance of this class
	private static Maussentials instance;
	
	@Override
	public void onEnable() {
		long st = System.currentTimeMillis();
		
		saveDefaultConfig();
		// Save the default language files
		saveResource("languages/en_US.lang", true);
		saveResource("languages/fi_FI.lang", true);
		// Save the default motd and rules
		saveResource("motd.txt", true);
		saveResource("rules.txt", true);
		
		// Add and enable the modules
		addModule("database", dbh = new DatabaseHandler(), true);
		addModule("playerdata", pd = new PlayerData(), true);
		addModule("bans", bans = new MauBans(), true);
		addModule("basic-messages", new BasicMessages(), true);
		addModule("command-uuid", new CommandUUID(), true);
		addModule("command-kill", new CommandKill(), true);
		addModule("command-seen", new CommandSeen(), true);
		addModule("command-plugins", new CommandPlugins(), true);
		addModule("command-plugin", new CommandReload(), true);
		addModule("command-psay", new CommandPlainSay(), true);
		addModule("godmode", new Godmode(), true);
		addModule("language", lang = new Language(), true);
		addModule("signeditor", new SignEditor(), true);
		addModule("privatemessaging", new PrivateMessaging(), true);
		addModule("commandspy", new CommandSpy(), true);
		addModule("delayed-teleports", new DelayedActionListeners(), true);
		
		instance = this;
		
		int et = (int) (System.currentTimeMillis() - st);
		getLogger().info("Maussentials v" + getDescription().getVersion() + " by Tulir293 enabled in " + et + "ms.");
	}
	
	@Override
	public void onDisable() {
		long st = System.currentTimeMillis();
		
		if (instance == this) instance = null;
		for (MauModule m : modules.values())
			m.unload();
		
		int et = (int) (System.currentTimeMillis() - st);
		getLogger().info("Maussentials v" + getDescription().getVersion() + " by Tulir293 disabled in " + et + "ms.");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(translateErr("commandnotloaded", label));
		return true;
	}
	
	public void die(String message, Throwable error) {
		getLogger().severe(message + ":");
		error.printStackTrace();
		getLogger().severe("Maussentials can not continue to function before the error is fixed.");
		getPluginLoader().disablePlugin(this);
	}
	
	public Database getDB() {
		return dbh.getDB();
	}
	
	/**
	 * Reload the module with the given name.
	 * 
	 * @param name The name of the module to reload.
	 * @return True if the module was reloaded. False if the module was not found.
	 */
	public boolean reloadModule(String name) {
		MauModule m = getModule(name);
		if (m == null) return false;
		if (m.isLoaded()) m.unload();
		m.load(this);
		return true;
	}
	
	/**
	 * Unload the module with the given name.
	 * 
	 * @param name The name of the module to unload.
	 * @return -1 if the module could not be found. 0 if the module was already unloaded. 1 if the module got successfully unloaded.
	 */
	public byte unloadModule(String name) {
		MauModule m = getModule(name);
		if (m == null) return -1;
		if (m.isLoaded()) m.unload();
		else return 0;
		return 1;
	}
	
	/**
	 * Unload the module with the given name.
	 * 
	 * @param name The name of the module to unload.
	 * @return -1 if the module could not be found. 0 if the module was already loaded. 1 if the module got successfully loaded.
	 */
	public byte loadModule(String name) {
		MauModule m = getModule(name);
		if (m == null) return -1;
		if (!m.isLoaded()) m.load(this);
		else return 0;
		return 1;
	}
	
	public void addModule(String name, MauModule m, boolean load) {
		modules.put(name, m);
		if (load) m.load(this);
	}
	
	public MauModule getModule(String name) {
		return modules.get(name);
	}
	
	/**
	 * Get the latest instance of Maussentials.
	 * 
	 * @return An instance of Maussentials, or null if Maussentials isn't loaded.
	 */
	public static Maussentials getInstance() {
		return instance;
	}
	
	public MauBans getBans() {
		return bans;
	}
	
	public PlayerData getPlayerData() {
		return pd;
	}
	
	public String translateStd(String node, Object... replace) {
		return lang.translateStd(node, replace);
	}
	
	public String translateErr(String node, Object... replace) {
		return lang.translateErr(node, replace);
	}
	
	public String translatePlain(String node, Object... replace) {
		return lang.translate(node, replace);
	}
	
	/**
	 * Check if the given player has the given permission. If not, send the player an error containing the permission node.
	 * 
	 * @param p The player to check.
	 * @param permission The permission to check.
	 * @return True if the player has the permission, false otherwise.
	 */
	public boolean checkPerms(CommandSender p, String permission) {
		if (!p.hasPermission(permission)) {
			p.sendMessage(Maussentials.getInstance().translateErr("permission-error", permission));
			return false;
		} else return true;
	}
	
	public void modules(CommandSender sender) {
		int en = 0, dis = 0;
		StringBuffer sb = new StringBuffer();
		for (Entry<String, MauModule> e : modules.entrySet()) {
			if (e.getValue().isLoaded()) {
				sb.append(ChatColor.GREEN);
				en++;
			} else {
				sb.append(ChatColor.DARK_GREEN);
				dis++;
			}
			sb.append(e.getKey());
			sb.append(ChatColor.WHITE);
			sb.append(", ");
		}
		
		sender.sendMessage(translateStd("module.list", en, dis, sb.toString()));
	}
}