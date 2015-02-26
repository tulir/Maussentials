package net.maunium.bukkit.Maussentials.Modules.Mixed;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.metadata.FixedMetadataValue;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.PlayerCommandModule;
import net.maunium.bukkit.Maussentials.Utils.PlayerUtils;

public class Godmode extends PlayerCommandModule implements Listener {
	private Maussentials plugin;
	private static final String DEFAULT_GOD = "MaussentialsGodDefault", PRIVATE_GOD = "MaussentialsGodPrivatized", DAMAGE_GOD = "MaussentialsGodDamage";
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@Override
	public void reload() {
		this.plugin.getCommand("maugod").setExecutor(this);
		this.permission = "maussentials.god";
	}
	
	public void onPlayerDamage(EntityDamageEvent evt) {
		if (evt.getEntity().getType().equals(EntityType.PLAYER)) {
			Player p = (Player) evt.getEntity();
			
			if (p.hasMetadata(DEFAULT_GOD)) evt.setCancelled(true);
			else if (p.hasMetadata(DAMAGE_GOD)) evt.setDamage(0.0D);
		}
	}
	
	@Override
	public boolean execute(Player sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			boolean removed = toggle_def(sender);
			if (removed) sender.removeMetadata(PRIVATE_GOD, plugin);
			sender.sendMessage(plugin.stag + plugin.translate("god." + (removed ? "off" : "on.def")));
		} else if (args.length > 1) {
			if (args[0].equalsIgnoreCase("default")) {
				if (args.length > 1 && args[1].equalsIgnoreCase("private")) sender.setMetadata(PRIVATE_GOD, new FixedMetadataValue(plugin, true));
				sender.sendMessage(plugin.stag + plugin.translate("god." + (toggle(DEFAULT_GOD, sender) ? "on.def" : "off")));
			} else if (args[0].equalsIgnoreCase("damage")) {
				if (args.length > 1 && args[1].equalsIgnoreCase("private")) sender.setMetadata(PRIVATE_GOD, new FixedMetadataValue(plugin, true));
				sender.sendMessage(plugin.stag + plugin.translate("god." + (toggle(DAMAGE_GOD, sender) ? "on.dmg" : "off")));
			} else if (checkPerms(sender, "maussentials.god.others")) {
				Player p = PlayerUtils.getPlayer(args[0]);
				if (p != null) {
					if (args.length == 1) {
						if (p.hasMetadata(PRIVATE_GOD)) sender.sendMessage(plugin.errtag + plugin.translate("god.private", p.getName()));
						sender.sendMessage(plugin.stag + plugin.translate("god." + (toggle_def(p) ? "off" : "on.def") + ".for", p.getName()));
					} else {
						if (args[1].equalsIgnoreCase("default")) {
							if (p.hasMetadata(PRIVATE_GOD)) sender.sendMessage(plugin.errtag + plugin.translate("god.private", p.getName()));
							else sender.sendMessage(plugin.stag + plugin.translate("god." + (toggle(DEFAULT_GOD, p) ? "on.def" : "off") + ".for", p.getName()));
						} else if (args[1].equalsIgnoreCase("damage")) {
							if (p.hasMetadata(PRIVATE_GOD)) sender.sendMessage(plugin.errtag + plugin.translate("god.private", p.getName()));
							else sender.sendMessage(plugin.stag + plugin.translate("god." + (toggle(DAMAGE_GOD, p) ? "on.dmg" : "off") + ".for", p.getName()));
						}
					}
				} else sender.sendMessage(plugin.errtag + plugin.translate("god.notfound"));
			}
		}
		return true;
	}
	
	public boolean toggle(String type, Player p) {
		if (p.hasMetadata(type)) {
			p.removeMetadata(type, plugin);
			return false;
		} else {
			p.setMetadata(type, new FixedMetadataValue(plugin, true));
			return true;
		}
	}
	
	public boolean toggle_def(Player p) {
		if (p.hasMetadata(DEFAULT_GOD)) p.removeMetadata(DEFAULT_GOD, plugin);
		else if (p.hasMetadata(DAMAGE_GOD)) p.removeMetadata(DAMAGE_GOD, plugin);
		else {
			p.setMetadata(DEFAULT_GOD, new FixedMetadataValue(plugin, true));
			return false;
		}
		return true;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(plugin.stag + plugin.translate("god.help", label));
	}
}
