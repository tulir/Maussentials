package net.maunium.bukkit.Maussentials.Modules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.MauModule;
import net.maunium.bukkit.Maussentials.Utils.SerializableLocation;

/**
 * @author Tulir293
 * @since 0.1
 */
public class PlayerData implements Listener, MauModule {
	private Maussentials plugin;
	public static final String TABLE_PLAYERS = "Players", TABLE_HISTORY = "OldNames";
	public static final String COLUMN_UUID = "UUID", COLUMN_USERNAME = "Username", COLUMN_IP = "IP", COLUMN_LASTLOGIN = "LastLogin",
			COLUMN_LOCATION = "Location", COLUMN_CHANGEDFROM = "ChangedFrom", COLUMN_CHANGEDTO = "ChangedTo";
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@Override
	public void reload() {
		try {
			// °FormatOff°
			// Create the table Players
			plugin.getDB().query("CREATE TABLE " + TABLE_PLAYERS + " ("
					+ COLUMN_UUID + " varchar(25) PRIMARY KEY"
					+ COLUMN_USERNAME + " varchar(16) NOT NULL"
					+ COLUMN_IP + " varchar(16) NOT NULL"
					+ COLUMN_CHANGEDTO + " INTEGER NOT NULL"
					+ COLUMN_LASTLOGIN + " INTEGER NOT NULL"
					+ COLUMN_LOCATION + " TEXT NOT NULL"
					+ ");");
			// Create the table OldNames
			plugin.getDB().query("CREATE TABLE " + TABLE_HISTORY + " ("
					+ COLUMN_UUID + " varchar(25) NOT NULL"
					+ COLUMN_USERNAME + " varchar(16) NOT NULL"
					+ COLUMN_CHANGEDTO + " INTEGER NOT NULL"
					+ COLUMN_CHANGEDFROM + " INTEGER NOT NULL"
					+ ");");
			// °FormatOn°
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerLogin(PlayerLoginEvent evt) {
		try {
			setEntry(evt.getPlayer().getUniqueId(), evt.getPlayer().getName(), evt.getAddress().getHostAddress(), evt.getPlayer().getLocation());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet setEntry(UUID uuid, String username, String ip, Location l) throws SQLException {
		return plugin.getDB().query(
				"INSERT OR REPLACE INTO " + TABLE_PLAYERS + " VALUES (" + "'" + uuid.toString() + "','" + username + "','" + ip + "','"
						+ System.currentTimeMillis() + "','" + new SerializableLocation(l).toString() + "');");
	}
	
	public ResultSet setTime(UUID uuid) throws SQLException {
		return plugin.getDB().query("UPDATE " + TABLE_PLAYERS + " SET LastLogin='" + System.currentTimeMillis() + "' WHERE UUID='" + uuid.toString() + "';");
	}
	
	public ResultSet setLocation(UUID uuid, Location l) throws SQLException {
		return plugin.getDB().query(
				"UPDATE " + TABLE_PLAYERS + " SET Location='" + new SerializableLocation(l).toString() + "' WHERE UUID='" + uuid.toString() + "';");
	}
}
