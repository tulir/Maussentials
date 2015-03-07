package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;
import net.maunium.bukkit.Maussentials.Utils.DelayedTeleport;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

/**
 * The listeners for Delayed Teleports.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class DelayedTeleportListeners implements MauModule, Listener {
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		DelayedTeleport.setPlugin(plugin);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		loaded = true;
	}
	
	@Override
	public void unload() {
		HandlerList.unregisterAll(this);
		loaded = false;
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent evt) {
		if (evt.getEntity() instanceof Player) {
			Player p = (Player) evt.getEntity();
			if (p.hasMetadata(DelayedTeleport.DELAYED_TP_META)) {
				DelayedTeleport dt = (DelayedTeleport) MetadataUtils.getMetadata(p, DelayedTeleport.DELAYED_TP_META, plugin).value();
				dt.failed();
			}
		}
		
		if (evt.getDamager() instanceof Player) {
			Player p = (Player) evt.getDamager();
			if (p.hasMetadata(DelayedTeleport.DELAYED_TP_META)) {
				DelayedTeleport dt = (DelayedTeleport) MetadataUtils.getMetadata(p, DelayedTeleport.DELAYED_TP_META, plugin).value();
				dt.failed();
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent evt) {
		if (evt.getFrom().getBlockX() == evt.getTo().getBlockX() && evt.getFrom().getBlockY() == evt.getTo().getBlockY()
				&& evt.getFrom().getBlockZ() == evt.getTo().getBlockZ()) return;
		Player p = evt.getPlayer();
		if (p.hasMetadata(DelayedTeleport.DELAYED_TP_META)) {
			DelayedTeleport dt = (DelayedTeleport) MetadataUtils.getMetadata(p, DelayedTeleport.DELAYED_TP_META, plugin).value();
			dt.failed();
		}
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
