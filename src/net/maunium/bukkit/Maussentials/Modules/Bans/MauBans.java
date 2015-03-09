package net.maunium.bukkit.Maussentials.Modules.Bans;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.event.HandlerList;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

public class MauBans implements MauModule {
	private Maussentials plugin;
	public static final String TABLE_BANS = "Bans";
	public static final String COLUMN_BANNED = "Banned", COLUMN_TYPE = "BanType", COLUMN_REASON = "Reason", COLUMN_BANNEDBY = "BannedBy",
			COLUMN_EXPIRE = "ExpireAt", TYPE_IP = "IP", TYPE_UUID = "UUID";
	private JoinListener jl;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		try {
			// °FormatOff°
			// Create the table Players
			plugin.getDB().query("CREATE TABLE " + TABLE_BANS + " ("
					+ COLUMN_BANNED + " varchar(25) NOT NULL,"
					+ COLUMN_TYPE + " varchar(8) NOT NULL,"
					+ COLUMN_REASON + " TEXT NOT NULL,"
					+ COLUMN_BANNEDBY + " varchar(25) NOT NULL,"
					+ COLUMN_EXPIRE + " INTEGER NOT NULL,"
					+ "PRIMARY KEY (" + COLUMN_BANNED + ", " + COLUMN_TYPE + "));");
			// °FormatOn°
		} catch (SQLException e) {}
		plugin.getServer().getPluginManager().registerEvents(jl = new JoinListener(plugin, this), plugin);
		loaded = true;
	}
	
	@Override
	public void unload() {
		HandlerList.unregisterAll(jl);
		loaded = false;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	public ResultSet getBanData(String banned, String type) throws SQLException {
		ResultSet rs = plugin.getDB().query(
				"SELECT * FROM " + TABLE_BANS + " WHERE " + COLUMN_BANNED + "='" + banned + "' AND " + COLUMN_TYPE + "='" + type + "';");
		
		long expire = rs.getLong(COLUMN_EXPIRE);
		if (expire > 0 && expire <= System.currentTimeMillis()) {
			plugin.getLogger().fine("Unbanning " + banned + " (" + type + " ban) as the ban has expired.");
			plugin.getDB().query("DELETE FROM " + TABLE_BANS + " WHERE " + COLUMN_BANNED + "='" + banned + "' AND " + COLUMN_TYPE + "='" + type + "';");
			return null;
		} else return rs;
	}
}
