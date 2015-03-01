package net.maunium.bukkit.Maussentials.Modules.Util;

import net.maunium.bukkit.Maussentials.Maussentials;

/**
 * The base of every module in Maussentials.
 * 
 * @author Tulir293
 * @since 0.1
 */
public interface MauModule {
	/**
	 * Initializes the module and loads all the configurations and everything else that needs to be loaded.
	 * 
	 * @param plugin The hosting instance of Maussentials.
	 */
	public void load(Maussentials plugin);
	
	/**
	 * Unload the MauModule.
	 */
	public void unload();
}
