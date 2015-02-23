package net.maunium.bukkit.Maussentials.Modules;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;
import net.maunium.bukkit.Maussentials.Utils.ChatFormatter;

/**
 * @author Tulir293
 * @since 0.1
 */
public class WelcomeMessage implements Listener, CommandExecutor, MauModule {
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
		int i = 0;
		// Loop through the lines
		for (String ss : s)
			// Translate the alt format codes to real format codes and add the string to the array.
			welcome[i] = ChatFormatter.translateAll(ss);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		// Send every line in the welcome message array.
		evt.getPlayer().sendMessage(welcome);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!plugin.checkPerms(sender, "maussentials.welcomemessage")) return true;
		// Send every line in the welcome message array.
		sender.sendMessage(welcome);
		return true;
	}
}
