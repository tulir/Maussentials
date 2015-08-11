package net.maunium.bukkit.Maussentials.Modules.Chat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;

import net.milkbowl.vault.permission.Permission;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

/**
 * Chat manager for Maussentials.
 * 
 * @author Tulir293
 * @since 0.3
 */
public class MauChat implements MauModule {
//	private Maussentials plugin;
	protected Permission vault;
	
	private File confFile;
	private YamlConfiguration conf;
	
	private Map<String, String> formats;
	private Map<UUID, String> playerFormats;
	private String defaultFormat;
	private List<Replaceable> replace;
	
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
//		this.plugin = plugin;
		
		// Register Replaceable as a thing that can be de/serialized from/to config.
		ConfigurationSerialization.registerClass(Replaceable.class);
		
		// Save default config
		plugin.saveResource("chat.yml", false);
		// Load config
		confFile = new File(plugin.getDataFolder(), "chat.yml");
		conf = YamlConfiguration.loadConfiguration(confFile);
		
		// Load vault permissions.
		vault = plugin.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
		
		// Get group formats from configuration.
		Map<String, Object> map1 = conf.getConfigurationSection("group-formats").getValues(false);
		for (Entry<String, Object> e : map1.entrySet())
			formats.put(e.getKey().toLowerCase(Locale.ENGLISH), e.getValue().toString());
			
		// Get player formats from configuration.
		Map<String, Object> map2 = conf.getConfigurationSection("player-formats").getValues(false);
		for (Entry<String, Object> e : map2.entrySet())
			playerFormats.put(UUID.fromString(e.getKey()), e.getValue().toString());
			
		// Get chat replace objects from configuration.
		List<?> list1 = conf.getList("chat-replace");
		replace = new ArrayList<Replaceable>();
		for (Object o : list1)
			if (o instanceof Replaceable) replace.add((Replaceable) o);
		Collections.sort(replace);
		
		// Get the default config.
		defaultFormat = conf.getString("chat-format.default");
		
		plugin.getServer().getPluginManager().registerEvents(new ChatListener(this), plugin);
		
		loaded = true;
	}
	
	@Override
	public void unload() {
		for (Entry<String, String> e : formats.entrySet())
			conf.set("format-groups." + e.getKey().toLowerCase(Locale.ENGLISH), e.getValue());
			
		Collections.sort(replace);
		conf.set("chat-replace", replace);
		
		try {
			conf.save(confFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ConfigurationSerialization.unregisterClass(Replaceable.class);
		defaultFormat = null;
		formats.clear();
		formats = null;
		vault = null;
		conf = null;
//		plugin = null;
		loaded = false;
	}
	
	/**
	 * Get the chat foramt for the given group.
	 * 
	 * @return The format for the group, or {@link #defaultFormat} if no specific format found for
	 *         the group.
	 */
	public String getGroupFormat(String group) {
		group = group.toLowerCase(Locale.ENGLISH);
		if (formats.containsKey(group)) return formats.get(group);
		else return defaultFormat;
	}
	
	public String getPlayerFormat(Player p) {
		if (playerFormats.containsKey(p.getUniqueId())) return playerFormats.get(p.getUniqueId());
		else return getGroupFormat(vault.getPrimaryGroup(p));
	}
	
	/**
	 * Add the given Replaceable to the list and then sort the list.
	 */
	public void addReplaceable(Replaceable r) {
		replace.add(r);
		Collections.sort(replace);
	}
	
	/**
	 * Remove the Replaceable in the given index.
	 */
	public void removeReplaceable(int i) {
		replace.remove(i);
		Collections.sort(replace);
	}
	
	/**
	 * Filter the message by calling the {@link Replaceable#replaceIn(String)} method for each
	 * Replaceable in the list.
	 * 
	 * @param message The message to filter.
	 * @return The filtered message.
	 */
	public String filter(String message) {
		for (Replaceable r : replace)
			message = r.replaceIn(message);
		return message;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}