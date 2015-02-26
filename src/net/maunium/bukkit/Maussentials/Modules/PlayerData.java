package net.maunium.bukkit.Maussentials.Modules;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;
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
	
	// °FormatOff°
	public ResultSet setEntry(UUID uuid, String username, String ip, Location l) throws SQLException {
		return plugin.getDB().query("INSERT OR REPLACE INTO " + TABLE_PLAYERS + " VALUES ("
				+ "'" + uuid.toString() + "','"
				+ username + "','"
				+ ip + "','"
				+ System.currentTimeMillis() + "','"
				+ new SerializableLocation(l).toString()
				+ "');");
	}
	
	public ResultSet setTime(UUID uuid) throws SQLException {
		return plugin.getDB().query("UPDATE " + TABLE_PLAYERS
				+ " SET " + COLUMN_LASTLOGIN + "='" + System.currentTimeMillis()+ "'"
				+ " WHERE " + COLUMN_UUID + "='" + uuid.toString() + "';");
	}
	
	public ResultSet setLocation(UUID uuid, Location l) throws SQLException {
		return plugin.getDB().query("UPDATE " + TABLE_PLAYERS
				+ " SET " + COLUMN_LOCATION + "='" + new SerializableLocation(l).toString() + "'"
				+ " WHERE " + COLUMN_UUID + "='" + uuid.toString() + "';");
	}
	// °FormatOn°
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent evt) {
		if (evt.getAddress() != null && evt.getUniqueId() != null && evt.getName() != null) {
			String ia = evt.getAddress().getHostAddress();
			try {
				setEntry(evt.getUniqueId(), evt.getName(), ia, new Location(plugin.getServer().getWorld("world"), 0, 0, 0, 0, 0));
				plugin.getLogger().fine("Updated database. New entry: " + evt.getUniqueId() + ", " + evt.getName() + ", " + ia);
			} catch (SQLException e) {
				plugin.getLogger().severe("Failed attempt to add new entry: " + evt.getUniqueId() + ", " + evt.getName() + ", " + ia);
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		try {
			setLocation(evt.getPlayer().getUniqueId(), evt.getPlayer().getLocation());
			plugin.getLogger().fine("Updated Location for " + evt.getPlayer().getName());
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to update Location for " + evt.getPlayer().getName());
			e.printStackTrace();
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent evt) {
		try {
			setTime(evt.getPlayer().getUniqueId());
			plugin.getLogger().fine("Updated LastLogin time for " + evt.getPlayer().getName());
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to update LastLogin time for " + evt.getPlayer().getName());
			e.printStackTrace();
		}
		try {
			setLocation(evt.getPlayer().getUniqueId(), evt.getPlayer().getLocation());
			plugin.getLogger().fine("Updated Location for " + evt.getPlayer().getName());
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to update Location for " + evt.getPlayer().getName());
			e.printStackTrace();
		}
	}
}
