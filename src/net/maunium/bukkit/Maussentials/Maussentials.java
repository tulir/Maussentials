package net.maunium.bukkit.Maussentials;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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

import net.maunium.bukkit.Maussentials.Utils.MauModule;

public class Maussentials extends JavaPlugin {
	
	public String version;
	public final String name = "Maussentials", author = "Tulir293", stag = ChatColor.DARK_GREEN + "[" + ChatColor.GREEN + name + ChatColor.DARK_GREEN + "] "
			+ ChatColor.GRAY, errtag = ChatColor.DARK_RED + "[" + ChatColor.RED + name + ChatColor.DARK_RED + "] " + ChatColor.RED;
	private Map<String, MauModule> modules = new HashMap<String, MauModule>();
	private MauData md;
	
	@Override
	public void onEnable() {
		long st = System.currentTimeMillis();
		version = this.getDescription().getVersion();
		this.saveDefaultConfig();
		
		try {
			initDatabase();
			md.open();
		} catch (SQLException e) {
			getLogger().severe("Failed to initialize database!");
			e.printStackTrace();
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
	
	public MauData getMauData() {
		return md;
	}
	
	public void enableModule(String name, MauModule m){
		m.initialize(this);
		modules.put(name, m);
	}
	
	/**
	 * Initializes the MauData system.
	 * 
	 * @throws SQLException
	 */
	public void initDatabase() throws SQLException {
		String database = getConfig().getString("sql.database");
		switch (this.getConfig().getString("sql.type").toLowerCase(Locale.ENGLISH)) {
			case "mysql":
				md = new MauData(new MySQL(Logger.getLogger("Minecraft"), "Maussentials", getConfig().getString("sql.hostname"),
						getConfig().getInt("sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "sqlite":
				md = new MauData(new SQLite(Logger.getLogger("Minecraft"), "Maussentials", this.getDataFolder().getPath(), database));
				break;
			case "h2":
				md = new MauData(new H2(Logger.getLogger("Minecraft"), "Maussentials", this.getDataFolder().getPath(), database));
				break;
			case "postgresql":
				md = new MauData(new PostgreSQL(Logger.getLogger("Minecraft"), "Maussentials", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "microsoftsql":
			case "mssql":
				md = new MauData(new MicrosoftSQL(Logger.getLogger("Minecraft"), "Maussentials", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "oracle":
				md = new MauData(new Oracle(Logger.getLogger("Minecraft"), "Maussentials", getConfig().getString("sql.hostname"), getConfig()
						.getInt("sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "ingres":
				md = new MauData(new Ingres(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "informix":
				md = new MauData(new Informix(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "db2":
				md = new MauData(new DB2(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig()
						.getInt("sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "frontbase":
				md = new MauData(new FrontBase(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "firebird":
				md = new MauData(new Firebird(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "mongo":
				md = new MauData(new Mongo(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "msql":
				md = new MauData(new mSQL(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
			case "maxdb":
				md = new MauData(new MaxDB(Logger.getLogger("Minecraft"), "[Maussentials] ", getConfig().getString("sql.hostname"), getConfig().getInt(
						"sql.port"), database, getConfig().getString("sql.username"), getConfig().getString("sql.password")));
				break;
		}
	}
}