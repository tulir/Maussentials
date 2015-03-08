package net.maunium.bukkit.Maussentials.Modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonArray;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;
import net.maunium.bukkit.Maussentials.Utils.SerializableLocation;

/**
 * Player data database systems.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class PlayerData implements Listener, MauModule {
	private Maussentials plugin;
	/** Table Names */
	public static final String TABLE_PLAYERS = "Players", TABLE_HISTORY = "OldNames", TABLE_IPS = "IPs";
	/** Used in many tables */
	public static final String COLUMN_UUID = "UUID", COLUMN_USERNAME = "Username";
	/** Used in IPs table */
	public static final String COLUMN_IP = "IP", COLUMN_LASTUSED = "LastUsed";
	/** Used in Players table */
	public static final String COLUMN_LASTLOGIN = "LastLogin", COLUMN_LOCATION = "Location";
	/** Used in OldNames table */
	public static final String COLUMN_CHANGEDTO = "ChangedTo";
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		try {
			// °FormatOff°
			// Create the table Players
			plugin.getDB().query("CREATE TABLE " + TABLE_PLAYERS + " ("
					+ COLUMN_UUID + " varchar(25) NOT NULL,"
					+ COLUMN_USERNAME + " varchar(16) NOT NULL,"
					+ COLUMN_LASTLOGIN + " INTEGER NOT NULL,"
					+ COLUMN_LOCATION + " TEXT NOT NULL,"
					+ "PRIMARY KEY (" + COLUMN_UUID + "));");
			// Create the table OldNames
			plugin.getDB().query("CREATE TABLE " + TABLE_HISTORY + " ("
					+ COLUMN_UUID + " varchar(25) NOT NULL,"
					+ COLUMN_USERNAME + " varchar(16) NOT NULL,"
					+ COLUMN_CHANGEDTO + " INTEGER NOT NULL,"
					+ "PRIMARY KEY (" + COLUMN_UUID + ", " + COLUMN_USERNAME + "));");
			// Create the table IPs
			plugin.getDB().query("CREATE TABLE " + TABLE_HISTORY + " ("
					+ COLUMN_UUID + " varchar(25) NOT NULL,"
					+ COLUMN_IP + " varchar(16) NOT NULL,"
					+ COLUMN_LASTUSED + " INTEGER NOT NULL,"
					+ "PRIMARY KEY (" + COLUMN_UUID + ", " + COLUMN_IP + "));");
			// °FormatOn°
		} catch (SQLException e) {}
		loaded = true;
	}
	
	@Override
	public void unload() {
		HandlerList.unregisterAll(this);
		plugin = null;
		loaded = false;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	/*
	 * Listeners for prelogin, join and quit.
	 */
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent evt) {
		if (evt.getAddress() != null && evt.getUniqueId() != null && evt.getName() != null) {
			try {
				ResultSet rs = plugin.getDB().query(
						"SELECT " + COLUMN_USERNAME + " FROM " + TABLE_PLAYERS + " WHERE " + COLUMN_UUID + "='" + evt.getUniqueId() + "';");
				if (rs.next()) {
					String old = rs.getString(COLUMN_USERNAME);
					if (!old.equals(evt.getName())) updateNameHistory(evt.getUniqueId());
				} else updateNameHistory(evt.getUniqueId());
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
			String ia = evt.getAddress().getHostAddress();
			try {
				setEntry(evt.getUniqueId(), evt.getName(), ia, new Location(plugin.getServer().getWorlds().get(0), 0, 0, 0));
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
	
	/**
	 * Method to update name history for the specific UUID from Mojang's servers. Called when the database system
	 * notices that an UUID logs in with an unidentified username.
	 */
	private void updateNameHistory(UUID uuid) {
		try {
			// Request the name history of the UUID.
			BufferedReader br = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/user/profiles/" + uuid.toString().replace("-", "")
					+ "/names").openStream()));
			String in = "";
			String s;
			// Read the response
			while ((s = br.readLine()) != null)
				in += s;
			
			// Make sure that it isn't empty.
			if (in.isEmpty()) throw new RuntimeException("Failed to process name change: Mojang API returned empty name history.");
			
			// Parse the response json.
			JsonArray ja = new JsonParser().parse(in).getAsJsonArray();
			// Loop through the name change entries and add the details to the string list.
			for (int i = 0; i < ja.size(); i++) {
				JsonObject jo = ja.get(i).getAsJsonObject();
				String name = jo.get("name").getAsString();
				try {
					// °FormatOff°
					ResultSet rs = plugin.getDB().query("SELECT * FROM " + TABLE_HISTORY
							+ " WHERE " + COLUMN_UUID + " = '" + uuid.toString() + "'"
							+ " AND " + COLUMN_USERNAME + " = '" + name + "'"
							+ ";");
					// °FormatOn°
					
					if (!rs.next()) {
						if (jo.has("changedToAt")) {
							long time = jo.get("changedToAt").getAsLong();
							setHistory(uuid, name, time);
						} else setHistory(uuid, name, 0);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e1) {
			throw new RuntimeException("Failed to fetch name history from Mojang API", e1);
		}
	}
	
	/*
	 * Methods for updating the values of the database.
	 */
	
	// °FormatOff°
	public ResultSet setEntry(UUID uuid, String username, String ip, Location l) throws SQLException {
		return plugin.getDB().query("INSERT OR REPLACE INTO " + TABLE_PLAYERS + " VALUES ("
				+ "'" + uuid.toString() + "','"
				+ username + "','"
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
	
	public ResultSet setHistory(UUID uuid, String username, long changedTo) throws SQLException {
		return plugin.getDB().query("INSERT OR REPLACE INTO " + TABLE_HISTORY + " VALUES ('"
				+ uuid.toString() + "','"
				+ username + "','"
				+ changedTo
				+ "');");
	}
	
	public ResultSet setIPs(UUID uuid, String ip) throws SQLException {
		return plugin.getDB().query("INSERT OR REPLACE INTO " + TABLE_IPS + " VALUES ('"
				+ uuid.toString() + "','"
				+ ip + "','"
				+ System.currentTimeMillis()
				+ "');");
	}
	// °FormatOn°
	
	/*
	 * Getting data from the IP Log
	 */
	/**
	 * Get all the UUIDs that have visited from the given IP.
	 * 
	 * @param ip The IP to search.
	 * @return A map containing the UUIDs as keys and last used timestamps as values.
	 * @throws SQLException If database querying or resultset getting throws something.
	 */
	public Map<UUID, Long> getUUIDsFromIP(String ip) throws SQLException {
		ResultSet rs = plugin.getDB().query("SELECT * FROM " + TABLE_IPS + " WHERE " + COLUMN_IP + "='" + ip + "';");
		Map<UUID, Long> rtrn = new HashMap<UUID, Long>();
		while (rs.next())
			rtrn.put(UUID.fromString(rs.getString(COLUMN_UUID)), rs.getLong(COLUMN_LASTUSED));
		return rtrn;
	}
	
	/**
	 * Get all the IPs that the given UUID has logged in using.
	 * 
	 * @param uuid The UUID to search.
	 * @return A map containing the IPs as keys and last used timestamps as values.
	 * @throws SQLException If database querying or resultset getting throws something.
	 */
	public Map<String, Long> getIPsFromUUID(UUID uuid) throws SQLException {
		ResultSet rs = plugin.getDB().query("SELECT * FROM " + TABLE_IPS + " WHERE " + COLUMN_UUID + "='" + uuid.toString() + "';");
		Map<String, Long> rtrn = new HashMap<String, Long>();
		while (rs.next())
			rtrn.put(rs.getString(COLUMN_IP), rs.getLong(COLUMN_LASTUSED));
		return rtrn;
	}
	
	/*
	 * Getting data from the Name History table
	 */
	
	/**
	 * Get all the usernames that the given UUID has logged in using.
	 * 
	 * @param uuid The UUID to search.
	 * @return A map containing the usernames as keys and changed to timestamps as values.
	 * @throws SQLException If database querying or resultset getting throws something.
	 */
	public Map<String, Long> getNamesFromUUID(UUID uuid) throws SQLException {
		ResultSet rs = plugin.getDB().query("SELECT * FROM " + TABLE_HISTORY + " WHERE " + COLUMN_UUID + "='" + uuid.toString() + "';");
		Map<String, Long> rtrn = new HashMap<String, Long>();
		while (rs.next())
			rtrn.put(rs.getString(COLUMN_USERNAME), rs.getLong(COLUMN_CHANGEDTO));
		return rtrn;
	}
	
	/**
	 * Get all the UUIDs that the given username has been used by.
	 * 
	 * @param username The username to search.
	 * @return A map containing the UUIDs as keys and changed to timestamps as values.
	 * @throws SQLException If database querying or resultset getting throws something.
	 */
	public Map<UUID, Long> getNamesFromUUID(String username) throws SQLException {
		ResultSet rs = plugin.getDB().query("SELECT * FROM " + TABLE_HISTORY + " WHERE " + COLUMN_USERNAME + "='" + username + "';");
		Map<UUID, Long> rtrn = new HashMap<UUID, Long>();
		while (rs.next())
			rtrn.put(UUID.fromString(rs.getString(COLUMN_UUID)), rs.getLong(COLUMN_CHANGEDTO));
		return rtrn;
	}
	
	/*
	 * Getting data from the Players table.
	 */
	
	// TODO: Get data from the players table.
}
