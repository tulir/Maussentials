package net.maunium.bukkit.Maussentials.Utils;

import org.bukkit.ChatColor;

/**
 * Can translate a message with alternate format codes to a string with real format codes.<br>
 * Contains preconfigured translators that have '&' as the alternate prefix and All, Colors, Style or Magic as the format value options.
 * 
 * @author Tulir293
 * @since 1.0
 */
public class ChatFormatter {
	/**
	 * Translate codes that start with alt and are immediately followed by a char in colorChars.
	 * 
	 * @param alt The alternate color code to translate to the actual code.
	 * @param toFormat The string to format.
	 * @param colorChars All the color values.
	 * @return The formatted string.
	 */
	public static String translate(char alt, String toFormat, String colorChars) {
		char[] b = toFormat.toCharArray();
		for (int i = 0; i < b.length - 1; i++) {
			if (b[i] == alt && b[i - 1] != alt && colorChars.indexOf(b[i + 1]) > -1) {
				b[i] = ChatColor.COLOR_CHAR;
				b[i + 1] = Character.toLowerCase(b[i + 1]);
			}
		}
		
		return new String(b).replace("&&", "&");
	}
	
	/**
	 * Translate all of the normal formatting codes with '&' as the alternate prefix.
	 * 
	 * @param s The string to format.
	 * @return The formatted string.
	 */
	public static String translateAll(String s) {
		return translate('&', s, "0123456789AaBbCcDdEeFfKkLlMmNnOoRr");
	}
	
	/**
	 * Translate the color formatting codes with '&' as the alternate prefix.
	 * 
	 * @param s The string to translate.
	 * @return The translated string.
	 */
	public static String translateColors(String s) {
		return translate('&', s, "0123456789AaBbCcDdEeFfRr");
	}
	
	/**
	 * Translate the style formatting codes with '&' as the alternate prefix.
	 * 
	 * @param s The string to translate.
	 * @return The translated string.
	 */
	public static String translateStyles(String s) {
		return translate('&', s, "LlMmNnOoRr");
	}
	
	/**
	 * Translate the magic formatting code with '&' as the alternate prefix.
	 * 
	 * @param s The string to translate.
	 * @return The translated string.
	 */
	public static String translateMagic(String s) {
		return translate('&', s, "KkRr");
	}
}
