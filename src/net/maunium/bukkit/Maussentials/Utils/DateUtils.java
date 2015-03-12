package net.maunium.bukkit.Maussentials.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.BooleanUtils;

import net.maunium.bukkit.Maussentials.Maussentials;

/**
 * A basic class for a few default ways to format milliseconds into human-readable time.
 * 
 * @author Tulir293
 * @since 0.1
 */
public class DateUtils {
	private static final SimpleDateFormat def = new SimpleDateFormat("dd.MM.yyyy ss:mm:HH");
	public static final int MODE_IN = 0, MODE_FOR = 1;
	
	/**
	 * Get the breakdown of millis to days, hours, minutes and seconds.
	 * 
	 * @param millis The amount of milliseconds to break down.
	 * @param mode Use {@link #MODE_IN} or {@link #MODE_FOR}. They define which translations to use after the amount of
	 *            the time unit. Defaults to {@link #MODE_IN}.
	 * @return
	 */
	public static String getDurationBreakdown(long millis, int mode) {
		Maussentials m = Maussentials.getInstance();
		if (millis < 0) return "never";
		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
		
		String md = mode == 1 ? "for" : "in";
		
		StringBuilder sb = new StringBuilder(48);
		if (days > 0) {
			sb.append(days);
			sb.append(" ");
			sb.append(m.translatePlain("date." + md + ".days"));
			
			if (hours > 0 || minutes > 0 || seconds > 0) {
				if (BooleanUtils.xor(new boolean[] { hours > 0, minutes > 0, seconds > 0 })) {
					sb.append(" ");
					sb.append(m.translatePlain("date.and"));
				} else sb.append(m.translatePlain("date.comma"));
			}
			sb.append(" ");
		}
		if (hours > 0) {
			sb.append(hours);
			sb.append(" ");
			sb.append(m.translatePlain("date." + md + ".hours"));
			
			if (minutes > 0 || seconds > 0) {
				if (BooleanUtils.xor(new boolean[] { minutes > 0, seconds > 0 })) {
					sb.append(" ");
					sb.append(m.translatePlain("date.and"));
				} else sb.append(m.translatePlain("date.comma"));
			}
			sb.append(" ");
		}
		if (minutes > 0) {
			sb.append(minutes);
			sb.append(" ");
			sb.append(m.translatePlain("date." + md + ".minutes"));
			
			if (seconds > 0) sb.append("date.and");
			sb.append(" ");
		}
		if (seconds > 0) {
			sb.append(seconds);
			sb.append(" ");
			sb.append(m.translatePlain("date." + md + ".seconds"));
			sb.append(" ");
		}
		sb.deleteCharAt(sb.length() - 1);
		if (sb.length() == 0) sb.append(m.translatePlain("date." + md + ".none"));
		return sb.toString();
	}
	
	/**
	 * Format the millisecond timestamp to a human-readable timestamp.
	 * 
	 * @param millis The timestamp.
	 * @return A human-readable timestamp in the default format used in Maunium products.
	 */
	public static String format(long millis) {
		return def.format(new Date(millis));
	}
	
	/**
	 * Format the Date timestamp to a human-readable timestamp.
	 * 
	 * @param d The timestamp.
	 * @return A human-readable timestamp in the default format used in Maunium products.
	 */
	public static String format(Date d) {
		return def.format(d);
	}
}
