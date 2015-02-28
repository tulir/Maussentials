package net.maunium.bukkit.Maussentials;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lib.PatPeter.SQLibrary.Database;

import org.bukkit.plugin.java.JavaPlugin;

import net.maunium.bukkit.Maussentials.Modules.CommandKill;
import net.maunium.bukkit.Maussentials.Modules.CommandSeen;
import net.maunium.bukkit.Maussentials.Modules.CommandSpy;
import net.maunium.bukkit.Maussentials.Modules.CommandUUID;
import net.maunium.bukkit.Maussentials.Modules.DatabaseHandler;
import net.maunium.bukkit.Maussentials.Modules.Godmode;
import net.maunium.bukkit.Maussentials.Modules.PlayerData;
import net.maunium.bukkit.Maussentials.Modules.PrivateMessaging;
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
		this.saveResource("/languages/en_US.lang", true);
		
		try {
			this.i18n = I18n.createInstance(new File(this.getDataFolder(), "languages"), getConfig().getString("language"));
		} catch (IOException e) {
			die("Failed to initialize internationalization", e);
		}
		
		this.stag = translate("message.stag");
		this.errtag = translate("message.errtag");
		
		enableModule("database", dbh = new DatabaseHandler());
		enableModule("welcome-message", new WelcomeMessage());
		enableModule("playerdata", /*pd = */new PlayerData());
		enableModule("command-uuid", new CommandUUID());
		enableModule("command-kill", new CommandKill());
		enableModule("command-seen", new CommandSeen());
		enableModule("godmode", new Godmode());
		enableModule("privatemessaging", new PrivateMessaging());
		enableModule("commandspy", new CommandSpy());
		
		int et = (int) (System.currentTimeMillis() - st);
		getLogger().info(name + " v" + version + " by " + author + " enabled in " + et + "ms.");
	}
	
	@Override
	public void onDisable() {
		long st = System.currentTimeMillis();
		
		if(instance == this) instance = null;
		
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
	
	public void reloadModule(String name) {
		getModule(name).reload();
	}
	
	public MauModule getModule(String name) {
		return modules.get(name);
	}
	
	public void enableModule(String name, MauModule m) {
		m.initialize(this);
		modules.put(name, m);
	}
	
	/**
	 * Get the latest instance of Maussentials.
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