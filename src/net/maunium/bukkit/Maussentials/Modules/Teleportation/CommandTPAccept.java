package net.maunium.bukkit.Maussentials.Modules.Teleportation;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.IngameMauCommandExecutor;

public class CommandTPAccept implements IngameMauCommandExecutor {
	private Maussentials plugin;
	
	public CommandTPAccept(Maussentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean execute(Player sender, Command cmd, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.tp.accept")) return true;
		return false;
	}
	
	@Override
	public void help(Player sender, Command cmd, String label, String[] args) {
		
	}
}
