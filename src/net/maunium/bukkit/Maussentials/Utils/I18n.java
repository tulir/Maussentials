package net.maunium.bukkit.Maussentials.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.bukkit.ChatColor;

/**
 * An basic internationalization class that uses files with the Java Properties format.
 * I18n = Internationalization
 * 
 * @author Tulir293
 * @since 0.1
 */
public class I18n {
	private Properties lang;
	private static Map<String, I18n> instances = new HashMap<String, I18n>();
	
	/**
	 * Creates an Internationalization instance with a name.<br>
	 * The created instance can be used through the static method {@link #format(instance, node, replace)}<br>
	 * 
	 * @param instanceName The name of the instance to be created.
	 * @param langDir The directory in which language files are stored.
	 * @param language The name of the language file without the .lang extension
	 * @throws FileNotFoundException Thrown when the language file could not be found.
	 * @throws IOException Thrown when there's an error creating the streams or loading the properties.
	 */
	public static I18n createInstance(String instanceName, File langDir, String language) throws FileNotFoundException, IOException {
		if (instances.containsKey(instanceName)) return instances.get(instanceName);
		else {
			I18n i = new I18n(new File(langDir, language + ".lang"));
			instances.put(instanceName, i);
			return i;
		}
	}
	
	/**
	 * Creates an Internationalization instance with a name.<br>
	 * The created instance can be used through the static method {@link #format(instance, node, replace)}<br>
	 * 
	 * @param instanceName The name of the instance to be created.
	 * @param langFile The language file.
	 * @throws FileNotFoundException Thrown when the language file could not be found.
	 * @throws IOException Thrown when there's an error creating the streams or loading the properties.
	 */
	public static I18n createInstance(String instanceName, File langFile) throws FileNotFoundException, IOException {
		if (instances.containsKey(instanceName)) return instances.get(instanceName);
		else {
			I18n i = new I18n(langFile);
			instances.put(instanceName, i);
			return i;
		}
	}
	
	/**
	 * Creates an Internationalization instance without a name.<br>
	 * The created instance can only be used through the non-static method {@link #format(node, replace)}
	 * 
	 * @param langDir The directory in which language files are stored.
	 * @param language The name of the language file without the .lang extension
	 * @throws FileNotFoundException Thrown when the language file could not be found.
	 * @throws IOException Thrown when there's an error creating the streams or loading the properties.
	 */
	public static I18n createInstance(File langDir, String language) throws FileNotFoundException, IOException {
		return new I18n(new File(langDir, language + ".lang"));
	}
	
	/**
	 * Creates an Internationalization instance without a name.<br>
	 * The created instance can only be used through the non-static method {@link #format(node, replace)}
	 * 
	 * @param langFile The language file.
	 * @throws FileNotFoundException Thrown when the language file could not be found.
	 * @throws IOException Thrown when there's an error creating the streams or loading the properties.
	 */
	public static I18n createInstance(File langFile) throws FileNotFoundException, IOException {
		return new I18n(langFile);
	}
	
	/**
	 * Hey! Don't touch that! It's private!
	 */
	private I18n(File f) throws FileNotFoundException, IOException {
		lang = new Properties();
		lang.load(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
	}
	
	/**
	 * Formats the given node with the given arguments using the instance with the given name.
	 * 
	 * @param instanceName The name of the instance to be used.
	 * @param node The internationalization node.
	 * @param arguments The arguments.
	 * @return The string returned by {@link #format(node, replace)} of the instance with the given name.<br>
	 *         If the instance could not be found, it returns a string with the following format:<br>
	 *         instanceName | node (arguments)
	 */
	public static String format(String instanceName, String node, Object... arguments) {
		if (instances.containsKey(instanceName)) return instances.get(instanceName).format(node, arguments);
		else return instanceName + " | " + node + (arguments.length != 0 ? " (" + Arrays.toString(arguments) + ")" : "");
	}
	
	/**
	 * Formats the given node with the given arguments using this I18n instance.
	 * 
	 * @param node The internationalization node.
	 * @param arguments The arguments.
	 * @return The value got from the language file with added arguments.<br>
	 *         If the node couldn't be found, it returns a string with the following format:<br>
	 *         node (arguments)
	 */
	public String format(String node, Object... arguments) {
		if (lang.containsKey(node)) {
			String rtrn = lang.getProperty(node);
			rtrn = ChatFormatter.translateAll(rtrn);
			int i = 0;
			for (Object o : arguments) {
				rtrn = rtrn.replace("{" + i + "}", o.toString());
				i++;
			}
			return rtrn.replace('&', ChatColor.COLOR_CHAR).replace(ChatColor.COLOR_CHAR + "" + ChatColor.COLOR_CHAR, "&").replace("<br>", "\n");
		} else return node + (arguments.length != 0 ? " (" + Arrays.toString(arguments) + ")" : "");
	}
	
	/**
	 * A basic interface that has a method which is usually used to call the {@link #format(node, arguments)} method of a specific i18n instance.
	 * I15r = Internationalizer
	 * 
	 * @author Tulir293
	 */
	public static interface I15r {
		public String format(String node, Object... replace);
	}
}
