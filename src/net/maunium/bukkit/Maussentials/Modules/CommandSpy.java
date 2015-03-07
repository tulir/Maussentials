package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.PlayerCommandModule;
import net.maunium.bukkit.Maussentials.Utils.ChatFormatter;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

/**
 * The /maucommandspy command
 * 
 * @author Tulir293
 * @since 0.1
 */
public class CommandSpy extends PlayerCommandModule implements Listener {
	private Maussentials plugin;
	private static final String CMDSPY_META = "MaussentialsCommandSpy";
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.plugin.getCommand("maucommandspy").setExecutor(this);
		this.permission = "maussentials.commandspy";
	}
	
	@Override
	public void unload() {
		HandlerList.unregisterAll(this);
		this.plugin.getCommand("maucommandspy").setExecutor(plugin);
		plugin = null;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPreCommand(PlayerCommandPreprocessEvent evt) {
		String msg = ChatFormatter.change('&', '`', evt.getMessage(), "0123456789AaBbCcDdEeFfKkLlMmNnOoRr");
		String spy = plugin.translate("cmdspy.cmd", evt.getPlayer().getName(), msg);
		spy = ChatFormatter.change('`', '&', spy, "0123456789AaBbCcDdEeFfKkLlMmNnOoRr");
		
		for (Player p : plugin.getServer().getOnlinePlayers())
			if (p.hasMetadata(CMDSPY_META) && p.hasPermission("maussentials.commandspy")) p.sendMessage(spy);
	}
	
	@Override
	public boolean execute(Player p, Command cmd, String label, String[] args) {
		if (p.hasMetadata(CMDSPY_META)) {
			MetadataUtils.removeMetadata(p, CMDSPY_META, plugin);
			p.sendMessage(plugin.stag + plugin.translate("cmdspy.off"));
		} else {
			MetadataUtils.setFixedMetadata(p, CMDSPY_META, true, plugin);
			p.sendMessage(plugin.stag + plugin.translate("cmdspy.on"));
		}
		return true;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		// Not needed, execute always returns true.
	}
}
