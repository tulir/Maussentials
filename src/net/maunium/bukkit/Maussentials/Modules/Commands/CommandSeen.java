package net.maunium.bukkit.Maussentials.Modules.Commands;

import java.sql.ResultSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Bans.MauBans;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;
import net.maunium.bukkit.Maussentials.Utils.DateUtils;

/**
 * The /mauseen command
 * 
 * @author Tulir293
 * @since 0.1
 */
public class CommandSeen extends CommandModule {
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getCommand("mauseen").setExecutor(this);
		this.permission = "maussentials.seen";
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
						sb.deleteCharAt(sb.length() - 1);
						sender.sendMessage(plugin.translateStd("seen.ipsearch.done", args[0], sb.toString()));
					} else sender.sendMessage(plugin.translateStd("seen.ipsearch.empty", args[0]));
					// TODO: Ban checking for IPs
				} catch (Exception e) {
					sender.sendMessage(plugin.translateErr("seen.ip.queryfail", e.getMessage()));
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
							String expire = DateUtils.getDurationBreakdown(expireExact - System.currentTimeMillis(), DateUtils.MODE_IN);
							sender.sendMessage(plugin.translatePlain("seen.ip.banned.temporary", reason, bannedBy, expire, args[0]));
						} else sender.sendMessage(plugin.translatePlain("seen.ip.banned.permanent", reason, bannedBy, args[0]));
					}
				} catch (Exception e) {
					sender.sendMessage(plugin.translateErr("seen.ip.bancheckfail", args[0], e.getMessage()));
					plugin.getLogger().severe("Failed to check if " + args[0] + " is banned: ");
					e.printStackTrace();
				}
			} else {
//				OfflinePlayer op = null;
//				UUID uuid = null;
//				String ip = null;
//				if (op.getUniqueId() == null) {
//					List<UUID> list = MauBukLib.getPlayerData().getUUIDsByName(args[0]);
//					if (!list.isEmpty()) uuid = list.get(0);
//				} else uuid = op.getUniqueId();
//				if (op.isOnline()) ip = op.getPlayer().getAddress().getAddress().getHostAddress();
//				else if (uuid != null) ip = MauBukLib.getPlayerData().getIPByUUID(uuid);
//				
//				sender.sendMessage(plugin.formatStd("seen.message.title", op.getName()));
//				
//				if (ip != null) sender.sendMessage(ChatColor.GRAY + plugin.format("seen.message.ip", ip));
//				else sender.sendMessage(ChatColor.RED + plugin.format("seen.message.ip", plugin.format("seen.message.notfound")));
//				
//				if (uuid != null) {
//					sender.sendMessage(ChatColor.GRAY + plugin.format("seen.message.uuid", uuid));
//					long lastlogin = MauBukLib.getPlayerData().getLastLoginByUUID(uuid);
//					if (lastlogin != 0) {
//						lastlogin = System.currentTimeMillis() - lastlogin;
//						if (op.isOnline()) sender.sendMessage(ChatColor.GRAY
//								+ plugin.format("seen.message.lastlogin.online", DateUtils.getDurationBreakdown(lastlogin, 1)));
//						else sender.sendMessage(ChatColor.GRAY + plugin.format("seen.message.lastlogin.offline", DateUtils.getDurationBreakdown(lastlogin, 1)));
//					}
//					Location l = op.isOnline() ? op.getPlayer().getLocation() : MauBukLib.getPlayerData().getLocationByUUID(uuid);
//					if (l != null) sender.sendMessage(ChatColor.GRAY + plugin.format("seen.message.location", MauUtils.toReadableString(l)));
//				} else sender.sendMessage(ChatColor.RED + plugin.format("seen.message.uuid", plugin.format("seen.message.notfound")));
//				
//				if (op.isOnline()) {
//					Player p = op.getPlayer();
//					if (p.hasMetadata("maucros")) {
//						MetadataValue mv = p.getMetadata("maucros").get(0);
//						sender.sendMessage(ChatColor.GRAY + plugin.format("seen.message.maucros", mv.asString()));
//					}
//				}
//				
//				if (plugin.isBanned(op.getUniqueId())) {
//					Ban b = plugin.getBan(op.getUniqueId());
//					sender.sendMessage(ChatColor.GRAY + plugin.format("seen.message.ban", op.getName(), b.getReason(), b.getBannedBy()));
//					if (!b.isPermanent())
//						sender.sendMessage(ChatColor.GRAY + plugin.format("seen.message.ban.expires", Ban.getDurationBreakdown(b.getTimeToTimeout(), 0)));
//				}
			}// else sender.sendMessage(plugin.formatErr("error.nevervisited", args[0]));
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
