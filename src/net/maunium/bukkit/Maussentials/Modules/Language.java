package net.maunium.bukkit.Maussentials.Modules;

import java.io.File;
import java.io.IOException;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;
import net.maunium.bukkit.Maussentials.Utils.I18n;
import net.maunium.bukkit.Maussentials.Utils.I18n.I15r;

public class Language implements I15r, MauModule {
	private Maussentials plugin;
	private String stag, errtag;
	private I18n i18n;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		try {
			this.i18n = I18n.createInstance(new File(this.plugin.getDataFolder(), "languages"), this.plugin.getConfig().getString("language"));
		} catch (IOException e) {
			plugin.die("Failed to initialize internationalization", e);
			return;
		}
		stag = i18n.translate("stag");
		errtag = i18n.translate("errtag");
		loaded = true;
	}
	
	@Override
	public void unload() {
		plugin = null;
		loaded = false;
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	@Override
	public String translate(String node, Object... replace) {
		return i18n.translate(node, replace);
	}
	
	public String translateStd(String node, Object... replace) {
		if (loaded) return stag + translate(node, replace);
		else return errtag + "Couldn't translate " + node + ": I18n not enabled.";
	}
	
	public String translateErr(String node, Object... replace) {
		if (loaded) return errtag + translate(node, replace);
		else return errtag + "Couldn't translate " + node + ": I18n not enabled.";
	}
}
