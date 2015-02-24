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
		if(!execute(sender, cmd, label, args)) help(sender, cmd, label, args);
		return true;
	}
	
	public abstract boolean execute(CommandSender sender, Command cmd, String label, String[] args);
	
	public boolean checkPerms(CommandSender p, String permission) {
		if (!p.hasPermission(permission)) {
			p.sendMessage(Maussentials.getInstance().translate("permission-error"));
			return false;
		} else return true;
	}
	
	public abstract void help(CommandSender sender, Command cmd, String label, String[] args);
}
