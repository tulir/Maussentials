package net.maunium.bukkit.Maussentials.Modules.Bans;

import net.maunium.bukkit.Maussentials.Maussentials;

public class StandardBan {
	private static Maussentials plugin = Maussentials.getInstance();
	private final String reason;
	private final long timeout;
	
	private StandardBan(String reason, long timeout) {
		this.reason = reason;
		this.timeout = timeout;
	}
	
	public String getReason() {
		return reason;
	}
	
	public long getTimeout() {
		return timeout;
	}
	
	public boolean isPermanent() {
		return getTimeout() == -1;
	}
	
	public static StandardBan create(String reason, String timeout) {
		if (plugin == null) plugin = Maussentials.getInstance();
		if (timeout.equalsIgnoreCase("permanent")) return new StandardBan(reason, -1);
		
		long time = 0;
		for (String s : timeout.split(" ")) {
			if (s == null || s.length() < 2) plugin.getServer().getConsoleSender().sendMessage(plugin.translateErr("bans.error.stdban.invalidtime", s));
			else {
				try {
					if (s.endsWith("ms")) time += Long.parseLong(s.substring(0, s.length() - 2));
					else {
						long timetemp = Long.parseLong(s.substring(0, s.length() - 1));
						if (s.endsWith("s")) time += timetemp * 1000;
						else if (s.endsWith("m")) time += timetemp * 1000 * 60;
						else if (s.endsWith("h")) time += timetemp * 1000 * 60 * 60;
						else if (s.endsWith("d")) time += timetemp * 1000 * 60 * 60 * 24;
						else if (s.endsWith("W")) time += timetemp * 1000 * 60 * 60 * 24 * 7;
						else if (s.endsWith("M")) time += timetemp * 1000 * 60 * 60 * 24 * 30;
						else if (s.endsWith("Y")) time += timetemp * 1000 * 60 * 60 * 24 * 365;
						else if (s.endsWith("D")) time += timetemp * 1000 * 60 * 60 * 24 * 365 * 10;
						else if (s.endsWith("C")) time += timetemp * 1000 * 60 * 60 * 24 * 365 * 100;
						else plugin.getServer().getConsoleSender().sendMessage(plugin.translateErr("bans.error.stdban.invalidtime", s));
					}
				} catch (Throwable t) {
					plugin.getServer().getConsoleSender().sendMessage(plugin.translateErr("bans.error.stdban.invalidtime", s));
				}
			}
		}
		return new StandardBan(reason, time);
	}
	
	public static StandardBan create(String reason, long timeout) {
		if (timeout < 0) timeout = -1;
		return new StandardBan(reason, timeout);
	}
}
