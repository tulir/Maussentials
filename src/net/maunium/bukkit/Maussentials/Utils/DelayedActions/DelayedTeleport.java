package net.maunium.bukkit.Maussentials.Utils.DelayedActions;

import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * An extension to Delayed Actions. Teleporting instead of running a runnable.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class DelayedTeleport extends DelayedAction {
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param target The location to teleport the player to on success.
	 * @param onSuccess The message to send after a successful teleport.
	 * @param onFail The message to send after a canceled teleport.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @param noCancel The list of players who can be near the player without disallowing use of safeDelay as delay. Can
	 *            be null and will not affect the status of the safeRange system if null or empty.
	 * @throws IllegalArgumentException if p is null.
	 */
	public DelayedTeleport(Player p, int delay, Location target, String onSuccess, String onFail, double safeRange, int safeDelay, List<UUID> noCancel) {
		super(p, delay, new MessageRunnable(p, onFail), new TeleportRunnable(p, target, onSuccess), safeRange, safeDelay, noCancel);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param target The location to teleport the player to on success.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @throws IllegalArgumentException if p is null.
	 */
	public DelayedTeleport(Player p, int delay, Location target, double safeRange, int safeDelay) {
		this(p, delay, target, null, null, safeRange, safeDelay, null);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param target The location to teleport the player to on success.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @param noCancel The list of players who can be near the player without disallowing use of safeDelay as delay. Can
	 *            be null and will not affect the status of the safeRange system if null or empty.
	 * @throws IllegalArgumentException if p is null.
	 */
	public DelayedTeleport(Player p, int delay, Location target, double safeRange, int safeDelay, List<UUID> noCancel) {
		this(p, delay, target, null, null, safeRange, safeDelay, noCancel);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param target The location to teleport the player to on success.
	 */
	public DelayedTeleport(Player p, int delay, Location target) {
		this(p, delay, target, null, null, -1, -1, null);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param target The location to teleport the player to on success.
	 * @param onSuccess The message to send after a successful teleport.
	 * @param onFail The message to send after a canceled teleport.
	 * @param safeRange If there are no players in the range or all the players in the range are in the noCancel array,
	 *            the player will use the safeDelay instead for delay for the delay of the teleportation. Setting to 0
	 *            or less disables the safeRange system.
	 * @param safeDelay The delay in ticks to use instead if all the players in the safeRange are in the noCancel list.
	 *            Setting to the same as delay or -1 disables the safeRange system.
	 * @throws IllegalArgumentException if p is null.
	 */
	public DelayedTeleport(Player p, int delay, Location target, String onSuccess, String onFail, double safeRange, int safeDelay) {
		this(p, delay, target, onSuccess, onFail, safeRange, safeDelay, null);
	}
	
	/**
	 * Creates an Delayed Teleport instance. All the non-primitive arguments except p and target can be null.
	 * 
	 * @param p The player to teleport.
	 * @param delay The delay in ticks to use for teleportation by default. This may be overriden by safeDelay if
	 *            certain conditions are met.
	 * @param target The location to teleport the player to on success.
	 * @param onSuccess The message to send after a successful teleport.
	 * @param onFail The message to send after a canceled teleport.
	 * @throws IllegalArgumentException if p is null.
	 */
	public DelayedTeleport(Player p, int delay, Location target, String onSuccess, String onFail) {
		this(p, delay, target, onSuccess, onFail, -1, -1, null);
	}
	
	public static class TeleportRunnable implements Runnable {
		private Player p;
		private Location target;
		private String msg;
		
		public TeleportRunnable(Player p, Location target, String msg) {
			this.p = p;
			this.target = target;
			this.msg = msg;
		}
		
		@Override
		public void run() {
			p.teleport(target);
			if (msg != null) p.sendMessage(msg);
		}
	};
	
	public static class MessageRunnable implements Runnable {
		private Player p;
		private String msg;
		
		public MessageRunnable(Player p, String msg) {
			this.p = p;
			this.msg = msg;
		}
		
		@Override
		public void run() {
			if (msg != null) p.sendMessage(msg);
		}
	};
}
