package net.maunium.bukkit.Maussentials.Modules.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

public class CommandKill implements CommandModule {
	private Maussentials plugin;
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(!plugin.checkPerms(sender, "maussentials.welcomemessage")) return true;
		// TODO: Kill command executor
		return false;
	}

	@Override
	public void reload() {
		plugin.getCommand("maukill").setExecutor(this);
	}

	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		reload();
	}
	
}
