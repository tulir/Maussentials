package net.maunium.bukkit.Maussentials.Modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

/**
 * Overrides the default /plugins command.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class CommandPlugins implements MauModule, Listener {
	private Maussentials plugin;
	
	@EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
	public void onPreCommandLate(PlayerCommandPreprocessEvent evt) {
		String msg = evt.getMessage().toLowerCase(Locale.ENGLISH).substring(1);
		if (msg.startsWith("plugins") || msg.equals("pl") || msg.equals("plug") || msg.equals("plugs") || msg.equals("plugin")) {
			evt.setCancelled(true);
			Player p = evt.getPlayer();
			if (!p.hasPermission("maussentials.plugins")) {
				p.sendMessage(plugin.errtag + plugin.translate("plugins-perms"));
				return;
			}
			List<String> plugins = new ArrayList<String>();
			
			int emp = 0;
			int dmp = 0;
			int ep = 0;
			int dp = 0;
			
			for (Plugin pl : Bukkit.getServer().getPluginManager().getPlugins()) {
				if (isMauPlugin(pl)) {
					if (pl.isEnabled()) {
						plugins.add(ChatColor.AQUA + pl.getName() + ChatColor.WHITE);
						emp++;
					} else {
						plugins.add(ChatColor.RED + pl.getName() + ChatColor.WHITE);
						dmp++;
					}
				} else {
					if (pl.isEnabled()) {
						plugins.add(ChatColor.AQUA + pl.getName() + ChatColor.WHITE);
						ep++;
					} else {
						plugins.add(ChatColor.RED + pl.getName() + ChatColor.WHITE);
						dp++;
					}
				}
			}
			Collections.sort(plugins, String.CASE_INSENSITIVE_ORDER);
			StringBuffer sb = new StringBuffer();
			for (String s : plugins) {
				sb.append(s);
				sb.append(", ");
			}
			String[] send = new String[] {
					ChatColor.GREEN + "Enabled MauPlugin (" + emp + ")" + ChatColor.WHITE + " - " + ChatColor.DARK_GREEN + "Disabled MauPlugin (" + dmp + ")",
					ChatColor.AQUA + "Enabled Plugin (" + ep + ")" + ChatColor.WHITE + " - " + ChatColor.RED + "Disabled Plugin (" + dp + ")", " ",
					sb.delete(sb.length() - 2, sb.length()).toString(), " " };
			p.sendMessage(send);
		}
	}
	
	public boolean isMauPlugin(Plugin pl) {
		if (pl.getDescription().getMain().startsWith("net.maunium") || pl.getName().toLowerCase(Locale.ENGLISH).contains("mau")) return true;
		else if (pl.getDescription().getAuthors() != null && pl.getDescription().getAuthors().contains("tulir293")) return true;
		else if (pl.getDescription().getWebsite() != null && pl.getDescription().getWebsite().toLowerCase(Locale.ENGLISH).contains("maunium.net")) return true;
		else return false;
	}
	
	@Override
	public void load(Maussentials plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin = plugin;
	}
	
	@Override
	public void unload() {
		HandlerList.unregisterAll(this);
	}
}
