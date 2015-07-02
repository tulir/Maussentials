package net.maunium.bukkit.Maussentials.Utils.ArenaMG;

import java.util.Set;

import org.bukkit.plugin.Plugin;

/**
 * The AMG (Arena minigame) base
 * 
 * @author Tulir293
 * @since 0.3
 */
public abstract class ArenaMG {
	private Plugin plugin;
	private int teamCount, playerCount, minPlayers;
	private Set<ArenaPlayer> players;
	
	public ArenaMG(Plugin plugin) {
		this.plugin = plugin;
	}
	
}