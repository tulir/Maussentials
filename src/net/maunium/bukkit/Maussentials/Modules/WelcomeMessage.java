package net.maunium.bukkit.Maussentials.Modules;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;
import net.maunium.bukkit.Maussentials.Utils.ChatFormatter;

/**
 * Welcome message
 * 
 * @author Tulir293
 * @since 0.1
 */
public class WelcomeMessage extends CommandModule implements Listener {
	private Maussentials plugin;
	private String[] welcome;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		// Set executor
		plugin.getCommand("mauwelcomemessage").setExecutor(this);
		// Load the list of lines to be in the welcome message from config.
		List<String> s = plugin.getConfig().getStringList("welcome-message");
		// Initialize the welcome message array
		welcome = new String[s.size()];
		// Loop through the lines
		for (int i = 0; i < s.size(); i++)
			// Translate the alt format codes to real format codes and add the string to the array.
			welcome[i] = ChatFormatter.translateAll(s.get(i));
	}
	
	@Override
	public void unload() {
		welcome = null;
		HandlerList.unregisterAll(this);
		plugin.getCommand("mauwelcomemessage").setExecutor(plugin);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		// Send every line in the welcome message array.
		evt.getPlayer().sendMessage(variables(welcome, evt.getPlayer()));
	}
	
	public String[] variables(String[] s, Player p) {
		String[] n = new String[s.length];
		for (int i = 0; i < s.length; i++) {
			String ss = s[i];
			ss = ss.replace("{PLAYER}", p.getName());
			ss = ss.replace("{DISPLAYNAME}", p.getDisplayName());
			ss = ss.replace("{ONLINE}", Integer.toString(plugin.getServer().getOnlinePlayers().size()));
			ss = ss.replace("{MAXPLAYERS}", Integer.toString(plugin.getServer().getMaxPlayers()));
			n[i] = ss;
		}
		return n;
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		// Send every line in the welcome message array.
		sender.sendMessage(welcome);
		return true;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		// This can be empty, since the execute always returns true and thus this method will never be called
	}
}
