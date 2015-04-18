package net.maunium.bukkit.Maussentials.Modules.MauInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;

import net.maunium.bukkit.Maussentials.Utils.ChatFormatter;

public class InfoEntry {
	private String[] content, aliases;
	private String name;
	private Permission permission;
	
	public InfoEntry(File f) throws IOException, FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(f));
		String s, permission = "";
		List<String> content = new ArrayList<String>(), aliases = new ArrayList<String>();
		while ((s = br.readLine()) != null) {
			if (s.startsWith("#!")) {
				if (s.startsWith("#!alias ")) {
					s = s.substring(8).trim();
					if (s.length() > 0) {
						if (s.contains(", ")) aliases.addAll(Arrays.asList(s.split(", ")));
						if (s.contains(",")) aliases.addAll(Arrays.asList(s.split(",")));
						else aliases.add(s);
					}
				} else if (s.startsWith("#!permission ")) permission = s.substring(13).trim();
				continue;
			} else if (s.startsWith("#")) continue;
			else if (s.startsWith("\\#")) s = s.substring(1);
			content.add(ChatFormatter.formatAll(s));
		}
		br.close();
		if (permission.length() > 0) {
			this.permission = new Permission(permission);
			if (Bukkit.getPluginManager().getPermission(permission) == null) Bukkit.getServer().getPluginManager().addPermission(this.permission);
		}
		this.content = content.toArray(new String[0]);
		this.aliases = aliases.toArray(new String[0]);
	}
	
	public boolean hasPermission(Permissible p) {
		return permission != null ? p.hasPermission(permission) : true;
	}
	
	public boolean isAlias(String s) {
		if (s.equalsIgnoreCase(name)) return true;
		else if (aliases.length > 0) {
			for (String ss : aliases)
				if (ss.equalsIgnoreCase(s)) return true;
		}
		return false;
	}
	
	public void sendMessage(Player p) {
		String[] ss = content.clone();
		for (int i = 0; i < ss.length; i++) {
			// @mauformat=off
			ss[i] = ss[i]
					.replace("{NAME}", p.getName())
					.replace("{DISPLAYNAME}", p.getDisplayName())
					.replace("{ONLINE}", Integer.toString(Bukkit.getOnlinePlayers().size()))
					.replace("{MAXPLAYERS}", Integer.toString(Bukkit.getMaxPlayers()));
			// @mauformat=on
		}
	}
}
