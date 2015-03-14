package net.maunium.bukkit.Maussentials.Utils.DelayedActions;

import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.MetadataUtils;

/**
 * A simple system for delayed actions. Also has option to allow faster (or slower) action running if nobody is within a
 * given range and you can even add a list of exceptions to who can be in that range.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class DelayedAction {
	private final Maussentials plugin;
	public static final String DELAYED_ACTION_META = "MaussentialsDelayedActionInfo";
	private final Player p;
	private final Runnable onSuccess, onFail;
	private final int delay, safeDelay;
	private final double safeRange;
	private final List<UUID> noCancel;
	
	/**
	 * Creates an Delayed Action instance. All the non-primitive arguments except p can be null.
	 * 
	 * @param p The player to teleport.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param onFail The runnable to run after a canceled teleport.
	 * @param onSuccess The runnable to run after a successful teleport.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @param noCancel The list of players who can be near the player without disallowing use of safeDelay as delay. Can
	 *            be null and will not affect the status of the safeRange system if null or empty.
	 * @throws IllegalArgumentException if p is null.
	 */
	public DelayedAction(Player p, int delay, Runnable onFail, Runnable onSuccess, double safeRange, int safeDelay, List<UUID> noCancel) {
		this.plugin = Maussentials.getInstance();
		if (p == null) throw new IllegalArgumentException("Player can't be null!");
		this.p = p;
		this.delay = delay;
		this.onFail = onFail;
		this.onSuccess = onSuccess;
		this.safeRange = safeRange;
		this.safeDelay = safeDelay;
		this.noCancel = noCancel;
	}
	
	/**
	 * Creates an Delayed Action instance. All the non-primitive arguments except p can be null.
	 * 
	 * @param p The player to teleport.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param onFail The runnable to run after a canceled teleport.
	 * @param onSuccess The runnable to run after a successful teleport.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @throws IllegalArgumentException if p is null.
	 */
	public DelayedAction(Player p, int delay, Runnable onFail, Runnable onSuccess, double safeRange, int safeDelay) {
		this(p, delay, onFail, onSuccess, safeRange, safeDelay, null);
	}
	
	/**
	 * Creates an Delayed Action instance. All the non-primitive arguments except p can be null.
	 * 
	 * @param p The player to teleport.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param onFail The runnable to run after a canceled teleport.
	 * @param onSuccess The runnable to run after a successful teleport.
	 * @throws IllegalArgumentException if p is null.
	 */
	public DelayedAction(Player p, int delay, Runnable onFail, Runnable onSuccess) {
		this(p, delay, onFail, onSuccess, -1, -1, null);
	}
	
	public void failed() {
		MetadataUtils.removeMetadata(p, DELAYED_ACTION_META, plugin);
		if (onFail != null) onFail.run();
	}
	
	/**
	 * Starts the delay of the delayed teleport. The checking for nearby players is done at this point, and after this
	 * only moving, hitting or taking damage can cancel the teleportation during the wait period
	 */
	public void start() {
		MetadataUtils.setFixedMetadata(p, DELAYED_ACTION_META, this, plugin);
		
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
			if (p.hasMetadata(DELAYED_ACTION_META)) {
				MetadataUtils.removeMetadata(p, DELAYED_ACTION_META, plugin);
				if (onSuccess != null) onSuccess.run();
			}
		}
	};
}
