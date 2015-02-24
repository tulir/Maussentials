package net.maunium.bukkit.Maussentials.Modules.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;

public abstract class PlayerCommandModule extends CommandModule {
	@Override
	public final boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)){
			sender.sendMessage(Maussentials.getInstance().translate("must-be-ingame"));
			return true;
		}
		return execute((Player) sender, cmd, label, args);
	}
	
	public abstract boolean execute(Player sender, Command cmd, String label, String[] args);
}
