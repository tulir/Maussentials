package net.maunium.bukkit.Maussentials;

import java.util.HashMap;
import java.util.Map;

import lib.PatPeter.SQLibrary.Database;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import net.maunium.bukkit.Maussentials.Modules.DatabaseHandler;
import net.maunium.bukkit.Maussentials.Modules.PlayerData;
import net.maunium.bukkit.Maussentials.Modules.WelcomeMessage;
import net.maunium.bukkit.Maussentials.Utils.MauModule;

public class Maussentials extends JavaPlugin {
	
	public String version;
	public final String name = "Maussentials", author = "Tulir293", stag = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + name + ChatColor.DARK_GREEN + "] "
			+ ChatColor.GRAY, errtag = ChatColor.DARK_RED + "[" + ChatColor.RED + name + ChatColor.DARK_RED + "] " + ChatColor.RED;
	private Map<String, MauModule> modules = new HashMap<String, MauModule>();
	private DatabaseHandler dbh;
	private PlayerData pd;
	
	@Override
	public void onEnable() {
		long st = System.currentTimeMillis();
		version = this.getDescription().getVersion();
		this.saveDefaultConfig();
		
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
	
	public Database getDB() {
		return dbh.getDB();
	}
	
	public void reloadModule(String name){
		getModule(name).reload();
	}
	
	public MauModule getModule(String name){
		return modules.get(name);
	}
	
	public void enableModule(String name, MauModule m) {
		m.initialize(this);
		modules.put(name, m);
	}
}