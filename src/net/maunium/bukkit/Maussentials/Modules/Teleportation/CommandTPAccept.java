package net.maunium.bukkit.Maussentials.Modules.Teleportation;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.IngameMauCommandExecutor;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;
import net.maunium.bukkit.Maussentials.Utils.DelayedActions.DelayedAction;

public class CommandTPAccept implements IngameMauCommandExecutor {
	private Maussentials plugin;
	private MauTPs host;
	
	public CommandTPAccept(Maussentials plugin, MauTPs host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean execute(Player sender, Command cmd, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.tp.accept")) return true;
		if (cmd.getName().equals("mautpaccept")) {
			if (sender.hasMetadata(MauTPs.TP_REQUEST_META)) {
				UUID u = (UUID) MetadataUtils.getMetadata(sender, MauTPs.TP_REQUEST_META, plugin).value();
				Player p = plugin.getServer().getPlayer(u);
				if (p != null && p.isOnline()) {
					sender.sendMessage(plugin.translateStd("tp.request.accepted.target", p.getName()));
					
					DelayedAction da = new DelayedAction(p, host.delay, new Runnable() {
						// On success
						@Override
						public void run() {
							p.sendMessage(plugin.translateStd("tp.request.teleporting"));
							p.teleport(sender);
						}
					}, new Runnable() {
						// On fail
						@Override
						public void run() {
							p.sendMessage(plugin.translateErr("tp.request.fail"));
						}
					}, host.safeRange, host.safeDelay);
					
					if (da.start()) {
						if (host.delay != 0) p.sendMessage(plugin.translateStd("tp.request.accepted.nearby", sender.getName(), host.delay / 20));
					} else {
						if (host.safeDelay != 0) p.sendMessage(plugin.translateStd("tp.request.accepted.nonearby", sender.getName(), host.safeDelay / 20));
					}
					
				} else sender.sendMessage(plugin.translateErr("tp.request.nobody"));
				MetadataUtils.removeMetadata(sender, MauTPs.TP_REQUEST_META, plugin);
			} else sender.sendMessage(plugin.translateErr("tp.request.nobody"));
		} else if (cmd.getName().equals("mautpdeny")) {
			if (sender.hasMetadata(MauTPs.TP_REQUEST_META)) {
				UUID u = (UUID) MetadataUtils.getMetadata(sender, MauTPs.TP_REQUEST_META, plugin).value();
				Player p = plugin.getServer().getPlayer(u);
				if (p != null && p.isOnline()) {
					sender.sendMessage(plugin.translateStd("tp.request.denied.target"));
					p.sendMessage(plugin.translateStd("tp.request.denied.requester"));
				} else sender.sendMessage(plugin.translateErr("tp.request.nobody"));
				MetadataUtils.removeMetadata(sender, MauTPs.TP_REQUEST_META, plugin);
			} else sender.sendMessage(plugin.translateErr("tp.request.nobody"));
		} else return false;
		return true;
	}
	
	@Override
	public void help(Player sender, Command cmd, String label, String[] args) {}
}
