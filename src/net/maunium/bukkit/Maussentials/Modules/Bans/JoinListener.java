package net.maunium.bukkit.Maussentials.Modules.Bans;

import java.sql.ResultSet;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.DateUtils;

public class JoinListener implements Listener {
	private Maussentials plugin;
	private MauBans host;
	
	public JoinListener(Maussentials plugin, MauBans host) {
		this.plugin = plugin;
		this.host = host;
	}
	
	@EventHandler
	public void onPreJoin(AsyncPlayerPreLoginEvent evt) {
		try {
			ResultSet rs = host.getBanData(evt.getAddress().getHostAddress(), MauBans.TYPE_IP);
			if (rs != null) {
				String reason = rs.getString(MauBans.COLUMN_REASON);
				String bannedBy = rs.getString(MauBans.COLUMN_BANNEDBY);
				long expireExact = rs.getLong(MauBans.COLUMN_EXPIRE);
				
				if (!bannedBy.equals("CONSOLE")) {
					UUID u = UUID.fromString(bannedBy);
					bannedBy = plugin.getServer().getOfflinePlayer(u).getName();
				}
				
				if (expireExact > 0) {
					String expire = DateUtils.getDurationBreakdown(expireExact - System.currentTimeMillis(), DateUtils.MODE_IN);
					evt.setLoginResult(Result.KICK_BANNED);
					evt.setKickMessage(plugin.translatePlain("bans.ipban.temporary", reason, bannedBy, expire, evt.getAddress().getHostAddress()));
				} else {
					evt.setLoginResult(Result.KICK_BANNED);
					evt.setKickMessage(plugin.translatePlain("bans.ipban.permanent", reason, bannedBy, evt.getAddress().getHostAddress()));
				}
			}
		} catch (Exception e) {
			plugin.getLogger().severe("Failed to check if " + evt.getAddress().getHostAddress() + " is banned: ");
			e.printStackTrace();
		}
		
		try {
			ResultSet rs = host.getBanData(evt.getUniqueId().toString(), MauBans.TYPE_UUID);
			if (rs != null) {
				String reason = rs.getString(MauBans.COLUMN_REASON);
				String bannedBy = rs.getString(MauBans.COLUMN_BANNEDBY);
				long expireExact = rs.getLong(MauBans.COLUMN_EXPIRE);
				
				if (!bannedBy.equals("CONSOLE")) {
					UUID u = UUID.fromString(bannedBy);
					bannedBy = plugin.getServer().getOfflinePlayer(u).getName();
				}
				
				if (expireExact > 0) {
					String expire = DateUtils.getDurationBreakdown(expireExact - System.currentTimeMillis(), DateUtils.MODE_IN);
					evt.setLoginResult(Result.KICK_BANNED);
					evt.setKickMessage(plugin.translatePlain("bans.ban.temporary", reason, bannedBy, expire, evt.getUniqueId().toString()));
				} else {
					evt.setLoginResult(Result.KICK_BANNED);
					evt.setKickMessage(plugin.translatePlain("bans.ban.permanent", reason, bannedBy, evt.getUniqueId().toString()));
				}
			}
		} catch (Exception e) {
			plugin.getLogger().severe("Failed to check if " + evt.getUniqueId().toString() + " is banned: ");
			e.printStackTrace();
		}
	}
}
