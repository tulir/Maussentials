package net.maunium.bukkit.Maussentials.Modules;

import java.util.List;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.ChatFormatter;
import net.maunium.bukkit.Maussentials.Utils.MauModule;

/**
 * @author Tulir293
 * @since 0.1
 */
public class PlayerJoinListener implements Listener, MauModule {
	private Maussentials plugin;
	private String[] welcome;
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		// The reload method contains the loading code so it can be used for loading too.
		reload();
	}
	
	@Override
	public void reload() {
		// Load the list of lines to be in the welcome message from config.
		List<String> s = plugin.getConfig().getStringList("welcome-message");
		// Initialize the welcome message array
		welcome = new String[s.size()];
		int i = 0;
		// Loop through the lines
		for (String ss : s)
			// Translate the alt format codes to real format codes and add the string to the array.
			welcome[i] = ChatFormatter.translateAll(ss);
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt) {
		// Send every line in the welcome message array.
		evt.getPlayer().sendMessage(welcome);
	}
}
