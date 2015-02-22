package net.maunium.bukkit.Maussentials.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.maunium.bukkit.Maussentials.Maussentials;

public class PlayerJoinListener implements Listener {
	private Maussentials plugin;
	
	
	public PlayerJoinListener(Maussentials plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt) {
		
	}
}
