package net.maunium.bukkit.Maussentials;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lib.PatPeter.SQLibrary.Database;

import org.bukkit.plugin.java.JavaPlugin;

import net.maunium.bukkit.Maussentials.Modules.CommandKill;
import net.maunium.bukkit.Maussentials.Modules.CommandPlainSay;
import net.maunium.bukkit.Maussentials.Modules.CommandReload;
import net.maunium.bukkit.Maussentials.Modules.CommandPlugins;
import net.maunium.bukkit.Maussentials.Modules.CommandSeen;
import net.maunium.bukkit.Maussentials.Modules.CommandSpy;
import net.maunium.bukkit.Maussentials.Modules.CommandUUID;
import net.maunium.bukkit.Maussentials.Modules.DatabaseHandler;
import net.maunium.bukkit.Maussentials.Modules.DelayedTeleportListeners;
import net.maunium.bukkit.Maussentials.Modules.Godmode;
import net.maunium.bukkit.Maussentials.Modules.PlayerData;
import net.maunium.bukkit.Maussentials.Modules.PrivateMessaging;
import net.maunium.bukkit.Maussentials.Modules.SignEditor;
import net.maunium.bukkit.Maussentials.Modules.WelcomeMessage;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;
import net.maunium.bukkit.Maussentials.Utils.I18n;
import net.maunium.bukkit.Maussentials.Utils.I18n.I15r;

public class Maussentials extends JavaPlugin implements I15r {
	public String version, stag, errtag;
	public final String name = "Maussentials", author = "Tulir293";
	private Map<String, MauModule> modules = new HashMap<String, MauModule>();
	private DatabaseHandler dbh;
//	private PlayerData pd;
	private I18n i18n;
	private static Maussentials instance;
	
	@Override
	public void onLoad() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		long st = System.currentTimeMillis();
		version = this.getDescription().getVersion();
		this.saveDefaultConfig();
		this.saveResource("languages/en_US.lang", true);
		try {
			this.i18n = I18n.createInstance(new File(this.getDataFolder(), "languages"), getConfig().getString("language"));
		} catch (IOException e) {
			die("Failed to initialize internationalization", e);
		}
		
		this.stag = translate("stag");
		this.errtag = translate("errtag");
		
		addModule("database", dbh = new DatabaseHandler(), true);
		addModule("welcome-message", new WelcomeMessage(), true);
		addModule("playerdata", /* pd = */new PlayerData(), true);
		addModule("command-uuid", new CommandUUID(), true);
		addModule("command-kill", new CommandKill(), true);
		addModule("command-seen", new CommandSeen(), true);
		addModule("command-plugins", new CommandPlugins(), true);
		addModule("command-plugin", new CommandReload(), true);
		addModule("command-psay", new CommandPlainSay(), true);
		addModule("godmode", new Godmode(), true);
		addModule("signeditor", new SignEditor(), true);
		addModule("privatemessaging", new PrivateMessaging(), true);
		addModule("commandspy", new CommandSpy(), true);
		addModule("delayed-teleports", new DelayedTeleportListeners(), true);
		
		int et = (int) (System.currentTimeMillis() - st);
		getLogger().info(name + " v" + version + " by " + author + " enabled in " + et + "ms.");
	}
	
	@Override
	public void onDisable() {
		long st = System.currentTimeMillis();
		
		if (instance == this) instance = null;
		for (MauModule m : modules.values())
			m.unload();
		
		int et = (int) (System.currentTimeMillis() - st);
		getLogger().info(name + " v" + version + " by " + author + " disabled in " + et + "ms.");
	}
	
	public void die(String message, Throwable error) {
		getLogger().severe(message + ":");
		error.printStackTrace();
		getLogger().severe("Maussentials can not continue to function before the error is fixed.");
		this.getPluginLoader().disablePlugin(this);
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
	 * @return -1 if the module could not be found. 0 if the module was already unloaded. 1 if the module got
	 *         successfully unloaded.
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
	 * @return -1 if the module could not be found. 0 if the module was already loaded. 1 if the module got successfully
	 *         loaded.
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
	
	@Override
	public String translate(String node, Object... replace) {
		return i18n.translate(node, replace);
	}
}