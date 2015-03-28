package net.maunium.bukkit.Maussentials.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface IngameMauCommandExecutor extends MauCommandExecutor {
	@Override
	public default boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) if (getErrorMessage() != null) sender.sendMessage(getErrorMessage());
		else return execute((Player) sender, cmd, label, args);
		return true;
	}
	
	@Override
	public default void help(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) if (getErrorMessage() != null) sender.sendMessage(getErrorMessage());
		else help((Player) sender, cmd, label, args);
	}
	
	/**
	 * @return The error message, or null if none wanted.
	 */
	public default String getErrorMessage() {
		return ChatColor.RED + "This command can only be used in-game.";
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
