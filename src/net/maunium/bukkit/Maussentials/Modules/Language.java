package net.maunium.bukkit.Maussentials.Modules;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.ChatColor;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.MauModule;
import net.maunium.bukkit.Maussentials.Utils.I18n;

/**
 * The language module for Maussentials allowing proper fallback handling. This class was just an
 * I18n container up till 0.2
 * 
 * @author Tulir293
 * @since 0.1
 */
public class Language implements MauModule {
	private Maussentials plugin;
	private String stag, errtag;
	private I18n i18n;
	private static I18n fallback = null;
	private boolean loaded = false;
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		try {
			i18n = I18n.createInstance(new File(this.plugin.getDataFolder(), "languages"), this.plugin.getConfig().getString("language", "en_US"), "en_US");
		} catch (IOException e) {
			plugin.die("Failed to initialize internationalization", e);
			return;
		}
		
		stag = i18n.translate("stag");
		errtag = i18n.translate("errtag");
		loaded = true;
	}
	
	/**
	 * Reload the fallback language using the given Maussentials instance.
	 */
	public static void reloadFallback(Maussentials plugin) {
		try {
			fallback = I18n.createInstance(plugin.getResource("languages/en_US.lang"));
		} catch (Throwable t) {
			plugin.getLogger().warning("Couldn't load fallback language. This can cause problems if Language module is unloaded.");
			t.printStackTrace();
		}
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
	
	/**
	 * Translate the given node with the given arguments and prepend the given prefix.
	 * 
	 * @return The translated node, or null if translation not found.
	 */
	private String translate(String node, String prefix, Object... replace) {
		if (loaded) return prefix + i18n.translate(node, replace);
		else return null;
	}
	
	/**
	 * Translate the given node with the given arguments and prepend the standard output tag. First
	 * try using the given Language instance and then use the fallback language.
	 * 
	 * @param lang The primary Language instance.
	 * @param node The node to translate.
	 * @param replace The arguments to give.
	 * @return The translated message with the standard output tag prepended or the node if no
	 *         translation was found.
	 */
	public static String translateStd(Language lang, String node, Object... replace) {
		if (lang != null && lang.isLoaded()) {
			String s = lang.translate(node, lang.stag, replace);
			if (s != null) return s;
		}
		if (fallback != null) {
			String stag = fallback.translate("stag");
			String s = fallback.translate(node, replace);
			if (stag != null && s != null) return stag + s;
			else if (s != null) return ChatColor.GRAY + s;
			else {
				System.out.println("[Maussentials] Couldn't find translation for " + node + " anywhere. (Arguments: " + Arrays.toString(replace) + ")");
				return node;
			}
		} else {
			System.out.println("[Maussentials] Couldn't find translation for " + node + " anywhere. (Arguments: " + Arrays.toString(replace) + ")");
			return node;
		}
	}
	
	/**
	 * Translate the given node with the given arguments and prepend the error output tag. First try
	 * using the given Language instance and then use the fallback language.
	 * 
	 * @param lang The primary Language instance.
	 * @param node The node to translate.
	 * @param replace The arguments to give.
	 * @return The translated message with the error output tag prepended or the node if no
	 *         translation was found.
	 */
	public static String translateErr(Language lang, String node, Object... replace) {
		if (lang != null && lang.isLoaded()) {
			String s = lang.translate(node, lang.errtag, replace);
			if (s != null) return s;
		}
		if (fallback != null) {
			String errtag = fallback.translate("errtag");
			String s = fallback.translate(node, replace);
			if (errtag != null && s != null) return errtag + s;
			else if (s != null) return ChatColor.RED + s;
			else {
				System.out.println("[Maussentials] Couldn't find translation for " + node + " anywhere. (Arguments: " + Arrays.toString(replace) + ")");
				return node;
			}
		} else {
			System.out.println("[Maussentials] Couldn't find translation for " + node + " anywhere. (Arguments: " + Arrays.toString(replace) + ")");
			return node;
		}
	}
	
	/**
	 * Translate the given node with the given arguments. First try using the given Language
	 * instance and then use the fallback language.
	 * 
	 * @param lang The primary Language instance.
	 * @param node The node to translate.
	 * @param replace The arguments to give.
	 * @return The translated message or the node if no translation was found.
	 */
	public static String translatePlain(Language lang, String node, Object... replace) {
		if (lang != null && lang.isLoaded()) {
			String s = lang.translate(node, "", replace);
			if (s != null) return s;
		}
		if (fallback != null) {
			String s = fallback.translate(node, replace);
			if (s != null) return s;
			else {
				System.out.println("[Maussentials] Couldn't find translation for " + node + " anywhere. (Arguments: " + Arrays.toString(replace) + ")");
				return node;
			}
		} else {
			System.out.println("[Maussentials] Couldn't find translation for " + node + " anywhere. (Arguments: " + Arrays.toString(replace) + ")");
			return node;
		}
	}
}
