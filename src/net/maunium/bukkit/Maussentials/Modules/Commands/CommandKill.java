package net.maunium.bukkit.Maussentials.Modules.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;
import net.maunium.bukkit.Maussentials.Utils.PlayerUtils;

public class CommandKill extends CommandModule {
	private Maussentials plugin;
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			Player p = PlayerUtils.getPlayer(args[0]);
			if (p != null) {
				p.setHealth(0);
				sender.sendMessage(plugin.stag + plugin.translate("kill.ed", p.getName()));
			} else sender.sendMessage(plugin.errtag + plugin.translate("kill.notfound"));
			return true;
		}
		return false;
	}
	
	@Override
	public void reload() {
		plugin.getCommand("maukill").setExecutor(this);
	}
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		this.permission = "maussentials.kill";
		reload();
	}
	
	@Override
	public void help(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(plugin.stag + plugin.translate("kill.help", label));
	}
	
}
