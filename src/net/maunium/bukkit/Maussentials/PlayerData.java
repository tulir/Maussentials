package net.maunium.bukkit.Maussentials;

import lib.PatPeter.SQLibrary.Database;

public class PlayerData {
	private Database db;
	
	public PlayerData(Database db) {
		this.db = db;
		db.open();
	}
}
