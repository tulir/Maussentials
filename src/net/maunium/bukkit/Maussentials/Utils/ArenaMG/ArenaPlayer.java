package net.maunium.bukkit.Maussentials.Utils.ArenaMG;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * An object containing the information about an AMG player.
 * 
 * @author Tulir293
 * @since 0.3
 */
public class ArenaPlayer {
	private final UUID uuid;
	
	public ArenaPlayer(UUID uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * Get the UUID of this player.
	 */
	public UUID getUUID() {
		return uuid;
	}
	
	/**
	 * Get the OfflinePlayer instance of this player.
	 */
	public OfflinePlayer getPlayerSafe() {
		return Bukkit.getServer().getOfflinePlayer(getUUID());
	}
	
	/**
	 * Get the Player instance of this player.
	 */
	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(getUUID());
	}
}
