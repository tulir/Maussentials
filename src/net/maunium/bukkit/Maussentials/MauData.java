package net.maunium.bukkit.Maussentials;

import java.sql.SQLException;

import lib.PatPeter.SQLibrary.Database;

public class MauData {
	private Database sql;
	public static final String TABLE_PLAYERS = "Players", TABLE_HISTORY = "OldNames";
	
	public MauData(Database sql) {
		this.sql = sql;
	}
	
	public void open() throws SQLException{
		sql.open();
		sql.query("CREATE TABLE " + TABLE_PLAYERS + " ("
				+ "UUID varchar(25) PRIMARY KEY"
				+ "Username varchar(16) NOT NULL"
				+ "IP varchar(16) NOT NULL"
				+ "NameChanged INTEGER NOT NULL"
				+ "LastLogin INTEGER NOT NULL"
				+ "Location TEXT NOT NULL"
				+ ");");
		sql.query("CREATE TABLE " + TABLE_HISTORY + " ("
				+ "UUID varchar(25) NOT NULL"
				+ "OldUsername varchar(16) NOT NULL"
				+ "ChangedTo INTEGER NOT NULL"
				+ "ChangedFrom INTEGER NOT NULL"
				+ ");");
	}
}
