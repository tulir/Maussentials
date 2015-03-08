package net.maunium.bukkit.Maussentials.Modules.Bans;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

public class MauBans implements MauModule {
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		loaded = true;
	}
	
	@Override
	public void unload() {
		loaded = false;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
