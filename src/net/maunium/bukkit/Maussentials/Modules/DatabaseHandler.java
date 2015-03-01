package net.maunium.bukkit.Maussentials.Modules;

import java.sql.SQLException;
import java.util.Locale;
import java.util.logging.Logger;

import lib.PatPeter.SQLibrary.*;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

/**
 * Handles the database for Maussentials.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class DatabaseHandler implements MauModule {
	private Maussentials plugin;
	private Database db;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		String database = plugin.getConfig().getString("sql.database");
		switch (plugin.getConfig().getString("sql.type").toLowerCase(Locale.ENGLISH)) {
			case "mysql":
				db = new MySQL(Logger.getLogger("Minecraft"), "Maussentials", plugin.getConfig().getString("sql.hostname"), plugin.getConfig().getInt(
						"sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				break;
			case "flatfile":
			case "sqlite":
				db = new SQLite(Logger.getLogger("Minecraft"), "Maussentials", plugin.getDataFolder().getPath(), database);
				break;
			case "h2":
				db = new H2(Logger.getLogger("Minecraft"), "Maussentials", plugin.getDataFolder().getPath(), database);
				break;
			case "postgresql":
				db = new PostgreSQL(Logger.getLogger("Minecraft"), "Maussentials", plugin.getConfig().getString("sql.hostname"), plugin.getConfig().getInt(
						"sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				break;
			case "microsoftsql":
			case "mssql":
				try {
					db = new MicrosoftSQL(Logger.getLogger("Minecraft"), "Maussentials", plugin.getConfig().getString("sql.hostname"), plugin.getConfig()
							.getInt("sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "oracledb":
			case "oracle":
				try {
					db = new Oracle(Logger.getLogger("Minecraft"), "Maussentials", plugin.getConfig().getString("sql.hostname"), plugin.getConfig().getInt(
							"sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				break;
			case "ingres":
				db = new Ingres(Logger.getLogger("Minecraft"), "[Maussentials] ", plugin.getConfig().getString("sql.hostname"), plugin.getConfig().getInt(
						"sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				break;
			case "informix":
				db = new Informix(Logger.getLogger("Minecraft"), "[Maussentials] ", plugin.getConfig().getString("sql.hostname"), plugin.getConfig().getInt(
						"sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				break;
			case "db2":
				db = new DB2(Logger.getLogger("Minecraft"), "[Maussentials] ", plugin.getConfig().getString("sql.hostname"), plugin.getConfig().getInt(
						"sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				break;
			case "frontbase":
				db = new FrontBase(Logger.getLogger("Minecraft"), "[Maussentials] ", plugin.getConfig().getString("sql.hostname"), plugin.getConfig().getInt(
						"sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				break;
			case "firebird":
				db = new Firebird(Logger.getLogger("Minecraft"), "[Maussentials] ", plugin.getConfig().getString("sql.hostname"), plugin.getConfig().getInt(
						"sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				break;
			case "mongo":
				db = new Mongo(Logger.getLogger("Minecraft"), "[Maussentials] ", plugin.getConfig().getString("sql.hostname"), plugin.getConfig().getInt(
						"sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				break;
			case "msql":
				db = new mSQL(Logger.getLogger("Minecraft"), "[Maussentials] ", plugin.getConfig().getString("sql.hostname"), plugin.getConfig().getInt(
						"sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				break;
			case "maxdb":
				db = new MaxDB(Logger.getLogger("Minecraft"), "[Maussentials] ", plugin.getConfig().getString("sql.hostname"), plugin.getConfig().getInt(
						"sql.port"), database, plugin.getConfig().getString("sql.username"), plugin.getConfig().getString("sql.password"));
				break;
		}
		db.open();
	}
	
	public Database getDB() {
		return db;
	}
	
	@Override
	public void unload() {
		db.close();
	}
}
