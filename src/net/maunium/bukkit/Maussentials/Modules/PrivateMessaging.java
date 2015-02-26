package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;
import net.maunium.bukkit.Maussentials.Utils.PlayerUtils;

/**
 * A module for private messaging between players.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class PrivateMessaging extends CommandModule {
	private Maussentials plugin;
	private static final String REPLY_META = "MaussentialsPMReplyTarget";
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public void reload() {
		this.plugin.getCommand("maumessage").setExecutor(this);
		this.plugin.getCommand("maureply").setExecutor(this);
		this.plugin.getCommand("mausocialspy").setExecutor(this);
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("maumessage")) {
			if (args.length > 1) {
				Player p = PlayerUtils.getPlayer(args[0]);
				StringBuffer sb = new StringBuffer();
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i]);
					sb.append(" ");
				}
				sb.deleteCharAt(sb.length() - 1);
				
				String msg = plugin.translate("pm.msg", sender.getName(), p.getName(), sb.toString());
				p.sendMessage(msg);
				sender.sendMessage(msg);
				
				p.setMetadata(REPLY_META, new FixedMetadataValue(plugin, sender.getName()));
				if (sender instanceof Player) ((Player) sender).setMetadata(REPLY_META, new FixedMetadataValue(plugin, p.getName()));
			}
		} else if (cmd.getName().equals("maureply")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(plugin.errtag + plugin.translate("pm.reply.onlyplayer"));
				return true;
			}
			Player s = (Player) sender;
			String md = PlayerUtils.getMetadata(s, REPLY_META, plugin).asString();
			if (md == null || md.isEmpty()) {
				s.sendMessage(plugin.errtag + plugin.translate("pm.reply.nobody"));
				return true;
			}
			Player p = PlayerUtils.getPlayer(md);
			if (p == null) {
				s.sendMessage(plugin.errtag + plugin.translate("pm.reply.nobody"));
				return true;
			}
			
			if (args.length > 0) {
				StringBuffer sb = new StringBuffer();
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i]);
					sb.append(" ");
				}
				sb.deleteCharAt(sb.length() - 1);
				
				String msg = plugin.translate("pm.msg", sender.getName(), p.getName(), sb.toString());
				p.sendMessage(msg);
				s.sendMessage(msg);
				p.setMetadata(REPLY_META, new FixedMetadataValue(plugin, sender.getName()));
			}
		} else if (cmd.getName().equals("mausocialspy")){
			// TODO: Implementation for /mausocialspy
		}
		return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equals("maumessage")) sender.sendMessage(plugin.stag + plugin.translate("pm.help.pm", label));
		else if(cmd.getName().equals("maureply")) sender.sendMessage(plugin.stag + plugin.translate("pm.help.reply", label));
		else if(cmd.getName().equals("mausocialspy")) sender.sendMessage(plugin.stag + plugin.translate("pm.help.socialspy", label));
	}
	
}
