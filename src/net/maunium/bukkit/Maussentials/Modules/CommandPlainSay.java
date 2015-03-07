package net.maunium.bukkit.Maussentials.Modules;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;
import net.maunium.bukkit.Maussentials.Utils.ChatFormatter;

/**
 * The /mauplainsay command
 * 
 * @author Tulir293
 * @since 0.1
 */
public class CommandPlainSay extends CommandModule {
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		this.plugin.getCommand("mauplainsay").setExecutor(this);
		this.permission = "maussentials.plainsay";
		loaded = true;
	}
	
	@Override
	public void unload() {
		this.plugin.getCommand("mauplainsay").setExecutor(plugin);
		this.plugin = null;
		loaded = false;
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			StringBuilder sb = new StringBuilder();
			for (String s : args)
				sb.append(s + " ");
			sb.deleteCharAt(sb.length() - 1);
			
			String s = ChatFormatter.formatAll(sb.toString());
			for (Player p : plugin.getServer().getOnlinePlayers()) {
				if (p.hasPermission("maussentials.plainsay.see")) p.sendMessage(plugin.translate("plainsay.spy", s));
				else p.sendMessage(s);
			}
			return true;
		} else return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(plugin.errtag + plugin.translate("plainsay.help", label));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
