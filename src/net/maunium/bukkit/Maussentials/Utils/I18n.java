package net.maunium.bukkit.Maussentials.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * An basic internationalization class that uses files with the Java Properties format. I18n =
 * Internationalization
 * 
 * @author Tulir293
 * @since 0.1
 */
public class I18n {
	private Properties lang;
	
	/**
	 * Creates an Internationalization instance without a name.<br>
	 * The created instance can only be used through the non-static method
	 * {@link #format(node, replace)}
	 * 
	 * @param langDir The directory in which language files are stored.
	 * @param language The name of the language file without the .lang extension
	 * @throws FileNotFoundException Thrown when the language file could not be found.
	 * @throws IOException Thrown when there's an error creating the streams or loading the
	 *             properties.
	 */
	public static I18n createInstance(File langDir, String language) throws FileNotFoundException, IOException {
		return new I18n(new File(langDir, language + ".lang"), new File(langDir, "en_US.lang"));
	}
	
	/**
	 * Creates an Internationalization instance without a name.<br>
	 * The created instance can only be used through the non-static method
	 * {@link #format(node, replace)}
	 * 
	 * @param langFile The language file.
	 * @throws FileNotFoundException Thrown when the language file could not be found.
	 * @throws IOException Thrown when there's an error creating the streams or loading the
	 *             properties.
	 */
	public static I18n createInstance(File langFile) throws FileNotFoundException, IOException {
		return new I18n(langFile, new File(langFile.getParentFile(), "en_US.lang"));
	}
	
	/**
	 * Creates an Internationalization instance without a name.<br>
	 * The created instance can only be used through the non-static method
	 * {@link #format(node, replace)}
	 * 
	 * @param langDir The directory in which language files are stored.
	 * @param language The name of the language file without the .lang extension
	 * @param fallback The name of the language to use if the main language couldn't be found. Also
	 *            without the .lang extension.
	 * @throws FileNotFoundException Thrown when the language file could not be found.
	 * @throws IOException Thrown when there's an error creating the streams or loading the
	 *             properties.
	 */
	public static I18n createInstance(File langDir, String language, String fallback) throws FileNotFoundException, IOException {
		return new I18n(new File(langDir, language + ".lang"), new File(langDir, fallback + ".lang"));
	}
	
	/**
	 * Creates an Internationalization instance without a name.<br>
	 * The created instance can only be used through the non-static method
	 * {@link #format(node, replace)}
	 * 
	 * @param langFile The language file.
	 * @param fallbackFile The file to use if the language file doesn't exist.
	 * @throws FileNotFoundException Thrown when the language file could not be found.
	 * @throws IOException Thrown when there's an error creating the streams or loading the
	 *             properties.
	 */
	public static I18n createInstance(File langFile, File fallbackFile) throws FileNotFoundException, IOException {
		return new I18n(langFile, fallbackFile);
	}
	
	/**
	 * For those who just want me to eat whatever the stream gives.
	 * 
	 * @param is The inputstream to eat.
	 * @return The I18n instance that comes as a result of eating the inputstream.
	 */
	public static I18n createInstance(InputStream is) throws IOException {
		return new I18n(is);
	}
	
	/**
	 * Hey! Don't touch that! It's private!
	 */
	private I18n(File f, File fallback) throws FileNotFoundException, IOException {
		lang = new Properties();
		if (f.exists()) lang.load(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
		else lang.load(new InputStreamReader(new FileInputStream(fallback), StandardCharsets.UTF_8));
	}
	
	/**
	 * Hey! Don't touch that! It's private!
	 */
	private I18n(InputStream is) throws IOException {
		lang = new Properties();
		lang.load(is);
	}
	
	/**
	 * Formats the given node with the given arguments using this I18n instance.
	 * 
	 * @param node The internationalization node.
	 * @param arguments The arguments.
	 * @return The value got from the language file with added arguments.<br>
	 *         If the node couldn't be found, it returns null.
	 */
	public String translate(String node, Object... arguments) {
		try {
			// See if the language map contains the given node.
			if (lang.containsKey(node)) {
				// Get the message and parse styles/newlines in it.
				String rtrn = ChatFormatter.formatAll(lang.getProperty(node)).replace("<br>", "\n");
				int i = 0;
				// Loop through the given arguments.
				for (Object o : arguments) {
					// If the argument is null, replace it with the String "null".
					if (o == null) o = "null";
					// Replace all argument slots which should not be formatted with the
					// non-formatted argument.
					rtrn = rtrn.replace("{" + i + "}>>nf", o.toString());
					// Replace all remaining argument slots with the formatted argument.
					rtrn = rtrn.replace("{" + i + "}", ChatFormatter.formatAll(o.toString()));
					i++;
				}
				// Return the formatted message.
				return rtrn;
				// If the map doesn't contain the node, return null.
			} else return null;
		} catch (Throwable t) {
			// Return null if something went wrong.
			return null;
		}
	}
	
	/**
	 * A basic interface that has a method which is usually used to call the
	 * {@link #translate(node, arguments)} method of a specific i18n instance. I15r =
	 * Internationalizer
	 * 
	 * @author Tulir293
	 * @since 0.1
	 */
	public static interface I15r {
		public String translate(String node, Object... replace);
	}
}
