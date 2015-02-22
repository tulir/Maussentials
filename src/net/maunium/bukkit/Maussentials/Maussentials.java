package net.maunium.bukkit.Maussentials;

import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Logger;

import lib.PatPeter.SQLibrary.DB2;
import lib.PatPeter.SQLibrary.Firebird;
import lib.PatPeter.SQLibrary.FrontBase;
import lib.PatPeter.SQLibrary.H2;
import lib.PatPeter.SQLibrary.Informix;
import lib.PatPeter.SQLibrary.Ingres;
import lib.PatPeter.SQLibrary.MaxDB;
import lib.PatPeter.SQLibrary.MicrosoftSQL;
import lib.PatPeter.SQLibrary.Mongo;
import lib.PatPeter.SQLibrary.MySQL;
import lib.PatPeter.SQLibrary.Oracle;
import lib.PatPeter.SQLibrary.PostgreSQL;
import lib.PatPeter.SQLibrary.SQLite;
import lib.PatPeter.SQLibrary.mSQL;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class Maussentials extends JavaPlugin {
	
	public String version;
	public final String name = "Maussentials", author = "Tulir293", stag = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + name + ChatColor.DARK_GREEN + "] "
			+ ChatColor.GRAY, errtag = ChatColor.DARK_RED + "[" + ChatColor.RED + name + ChatColor.DARK_RED + "] " + ChatColor.RED;
	private PlayerData pd;
	
	@Override
	public void onEnable() {
		long st = System.currentTimeMillis();
		version = this.getDescription().getVersion();
		this.saveDefaultConfig();
		
		switch (this.getConfig().getString("sql.type").toLowerCase(Locale.ENGLISH)) {
			case "mysql":
				pd = new PlayerData(new MySQL(Logger.getLogger("Minecraft"), "Maussentials", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "sqlite":
				pd = new PlayerData(new SQLite(Logger.getLogger("Minecraft"), "Maussentials", this.getDataFolder().getPath(), getConfig().getString("sql.database")));
				break;
			case "h2":
				pd = new PlayerData(new H2(Logger.getLogger("Minecraft"), "Maussentials", this.getDataFolder().getPath(), getConfig().getString("sql.database")));
				break;
			case "postgresql":
				pd = new PlayerData(new PostgreSQL(Logger.getLogger("Minecraft"), "Maussentials", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "microsoftsql":
			case "mssql":
				try {
					pd = new PlayerData(new MicrosoftSQL(Logger.getLogger("Minecraft"), "Maussentials", getConfig().getString("sql.hostname"), getConfig().getInt(
							"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				} catch (SQLException e) {
					getLogger().severe("Failed to initialize MicrosoftSQL connection");
					e.printStackTrace();
				}
				break;
			case "oracle":
				try {
					pd = new PlayerData(new Oracle(Logger.getLogger("Minecraft"), "Maussentials", getConfig().getString("sql.hostname"), getConfig().getInt(
							"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				} catch (SQLException e) {
					getLogger().severe("Failed to initialize Oracle database connection");
					e.printStackTrace();
				}
				break;
			case "ingres":
				pd = new PlayerData(new Ingres(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "informix":
				pd = new PlayerData(new Informix(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "db2":
				pd = new PlayerData(new DB2(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "frontbase":
				pd = new PlayerData(new FrontBase(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "firebird":
				pd = new PlayerData(new Firebird(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "mongo":
				pd = new PlayerData(new Mongo(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "msql":
				pd = new PlayerData(new mSQL(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "maxdb":
				pd = new PlayerData(new MaxDB(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), getConfig().getString("sql.database"), getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
		}
		
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
}