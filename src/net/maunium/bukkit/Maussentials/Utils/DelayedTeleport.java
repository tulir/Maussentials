package net.maunium.bukkit.Maussentials.Utils;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;

public class DelayedTeleport {
	public static final String DELAYED_TP_META = "MaussentialsDelayedTeleportInfo";
	private final Player p;
	private final Location target;
	private final int delay, safeDelay;
	private final String fail, success;
	private final double safeRange;
	private final List<UUID> noCancel;
	
	/**
	 * Creates a delayed teleport with all the default options.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param fail The message to send on fail (canceled teleport). Can be null.
	 * @param success The message to send after successful teleport. Can be null.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 * @param noCancel The list of players who can be near the player without disallowing use of safeDelay as delay.
	 */
	public DelayedTeleport(Player p, Location target, int delay, String fail, String success, double safeRange, int safeDelay, List<UUID> noCancel) {
		this.p = p;
		this.target = target;
		this.delay = delay;
		this.fail = fail;
		this.success = success;
		this.safeRange = safeRange;
		this.safeDelay = safeDelay;
		this.noCancel = noCancel;
	}
	
	/**
	 * Creates a delayed teleport without the safe range and team systems.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param fail The message to send on fail (canceled teleport). Can be null.
	 * @param success The message to send after successful teleport. Can be null.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 * @param noCancel The list of players who can be near the player without disallowing use of safeDelay as delay.
	 */
	public DelayedTeleport(Player p, Location target, int delay, String fail, String success) {
		this.p = p;
		this.target = target;
		this.delay = delay;
		this.fail = fail;
		this.success = success;
		this.safeRange = -1;
		this.safeDelay = -1;
		this.noCancel = null;
	}
	
	/**
	 * Creates a delayed teleport without delays (only teleports if safe range applies)
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param fail The message to send on fail (canceled teleport). Can be null.
	 * @param success The message to send after successful teleport. Can be null.
	 * @param safeRange If there are players in the safeRange that not in the noCancel list, the player will not be
	 *            teleported.
	 * @param noCancel The list of players who can be near the player without disallowing teleportation.
	 */
	public DelayedTeleport(Player p, Location target, String fail, String success, double safeRange, List<UUID> noCancel) {
		this.p = p;
		this.target = target;
		this.delay = -1;
		this.fail = fail;
		this.success = success;
		this.safeRange = safeRange;
		this.safeDelay = -1;
		this.noCancel = noCancel;
	}
	
	/**
	 * Creates a delayed teleport with all the default options but with no fail/success messages.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 * @param noCancel The list of players who can be near the player without disallowing use of safeDelay as delay.
	 */
	public DelayedTeleport(Player p, Location target, int delay, double safeRange, int safeDelay, List<UUID> noCancel) {
		this.p = p;
		this.target = target;
		this.delay = delay;
		this.fail = null;
		this.success = null;
		this.safeRange = safeRange;
		this.safeDelay = safeDelay;
		this.noCancel = noCancel;
	}
	
	/**
	 * Creates a delayed teleport without the safe range and team systems but with no fail/success messages.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 * @param noCancel The list of players who can be near the player without disallowing use of safeDelay as delay.
	 */
	public DelayedTeleport(Player p, Location target, int delay) {
		this.p = p;
		this.target = target;
		this.delay = delay;
		this.fail = null;
		this.success = null;
		this.safeRange = -1;
		this.safeDelay = -1;
		this.noCancel = null;
	}
	
	/**
	 * Creates a delayed teleport without delays (only teleports if safe range applies) but with no fail/success
	 * messages.
	 * 
	 * @param p The player to teleport.
	 * @param target The target location.
	 * @param safeRange If there are players in the safeRange that not in the noCancel list, the player will not be
	 *            teleported.
	 * @param noCancel The list of players who can be near the player without disallowing teleportation.
	 */
	public DelayedTeleport(Player p, Location target, double safeRange, List<UUID> noCancel) {
		this.p = p;
		this.target = target;
		this.delay = -1;
		this.fail = null;
		this.success = null;
		this.safeRange = safeRange;
		this.safeDelay = -1;
		this.noCancel = noCancel;
	}
	
	public void failed() {
		if (fail != null) p.sendMessage(fail);
		MetadataUtils.removeMetadata(p, DELAYED_TP_META, Maussentials.getInstance());
	}
	
	/**
	 * Starts the delay of the delayed teleport. The checking for nearby players is done at this point, and after this
	 * only moving, hitting or taking damage can cancel the teleportation during the wait period
	 */
	public void start() {
		MetadataUtils.setFixedMetadata(p, DELAYED_TP_META, this, Maussentials.getInstance());
		
		for (Entity e : p.getNearbyEntities(safeRange, safeRange, safeRange)) {
			if (e instanceof Player) {
				Player x = (Player) e;
				if (x.getUniqueId().equals(p.getUniqueId())) continue;
				if (noCancel != null && noCancel.contains(x)) continue;
				Bukkit.getServer().getScheduler().runTaskLater(Maussentials.getInstance(), delayer, delay);
				return;
			}
		}
		
		Bukkit.getServer().getScheduler().runTaskLater(Maussentials.getInstance(), delayer, safeDelay);
	}
	
	private Runnable delayer = new Runnable() {
		@Override
		public void run() {
			if (p.hasMetadata(DELAYED_TP_META)) {
				p.teleport(target);
				if (success != null) p.sendMessage(success);
				MetadataUtils.removeMetadata(p, DELAYED_TP_META, Maussentials.getInstance());
			}
		}
	};
}
