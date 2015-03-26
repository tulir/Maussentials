package net.maunium.bukkit.Maussentials.Modules.Teleportation;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.IngameMauCommandExecutor;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

public class CommandTPRequest implements IngameMauCommandExecutor {
	private Maussentials plugin;
	
	public CommandTPRequest(Maussentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean execute(Player sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			Player p = plugin.getServer().getPlayer(args[0]);
			if (p != null) {
				MetadataUtils.setFixedMetadata(p, MauTPs.TP_REQUEST_META, sender.getUniqueId(), plugin);
				p.sendMessage(plugin.translateStd("tp.request.from", sender.getName()));
				sender.sendMessage(plugin.translateStd("tp.request.to", p.getName()));
			} else sender.sendMessage(plugin.translateErr("tp.request.playernotfound", args[0]));
			return true;
		}
		return false;
	}
	
	@Override
	public void help(Player sender, Command cmd, String label, String[] args) {
		sender.sendMessage(plugin.translateErr("tp.request.help", label));
	}
}
