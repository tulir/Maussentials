package net.maunium.bukkit.Maussentials.Modules.Bans;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.event.HandlerList;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

public class MauBans implements MauModule {
	private Maussentials plugin;
	public static final String TABLE_BANS = "Bans";
	public static final String COLUMN_BANNED = "Banned", COLUMN_TYPE = "BanType", COLUMN_REASON = "Reason", COLUMN_BANNEDBY = "BannedBy",
			COLUMN_EXPIRE = "ExpireAt", TYPE_IP = "IP", TYPE_UUID = "UUID";
	private Map<String, StandardBan> sbans;
	private JoinListener jl;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		try {
			// @mauformat=off
			// Create the table Players
			plugin.getDB().query("CREATE TABLE " + TABLE_BANS + " ("
					+ COLUMN_BANNED + " varchar(25) NOT NULL,"
					+ COLUMN_TYPE + " varchar(8) NOT NULL,"
					+ COLUMN_REASON + " TEXT NOT NULL,"
					+ COLUMN_BANNEDBY + " varchar(25) NOT NULL,"
					+ COLUMN_EXPIRE + " INTEGER NOT NULL,"
					+ "PRIMARY KEY (" + COLUMN_BANNED + ", " + COLUMN_TYPE + "));");
			// @mauformat=on
		} catch (SQLException e) {}
		
		sbans = new HashMap<String, StandardBan>();
		for (Entry<String, Object> e : plugin.getConfig().getConfigurationSection("standard-bans").getValues(false).entrySet()) {
			if (e.getValue() instanceof Map) {
				Map<?, ?> m = (Map<?, ?>) e.getValue();
				if (m.containsKey("reason") && m.containsKey("timeout")) {
					Object reason = m.get("reason"), timeout = m.get("timeout");
					if (reason != null && timeout != null && reason instanceof String && timeout instanceof String) sbans.put(e.getKey(),
							StandardBan.create((String) reason, (String) timeout));
					else plugin.getServer().getConsoleSender().sendMessage(plugin.translateErr("bans.error.stdban.entry", e.getKey()));
				} else plugin.getServer().getConsoleSender().sendMessage(plugin.translateErr("bans.error.stdban.entry", e.getKey()));
			} else plugin.getServer().getConsoleSender().sendMessage(plugin.translateErr("bans.error.stdban.entry", e.getKey()));
		}
		
		plugin.getServer().getPluginManager().registerEvents(jl = new JoinListener(plugin, this), plugin);
		plugin.getCommand("mauban").setExecutor(new CommandBan(plugin, this));
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
	
	public ResultSet getBanData(String banned, String type) throws SQLException {
		ResultSet rs = plugin.getDB().query(
				"SELECT * FROM " + TABLE_BANS + " WHERE " + COLUMN_BANNED + "='" + banned + "' AND " + COLUMN_TYPE + "='" + type + "';");
		
		if (rs.next()) {
			long expire = rs.getLong(COLUMN_EXPIRE);
			if (expire > 0 && expire <= System.currentTimeMillis()) {
				plugin.getLogger().info("Unbanning " + banned + " (" + type + " ban) as the ban has expired.");
				plugin.getDB().query("DELETE FROM " + TABLE_BANS + " WHERE " + COLUMN_BANNED + "='" + banned + "' AND " + COLUMN_TYPE + "='" + type + "';");
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
			// @mauformat=on
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to add IP ban entry for " + ip + ":");
			e.printStackTrace();
		}
	}
	
	public void unban(UUID u) {
		try {
			plugin.getDB().query(
					"DELETE FROM " + TABLE_BANS + " WHERE " + COLUMN_BANNED + "='" + u.toString() + "' AND " + COLUMN_TYPE + "='" + TYPE_UUID + "';");
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to unban " + u + ":");
			e.printStackTrace();
		}
	}
	
	public void unbanip(String ip) {
		try {
			plugin.getDB().query("DELETE FROM " + TABLE_BANS + " WHERE " + COLUMN_BANNED + "='" + ip + "' AND " + COLUMN_TYPE + "='" + TYPE_IP + "';");
		} catch (SQLException e) {
			plugin.getLogger().severe("Failed to unban " + ip + ":");
			e.printStackTrace();
		}
	}
	
	public StandardBan getStandardBan(String name) {
		return sbans.get(name);
	}
}
