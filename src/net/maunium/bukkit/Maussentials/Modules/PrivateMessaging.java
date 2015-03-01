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
	private String CONSOLE_REPLY = "";
	private boolean CONSOLE_SPY = true;
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		reload();
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
				Player p = null;
				boolean console = false;
				if (!args[0].equalsIgnoreCase("console")) p = plugin.getServer().getPlayer(args[0]);
				else console = true;
				
				if(!console && p == null) {
					sender.sendMessage(plugin.errtag + plugin.translate("pm.msg.nobody"));
					return true;
				}
				
				StringBuffer sb = new StringBuffer();
				for (int i = 1; i < args.length; i++) {
					sb.append(args[i]);
					sb.append(" ");
				}
				sb.deleteCharAt(sb.length() - 1);
				
				message(sb.toString(), sender, p);
				return true;
			}
		} else if (cmd.getName().equals("maureply")) {
			String replyTo = null;
			if (sender instanceof Player) replyTo = MetadataUtils.getMetadata((Player) sender, REPLY_META, plugin).asString();
			else replyTo = CONSOLE_REPLY;
			if (replyTo == null || replyTo.isEmpty()) {
				sender.sendMessage(plugin.errtag + plugin.translate("pm.reply.nobody"));
				return true;
			}
			Player p = null;
			boolean console = false;
			if (!replyTo.equals("CONSOLE")) p = plugin.getServer().getPlayer(replyTo);
			else console = true;
			if (!console && p == null) {
				sender.sendMessage(plugin.errtag + plugin.translate("pm.reply.nobody"));
				return true;
			}
			
			if (args.length > 0) {
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < args.length; i++) {
					sb.append(args[i]);
					sb.append(" ");
				}
				sb.deleteCharAt(sb.length() - 1);
				
				message(sb.toString(), sender, p);
				return true;
			}
		} else if (cmd.getName().equals("mausocialspy")) {
			if (!checkPerms(sender, "maussentials.message.spy")) return true;
			if (!(sender instanceof Player)) {
				if(CONSOLE_SPY){
					CONSOLE_SPY = false;
					sender.sendMessage(plugin.stag + plugin.translate("pm.spy.off"));
				} else {
					CONSOLE_SPY = true;
					sender.sendMessage(plugin.stag + plugin.translate("pm.spy.on"));
				}
				return true;
			}
			Player p = (Player) sender;
			if (p.hasMetadata(MSGSPY_META)) {
				MetadataUtils.removeMetadata(p, MSGSPY_META, plugin);
				p.sendMessage(plugin.stag + plugin.translate("pm.spy.off"));
			} else {
				MetadataUtils.setFixedMetadata(p, MSGSPY_META, true, plugin);
				p.sendMessage(plugin.stag + plugin.translate("pm.spy.on"));
			}
			return true;
		}
		return false;
	}
	
	public void message(String message, CommandSender s, Player t) {
		String target = t == null ? "CONSOLE" : t.getName();
		s.sendMessage(plugin.translate("pm.msg.from", s.getName(), target, message));
		if (!target.equals("CONSOLE")) t.sendMessage(plugin.translate("pm.msg.to", s.getName(), target, message));
		else System.out.println(plugin.translate("pm.msg.to", s.getName(), target, message));
		
		if(t != null) MetadataUtils.setFixedMetadata(t, REPLY_META, s.getName(), plugin);
		else CONSOLE_REPLY = s.getName();
		
		if (s instanceof Player) MetadataUtils.setFixedMetadata((Player) s, REPLY_META, target, plugin);
		else CONSOLE_REPLY = target;
		
		String spymsg = plugin.translate("pm.spy.msg", s.getName(), target, message);
		for (Player spy : plugin.getServer().getOnlinePlayers())
			if (spy.hasMetadata(MSGSPY_META) && spy.hasPermission("maussentials.message.spy")) spy.sendMessage(spymsg);
		if (!target.equals("CONSOLE")) plugin.getLogger().info(spymsg);
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("maumessage")) sender.sendMessage(plugin.stag + plugin.translate("pm.help.pm", label));
		else if (cmd.getName().equals("maureply")) sender.sendMessage(plugin.stag + plugin.translate("pm.help.reply", label));
		else if (cmd.getName().equals("mausocialspy")) sender.sendMessage(plugin.stag + plugin.translate("pm.help.socialspy", label));
	}
	
}
