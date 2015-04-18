package net.maunium.bukkit.Maussentials.Modules.MauInfo;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

public class MauInfo implements MauModule, Listener {
	private Map<String, InfoEntry> infos;
	private boolean loaded;
	
	@Override
	public void load(Maussentials plugin) {
		File f = new File(plugin.getDataFolder(), "infos");
		for (File ff : f.listFiles()) {
			if (ff.getName().endsWith(".mauinfo")) try {
				infos.put(ff.getName().substring(0, ff.getName().length() - 8).toLowerCase(Locale.ENGLISH), new InfoEntry(ff));
			} catch (IOException e) {
				plugin.getLogger().warning("Failed to load mauinfo file " + ff.getName());
				e.printStackTrace();
				continue;
			}
		}
		
		loaded = true;
	}
	
	@Override
	public void unload() {
		HandlerList.unregisterAll(this);
		infos.clear();
		loaded = false;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent evt) {
		if (infos.containsKey("motd")) infos.get("motd").sendMessage(evt.getPlayer());
	}
	
	@EventHandler
	public void onPreCommand(PlayerCommandPreprocessEvent evt) {
		String msg = evt.getMessage().substring(1);
		for (InfoEntry e : infos.values()) {
			if (e.isAlias(msg)) {
				e.sendMessage(evt.getPlayer());
				evt.setCancelled(true);
				return;
			}
		}
	}
}
