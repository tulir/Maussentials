package net.maunium.bukkit.Maussentials.Modules.Commands;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Bans.MauBans;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;
import net.maunium.bukkit.Maussentials.Utils.DateUtils;
import net.maunium.bukkit.Maussentials.Utils.SerializableLocation;

/**
 * The /mauseen command
 * 
 * @author Tulir293
 * @since 0.1
 */
public class CommandSeen implements CommandModule {
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getCommand("mauseen").setExecutor(this);
		loaded = true;
	}
	
	@Override
	public void unload() {
		plugin.getCommand("mauseen").setExecutor(plugin);
		plugin = null;
		loaded = false;
	}
	
	@Override
	public boolean execute(CommandSender sender, Command cmd, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.seen")) return true;
		if (args.length == 1) {
			if (args[0].contains(".")) {
				Map<UUID, Long> uuids;
				try {
					uuids = plugin.getPlayerData().getUUIDsFromIP(args[0]);
					
					StringBuffer sb = new StringBuffer();
					for (Entry<UUID, Long> e : uuids.entrySet()) {
						String name = plugin.getPlayerData().getNameByUUID(e.getKey());
						sb.append(ChatColor.GOLD);
						if (name != null) sb.append(name);
						else sb.append(e.getKey().toString());
						
						sb.append(ChatColor.GRAY);
						sb.append(", ");
					}
					if (sb.length() != 0) {
						sb.delete(sb.length() - 2, sb.length());
						sender.sendMessage(plugin.translateStd("seen.ip.done", args[0], sb.toString()));
					} else sender.sendMessage(plugin.translateErr("seen.ip.empty", args[0]));
				} catch (Exception e) {
					sender.sendMessage(plugin.translateErr("seen.error", e.getMessage()));
					plugin.getLogger().severe("Failed to get UUIDs/Usernames from IP " + args[0] + ":");
					e.printStackTrace();
					return true;
				}
				
				try {
					ResultSet rs = plugin.getBans().getBanData(args[0], MauBans.TYPE_IP);
					if (rs != null) {
						String reason = rs.getString(MauBans.COLUMN_REASON);
						String bannedBy = rs.getString(MauBans.COLUMN_BANNEDBY);
						long expireExact = rs.getLong(MauBans.COLUMN_EXPIRE);
						
						if (!bannedBy.equals("CONSOLE")) {
							UUID u = UUID.fromString(bannedBy);
							bannedBy = plugin.getServer().getOfflinePlayer(u).getName();
						}
						
						if (expireExact > 0) {
							String expire = DateUtils.getDurationBreakdown(expireExact - System.currentTimeMillis(), DateUtils.MODE_FOR);
							sender.sendMessage(plugin.translatePlain("seen.ip.banned.temporary", reason, bannedBy, expire, args[0]));
						} else sender.sendMessage(plugin.translatePlain("seen.ip.banned.permanent", reason, bannedBy, args[0]));
					}
				} catch (Exception e) {
					sender.sendMessage(plugin.translateErr("seen.ip.bancheckfail", args[0], e.getMessage()));
					plugin.getLogger().severe("Failed to check if " + args[0] + " is banned: ");
					e.printStackTrace();
				}
			} else {
				UUID uuid = null;
				try {
					uuid = plugin.getPlayerData().getLatestUUIDByName(args[0]);
				} catch (SQLException e1) {
					sender.sendMessage(plugin.translateErr("seen.nevervisited", args[0]));
					e1.printStackTrace();
					return true;
				}
				
				if (uuid == null) {
					sender.sendMessage(plugin.translateErr("seen.nevervisited", args[0]));
					return true;
				}
				
				try {
					OfflinePlayer op = plugin.getServer().getPlayer(uuid);
					
					sender.sendMessage(plugin.translateStd("seen.uuid.info", op != null ? op.getName() : args[0]));
					sender.sendMessage(plugin.translatePlain("seen.uuid.uuid", uuid.toString()));
					
					// Check if the player is online.
					if (op != null && op.isOnline()) {
						// The player is online. Get the player instance and use it to get the data.
						Player p = op.getPlayer();
						
						// Get the players connection IP and send it.
						sender.sendMessage(plugin.translatePlain("seen.uuid.latestip", p.getAddress().getAddress().getHostAddress()));
						
						// Get the time since the player logged in, format it...
						String s = DateUtils.getDurationBreakdown(System.currentTimeMillis() - plugin.getPlayerData().getLastLoginByUUID(uuid),
								DateUtils.MODE_FOR);
						// ...and send it.
						sender.sendMessage(plugin.translatePlain("seen.uuid.lastseen.online", s));
						
						// Get the players current location...
						SerializableLocation sl = new SerializableLocation(p.getLocation());
						// ...and send it.
						sender.sendMessage(plugin.translatePlain("seen.uuid.location", sl.toReadableString()));
					} else {
						// The player is offline. Use data from database.
						
						// Get the latest IP of the UUID.
						String lip = plugin.getPlayerData().getLatestIPByUUID(uuid);
						// Check if the IP was found and send it if it was.
						if (lip != null && !lip.isEmpty()) sender.sendMessage(plugin.translatePlain("seen.uuid.latestip", lip));
						// If it wasn't, send an error.
						else sender.sendMessage(plugin.translatePlain("seen.uuid.latestip.unknown"));
						
						// Get the time since the player logged out, format it...
						String s = DateUtils.getDurationBreakdown(System.currentTimeMillis() - plugin.getPlayerData().getLastLoginByUUID(uuid),
								DateUtils.MODE_FOR);
						// ...and send it.
						sender.sendMessage(plugin.translatePlain("seen.uuid.lastseen.offline", s));
						
						// Get the players log-out location...
						SerializableLocation sl = plugin.getPlayerData().getLocationByUUID(uuid);
						// ...and send it.
						sender.sendMessage(plugin.translatePlain("seen.uuid.location", sl.toReadableString()));
					}
				} catch (Exception e) {
					sender.sendMessage(plugin.translateErr("seen.error", e.getMessage()));
					plugin.getLogger().severe("Failed to get UUIDs/Usernames from IP " + args[0] + ":");
					e.printStackTrace();
					return true;
				}
				
				try {
					ResultSet rs = plugin.getBans().getBanData(uuid.toString(), MauBans.TYPE_UUID);
					if (rs != null) {
						String reason = rs.getString(MauBans.COLUMN_REASON);
						String bannedBy = rs.getString(MauBans.COLUMN_BANNEDBY);
						long expireExact = rs.getLong(MauBans.COLUMN_EXPIRE);
						
						if (!bannedBy.equals("CONSOLE")) {
							UUID u = UUID.fromString(bannedBy);
							bannedBy = plugin.getServer().getOfflinePlayer(u).getName();
						}
						
						if (expireExact > 0) {
							String expire = DateUtils.getDurationBreakdown(expireExact - System.currentTimeMillis(), DateUtils.MODE_FOR);
							sender.sendMessage(plugin.translatePlain("seen.uuid.banned.temporary", reason, bannedBy, expire, args[0]));
						} else sender.sendMessage(plugin.translatePlain("seen.uuid.banned.permanent", reason, bannedBy, args[0]));
					}
				} catch (Exception e) {
					sender.sendMessage(plugin.translateErr("seen.ip.bancheckfail", args[0], e.getMessage()));
					plugin.getLogger().severe("Failed to check if " + args[0] + " is banned: ");
					e.printStackTrace();
				}
			}
			return true;
		} else return false;
	}
	
	@Override
	public void help(CommandSender sender, Command cmd, String label, String[] args) {
		sender.sendMessage(plugin.translateErr("seen.help"));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
