package net.maunium.bukkit.Maussentials;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.maunium.bukkit.Maussentials.Modules.DatabaseHandler;
import net.maunium.bukkit.Maussentials.Modules.DelayedActionListeners;
import net.maunium.bukkit.Maussentials.Modules.Godmode;
import net.maunium.bukkit.Maussentials.Modules.Language;
import net.maunium.bukkit.Maussentials.Modules.PrivateMessaging;
import net.maunium.bukkit.Maussentials.Modules.SignEditor;
import net.maunium.bukkit.Maussentials.Modules.Bans.MauBans;
import net.maunium.bukkit.Maussentials.Modules.Chat.MauChat;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandKill;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandPlainSay;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandPlugins;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandReload;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandSeen;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandSpy;
import net.maunium.bukkit.Maussentials.Modules.Commands.CommandUUID;
import net.maunium.bukkit.Maussentials.Modules.MauInfo.MauInfo;
import net.maunium.bukkit.Maussentials.Modules.PlayerData.PlayerData;
import net.maunium.bukkit.Maussentials.Modules.Teleportation.MauTPs;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;
import net.maunium.bukkit.Maussentials.Utils.Deserialization.DeserializationException;

import lib.PatPeter.SQLibrary.Database;

/**
 * The main class of Maussentials
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Maussentials extends JavaPlugin {
	// Instances of all modules, including those that have separately saved instances below.
	private Map<String, MauModule> modules = new HashMap<String, MauModule>();
	private List<String> disabled;
	/*
	 * Instances of modules that need to be accessed from other modules. Other than these, all
	 * modules are mostly separate from each other excluding public functionality modules such as
	 * the delayed actions system.
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
		
		Language.reloadFallback(this);
		
		saveDefaultConfig();
		File f = new File(getDataFolder(), "languages");
		if (!f.exists()) f.mkdirs();
		// Save the default language files
		saveResource("languages/en_US.lang", true);
		saveResource("languages/fi_FI.lang", true);
		saveResource("languages/de_DE.lang", true);
		
		disabled = getConfig().getStringList("disable-on-startup");
		DeserializationException.debug = getConfig().getBoolean("deserialization-debug");
		
		// Add and enable the modules
		addModule("database", dbh = new DatabaseHandler());
		addModule("playerdata", pd = new PlayerData());
		addModule("bans", bans = new MauBans());
		addModule("chat", new MauChat());
		addModule("teleportation", new MauTPs());
		addModule("mauinfo", new MauInfo());
		addModule("uuidtools", new CommandUUID());
		addModule("command-kill", new CommandKill());
		addModule("command-seen", new CommandSeen());
		addModule("command-plugins", new CommandPlugins());
		addModule("command-reloader", new CommandReload());
		addModule("command-psay", new CommandPlainSay());
		addModule("godmode", new Godmode());
		addModule("language", lang = new Language());
		addModule("signeditor", new SignEditor());
		addModule("privatemessaging", new PrivateMessaging());
		addModule("commandspy", new CommandSpy());
		addModule("delayed-actions", new DelayedActionListeners());
		
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
	
//	/**
//	 * Execute a full reload of Maussentials including all modules.
//	 */
//	public void fullReload() {
//		Language.reloadFallback(this);
//		
//		saveDefaultConfig();
//		File f = new File(getDataFolder(), "languages");
//		if (!f.exists()) f.mkdirs();
//		// Save the default language files
//		saveResource("languages/en_US.lang", true);
//		saveResource("languages/fi_FI.lang", true);
//		saveResource("languages/de_DE.lang", true);
//		
//		reloadConfig();
//		
//		for (MauModule m : modules.values()) {
//			if (m.isLoaded()) {
//				m.unload();
//				m.load(this);
//			}
//		}
//	}
	
	/**
	 * The default onCommand method. Returns an error saying that the command is not loaded or
	 * implemented.
	 */
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(translateErr("commandnotloaded", label));
		return true;
	}
	
	/**
	 * Very severe error which results in Maussentials not being able to continue operating.
	 */
	public void die(String message, Throwable error) {
		getLogger().severe(message + ":");
		error.printStackTrace();
		getLogger().severe("Maussentials can not continue to function before the error is fixed.");
		try {
			getPluginLoader().disablePlugin(this);
		} catch (Throwable t) {}
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
		name = name.toLowerCase(Locale.ENGLISH);
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
	 * @return -1 if the module could not be found. 0 if the module was already unloaded. 1 if the
	 *         module got successfully unloaded.
	 */
	public byte unloadModule(String name) {
		name = name.toLowerCase(Locale.ENGLISH);
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
	 * @return -1 if the module could not be found. 0 if the module was already loaded. 1 if the
	 *         module got successfully loaded.
	 */
	public byte loadModule(String name) {
		name = name.toLowerCase(Locale.ENGLISH);
		MauModule m = getModule(name);
		if (m == null) return -1;
		if (!m.isLoaded()) m.load(this);
		else return 0;
		return 1;
	}
	
	/**
	 * Add a module.
	 * 
	 * @param name The name of the module.
	 * @param m An instance of the module.
	 */
	public void addModule(String name, MauModule m) {
		name = name.toLowerCase(Locale.ENGLISH);
		modules.put(name, m);
		if (!disabled.contains(name)) m.load(this);
	}
	
	/**
	 * Get an instance of the module with the given name.
	 * 
	 * @return The instance of the module, or null if not found.
	 */
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
	
	/**
	 * @return The instance of MauBans connected to this instance of Maussentials.
	 */
	public MauBans getBans() {
		return bans;
	}
	
	/**
	 * @return The instance of PlayerData connected to this instance of Maussentials.
	 */
	public PlayerData getPlayerData() {
		return pd;
	}
	
	/**
	 * Check if the given player has the given permission. If not, send the player an error
	 * containing the permission node.
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
	
	/**
	 * Print the list of modules to the given CommandSender.
	 */
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
	
	/**
	 * Internationalization method. This should not be used from other plugins.
	 */
	public String translateStd(String node, Object... replace) {
		return Language.translateStd(lang, node, replace);
	}
	
	/**
	 * Internationalization method. This should not be used from other plugins.
	 */
	public String translateErr(String node, Object... replace) {
		return Language.translateErr(lang, node, replace);
	}
	
	/**
	 * Internationalization method. This should not be used from other plugins.
	 */
	public String translatePlain(String node, Object... replace) {
		return Language.translatePlain(lang, node, replace);
	}
}