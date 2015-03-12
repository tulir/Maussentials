package net.maunium.bukkit.Maussentials.Utils;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;

/**
 * A simple system for delayed teleports. Also has option to allow faster (or slower) teleportation if nobody is within
 * a given range and you can even add a list of exceptions to who can be in that range.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class DelayedTeleport {
	private final Maussentials plugin;
	public static final String DELAYED_TP_META = "MaussentialsDelayedTeleportInfo";
	private final Player p;
	private final Runnable onSuccess, onFail;
	private final Location target;
	private final int delay, safeDelay;
	private final String fail, success;
	private final double safeRange;
	private final List<UUID> noCancel;
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param fail The message to send on fail (canceled teleport).
	 * @param success The message to send after a successful teleport.
	 * @param onFail The runnable to run on fail (canceled teleport).
	 * @param onSuccess The runnable to run after a successful teleport.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @param noCancel The list of players who can be near the player without disallowing use of safeDelay as delay. Can
	 *            be null and will not affect the status of the safeRange system if null or empty.
	 * @throws IllegalArgumentException if p or target is null.
	 */
	public DelayedTeleport(Player p, Location target, int delay, String fail, String success, Runnable onFail, Runnable onSuccess, double safeRange, int safeDelay, List<UUID> noCancel) {
		this.plugin = Maussentials.getInstance();
		if (p == null || target == null) throw new IllegalArgumentException("Player/Target can't be null!");
		this.p = p;
		this.target = target;
		this.delay = delay;
		this.fail = fail;
		this.success = success;
		this.onFail = onFail;
		this.onSuccess = onSuccess;
		this.safeRange = safeRange;
		this.safeDelay = safeDelay;
		this.noCancel = noCancel;
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param fail The message to send on fail (canceled teleport).
	 * @param success The message to send after a successful teleport.
	 * @param onFail The runnable to run on fail (canceled teleport).
	 * @param onSuccess The runnable to run after a successful teleport.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @throws IllegalArgumentException if p or target is null.
	 */
	public DelayedTeleport(Player p, Location target, int delay, String fail, String success, Runnable onFail, Runnable onSuccess, double safeRange, int safeDelay) {
		this(p, target, delay, fail, success, onFail, onSuccess, safeRange, safeDelay, null);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param onFail The runnable to run on fail (canceled teleport).
	 * @param onSuccess The runnable to run after a successful teleport.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @throws IllegalArgumentException if p or target is null.
	 */
	public DelayedTeleport(Player p, Location target, int delay, Runnable onFail, Runnable onSuccess, double safeRange, int safeDelay) {
		this(p, target, delay, null, null, onFail, onSuccess, safeRange, safeDelay, null);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param fail The message to send on fail (canceled teleport).
	 * @param success The message to send after a successful teleport.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @throws IllegalArgumentException if p or target is null.
	 */
	public DelayedTeleport(Player p, Location target, int delay, String fail, String success, double safeRange, int safeDelay) {
		this(p, target, delay, fail, success, null, null, safeRange, safeDelay, null);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param onFail The runnable to run on fail (canceled teleport).
	 * @param onSuccess The runnable to run after a successful teleport.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @param noCancel The list of players who can be near the player without disallowing use of safeDelay as delay. Can
	 *            be null and will not affect the status of the safeRange system if null or empty.
	 * @throws IllegalArgumentException if p or target is null.
	 */
	public DelayedTeleport(Player p, Location target, int delay, Runnable onFail, Runnable onSuccess, double safeRange, int safeDelay, List<UUID> noCancel) {
		this(p, target, delay, null, null, onFail, onSuccess, safeRange, safeDelay, noCancel);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param fail The message to send on fail (canceled teleport).
	 * @param success The message to send after a successful teleport.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @param noCancel The list of players who can be near the player without disallowing use of safeDelay as delay. Can
	 *            be null and will not affect the status of the safeRange system if null or empty.
	 * @throws IllegalArgumentException if p or target is null.
	 */
	public DelayedTeleport(Player p, Location target, int delay, String fail, String success, double safeRange, int safeDelay, List<UUID> noCancel) {
		this(p, target, delay, fail, success, null, null, safeRange, safeDelay, noCancel);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param fail The message to send on fail (canceled teleport).
	 * @param success The message to send after a successful teleport.
	 * @param onFail The runnable to run on fail (canceled teleport).
	 * @param onSuccess The runnable to run after a successful teleport.
	 * @throws IllegalArgumentException if p or target is null.
	 */
	public DelayedTeleport(Player p, Location target, int delay, String fail, String success, Runnable onFail, Runnable onSuccess) {
		this(p, target, delay, fail, success, onFail, onSuccess, -1, -1, null);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param onFail The runnable to run on fail (canceled teleport).
	 * @param onSuccess The runnable to run after a successful teleport.
	 * @throws IllegalArgumentException if p or target is null.
	 */
	public DelayedTeleport(Player p, Location target, int delay, Runnable onFail, Runnable onSuccess) {
		this(p, target, delay, null, null, onFail, onSuccess, -1, -1, null);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param fail The message to send on fail (canceled teleport).
	 * @param success The message to send after a successful teleport.
	 * @throws IllegalArgumentException if p or target is null.
	 */
	public DelayedTeleport(Player p, Location target, int delay, String fail, String success) {
		this(p, target, delay, fail, success, null, null, -1, -1, null);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param fail The message to send on fail (canceled teleport).
	 * @param success The message to send after a successful teleport.
	 * @throws IllegalArgumentException if p or target is null.
	 */
	public DelayedTeleport(Player p, Location target, int delay) {
		this(p, target, delay, null, null, null, null, -1, -1, null);
	}
	
	public void failed() {
		MetadataUtils.removeMetadata(p, DELAYED_TP_META, plugin);
		if (fail != null) p.sendMessage(fail);
		if (onFail != null) onFail.run();
	}
	
	/**
	 * Starts the delay of the delayed teleport. The checking for nearby players is done at this point, and after this
	 * only moving, hitting or taking damage can cancel the teleportation during the wait period
	 */
	public void start() {
		MetadataUtils.setFixedMetadata(p, DELAYED_TP_META, this, plugin);
		
		if (safeRange > 0 && safeDelay != -1 && safeDelay != delay) {
			for (Entity e : p.getNearbyEntities(safeRange, safeRange, safeRange)) {
				if (e instanceof Player) {
					Player x = (Player) e;
					if (x.getUniqueId().equals(p.getUniqueId())) continue;
					if (noCancel != null && noCancel.contains(x.getUniqueId())) continue;
					Bukkit.getServer().getScheduler().runTaskLater(plugin, delayer, delay);
					return;
				}
			}
			Bukkit.getServer().getScheduler().runTaskLater(plugin, delayer, safeDelay);
		} else Bukkit.getServer().getScheduler().runTaskLater(plugin, delayer, delay);
	}
	
	private Runnable delayer = new Runnable() {
		@Override
		public void run() {
			if (p.hasMetadata(DELAYED_TP_META)) {
				p.teleport(target);
				MetadataUtils.removeMetadata(p, DELAYED_TP_META, plugin);
				if (success != null) p.sendMessage(success);
				if (onSuccess != null) onSuccess.run();
			}
		}
	};
}
