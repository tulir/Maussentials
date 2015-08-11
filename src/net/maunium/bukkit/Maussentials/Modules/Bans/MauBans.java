package net.maunium.bukkit.Maussentials.Modules.Bans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.MemorySection;
import org.bukkit.event.HandlerList;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

public class MauBans implements MauModule {
	private Maussentials plugin;
	public static final String TABLE_BANS = "Bans", TABLE_HISTORY = "BanHistory";
	public static final String COLUMN_BANNED = "Banned", COLUMN_TYPE = "BanType", COLUMN_REASON = "Reason", COLUMN_BANNEDBY = "BannedBy",
			COLUMN_EXPIRE = "ExpireAt", COLUMN_BANNEDAT = "AppliedAt", TYPE_IP = "IP", TYPE_UUID = "UUID", TYPE_UNBAN = "Unban";
	private Map<String, StandardBan> sbans;
	private JoinListener jl;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		try {
			// @mauformat=off
			// Create the table Bans
			plugin.getDB().query("CREATE TABLE " + TABLE_BANS + " ("
					+ COLUMN_BANNED + " varchar(25) NOT NULL,"
					+ COLUMN_TYPE + " varchar(8) NOT NULL,"
					+ COLUMN_REASON + " TEXT NOT NULL,"
					+ COLUMN_BANNEDBY + " varchar(25) NOT NULL,"
					+ COLUMN_EXPIRE + " INTEGER NOT NULL,"
					+ "PRIMARY KEY (" + COLUMN_BANNED + ", " + COLUMN_TYPE + "));");
			// Create the table BanHistory
			plugin.getDB().query("CREATE TABLE " + TABLE_HISTORY + " ("
					+ COLUMN_BANNED + " varchar(25) NOT NULL,"
					+ COLUMN_BANNEDAT + " INTEGER NOT NULL,"
					+ COLUMN_TYPE + " varchar(8) NOT NULL,"
					+ COLUMN_REASON + " TEXT NOT NULL,"
					+ COLUMN_BANNEDBY + " varchar(25) NOT NULL,"
					+ COLUMN_EXPIRE + " INTEGER NOT NULL,"
					+ "PRIMARY KEY (" + COLUMN_BANNED + ", " + COLUMN_BANNEDAT + ", " + COLUMN_TYPE + "));");
			// @mauformat=on
		} catch (SQLException e) {}
		
		sbans = new HashMap<String, StandardBan>();
		for (Entry<String, Object> e : plugin.getConfig().getConfigurationSection("standard-bans").getValues(false).entrySet()) {
			if (e.getValue() instanceof MemorySection) {
				MemorySection m = (MemorySection) e.getValue();
				if (m.contains("reason") && m.contains("timeout")) {
					Object reason = m.get("reason"), timeout = m.get("timeout");
					if (reason != null && timeout != null && reason instanceof String && timeout instanceof String)
						sbans.put(e.getKey(), StandardBan.create((String) reason, (String) timeout));
					else plugin.getServer().getConsoleSender().sendMessage(plugin.translateErr("bans.error.stdban.entry", e.getKey()));
				} else plugin.getServer().getConsoleSender().sendMessage(plugin.translateErr("bans.error.stdban.entry", e.getKey()));
			} else plugin.getServer().getConsoleSender().sendMessage(plugin.translateErr("bans.error.stdban.entry", e.getKey()));
		}
		
		plugin.getServer().getPluginManager().registerEvents(jl = new JoinListener(plugin, this), plugin);
		plugin.getCommand("mauban").setExecutor(new CommandBan(plugin, this));
		plugin.getCommand("maubanhistory").setExecutor(new CommandBanHistory(plugin, this));
		plugin.getCommand("maustandardban").setExecutor(new CommandSBan(plugin, this));
		plugin.getCommand("mautempban").setExecutor(new CommandTempBan(plugin, this));
		plugin.getCommand("mauunban").setExecutor(new CommandUnban(plugin, this));
		plugin.getCommand("maubanip").setExecutor(new CommandBanIP(plugin, this));
		plugin.getCommand("mautempbanip").setExecutor(new CommandTempBanIP(plugin, this));
		plugin.getCommand("mauunbanip").setExecutor(new CommandUnbanIP(plugin, this));
		
