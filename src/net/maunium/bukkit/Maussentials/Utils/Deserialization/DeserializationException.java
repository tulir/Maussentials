package net.maunium.bukkit.Maussentials.Utils.Deserialization;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

/**
 * The exception to throw if deserialization fails.
 * 
 * @author Tulir293
 * @since 0.3
 */
public class DeserializationException extends Exception {
	public static boolean debug = false;
	private static final long serialVersionUID = -8209409003616345408L;
	/** Used to bypass Bukkits System.out formatting. */
	private static final PrintStream sout = new PrintStream(new FileOutputStream(FileDescriptor.out));
	/** Set to true if debug info should be printed when calling printStackTrace. */
	private final Map<?, ?> serialized;
	
	/**
	 * Construct a Deserialization Exception with the given message.
	 * 
	 * @param s The message.
	 * @param serialized The serialized map.
	 */
	public DeserializationException(String s, Map<?, ?> serialized) {
		super(s);
		this.serialized = serialized;
	}
	
	/**
	 * Construct a Deserialization Exception with the given message and the given exception as the cause of this exception.
	 * 
	 * @param s The message.
	 * @param cause The throwable that caused this.
	 * @param serialized The serialized map.
	 */
	public DeserializationException(String s, Throwable cause, Map<?, ?> serialized) {
		super(s, cause);
		this.serialized = serialized;
	}
	
	/**
	 * Construct a Deserialization Exception with the given exception as the cause of this exception.
	 * 
	 * @param cause The throwable that caused this.
	 * @param serialized The serialized map.
	 */
	public DeserializationException(Throwable cause, Map<?, ?> serialized) {
		super(cause);
		this.serialized = serialized;
	}
	
	@Override
	public void printStackTrace() {
		super.printStackTrace(sout);
		if (debug) debugPrint();
	}
	
	/**
	 * Print the debug information of this deserialization exception.
	 */
	public void debugPrint() {
		sout.print("\n---------- Start Deserialization Debug ----------\n");
		
		sout.println("Contents of serialized map");
		mapPrint("  ", serialized);
		
		// More debug info printing can be put here if needed.
		
		sout.println("----------- End Deserialization Debug -----------");
	}
	
	/**
	 * Recursively print the contents of the map.
	 */
	private void mapPrint(String indent, Map<?, ?> s) {
		// Loop through the contents of the map.
		for (Entry<?, ?> e : s.entrySet()) {
			// Print the key of the current entry.
			sout.println(indent + e.getKey());
			// Increase the indentation.
			indent = indent + "  ";
			// Get the value of the current entry.
			Object o = e.getValue();
			if (o instanceof Collection) {
				// The value is a collection. Cast it.
				Collection<?> c = (Collection<?>) o;
				// Then loop through the objects in the collection.
				for (Object oo : c)
					// Print each object with indentation.
					sout.println(indent + oo);
			} else if (o instanceof Map) {
				// The value is a map. Cast it.
				Map<?, ?> m = (Map<?, ?>) o;
				// Recurse into printing the contents of the map.
				mapPrint(indent, m);
			} else {
				// The value is another type. Use its toString method.
				sout.println(indent + o);
			}
		}
	}
}