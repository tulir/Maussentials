package net.maunium.bukkit.Maussentials.Modules.PlayerData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.SerializableLocation;

/**
 * Listeners for the PlayerData class. Moved to separate class, since all listeners need to be made asynchronous.
 * 
 * @author Tulir293
 * @since 0.3
 */
public class PDListeners implements Listener {
	private Maussentials plugin;
	private PlayerData host;
	
	public PDListeners(Maussentials plugin, PlayerData host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerPreLogin(AsyncPlayerPreLoginEvent evt) {
		if (evt.getAddress() != null && evt.getUniqueId() != null && evt.getName() != null
				&& evt.getLoginResult().equals(AsyncPlayerPreLoginEvent.Result.ALLOWED)) {
			try {
				ResultSet rs = plugin.getDB()
						.query("SELECT * FROM " + PlayerData.TABLE_PLAYERS + " WHERE " + PlayerData.COLUMN_UUID + "='" + evt.getUniqueId() + "';");
				if (rs.next()) {
					String old = rs.getString(PlayerData.COLUMN_USERNAME);
					if (!old.equals(evt.getName())) host.updateNameHistory(evt.getUniqueId());
				} else host.updateNameHistory(evt.getUniqueId());
				
				String ia = evt.getAddress().getHostAddress();
				try {
					host.setIPs(evt.getUniqueId(), ia);
					plugin.getLogger().fine("Updated IP history. New entry: " + evt.getUniqueId() + ", " + ia);
				} catch (SQLException e) {
					plugin.getLogger().severe("Failed attempt to add new entry: " + evt.getUniqueId() + ", " + ia);
					e.printStackTrace();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerLogin(PlayerLoginEvent evt) {
		if (!evt.getResult().equals(PlayerLoginEvent.Result.ALLOWED)) return;
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin,
				new PlayerLoginHandler(evt.getPlayer().getUniqueId(), evt.getPlayer().getName(), evt.getPlayer().getLocation()));
	}
	
	private class PlayerLoginHandler implements Runnable {
		private UUID uuid;
		private String name;
		private SerializableLocation loc;
		
		public PlayerLoginHandler(UUID uuid, String name, Location loc) {
			this.uuid = uuid;
			this.name = name;
			this.loc = new SerializableLocation(loc);
		}
		
		@Override
		public void run() {
			try {
				host.setEntry(uuid, name, loc);
				plugin.getLogger().fine("Updated database. New entry: " + uuid + ", " + name);
			} catch (SQLException e) {
				plugin.getLogger().severe("Failed attempt to add new entry: " + uuid + ", " + name);
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin,
				new PlayerJoinHandler(evt.getPlayer().getUniqueId(), evt.getPlayer().getName(), evt.getPlayer().getLocation()));
	}
	
	private class PlayerJoinHandler implements Runnable {
		private UUID uuid;
		private String name;
		private SerializableLocation loc;
		
		public PlayerJoinHandler(UUID uuid, String name, Location loc) {
			this.uuid = uuid;
			this.name = name;
			this.loc = new SerializableLocation(loc);
		}
		
		@Override
		public void run() {
			try {
				host.setLocation(uuid, loc);
				plugin.getLogger().fine("Updated Location for " + name);
			} catch (SQLException e) {
				plugin.getLogger().severe("Failed to update Location for " + name);
				e.printStackTrace();
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent evt) {
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin,
				new PlayerQuitHandler(evt.getPlayer().getUniqueId(), evt.getPlayer().getName(), evt.getPlayer().getLocation()));
	}
	
	private class PlayerQuitHandler implements Runnable {
		private UUID uuid;
		private String name;
		private SerializableLocation loc;
		
		public PlayerQuitHandler(UUID uuid, String name, Location loc) {
			this.uuid = uuid;
			this.name = name;
			this.loc = new SerializableLocation(loc);
		}
		
		@Override
		public void run() {
			try {
				host.setTime(uuid);
				plugin.getLogger().fine("Updated LastLogin time for " + name);
			} catch (SQLException e) {
				plugin.getLogger().severe("Failed to update LastLogin time for " + name);
				e.printStackTrace();
			}
			try {
				host.setLocation(uuid, loc);
				plugin.getLogger().fine("Updated Location for " + name);
			} catch (SQLException e) {
				plugin.getLogger().severe("Failed to update Location for " + name);
				e.printStackTrace();
			}
		}
	}
}
