package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.PlayerCommandModule;
import net.maunium.bukkit.Maussentials.Utils.ChatFormatter;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

public class SignEditor extends PlayerCommandModule implements Listener {
	private static final String EDIT_META = "MaussentialsSignEditPreparedLines";
	private Maussentials plugin;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		this.plugin.getCommand("mausignedit").setExecutor(this);
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.permission = "maussentials.sign.edit";
	}
	
	@Override
	public void unload() {
		this.plugin.getCommand("mausignedit").setExecutor(plugin);
		HandlerList.unregisterAll(this);
		this.plugin = null;
	}
	
	@Override
	public boolean execute(Player sender, Command cmd, String label, String[] args) {
		if (args.length > 1) {
			int line;
			try {
				line = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				return false;
			}
			
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < args.length; i++)
				sb.append(args[i] + " ");
			
			sb.deleteCharAt(sb.length() - 1);
			
			String s = sb.toString();
			
			if (s.length() > 16) sender.sendMessage(plugin.errtag + plugin.translate("signedit.toolong"));
			else {
				MetadataUtils.setFixedMetadata(sender, EDIT_META, line + ">§>" + s, plugin);
				sender.sendMessage(plugin.stag + plugin.translate("signedit.click"));
			}
			return true;
		} else return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(plugin.errtag + plugin.translate("signedit.help", label));
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent evt) {
		if ((evt.getAction().equals(Action.RIGHT_CLICK_BLOCK) || evt.getAction().equals(Action.LEFT_CLICK_BLOCK))
				&& (evt.getClickedBlock().getType().equals(Material.SIGN_POST) || evt.getClickedBlock().getType().equals(Material.WALL_SIGN))
				&& evt.getPlayer().hasMetadata(EDIT_META)) {
			String s = MetadataUtils.getMetadata(evt.getPlayer(), EDIT_META, plugin).asString();
			String[] ss = s.split(">§>", 2);
			int line = Integer.parseInt(ss[0]);
			s = ss[1];
			Sign x = (Sign) evt.getClickedBlock().getState();
			x.setLine(line, s);
			evt.getPlayer().sendMessage(plugin.stag + plugin.translate("signedit.edited"));
			MetadataUtils.removeMetadata(evt.getPlayer(), EDIT_META, plugin);
		}
	}
	
	@EventHandler
	public void onSignFinish(SignChangeEvent evt) {
		boolean color = evt.getPlayer().hasPermission("maussentials.sign.format.color");
		boolean style = evt.getPlayer().hasPermission("maussentials.sign.format.style");
		boolean magic = evt.getPlayer().hasPermission("maussentials.sign.format.magic");
		
		if (!color && !style && !magic) return;
		
		int i = 0;
		for (String s : evt.getLines()) {
			if (color && style && magic) evt.setLine(i, ChatFormatter.formatAll(s));
			else {
				if (color) s = ChatFormatter.formatColors(s);
				if (style) s = ChatFormatter.formatStyles(s);
				if (magic) s = ChatFormatter.formatMagic(s);
				evt.setLine(i, s);
			}
			i++;
		}
	}
}