		loaded = true;
	}
	
	@Override
	public void unload() {
		HandlerList.unregisterAll(jl);
		plugin.getCommand("mauban").setExecutor(plugin);
		plugin.getCommand("maubanhistory").setExecutor(plugin);
		plugin.getCommand("maustandardban").setExecutor(plugin);
		plugin.getCommand("mautempban").setExecutor(plugin);
		plugin.getCommand("mauunban").setExecutor(plugin);
		plugin.getCommand("maubanip").setExecutor(plugin);
		plugin.getCommand("mautempbanip").setExecutor(plugin);
		plugin.getCommand("mauunbanip").setExecutor(plugin);
		sbans.clear();
		sbans = null;
		plugin = null;
		loaded = false;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	public ResultSet getBanHistory(String banned, String type, long from, long to) throws SQLException {
		// @mauformat=off
		return plugin.getDB().query("SELECT * FROM " + TABLE_HISTORY
				+ " WHERE " + COLUMN_BANNED + "='" + banned + "'"
				+ (type != null ?			" AND "	+ COLUMN_TYPE		+ "='" 			+ type + "'"			: "")
				+ (from != -1 && to != -1 ?	" AND "	+ COLUMN_BANNEDAT	+ " BETWEEN "	+ from + " AND " + to	: "")
				+ ";");
		// @mauformat=on
	}
	
	public ResultSet getBanData(String banned, String type) throws SQLException {
		ResultSet rs = plugin.getDB()
				.query("SELECT * FROM " + TABLE_BANS + " WHERE " + COLUMN_BANNED + "='" + banned + "' AND " + COLUMN_TYPE + "='" + type + "';");
				
		if (rs.next()) {
			long expire = rs.getLong(COLUMN_EXPIRE);
			if (expire > 0 && expire <= System.currentTimeMillis()) {
				plugin.getLogger().info("Unbanning " + banned + " (" + type + " ban) as the ban has expired.");
				plugin.getDB().query("DELETE FROM " + TABLE_BANS + " WHERE " + COLUMN_BANNED + "='" + banned + "' AND " + COLUMN_TYPE + "='" + type + "';");
				// @mauformat=off
				plugin.getDB().query("INSERT OR REPLACE INTO " + TABLE_HISTORY + " VALUES ('"
						+ banned + "','"
						+ System.currentTimeMillis() + "','"
						+ TYPE_UNBAN + "','"
						+ type + "','"
						+ "Automatic" + "','"
						+ -1
						+ "');");
				// @mauformat=on
				return null;
			} else return rs;
		} else return null;
	}
	
	public void ban(UUID uuid, String banner, String reason, long timeout) {
		try {
			// @mauformat=off
			plugin.getDB().query("INSERT OR REPLACE INTO " + TABLE_BANS + " VALUES ('"
					+ uuid.toString() + "','"
					+ TYPE_UUID + "','"
					+ reason + "','"
					+ banner + "','"
					+ timeout
					+ "');");
			plugin.getDB().query("INSERT OR REPLACE INTO " + TABLE_HISTORY + " VALUES ('"
					+ uuid.toString() + "','"
					+ System.currentTimeMillis() + "','"
					+ TYPE_UUID + "','"
					+ reason + "','"
					+ banner + "','"
					+ timeout
					+ "');");
			// @mauformat=on
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to add ban entry for " + uuid + ":");
			e.printStackTrace();
		}
	}
	
	public void ipban(String ip, String banner, String reason, long timeout) {
		try {
			// @mauformat=off
			plugin.getDB().query("INSERT OR REPLACE INTO " + TABLE_BANS + " VALUES ('"
					+ ip + "','"
					+ TYPE_IP + "','"
					+ reason + "','"
					+ banner + "','"
					+ timeout
					+ "');");
			plugin.getDB().query("INSERT OR REPLACE INTO " + TABLE_HISTORY + " VALUES ('"
					+ ip + "','"
					+ System.currentTimeMillis() + "','"
					+ TYPE_UUID + "','"
					+ reason + "','"
					+ banner + "','"
					+ timeout
					+ "');");
			// @mauformat=on
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to add IP ban entry for " + ip + ":");
			e.printStackTrace();
		}
	}
	
	public void unban(String unbanner, UUID u) {
		try {
			plugin.getDB()
					.query("DELETE FROM " + TABLE_BANS + " WHERE " + COLUMN_BANNED + "='" + u.toString() + "' AND " + COLUMN_TYPE + "='" + TYPE_UUID + "';");
			// @mauformat=off
			plugin.getDB().query("INSERT OR REPLACE INTO " + TABLE_HISTORY + " VALUES ('"
					+ u + "','"
					+ System.currentTimeMillis() + "','"
					+ TYPE_UNBAN + "','"
					+ TYPE_UUID + "','"
					+ unbanner + "','"
					+ -1
					+ "');");
			// @mauformat=on
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to unban " + u + ":");
			e.printStackTrace();
		}
	}
	
	public void unbanip(String unbanner, String ip) {
		try {
			plugin.getDB().query("DELETE FROM " + TABLE_BANS + " WHERE " + COLUMN_BANNED + "='" + ip + "' AND " + COLUMN_TYPE + "='" + TYPE_IP + "';");
			// @mauformat=off
			plugin.getDB().query("INSERT OR REPLACE INTO " + TABLE_HISTORY + " VALUES ('"
					+ ip + "','"
					+ System.currentTimeMillis() + "','"
					+ TYPE_UNBAN + "','"
					+ TYPE_IP + "','"
					+ unbanner + "','"
					+ -1
					+ "');");
			// @mauformat=on
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to unban " + ip + ":");
			e.printStackTrace();
		}
	}
	
	public void warn(UUID uuid, String banner, String reason, long timeout) {
	
	}
	
	public StandardBan getStandardBan(String name) {
		return sbans.get(name);
	}
	
	@Override
	public String[] getDependencies() {
		return new String[] { "playerdata" };
	}
}
