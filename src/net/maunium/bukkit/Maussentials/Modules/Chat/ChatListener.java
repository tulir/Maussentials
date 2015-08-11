package net.maunium.bukkit.Maussentials.Modules.Chat;

import java.util.Locale;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scoreboard.Team;

import net.maunium.bukkit.Maussentials.Utils.ChatFormatter;

/**
 * Chat listener for Maussentials Chat
 * 
 * @author Tulir293
 * @since 0.3
 */
public class ChatListener implements Listener {
	public static final String PREV_MSG_META = "MaussentialsChatPreviousMessage", SPAM_COUNT_META = "MaussentialsChatSpamCount";
	private MauChat host;
	
	public ChatListener(MauChat host) {
		this.host = host;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onChat(AsyncPlayerChatEvent evt) {
		// Filter the message.
		evt.setMessage(host.filter(evt.getMessage()));
		
		// Get the player group.
		String g = host.vault.getPrimaryGroup(evt.getPlayer());
		// Get the player team.
		Team t = evt.getPlayer().getScoreboard().getPlayerTeam(evt.getPlayer());
		// Get the player world.
		String world = evt.getPlayer().getWorld().getName();
		// Format the message.
		// @mauformat=off
		evt.setFormat(ChatFormatter.formatAll(
				host.getPlayerFormat(evt.getPlayer())
				.replace("{DISPLAYNAME}", "%s")
				.replace("{MESSAGE}", "%s")
				.replace("{GROUP}", g)
				.replace("{NAME}", evt.getPlayer().getName())
				.replace("{WORLD}", world)
				.replace("{SWORLD}", world.substring(0, 1))
				.replace("{TEAMPREFIX}", t != null ? t.getPrefix() : "")
				.replace("{TEAMSUFFIX}", t != null ? t.getSuffix() : "")
				.replace("{TEAMNAME}", t != null ? t.getName() : "")
				.replace("{TEAMDISPLAYNAME}", t != null ? t.getDisplayName() : "")
			));
		// @mauformat=on
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onChatLater(AsyncPlayerChatEvent evt) {
		// Get the lowercase message.
		String msg = evt.getMessage().toLowerCase(Locale.ENGLISH);
		// Make sure it contains @ chars
		if (!msg.contains("@")) return;
		Player rec = null;
		// Loop through the recipents.
		for (Player p : evt.getRecipients()) {
			// If the message contains @PlayerName, highlight the message for the player PlayerName
			if (msg.contains("@" + p.getName().toLowerCase(Locale.ENGLISH))) {
				rec = p;
				break;
			}
		}
		if (rec == null) return;
		// Remove PlayerName from the default message recipents.
		evt.getRecipients().remove(rec);
		// Send the highlighted version to PlayerName.
		rec.sendMessage(String.format(evt.getFormat(), evt.getPlayer().getDisplayName(), ChatColor.DARK_RED + evt.getMessage()));
	}
}