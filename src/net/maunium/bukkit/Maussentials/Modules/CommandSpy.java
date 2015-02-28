package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.metadata.FixedMetadataValue;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

public class CommandSpy extends CommandModule implements Listener {
	private Maussentials plugin;
	private static final String CMDSPY_META = "MaussentialsCommandSpy";
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		this.plugin.getServer().getPluginManager().registerEvents(this, plugin);
		reload();
	}
	
	@Override
	public void reload() {
		this.plugin.getCommand("maucommandspy").setExecutor(this);
		this.permission = "maussentials.commandspy";
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void onPreCommand(PlayerCommandPreprocessEvent evt) {
		String spy = plugin.translate("cmdspy.cmd", evt.getPlayer().getName(), evt.getMessage());
		for (Player p : plugin.getServer().getOnlinePlayers())
			if (p.hasMetadata(CMDSPY_META) && p.hasPermission("maussentials.commandspy")) p.sendMessage(spy);
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(Maussentials.getInstance().translate("cmdspy.onlyplayer"));
			return true;
		}
		Player p = (Player) sender;
		
		if (p.hasMetadata(CMDSPY_META)) {
			p.setMetadata(CMDSPY_META, new FixedMetadataValue(plugin, true));
			p.sendMessage(plugin.stag + plugin.translate("cmdspy.on"));
		} else {
			p.removeMetadata(CMDSPY_META, plugin);
			p.sendMessage(plugin.stag + plugin.translate("cmdspy.off"));
		}
		
		return true;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		// Not needed, execute always returns true.
	}
}