package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

/**
 * The /maukill command
 * 
 * @author Tulir293
 * @since 0.1
 */
public class CommandKill extends CommandModule {
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (args.length > 0) {
			Player p = plugin.getServer().getPlayer(args[0]);
			if (p != null) {
				p.setHealth(0);
				sender.sendMessage(plugin.stag + plugin.translate("kill.ed", p.getName()));
			} else sender.sendMessage(plugin.errtag + plugin.translate("kill.notfound", args[0]));
			return true;
		}
		return false;
	}
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		this.permission = "maussentials.kill";
		plugin.getCommand("maukill").setExecutor(this);
		loaded = true;
	}
	
	@Override
	public void unload() {
		plugin.getCommand("maukill").setExecutor(plugin);
		plugin = null;
		loaded = false;
	}
	
	@Override
	public void help(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(plugin.stag + plugin.translate("kill.help", label));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
