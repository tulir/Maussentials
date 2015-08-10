package net.maunium.bukkit.Maussentials.Modules.Chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

@SerializableAs("MaussentialsChatChannel")
public class MauChannel implements Serializable, ConfigurationSerializable {
	private static final long serialVersionUID = 5303868495770244103L;
	private Set<UUID> invited, users, managers;
	private String name, password;
	private boolean restricted;
	
	public MauChannel(UUID creator, String name) {
		invited = new HashSet<UUID>();
		users = new HashSet<UUID>();
		managers = new HashSet<UUID>();
		password = null;
		restricted = true;
		this.name = name;
		users.add(creator);
		managers.add(creator);
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean checkPassword(String password) {
		return this.password == null ? restricted : this.password.equals(password);
	}
	
	/**
	 * Remove the channel password.
	 * 
	 * @return True if there is no longer a password, false if there never was a password.
	 */
	public boolean removePassword() {
		if (password == null) return false;
		else password = null;
		return true;
	}
	
	/**
	 * Restrict joining to this channel.
	 * 
	 * @return True if the channel now restricted, false if it was already restricted.
	 */
	public boolean restrict() {
		if (restricted) return false;
		else restricted = true;
		return true;
	}
	
	/**
	 * Remove the join restrictions from this channel.
	 * 
	 * @return True if the channel is no longer restricted, false if it was never restricted.
	 */
	public boolean unrestrict() {
		if (!restricted) return false;
		else restricted = false;
		return true;
	}
	
	/**
	 * @return If joining to this channel is restricted with invitations and/or password only
	 */
	public boolean isRestricted() {
		return restricted;
	}
	
	public Set<UUID> getPlayers() {
		return users;
	}
	
	public void join(Player p) {
		users.add(p.getUniqueId());
	}
	
	public void leave(Player p) {
		users.remove(p.getUniqueId());
		if (managers.contains(p.getUniqueId())) managers.remove(p.getUniqueId());
	}
	
	public boolean isUser(Player p) {
		return users.contains(p.getUniqueId());
	}
	
	public boolean isInvited(Player p) {
		return invited.contains(p.getUniqueId());
	}
	
	public MauChannel(Map<String, Object> serialized) {
		if (!serialized.containsKey("users")) throw new IllegalArgumentException("Serialized map did not contain the \"users\" field");
		if (!serialized.containsKey("managers")) throw new IllegalArgumentException("Serialized map did not contain the \"managers\" field");
		if (!serialized.containsKey("restricted")) throw new IllegalArgumentException("Serialized map did not contain the \"restricted\" field");
		if (!serialized.containsKey("name")) throw new IllegalArgumentException("Serialized map did not contain the \"name\" field");
		
		try {
			users = new HashSet<UUID>(((List<?>) serialized.get("users")).stream().map(e -> (UUID) e).collect(Collectors.toList()));
		} catch (Throwable t) {
			throw new IllegalArgumentException("Failed to read users!", t);
		}
		
		try {
			managers = new HashSet<UUID>(((List<?>) serialized.get("managers")).stream().map(e -> (UUID) e).collect(Collectors.toList()));
		} catch (Throwable t) {
			throw new IllegalArgumentException("Failed to read managers!", t);
		}
		
		if (serialized.containsKey("invited")) {
			try {
				invited = new HashSet<UUID>(((List<?>) serialized.get("invited")).stream().map(e -> (UUID) e).collect(Collectors.toList()));
			} catch (Throwable t) {
				throw new IllegalArgumentException("Failed to read invited UUIDs!", t);
			}
		} else invited = new HashSet<UUID>();
		
		restricted = serialized.get("restricted").toString().equalsIgnoreCase("false");
		name = serialized.get("name").toString();
		
		if (serialized.containsKey("password")) password = serialized.get("password").toString();
	}
	
	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> serialized = new HashMap<String, Object>();
		
		serialized.put("users", new ArrayList<UUID>(users));
		serialized.put("managers", new ArrayList<UUID>(managers));
		serialized.put("restricted", restricted);
		serialized.put("name", name);
		
		if (password != null) serialized.put("password", password);
		if (!invited.isEmpty()) serialized.put("invited", invited);
		
		return serialized;
	}
	
	public static MauChannel deserialize(Map<String, Object> serialized) {
		return new MauChannel(serialized);
	}
	
	public static MauChannel valueOf(Map<String, Object> serialized) {
		return new MauChannel(serialized);
	}
}