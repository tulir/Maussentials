package net.maunium.bukkit.Maussentials.Modules;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
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
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getCommand("mauwelcomemessage");
		// The reload method contains the loading code so it can be used for loading too.
		reload();
	}
	
	@Override
	public void reload() {
		// Load the list of lines to be in the welcome message from config.
		List<String> s = plugin.getConfig().getStringList("welcome-message");
		// Initialize the welcome message array
		welcome = new String[s.size()];
		// Loop through the lines
		for (int i = 0; i < s.size(); i++)
			// Translate the alt format codes to real format codes and add the string to the array.
			welcome[i] = ChatFormatter.translateAll(s.get(i));
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		// Send every line in the welcome message array.
		evt.getPlayer().sendMessage(welcome);
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
