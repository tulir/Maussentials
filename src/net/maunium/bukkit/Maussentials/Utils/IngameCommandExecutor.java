package net.maunium.bukkit.Maussentials.Utils;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Represents a class which contains a single method for executing commands that were sent by a player.
 * 
 * @author Tulir293
 * @since 0.1
 */
public abstract class IngameCommandExecutor implements CommandExecutor {
	private String error_message = ChatColor.RED + "This command can only be used in-game.";
	
	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) return onCommand((Player) sender, cmd, label, args);
		else if (error_message != null) sender.sendMessage(error_message);
		return true;
	}
	
	/**
	 * Set the error message.
	 * 
	 * @param message The message to use.
	 */
	public void setErrorMessage(String message) {
		this.error_message = message;
	}
	
	/**
	 * @return The error message.
	 */
	public String getErrorMessage() {
		return error_message;
	}
	
	/**
	 * Executes the given command, returning its success
	 *
	 * @param p The player who sent the command
	 * @param command Command which was executed
	 * @param label Alias of the command which was used
	 * @param args Passed command arguments
	 * @return true if a valid command, otherwise false
	 */
	public abstract boolean onCommand(Player p, Command cmd, String label, String[] args);
}
