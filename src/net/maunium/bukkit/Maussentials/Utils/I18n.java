package net.maunium.bukkit.Maussentials.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Properties;

/**
 * An basic internationalization class that uses files with the Java Properties format. I18n = Internationalization
 * 
 * @author Tulir293
 * @since 0.1
 */
public class I18n {
	private Properties lang;
	
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
		return new I18n(new File(langDir, language + ".lang"), new File(langDir, "en_US.lang"));
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
		return new I18n(langFile, new File(langFile.getParentFile(), "en_US.lang"));
	}
	
	/**
	 * Creates an Internationalization instance without a name.<br>
	 * The created instance can only be used through the non-static method {@link #format(node, replace)}
	 * 
	 * @param langDir The directory in which language files are stored.
	 * @param language The name of the language file without the .lang extension
	 * @param backup The name of the language to use if the main language couldn't be found. Also without the .lang extension.
	 * @throws FileNotFoundException Thrown when the language file could not be found.
	 * @throws IOException Thrown when there's an error creating the streams or loading the properties.
	 */
	public static I18n createInstance(File langDir, String language, String backup) throws FileNotFoundException, IOException {
		return new I18n(new File(langDir, language + ".lang"), new File(langDir, backup + ".lang"));
	}
	
	/**
	 * Creates an Internationalization instance without a name.<br>
	 * The created instance can only be used through the non-static method {@link #format(node, replace)}
	 * 
	 * @param langFile The language file.
	 * @param backupFile The file to use if the language file doesn't exist.
	 * @throws FileNotFoundException Thrown when the language file could not be found.
	 * @throws IOException Thrown when there's an error creating the streams or loading the properties.
	 */
	public static I18n createInstance(File langFile, File backupFile) throws FileNotFoundException, IOException {
		return new I18n(langFile, backupFile);
	}
	
	/**
	 * Hey! Don't touch that! It's private!
	 */
	private I18n(File f, File backup) throws FileNotFoundException, IOException {
		lang = new Properties();
		if (f.exists()) lang.load(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8));
		else lang.load(new InputStreamReader(new FileInputStream(backup), StandardCharsets.UTF_8));
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
	public String translate(String node, Object... arguments) {
		try {
			if (lang.containsKey(node)) {
				String rtrn = ChatFormatter.formatAll(lang.getProperty(node)).replace("<br>", "\n");
				int i = 0;
				for (Object o : arguments) {
					if (o == null) o = "null";
					rtrn = rtrn.replace("{" + i + "}>>nf", o.toString());
					rtrn = rtrn.replace("{" + i + "}", ChatFormatter.formatAll(o.toString()));
					i++;
				}
				return rtrn;
			} else return node + (arguments.length != 0 ? " (" + Arrays.toString(arguments) + ")" : "");
		} catch (Throwable t) {
			return node + (arguments.length != 0 ? " (" + Arrays.toString(arguments) + ")" : "");
		}
	}
	
	/**
	 * A basic interface that has a method which is usually used to call the {@link #format(node, arguments)} method of a specific i18n instance. I15r =
	 * Internationalizer
	 * 
	 * @author Tulir293
	 */
	public static interface I15r {
		public String translate(String node, Object... replace);
	}
}
