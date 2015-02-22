package net.maunium.bukkit.Maussentials.Listeners;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.maunium.bukkit.Maussentials.ChatFormatter;
import net.maunium.bukkit.Maussentials.MauModule;
import net.maunium.bukkit.Maussentials.Maussentials;

public class PlayerJoinListener implements Listener, MauModule {
	private Maussentials plugin;
	private String[] welcome;
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		reload();
	}
	
	@Override
	public void reload() {
		List<String> s = plugin.getConfig().getStringList("welcome-message");
		welcome = new String[s.size()];
		int i = 0;
		for (String ss : s)
			welcome[i] = ChatFormatter.translateAll(ss);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt) {
		evt.getPlayer().sendMessage(welcome);
	}
}
