package net.maunium.bukkit.Maussentials.Modules.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;

/**
 * A module mainly used for a command executor of a in-game-only command.
 * 
 * @author Tulir293
 * @since 0.1
 */
public interface PlayerCommandModule extends CommandModule {
	@Override
	public default boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) sender.sendMessage(Maussentials.getInstance().translateErr("must-be-ingame"));
		else return execute((Player) sender, cmd, label, args);
		return true;
	}
	
	@Override
	public default void help(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) sender.sendMessage(Maussentials.getInstance().translateErr("must-be-ingame"));
		else help((Player) sender, cmd, label, args);
	}
	
	/**
	 * Execute the command.
	 * 
	 * @param sender The Player who is executing the command.
	 * @param cmd The command object. The getName() method in this returns the main command name.
	 * @param label The label used. May not be the main command name.
	 * @param args The arguments.
	 * @return If the syntax of the command was correct or not. If false, will print a help message.
	 */
	public boolean execute(Player sender, Command cmd, String label, String[] args);
	
	/**
	 * Print the help message.
	 * 
	 * @param sender The Player who tried to execute a command with incorrect syntax.
	 * @param cmd The command object.
	 * @param label The label used. Useful if you want to show an usage example with the same label as the sender used.
	 * @param args The arguments used.
	 */
	public void help(Player sender, Command cmd, String label, String[] args);
}
