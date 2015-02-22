package net.maunium.bukkit.Maussentials;

import lib.PatPeter.SQLibrary.Database;

public class MauData {
	private Database db;
	
	public MauData(Database db) {
		this.db = db;
		db.open();
	}
}
