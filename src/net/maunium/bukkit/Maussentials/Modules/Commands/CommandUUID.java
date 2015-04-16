package net.maunium.bukkit.Maussentials.Modules.Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonArray;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParser;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;
import net.maunium.bukkit.Maussentials.Utils.DateUtils;

/**
 * UUID tools (/mauuuid command)
 * 
 * @author Tulir293
 * @since 0.1
 */
public class CommandUUID implements CommandModule {
	private Maussentials plugin;
	private boolean loaded = false;
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
		if (!plugin.checkPerms(sender, "maussentials.uuid")) return true;
		if (args.length > 1) {
			if (args[0].equalsIgnoreCase("get")) {
				String name = args[1];
				// Get the preferred at time argument for the request. If none given, use current time.
				long time = args.length > 2 ? Long.parseLong(args[2]) : System.currentTimeMillis() / 1000L;
				try {
					// Request the profile details of the given name at the given time.
					BufferedReader br = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name + "?at="
							+ time).openStream()));
					String in = "";
					String s;
					// Read the response
					while ((s = br.readLine()) != null)
						in += s;
					
					// Make sure that it isn't empty. If it is, show an error.
					if (in.isEmpty()) {
						sender.sendMessage(plugin.translateErr("uuid.get.invalid", name));
						return true;
					}
					
					// Parse the response json.
					JsonObject jo = new JsonParser().parse(in).getAsJsonObject();
					// Check if the username was found.
					if (jo.has("error")) {
						// Not found. Show error.
						sender.sendMessage(plugin.translateErr("uuid.get.error", name, jo.get("error").getAsString(), jo.get("errorMessage").getAsString()));
					} else if (jo.has("id")) {
						// Found. Show latest username and UUID.
						sender.sendMessage(plugin.translateStd("uuid.get.uuid", jo.get("id").getAsString()));
						sender.sendMessage(plugin.translateStd("uuid.get.name", jo.get("name").getAsString()));
					}
				} catch (IOException e1) {
					// Something went wrong. Tell it to the user...
					sender.sendMessage(plugin.translateErr("uuid.error", e1.getMessage()));
					// ... and print it to the console
					plugin.getLogger().severe("Error while fetching UUID from name: ");
					e1.printStackTrace();
				}
				return true;
			} else if (args[0].equalsIgnoreCase("history") && args.length > 1) {
				String uuid = "";
				if (args.length > 2 && args[1].equalsIgnoreCase("by-uuid")) uuid = args[2].replace("-", "");
				else {
					try {
						if (args.length > 2 && args[2].equalsIgnoreCase("nocache")) {
							try {
								long timestamp = System.currentTimeMillis();
								if (args.length > 3) timestamp = Long.parseLong(args[3]);
								BufferedReader br = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/"
										+ args[1] + "?at=" + timestamp).openStream()));
								
								String in = "", s;
								while ((s = br.readLine()) != null)
									in += s;
								
								if (in.isEmpty()) {
									sender.sendMessage(plugin.translateErr("uuid.history.notfound.server", args[1]));
									return true;
								}
								
								JsonObject jo = new JsonParser().parse(in).getAsJsonObject();
								if (jo.has("error")) {
									sender.sendMessage(plugin.translateErr("uuid.history.notfound.server", args[1]));
									return true;
								} else if (jo.has("id")) uuid = jo.get("id").getAsString();
							} catch (Throwable e) {
								sender.sendMessage(plugin.translateErr("uuid.history.notfound.server", args[1]));
								return true;
							}
						} else {
							UUID uuidt = plugin.getPlayerData().getUUIDByName(args[1]);
							if (uuidt != null) uuid = uuidt.toString().replace("-", "");
							else {
								sender.sendMessage(plugin.translateErr("uuid.history.notfound.cache", args[1]));
								return true;
							}
						}
					} catch (SQLException e) {
						sender.sendMessage(plugin.translateErr("uuid.error", e.getMessage()));
						plugin.getLogger().severe("Error while fetching UUID from name (from cache, for name history): ");
						e.printStackTrace();
						return true;
					}
				}
				
				try {
					// Request the name history of the given UUID.
					BufferedReader br = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/user/profiles/" + uuid.replace("-", "")
							+ "/names").openStream()));
					String in = "";
					String s;
					// Read the response
					while ((s = br.readLine()) != null)
						in += s;
					
					// Make sure that it isn't empty. If it is, show an error.
					if (in.isEmpty()) {
						sender.sendMessage(plugin.translateErr("uuid.history.invalid", uuid));
						return true;
					}
					
					// Parse the response json.
					JsonArray ja = new JsonParser().parse(in).getAsJsonArray();
					List<String> ss = new ArrayList<String>();
					ss.add(plugin.translateStd("uuid.history", uuid));
					// Loop through the name change entries and add the details to the string list.
					for (int i = 0; i < ja.size(); i++) {
						JsonObject jo = ja.get(i).getAsJsonObject();
						// Get the name changed to
						String name = jo.get("name").getAsString();
						// Check if the name is the new name or the original one
						if (jo.has("changedToAt")) {
							// The name is a new one. Get the time it was changed to at.
							String timeReadable = DateUtils.format(jo.get("changedToAt").getAsLong());
							long time = jo.get("changedToAt").getAsLong();
							
							// Add an entry with the time included
							ss.add(plugin.translatePlain("uuid.history.new", name, timeReadable, time));
						} else {
							// The name is the original one.
							// Add an entry without the time changed to at.
							ss.add(plugin.translatePlain("uuid.history.original", name));
						}
					}
					// Send the contents of the array
					sender.sendMessage(ss.toArray(new String[0]));
				} catch (IOException e1) {
					// Something went wrong. Tell it to the user...
					sender.sendMessage(plugin.translateErr("uuid.error", e1.getMessage()));
					// ... and print it to the console
					plugin.getLogger().severe("Error while fetching name history from UUID: ");
					e1.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void unload() {
		plugin.getCommand("mauuuid").setExecutor(plugin);
		plugin = null;
		loaded = false;
	}
	
	@Override
	public void load(Maussentials plugin) {
		this.plugin = plugin;
		plugin.getCommand("mauuuid").setExecutor(this);
		loaded = true;
	}
	
	@Override
	public void help(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(plugin.translateErr("uuid.help.get", label));
		sender.sendMessage(plugin.translateErr("uuid.help.history", label));
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
}
