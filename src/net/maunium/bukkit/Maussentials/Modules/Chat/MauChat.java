package net.maunium.bukkit.Maussentials.Modules.Chat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;

public class MauChat implements MauModule {
	private Maussentials plugin;
	private YamlConfiguration conf = new YamlConfiguration();
	private Map<String, String> groupFormats = new HashMap<String, String>();
	private Map<UUID, String> playerFormats = new HashMap<UUID, String>();
	private boolean loaded;
	
	@Override
	public void load(Maussentials plugin) {
		try {
			conf.load(new File(plugin.getDataFolder(), "chat.yml"));
		} catch (Exception e) {
			plugin.getLogger().severe("Failed to load chat config:");
			e.printStackTrace();
		}
		
		for (Entry<String, Object> e : conf.getConfigurationSection("group-formats").getValues(true).entrySet()) {
			groupFormats.put(e.getKey(), e.getValue().toString());
		}
		for (Entry<String, Object> e : conf.getConfigurationSection("player-formats").getValues(true).entrySet()) {
			playerFormats.put(UUID.fromString(e.getKey()), e.getValue().toString());
		}
		loaded = true;
	}
	
	@Override
	public void unload() {
		try {
			conf.save(new File(plugin.getDataFolder(), "chat.yml"));
		} catch (Exception e) {
			plugin.getLogger().severe("Failed to save chat config:");
			e.printStackTrace();
		}
		loaded = false;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
