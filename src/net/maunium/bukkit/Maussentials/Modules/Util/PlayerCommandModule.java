package net.maunium.bukkit.Maussentials.Modules.Util;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Utils.IngameMauCommandExecutor;

/**
 * A module mainly used for a command executor of a in-game-only command.
 * 
 * @author Tulir293
 * @since 0.1
 */
public interface PlayerCommandModule extends IngameMauCommandExecutor {
	@Override
	public default String getErrorMessage() {
		return Maussentials.getInstance().translateErr("must-be-ingame");
	}
}
