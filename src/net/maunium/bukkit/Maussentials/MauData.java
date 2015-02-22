package net.maunium.bukkit.Maussentials;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import lib.PatPeter.SQLibrary.Database;

import org.bukkit.Location;

public class MauData {
	private Database sql;
	public static final String TABLE_PLAYERS = "Players", TABLE_HISTORY = "OldNames";
	public static final String COLUMN_UUID = "UUID", COLUMN_USERNAME = "Username", COLUMN_IP = "IP", COLUMN_LASTLOGIN = "LastLogin",
			COLUMN_LOCATION = "Location", COLUMN_CHANGEDFROM = "ChangedFrom", COLUMN_CHANGEDTO = "ChangedTo";
	
	public MauData(Database sql) {
		this.sql = sql;
	}
	
	public void open() throws SQLException {
		// °FormatOff°
		sql.open();
		// Create the table Players
		sql.query("CREATE TABLE " + TABLE_PLAYERS + " ("
				+ COLUMN_UUID + " varchar(25) PRIMARY KEY"
				+ COLUMN_USERNAME + " varchar(16) NOT NULL"
				+ COLUMN_IP + " varchar(16) NOT NULL"
				+ COLUMN_CHANGEDTO + " INTEGER NOT NULL"
				+ COLUMN_LASTLOGIN + " INTEGER NOT NULL"
				+ COLUMN_LOCATION + " TEXT NOT NULL"
				+ ");");
		// Create the table OldNames
		sql.query("CREATE TABLE " + TABLE_HISTORY + " ("
				+ COLUMN_UUID + " varchar(25) NOT NULL"
				+ COLUMN_USERNAME + " varchar(16) NOT NULL"
				+ COLUMN_CHANGEDTO + " INTEGER NOT NULL"
				+ COLUMN_CHANGEDFROM + " INTEGER NOT NULL"
				+ ");");
		// °FormatOn°
	}
	
	public void close() {
		sql.close();
	}
	
	public ResultSet setEntry(UUID uuid, String username, String ip, Location l) throws SQLException {
		return sql.query("INSERT OR REPLACE INTO " + TABLE_PLAYERS + " VALUES (" + "'" + uuid.toString() + "','" + username + "','" + ip + "','"
				+ System.currentTimeMillis() + "','" + MauUtils.toString(l) + "');");
	}
	
	public ResultSet setTime(UUID uuid) throws SQLException {
		return sql.query("UPDATE " + TABLE_PLAYERS + " SET LastLogin='" + System.currentTimeMillis() + "' WHERE UUID='" + uuid.toString() + "';");
	}
	
	public ResultSet setLocation(UUID uuid, Location l) throws SQLException {
		return sql.query("UPDATE " + TABLE_PLAYERS + " SET Location='" + MauUtils.toString(l) + "' WHERE UUID='" + uuid.toString() + "';");
	}
}
