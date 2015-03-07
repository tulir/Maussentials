package net.maunium.bukkit.Maussentials.Utils;

import org.bukkit.ChatColor;

/**
 * Can format a message with alternate format codes to a string with real format codes.<br>
 * Contains preconfigured translators that have '&' as the alternate prefix and All, Colors, Style or Magic as the
 * format value options.
 * 
 * @author Tulir293
 * @since 1.0
 */
public class ChatFormatter {
	/**
	 * Replace all instances of char {@code from} with {@code to} in {@code change} if {@code from} is followed with a
	 * char in the {@code suffixes} string. Also replaces all occurences where {@code from} is followed by itself with a
	 * single {@code from}.
	 * 
	 * @param from The char to replace with {@code to} if specific rules apply.
	 * @param to The char to replace {@code from} with if specific rules apply.
	 * @param change The string to change.
	 * @param suffixes All the allowed prefixes for {@code from}.
	 * @return The changed string.
	 */
	public static String change(char from, char to, String change, String suffixes) {
		char[] b = change.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == from && (i - 1 < 0 || b[i - 1] != from) && suffixes.indexOf(b[i + 1]) > -1) {
				b[i] = to;
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}
		
		return new String(b).replace(from + "" + from, from + "");
	}
	
	/**
	 * Format codes that start with alt and are immediately followed by a char in colorChars.
	 * 
	 * @param alt The alternate color code to format to the actual code.
	 * @param toFormat The string to format.
	 * @param colorChars All the color values.
	 * @return The formatted string.
	 */
	public static String format(char alt, String toFormat, String colorChars) {
		return change(alt, ChatColor.COLOR_CHAR, toFormat, colorChars);
	}
	
	/**
	 * Deformat codes that start with the format char and are immediately followed by a char in colorChars.
	 * 
	 * @param alt The alternate color code to format to the actual code.
	 * @param toFormat The string to format.
	 * @param colorChars All the color values.
	 * @return The formatted string.
	 */
	public static String deformat(char alt, String toDeformat, String colorChars) {
		return change(ChatColor.COLOR_CHAR, alt, toDeformat, colorChars);
	}
	
	/**
	 * Format all of the normal formatting codes with '&' as the alternate prefix.
	 * 
	 * @param s The string to format.
	 * @return The formatted string.
	 */
	public static String formatAll(String s) {
		return format('&', s, "0123456789AaBbCcDdEeFfKkLlMmNnOoRr");
	}
	
	/**
	 * Format the color formatting codes with '&' as the alternate prefix.
	 * 
	 * @param s The string to format.
	 * @return The formatted string.
	 */
	public static String formatColors(String s) {
		return format('&', s, "0123456789AaBbCcDdEeFfRr");
	}
	
	/**
	 * Format the style formatting codes with '&' as the alternate prefix.
	 * 
	 * @param s The string to format.
	 * @return The formatted string.
	 */
	public static String formatStyles(String s) {
		return format('&', s, "LlMmNnOoRr");
	}
	
	/**
	 * Format the magic formatting code with '&' as the alternate prefix.
	 * 
	 * @param s The string to format.
	 * @return The formatted string.
	 */
	public static String formatMagic(String s) {
		return format('&', s, "KkRr");
	}
	
	/**
	 * Deformat all of the normal formatting codes with '&' as the alternate prefix.
	 * 
	 * @param s The string to format.
	 * @return The formatted string.
	 */
	public static String deformatAll(String s) {
		return deformat('&', s, "0123456789AaBbCcDdEeFfKkLlMmNnOoRr");
	}
	
	/**
	 * Deformat the color formatting codes with '&' as the alternate prefix.
	 * 
	 * @param s The string to format.
	 * @return The formatted string.
	 */
	public static String deformatColors(String s) {
		return deformat('&', s, "0123456789AaBbCcDdEeFfRr");
	}
	
	/**
	 * Deformat the style formatting codes with '&' as the alternate prefix.
	 * 
	 * @param s The string to format.
	 * @return The formatted string.
	 */
	public static String deformatStyles(String s) {
		return deformat('&', s, "LlMmNnOoRr");
	}
	
	/**
	 * Deformat the magic formatting code with '&' as the alternate prefix.
	 * 
	 * @param s The string to format.
	 * @return The formatted string.
	 */
	public static String deformatMagic(String s) {
		return deformat('&', s, "KkRr");
	}
}
