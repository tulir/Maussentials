package net.maunium.bukkit.Maussentials.Modules.Bans;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

public class CommandUnbanIP implements CommandExecutor {
	private Maussentials plugin;
	private MauBans host;
	
	public CommandUnbanIP(Maussentials plugin, MauBans host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!CommandModule.checkPerms(sender, "maussentials.bans.unipban")) return true;
		if (args.length > 0) {
			host.unbanip(args[0]);
			if (args.length > 1 && args[1].equalsIgnoreCase("silent")) return true;
			plugin.getServer().broadcast(plugin.translatePlain("bans.broadcast.unipbanned", args[0], sender.getName()), "maussentials.bans.see.unipban");
			return true;
		} else {
			sender.sendMessage(plugin.translateErr("bans.help.unipban", label));
			return true;
		}
	}
}
