package net.maunium.bukkit.Maussentials.Modules.Chat;

import java.io.File;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import net.milkbowl.vault.permission.Permission;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

public class MauChat implements MauModule {
	public static final String CURRENT_CHANNEL = "MaussentialsChatCurrentChannel";
	protected Permission vault;
	private File channelsFile;
	private YamlConfiguration conf;
	private Maussentials plugin;
	private Map<String, MauChannel> channels;
	private Map<String, String> formats;
	private String defaultFormat;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		
		channelsFile = new File(plugin.getDataFolder(), "channels.yml");
		ConfigurationSerialization.registerClass(MauChannel.class);
		try {
			conf = YamlConfiguration.loadConfiguration(channelsFile);
			for (Entry<String, Object> e : conf.getConfigurationSection("channels").getValues(true).entrySet()) {
				try {
					if (e.getValue() instanceof MauChannel) channels.put(e.getKey(), (MauChannel) e.getValue());
					else plugin.getLogger().severe("Failed to load channel " + e.getKey());
				} catch (Throwable t) {
					plugin.getLogger().severe("Failed to load channel " + e.getKey());
					t.printStackTrace();
				}
			}
		} catch (Throwable t) {
			plugin.getLogger().severe("Failed to load channels");
			t.printStackTrace();
		}
		
		vault = plugin.getServer().getServicesManager().getRegistration(Permission.class).getProvider();
		
		Map<String, Object> frmts = plugin.getConfig().getConfigurationSection("chat-format.groups").getValues(false);
		for (Entry<String, Object> e : frmts.entrySet())
			formats.put(e.getKey().toLowerCase(Locale.ENGLISH), e.getValue().toString());
		defaultFormat = plugin.getConfig().getString("chat-format.default");
		
		loaded = true;
	}
	
	@Override
	public void unload() {
		try {
			for (Entry<String, MauChannel> e : channels.entrySet()) {
				try {
					conf.set(e.getKey(), e.getValue());
				} catch (Throwable t) {
					plugin.getLogger().severe("Failed to save channel " + e.getKey());
					t.printStackTrace();
				}
			}
			conf.save(channelsFile);
		} catch (Throwable t) {
			plugin.getLogger().severe("Failed to save channels");
			t.printStackTrace();
		}
		ConfigurationSerialization.unregisterClass(MauChannel.class);
		
		formats.clear();
		channels.clear();
		defaultFormat = null;
		vault = null;
		conf = null;
		channels = null;
		plugin = null;
		loaded = false;
	}
	
	public String getGroupFormat(String group) {
		group = group.toLowerCase(Locale.ENGLISH);
		if (formats.containsKey(group)) return formats.get(group);
		else return defaultFormat;
	}
	
	public boolean containsChannel(String name) {
		return channels.containsKey(name.toLowerCase(Locale.ENGLISH));
	}
	
	public MauChannel getChannel(String name) {
		return channels.get(name.toLowerCase(Locale.ENGLISH));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}