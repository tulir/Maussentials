package net.maunium.bukkit.Maussentials;

import org.bukkit.ChatColor;

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
	
	public static String translateAll(String s) {
		return translate('&', s, "0123456789AaBbCcDdEeFfKkLlMmNnOoRr");
	}
	
	public static String translateColors(String s) {
		return translate('&', s, "0123456789AaBbCcDdEeFfRr");
	}
	
	public static String translateFormats(String s) {
		return translate('&', s, "LlMmNnOoRr");
	}
	
	public static String translateMagic(String s) {
		return translate('&', s, "KkRr");
	}
}
