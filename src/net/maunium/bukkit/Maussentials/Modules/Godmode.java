package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.PlayerCommandModule;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

/**
 * The /maugod command
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Godmode extends PlayerCommandModule implements Listener {
	private Maussentials plugin;
	private static final String DEFAULT_GOD = "MaussentialsGodDefault", PRIVATE_GOD = "MaussentialsGodPrivatized", DAMAGE_GOD = "MaussentialsGodDamage";
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin.getCommand("maugod").setExecutor(this);
		this.permission = "maussentials.god";
	}
	
	@Override
	public void unload() {
		this.plugin.getCommand("maugod").setExecutor(plugin);
		HandlerList.unregisterAll(this);
		plugin = null;
	}
	
	@EventHandler
	public void onPlayerDamage(EntityDamageEvent evt) {
		if (evt.getEntity() instanceof Player) {
			Player p = (Player) evt.getEntity();
			
			if (p.hasMetadata(DEFAULT_GOD)) evt.setCancelled(true);
			else if (p.hasMetadata(DAMAGE_GOD)) evt.setDamage(0.0D);
		}
	}
	
	@Override
	public boolean execute(Player sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			sender.sendMessage(plugin.stag + plugin.translate("god." + (toggle_def(sender) ? "off" : "on.def")));
		} else if (args.length > 0) {
			if (args[0].equalsIgnoreCase("default") || args[0].equalsIgnoreCase("def")) {
				if (args.length > 1 && args[1].equalsIgnoreCase("private")) MetadataUtils.setFixedMetadata(sender, PRIVATE_GOD, true, plugin);
				sender.sendMessage(plugin.stag + plugin.translate("god." + (toggle(DEFAULT_GOD, sender) ? "on.def" : "off")));
			} else if (args[0].equalsIgnoreCase("damage") || args[0].equalsIgnoreCase("dmg")) {
				if (args.length > 1 && args[1].equalsIgnoreCase("private")) MetadataUtils.setFixedMetadata(sender, PRIVATE_GOD, true, plugin);
				sender.sendMessage(plugin.stag + plugin.translate("god." + (toggle(DAMAGE_GOD, sender) ? "on.dmg" : "off")));
			} else if (checkPerms(sender, "maussentials.god.others")) {
				Player p = plugin.getServer().getPlayer(args[0]);
				if (p != null) {
					if (args.length == 1) {
						if (p.hasMetadata(PRIVATE_GOD)) sender.sendMessage(plugin.errtag + plugin.translate("god.private", p.getName()));
						else sender.sendMessage(plugin.stag + plugin.translate("god.for." + (toggle_def(p) ? "off" : "on.def"), p.getName()));
					} else {
						if (args[1].equalsIgnoreCase("default")) {
							if (p.hasMetadata(PRIVATE_GOD)) sender.sendMessage(plugin.errtag + plugin.translate("god.private", p.getName()));
							else sender.sendMessage(plugin.stag + plugin.translate("god.for." + (toggle(DEFAULT_GOD, p) ? "on.def" : "off"), p.getName()));
						} else if (args[1].equalsIgnoreCase("damage")) {
							if (p.hasMetadata(PRIVATE_GOD)) sender.sendMessage(plugin.errtag + plugin.translate("god.private", p.getName()));
							else sender.sendMessage(plugin.stag + plugin.translate("god.for." + (toggle(DAMAGE_GOD, p) ? "on.dmg" : "off"), p.getName()));
						}
					}
				} else sender.sendMessage(plugin.errtag + plugin.translate("god.notfound", args[0]));
			}
		}
		return true;
	}
	
	public boolean toggle(String type, Player p) {
		if (p.hasMetadata(DAMAGE_GOD) || p.hasMetadata(DEFAULT_GOD)) {
			MetadataUtils.removeMetadata(p, DEFAULT_GOD, plugin);
			MetadataUtils.removeMetadata(p, DAMAGE_GOD, plugin);
			MetadataUtils.removeMetadata(p, PRIVATE_GOD, plugin);
			return false;
		} else {
			MetadataUtils.setFixedMetadata(p, type, true, plugin);
			return true;
		}
	}
	
	public boolean toggle_def(Player p) {
		if (p.hasMetadata(DEFAULT_GOD)) MetadataUtils.removeMetadata(p, DEFAULT_GOD, plugin);
		else if (p.hasMetadata(DAMAGE_GOD)) MetadataUtils.removeMetadata(p, DAMAGE_GOD, plugin);
		else {
			MetadataUtils.setFixedMetadata(p, DEFAULT_GOD, true, plugin);
			return false;
		}
		MetadataUtils.removeMetadata(p, PRIVATE_GOD, plugin);
		return true;
	}
	
	@Override
	public void help(Player sender, Command cmd, String label, String[] args) {
		sender.sendMessage(plugin.stag + plugin.translate("god.help", label));
	}
}
