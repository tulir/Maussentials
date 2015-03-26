package net.maunium.bukkit.Maussentials.Modules.Util;

import net.maunium.bukkit.Maussentials.Utils.MauCommandExecutor;

/**
 * The base of a MauModule which is mainly used for command executors. Basically combines MauModule and MauCommandExecutor and doesn't add anything on its own.
 * 
 * @author Tulir293
 * @since 0.1
 */
public interface CommandModule extends MauModule, MauCommandExecutor {}
