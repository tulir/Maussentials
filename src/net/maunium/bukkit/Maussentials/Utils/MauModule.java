package net.maunium.bukkit.Maussentials.Utils;

import net.maunium.bukkit.Maussentials.Maussentials;

/**
 * The base of every module in Maussentials.
 * 
 * @author Tulir293
 * @since 0.1
 */
public interface MauModule {
	/**
	 * Reloads all the configurations and everything else that needs reloading.
	 */
	public void reload();
	
	/**
	 * Initializes the module and loads all the configurations and everything else that needs to be loaded.
	 * 
	 * @param plugin The hosting instance of Maussentials.
	 */
	public void initialize(Maussentials plugin);
}
