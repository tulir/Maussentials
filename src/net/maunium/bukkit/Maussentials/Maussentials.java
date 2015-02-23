package net.maunium.bukkit.Maussentials;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lib.PatPeter.SQLibrary.Database;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.maunium.bukkit.Maussentials.Modules.DatabaseHandler;
import net.maunium.bukkit.Maussentials.Modules.PlayerData;
import net.maunium.bukkit.Maussentials.Modules.WelcomeMessage;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;
import net.maunium.bukkit.Maussentials.Utils.I18n;
import net.maunium.bukkit.Maussentials.Utils.I18n.I15r;

public class Maussentials extends JavaPlugin implements I15r {
	
	public String version;
	public final String name = "Maussentials", author = "Tulir293", stag = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + name + ChatColor.DARK_GREEN + "] "
			+ ChatColor.GRAY, errtag = ChatColor.DARK_RED + "[" + ChatColor.RED + name + ChatColor.DARK_RED + "] " + ChatColor.RED;
	private Map<String, MauModule> modules = new HashMap<String, MauModule>();
	private DatabaseHandler dbh;
	private PlayerData pd;
	private I18n i18n;
	
	@Override
	public void onEnable() {
		long st = System.currentTimeMillis();
		version = this.getDescription().getVersion();
		this.saveDefaultConfig();
		
		try {
			this.i18n = I18n.createInstance(new File(this.getDataFolder(), "languages"), getConfig().getString("language"));
		} catch (IOException e) {
			die("Failed to initialize internationalization", e);
		}
		
		enableModule("database", dbh = new DatabaseHandler());
		enableModule("welcome-message", new WelcomeMessage());
		enableModule("playerdata", pd = new PlayerData());
		
		int et = (int) (System.currentTimeMillis() - st);
		getLogger().info(name + " v" + version + " by " + author + " enabled in " + et + "ms.");
	}
	
	@Override
	public void onDisable() {
		long st = System.currentTimeMillis();
		
		// TODO: Disable code
		
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
	
	public boolean checkPerms(CommandSender p, String permission) {
		if(!p.hasPermission(permission)) {
			p.sendMessage(translate("permission-error"));
			return false;
		} else return true;
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
	
	@Override
	public String translate(String node, Object... replace) {
		return i18n.translate(node, replace);
	}
}