package net.maunium.bukkit.Maussentials.Utils.ArenaMG;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class ArenaPlayer {
	private final UUID uuid;
	
	public ArenaPlayer(UUID uuid) {
		this.uuid = uuid;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public OfflinePlayer getPlayerSafe() {
		return Bukkit.getServer().getOfflinePlayer(getUUID());
	}
	
	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(getUUID());
	}
}
