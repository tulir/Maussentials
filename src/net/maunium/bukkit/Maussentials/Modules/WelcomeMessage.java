package net.maunium.bukkit.Maussentials.Modules;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
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
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		try {
			List<String> welcome = new ArrayList<String>();
			BufferedReader br = new BufferedReader(new FileReader(new File(plugin.getDataFolder(), "motd.txt")));
			String s;
			while ((s = br.readLine()) != null)
				welcome.add(ChatFormatter.formatAll(s));
			br.close();
			
			this.welcome = welcome.toArray(new String[0]);
		} catch (Exception e) {
			plugin.getLogger().severe("Failed to load welcome message:");
			e.printStackTrace();
			return;
		}
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		plugin.getCommand("mauwelcomemessage").setExecutor(this);
		loaded = true;
	}
	
	@Override
	public void unload() {
		welcome = null;
		HandlerList.unregisterAll(this);
		plugin.getCommand("mauwelcomemessage").setExecutor(plugin);
		plugin = null;
		loaded = false;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		// Send every line in the welcome message array.
		evt.getPlayer().sendMessage(variables(welcome, evt.getPlayer()));
	}
	
	public String[] variables(String[] s, CommandSender p) {
		String[] n = new String[s.length];
		for (int i = 0; i < s.length; i++) {
			String ss = s[i];
			ss = ss.replace("{PLAYER}", p.getName());
			ss = ss.replace("{DISPLAYNAME}", (p instanceof Player) ? ((Player) p).getDisplayName() : p.getName());
			ss = ss.replace("{ONLINE}", Integer.toString(plugin.getServer().getOnlinePlayers().size()));
			ss = ss.replace("{MAXPLAYERS}", Integer.toString(plugin.getServer().getMaxPlayers()));
			n[i] = ss;
		}
		return n;
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		// Send every line in the welcome message array.
		sender.sendMessage(variables(welcome, sender));
		return true;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		// This can be empty, since the execute always returns true and thus this method will never be called
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
