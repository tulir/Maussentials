package net.maunium.bukkit.Maussentials.Modules.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * A module mainly used for a command executor.
 * 
 * @author Tulir293
 * @since 0.1
 */
public interface CommandModule extends MauModule, CommandExecutor {
	@Override
	public default boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!execute(sender, cmd, label, args)) help(sender, cmd, label, args);
		return true;
	}
	
	/**
	 * Execute the command.
	 * 
	 * @param sender The CommandSender who is executing the command.
	 * @param cmd The command object. The getName() method in this returns the main command name.
	 * @param label The label used. May not be the main command name.
	 * @param args The arguments.
	 * @return If the syntax of the command was correct or not. If false, will print a help message.
	 */
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args);
	
	/**
	 * Print the help message.
	 * 
	 * @param sender The CommandSender who tried to execute a command with incorrect syntax.
	 * @param cmd The command object.
	 * @param label The label used. Useful if you want to show an usage example with the same label as the sender used.
	 * @param args The arguments used.
	 */
	public void help(CommandSender sender, Command cmd, String label, String[] args);
}
