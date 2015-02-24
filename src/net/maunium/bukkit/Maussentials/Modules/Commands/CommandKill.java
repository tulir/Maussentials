package net.maunium.bukkit.Maussentials.Modules.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

public class CommandKill extends CommandModule {
	private Maussentials plugin;
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if(args.length > 0){
			// TODO: Kill implementation
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
		// TODO: Help for /kill
	}
	
}
