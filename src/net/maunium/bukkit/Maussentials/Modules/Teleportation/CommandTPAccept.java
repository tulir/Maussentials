package net.maunium.bukkit.Maussentials.Modules.Teleportation;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.IngameMauCommandExecutor;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

public class CommandTPAccept implements IngameMauCommandExecutor {
	private Maussentials plugin;
	
	public CommandTPAccept(Maussentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean execute(Player sender, Command cmd, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.tp.accept")) return true;
		
		if (sender.hasMetadata(MauTPs.TP_REQUEST_META)) {
			UUID u = (UUID) MetadataUtils.getMetadata(sender, MauTPs.TP_REQUEST_META, plugin).value();
			Player p = plugin.getServer().getPlayer(u);
			if (p != null) {
				p.sendMessage(plugin.translateStd("tp.request.accepted.from", sender.getName()));
				sender.sendMessage(plugin.translateStd("tp.request.accepted.to", p.getName()));
				sender.teleport(p);
			} else sender.sendMessage(plugin.translateErr("tp.request.nobody"));
			MetadataUtils.removeMetadata(sender, MauTPs.TP_REQUEST_META, plugin);
		} else sender.sendMessage(plugin.translateErr("tp.request.nobody"));
		
		return true;
	}
	
	@Override
	public void help(Player sender, Command cmd, String label, String[] args) {}
}
