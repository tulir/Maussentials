package net.maunium.bukkit.Maussentials.Modules.Chat;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.IngameMauCommandExecutor;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

public class CommandChannel implements IngameMauCommandExecutor {
	private Maussentials plugin;
	private MauChat host;
	
	public CommandChannel(Maussentials plugin, MauChat host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@Override
	public boolean execute(Player sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("join") && args.length > 1) {
				MauChannel ch = host.getChannel(args[1]);
				if (ch != null) {
					if (ch.isInvited(sender)) {
						ch.join(sender);
						MetadataUtils.setFixedMetadata(sender, MauChat.CURRENT_CHANNEL, ch, plugin);
						sender.sendMessage(plugin.translateStd("channel.joined", ch.getName()));
					} else if (args.length > 2) {
						if (ch.checkPassword(args[2])) {
							ch.join(sender);
							MetadataUtils.setFixedMetadata(sender, MauChat.CURRENT_CHANNEL, ch, plugin);
							sender.sendMessage(plugin.translateStd("channel.joined", ch.getName()));
						} else sender.sendMessage(plugin.translateErr("channel.incorrectpassword", ch.getName()));
					} else sender.sendMessage(plugin.translateErr("channel.notinvited", ch.getName()));
				} else sender.sendMessage(plugin.translateStd("channel.notfound", args[1]));
			} else if (args[0].equalsIgnoreCase("leave") && args.length > 1) {
				MauChannel ch = host.getChannel(args[1]);
				if (ch != null) {
					ch.leave(sender);
					sender.sendMessage(plugin.translateStd("channel.left", ch.getName()));
				} else sender.sendMessage(plugin.translateStd("channel.notfound", args[1]));
			} else {
				if (args[0].equalsIgnoreCase("global")) {
					MetadataUtils.removeMetadata(sender, MauChat.CURRENT_CHANNEL, plugin);
					sender.sendMessage(plugin.translateStd("channel.changed.global"));
				} else {
					MauChannel ch = host.getChannel(args[0]);
					if (ch != null) {
						if (ch.isUser(sender) || sender.hasPermission("maussentials.chat.admin")) {
							MetadataUtils.setFixedMetadata(sender, MauChat.CURRENT_CHANNEL, ch, plugin);
							sender.sendMessage(plugin.translateStd("channel.changed", ch.getName()));
						} else sender.sendMessage(plugin.translateErr("channel.notuser", ch.getName()));
					} else sender.sendMessage(plugin.translateErr("channel.notfound", args[1]));
				}
			}
		}
		return false;
	}
	
	@Override
	public void help(Player sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		
	}
}