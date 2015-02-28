package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

/**
 * A module for private messaging between players.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class PrivateMessaging extends CommandModule {
	private Maussentials plugin;
	private static final String REPLY_META = "MaussentialsPMReplyTarget", MSGSPY_META = "MaussentialsPMSpy";
	
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
				Player p = plugin.getServer().getPlayer(args[0]);
				StringBuffer sb = new StringBuffer();
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i]);
					sb.append(" ");
				}
				sb.deleteCharAt(sb.length() - 1);
				
				String msg = plugin.translate("pm.msg", sender.getName(), p.getName(), sb.toString());
				p.sendMessage(msg);
				sender.sendMessage(msg);
				
				MetadataUtils.setFixedMetadata(p, REPLY_META, sender.getName(), plugin);
				if (sender instanceof Player) MetadataUtils.setFixedMetadata((Player) sender, REPLY_META, p.getName(), plugin);
			}
		} else if (cmd.getName().equals("maureply")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(plugin.errtag + plugin.translate("pm.reply.onlyplayer"));
				return true;
			}
			Player s = (Player) sender;
			String md = MetadataUtils.getMetadata(s, REPLY_META, plugin).asString();
			if (md == null || md.isEmpty()) {
				s.sendMessage(plugin.errtag + plugin.translate("pm.reply.nobody"));
				return true;
			}
			Player p = plugin.getServer().getPlayer(md);
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
				MetadataUtils.setFixedMetadata(p, REPLY_META, sender.getName(), plugin);
				
				String spymsg = plugin.translate("pm.spy.msg", sender.getName(), p.getName(), sb.toString());
				for (Player spy : plugin.getServer().getOnlinePlayers())
					if (spy.hasMetadata(MSGSPY_META) && spy.hasPermission("maussentials.message.spy")) spy.sendMessage(spymsg);
				plugin.getLogger().info(spymsg);
			}
		} else if (cmd.getName().equals("mausocialspy")) {
			if (!checkPerms(sender, "maussentials.message.spy")) return true;
			if (!(sender instanceof Player)) {
				sender.sendMessage(plugin.errtag + plugin.translate("pm.spy.onlyplayer"));
				return true;
			}
			Player p = (Player) sender;
			if (p.hasMetadata(MSGSPY_META)) {
				MetadataUtils.setFixedMetadata(p, MSGSPY_META, true, plugin);
				p.sendMessage(plugin.stag + plugin.translate("pm.spy.on"));
			} else {
				MetadataUtils.removeMetadata(p, MSGSPY_META, plugin);
				p.sendMessage(plugin.stag + plugin.translate("pm.spy.off"));
			}
		}
		return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("maumessage")) sender.sendMessage(plugin.stag + plugin.translate("pm.help.pm", label));
		else if (cmd.getName().equals("maureply")) sender.sendMessage(plugin.stag + plugin.translate("pm.help.reply", label));
		else if (cmd.getName().equals("mausocialspy")) sender.sendMessage(plugin.stag + plugin.translate("pm.help.socialspy", label));
	}
	
}
