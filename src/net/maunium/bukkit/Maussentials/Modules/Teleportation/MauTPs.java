package net.maunium.bukkit.Maussentials.Modules.Teleportation;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

public class MauTPs implements MauModule {
	public static final String TP_REQUEST_META = "MaussentialsTPRequestFrom";
	protected int delay, safeRange, safeDelay;
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getCommand("mautp").setExecutor(new CommandTP(plugin));
		plugin.getCommand("mautprequest").setExecutor(new CommandTPRequest(plugin));
		plugin.getCommand("mautpaccept").setExecutor(new CommandTPAccept(plugin, this));
		plugin.getCommand("mautpdeny").setExecutor(new CommandTPAccept(plugin, this));
		delay = plugin.getConfig().getInt("teleport.delay", 100);
		safeRange = plugin.getConfig().getInt("teleport.saferange", 10);
		safeDelay = plugin.getConfig().getInt("teleport.safedelay", 0);
		loaded = true;
	}
	
	@Override
	public void unload() {
		plugin.getCommand("mautp").setExecutor(plugin);
		plugin.getCommand("mautprequest").setExecutor(plugin);
		plugin.getCommand("mautpaccept").setExecutor(plugin);
		plugin.getCommand("mautpdeny").setExecutor(plugin);
		plugin = null;
		loaded = false;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
