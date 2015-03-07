package net.maunium.bukkit.Maussentials.Modules;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;
import net.maunium.bukkit.Maussentials.Utils.ChatFormatter;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

public class SignEditor extends CommandModule implements Listener {
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
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO: /mausignedit executor
		return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(plugin.errtag + plugin.translate("signedit.help"));
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent evt) {
		if ((evt.getAction().equals(Action.RIGHT_CLICK_BLOCK) || evt.getAction().equals(Action.LEFT_CLICK_BLOCK))
				&& evt.getClickedBlock().getType().equals(Material.SIGN) && evt.getPlayer().hasMetadata(EDIT_META)) {
			MetadataValue mv = MetadataUtils.getMetadata(evt.getPlayer(), EDIT_META, plugin);
			int i = 0;
			Sign x = (Sign) evt.getClickedBlock().getState();
			for (Object s : (List<?>) mv.value()) {
				if (i > 3) break;
				x.setLine(i, s.toString());
				i++;
			}
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
