package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;
import net.maunium.bukkit.Maussentials.Utils.ChatFormatter;
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
	private boolean CONSOLE_SPY = true, loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		this.plugin.getCommand("maumessage").setExecutor(this);
		this.plugin.getCommand("maureply").setExecutor(this);
		this.plugin.getCommand("mausocialspy").setExecutor(this);
		loaded = true;
	}
	
	@Override
	public void unload() {
		this.plugin.getCommand("maumessage").setExecutor(plugin);
		this.plugin.getCommand("maureply").setExecutor(plugin);
		this.plugin.getCommand("mausocialspy").setExecutor(plugin);
		plugin = null;
		loaded = false;
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (!checkPerms(sender, "maussentials.message")) return true;
		if (cmd.getName().equals("maumessage")) {
			if (args.length > 1) {
				CommandSender p;
				if (!args[0].equalsIgnoreCase("console")) p = plugin.getServer().getPlayer(args[0]);
				else p = plugin.getServer().getConsoleSender();
				
				if (p == null) {
					sender.sendMessage(plugin.translateErr("pm.msg.nobody"));
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
			return false;
		} else if (cmd.getName().equals("maureply")) {
			String replyTo = null;
			if (sender instanceof Player) {
				MetadataValue mv = MetadataUtils.getMetadata((Player) sender, REPLY_META, plugin);
				if (mv != null) replyTo = mv.asString();
				else {
					sender.sendMessage(plugin.translateErr("pm.reply.nobody"));
					return true;
				}
				
			} else replyTo = CONSOLE_REPLY;
			if (replyTo == null || replyTo.isEmpty()) {
				sender.sendMessage(plugin.translateErr("pm.reply.nobody"));
				return true;
			}
			
			CommandSender p;
			if (!replyTo.equals("CONSOLE")) p = plugin.getServer().getPlayer(replyTo);
			else p = plugin.getServer().getConsoleSender();
			
			if (p == null) {
				sender.sendMessage(plugin.translateErr("pm.reply.nobody"));
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
			return false;
		} else if (cmd.getName().equals("mausocialspy")) {
			if (!checkPerms(sender, "maussentials.message.spy")) return true;
			if (!(sender instanceof Player)) {
				if (CONSOLE_SPY) {
					CONSOLE_SPY = false;
					sender.sendMessage(plugin.translateStd("pm.spy.off"));
				} else {
					CONSOLE_SPY = true;
					sender.sendMessage(plugin.translateStd("pm.spy.on"));
				}
				return true;
			}
			Player p = (Player) sender;
			if (p.hasMetadata(MSGSPY_META)) {
				MetadataUtils.removeMetadata(p, MSGSPY_META, plugin);
				p.sendMessage(plugin.translateStd("pm.spy.off"));
			} else {
				MetadataUtils.setFixedMetadata(p, MSGSPY_META, true, plugin);
				p.sendMessage(plugin.translateStd("pm.spy.on"));
			}
			return true;
		} else return false;
	}
	
	private void message(String message, CommandSender s, CommandSender t) {
		boolean sender = s instanceof Player;
		boolean target = t instanceof Player;
		
		if (s.hasPermission("maussentials.message.color")) message = ChatFormatter.formatColors(message);
		if (s.hasPermission("maussentials.message.style")) message = ChatFormatter.formatStyles(message);
		if (s.hasPermission("maussentials.message.magic")) message = ChatFormatter.formatMagic(message);
		
		s.sendMessage(plugin.translatePlain("pm.msg.from", s.getName(), t.getName(), message));
		t.sendMessage(plugin.translatePlain("pm.msg.to", s.getName(), t.getName(), message));
		
		if (sender) MetadataUtils.setFixedMetadata((Player) s, REPLY_META, t.getName(), plugin);
		else CONSOLE_REPLY = t.getName();
		if (target) MetadataUtils.setFixedMetadata((Player) t, REPLY_META, s.getName(), plugin);
		else CONSOLE_REPLY = s.getName();
		
		String spymsg = plugin.translatePlain("pm.spy.msg", s.getName(), t.getName(), message);
		for (Player spy : plugin.getServer().getOnlinePlayers())
			if (spy.hasMetadata(MSGSPY_META) && spy.hasPermission("maussentials.message.spy")) spy.sendMessage(spymsg);
		if (CONSOLE_SPY) plugin.getServer().getConsoleSender().sendMessage(spymsg);
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("maumessage")) sender.sendMessage(plugin.translateErr("pm.help.pm", label));
		else if (cmd.getName().equals("maureply")) sender.sendMessage(plugin.translateErr("pm.help.reply", label));
		else if (cmd.getName().equals("mausocialspy")) sender.sendMessage(plugin.translateErr("pm.help.socialspy", label));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
