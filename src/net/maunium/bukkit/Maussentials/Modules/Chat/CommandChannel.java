package net.maunium.bukkit.Maussentials.Modules.Chat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.IngameCommandExecutor;

public class CommandChannel implements IngameCommandExecutor {
	private Maussentials plugin;
	private MauChat host;
	
	public CommandChannel(Maussentials plugin, MauChat host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onCommand(Player p, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
