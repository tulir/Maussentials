package net.maunium.bukkit.Maussentials.Modules;

import java.sql.SQLException;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.MauModule;

/**
 * @author Tulir293
 * @since 0.1
 */
public class PlayerData implements Listener, MauModule {
	private Maussentials plugin;
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@Override
	public void reload() {}
	
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent evt) {
		try {
			plugin.getMauData().setEntry(evt.getPlayer().getUniqueId(), evt.getPlayer().getName(), evt.getAddress().getHostAddress(),
					evt.getPlayer().getLocation());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
