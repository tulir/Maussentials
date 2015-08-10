package net.maunium.bukkit.Maussentials.Modules.Chat;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scoreboard.Team;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

public class ChatListener implements Listener {
	private Maussentials plugin;
	private MauChat host;
	
	public ChatListener(Maussentials plugin, MauChat host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent evt) {
		String g = host.vault.getPrimaryGroup(evt.getPlayer());
		
		Team t = evt.getPlayer().getScoreboard().getPlayerTeam(evt.getPlayer());
		String world = evt.getPlayer().getWorld().getName();
		// @mauformat=off
		evt.setFormat(
				host.getGroupFormat(g)
				.replace("{DISPLAYNAME}", "$1")
				.replace("{MESSAGE}", "$2")
				.replace("{GROUP}", g)
				.replace("{NAME}", evt.getPlayer().getName())
				.replace("{WORLD}", world)
				.replace("{SWORLD}", world.substring(0, 1))
				.replace("{TEAMPREFIX}", t.getPrefix())
				.replace("{TEAMSUFFIX}", t.getSuffix())
				.replace("{TEAMNAME}", t.getName())
				.replace("{TEAMDISPLAYNAME}", t.getDisplayName())
			);
		// @mauformat=on
		
		if (evt.getPlayer().hasMetadata(MauChat.CURRENT_CHANNEL)) {
			MetadataValue mv = MetadataUtils.getMetadata(evt.getPlayer(), MauChat.CURRENT_CHANNEL, plugin);
			MauChannel ch;
			if (mv != null && mv.value() != null && mv.value() instanceof MauChannel) ch = (MauChannel) mv.value();
			else return;
			evt.getRecipients().clear();
			for (UUID u : ch.getPlayers()) {
				Player p = plugin.getServer().getPlayer(u);
				if (p.isOnline()) evt.getRecipients().add(p);
			}
			if (evt.getFormat().contains("{CHANNEL}")) evt.setFormat(evt.getFormat().replace("{CHANNEL}", ch.getName()));
			else evt.setFormat(plugin.translatePlain("channel.prefix", ch.getName()) + evt.getFormat());
		}
	}
}