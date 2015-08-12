package net.maunium.bukkit.Maussentials.Modules.Chat;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scoreboard.Team;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.ChatFormatter;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

/**
 * Chat listener for Maussentials Chat
 * 
 * @author Tulir293
 * @since 0.3
 */
public class ChatListener implements Listener {
	public static final String PREV_MSG = "MaussentialsChatPreviousMessage", SPAM_COUNT = "MaussentialsChatSpamCount",
			PREV_MSG_TIME = "MaussentialsChatPrevMsgTime";
	private Maussentials plugin;
	private MauChat host;
	
	public ChatListener(Maussentials plugin, MauChat host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onChat(AsyncPlayerChatEvent evt) {
		// Filter the message.
		evt.setMessage(host.filter(evt.getMessage()));
		
		/*
		 * Simple, non-annoying spam filter that blocks sending very similar messages too quickly.
		 */
		MetadataValue mv = MetadataUtils.getMetadata(evt.getPlayer(), PREV_MSG, plugin);
		String msg = evt.getMessage().toLowerCase(Locale.ENGLISH);
		if (mv != null && mv.value() != null) {
			// Save previous message and current message to variables.
			String prevMsg = mv.asString();
			
			// Get the time of the previous message.
			mv = MetadataUtils.getMetadata(evt.getPlayer(), PREV_MSG_TIME, plugin);
			long prevMsgTime = 0;
			if (mv != null && mv.value() != null) prevMsgTime = mv.asLong();
			// If it's less than 6 seconds from the previous message, do some other checks.
			if (System.currentTimeMillis() - prevMsgTime < host.getConfig().getInt("antispam.reset-delay") * 1000) {
				// Get the spam count metadata.
				mv = MetadataUtils.getMetadata(evt.getPlayer(), SPAM_COUNT, plugin);
				int spamCount = 0;
				if (mv != null && mv.value() != null) spamCount = mv.asInt();
				
				// Calculate the levenshtein distance of the strings and if it's below the limit set
				// in the chat config, increase spam count.
				if (StringUtils.getLevenshteinDistance(prevMsg, msg) < host.getConfig().getInt("antispam.difference-limit")) spamCount++;
				// Otherwise set spam count to zero.
				else spamCount = 0;
				
				// If spam count is above one, cancel the message and warn the player.
				if (spamCount > 1) {
					evt.setCancelled(true);
					evt.getPlayer().sendMessage(plugin.translateErr("spamming"));
				}
				
				// Set the new spam count meta.
				MetadataUtils.setFixedMetadata(evt.getPlayer(), SPAM_COUNT, spamCount, plugin);
			} else MetadataUtils.setFixedMetadata(evt.getPlayer(), SPAM_COUNT, 0, plugin);
		}
		
		// Set the previous message meta to the message in this event.
		MetadataUtils.setFixedMetadata(evt.getPlayer(), PREV_MSG, msg, plugin);
		// Set the previous message timestamp meta to the current time.
		MetadataUtils.setFixedMetadata(evt.getPlayer(), PREV_MSG_TIME, System.currentTimeMillis(), plugin);
		
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
		Player rec = null;
		// Loop through the recipents.
		for (Player p : evt.getRecipients()) {
			// If the message contains a players name, highlight the message for the player.
			if (msg.contains(p.getName().toLowerCase(Locale.ENGLISH))) {
				rec = p;
				break;
			}
		}
		if (rec == null) return;
		// Remove the player from the default message recipents.
		evt.getRecipients().remove(rec);
		// Send the highlighted version to the player.
		rec.sendMessage(String.format(evt.getFormat(), evt.getPlayer().getDisplayName(), ChatColor.AQUA + evt.getMessage()));
	}
}