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
			String[] lines = new String[4];
			boolean color = evt.getPlayer().hasPermission("maussentials.sign.color");
			boolean style = evt.getPlayer().hasPermission("maussentials.sign.style");
			boolean magic = evt.getPlayer().hasPermission("maussentials.sign.magic");
			int i = 0;
			Sign x = (Sign) evt.getClickedBlock().getState();
			for (Object s : (List<?>) mv.value()) {
				if (i > 3) break;
				// Use a more efficient technique to translate colors if all of the color permissions apply.
				if (color && style && magic) x.setLine(i, ChatFormatter.formatAll(s.toString()));
				// Format each separately if not all of the permissions apply.
				else {
					String ss = s.toString();
					if (color) ss = ChatFormatter.formatColors(ss);
					if (style) ss = ChatFormatter.formatColors(ss);
					if (magic) ss = ChatFormatter.formatColors(ss);
					x.setLine(i, ss);
				}
				i++;
			}
		}
	}
}
