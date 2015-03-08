package net.maunium.bukkit.Maussentials.Modules.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;

/**
 * A module mainly used for a command executor.
 * 
 * @author Tulir293
 * @since 0.1
 */
public abstract class CommandModule implements MauModule, CommandExecutor {
	protected String permission = "";
	
	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!checkPerms(sender, permission)) return true;
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
	public abstract boolean execute(CommandSender sender, Command cmd, String label, String[] args);
	
	/**
	 * Check if the given player has the given permission. If not, send the player an error containing the permission
	 * node.
	 * 
	 * @param p The player to check.
	 * @param permission The permission to check.
	 * @return True if the player has the permission, false otherwise.
	 */
	public boolean checkPerms(CommandSender p, String permission) {
		if (!p.hasPermission(permission)) {
			p.sendMessage(Maussentials.getInstance().translateErr("permission-error", permission));
			return false;
		} else return true;
	}
	
	/**
	 * Print the help message.
	 * 
	 * @param sender The CommandSender who tried to execute a command with incorrect syntax.
	 * @param cmd The command object.
	 * @param label The label used. Useful if you want to show an usage example with the same label as the sender used.
	 * @param args The arguments used.
	 */
	public abstract void help(CommandSender sender, Command cmd, String label, String[] args);
}
