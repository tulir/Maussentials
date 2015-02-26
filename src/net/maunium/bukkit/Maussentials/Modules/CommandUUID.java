package net.maunium.bukkit.Maussentials.Modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.maunium.bukkit.Maussentials.Maussentials;
import net.maunium.bukkit.Maussentials.Modules.Util.CommandModule;

public class CommandUUID extends CommandModule {
	private Maussentials plugin;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy ss:mm:HH");
	
	@Override
	public boolean execute(CommandSender sender, Command command, String label, String[] args) {
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
						sender.sendMessage(plugin.errtag + plugin.translate("uuid.get.invalid", name));
						return true;
					}
					
					// Parse the response json.
					JsonObject jo = new JsonParser().parse(in).getAsJsonObject();
					// Check if the username was found.
					if (jo.has("error")) {
						// Not found. Show error.
						sender.sendMessage(plugin.errtag + plugin.translate("uuid.get.error", name, jo.get("error").getAsString(), jo.get("errorMessage").getAsString()));
					} else if (jo.has("id")) {
						// Found. Show latest username and UUID.
						sender.sendMessage(plugin.stag + plugin.translate("uuid.get.uuid", jo.get("id").getAsString()));
						sender.sendMessage(plugin.stag + plugin.translate("uuid.get.name", jo.get("name").getAsString()));
					}
				} catch (IOException e1) {
					// Something went wrong. Tell it to the user...
					sender.sendMessage(plugin.errtag + plugin.translate("uuid.error", e1.getMessage()));
					// ... and print it to the console
					plugin.getLogger().severe("Error while fetching name history from UUID: ");
					e1.printStackTrace();
				}
			} else if (args[0].equalsIgnoreCase("history")) {
				String uuid = args[1];
				if (args.length > 2 && args[1].equalsIgnoreCase("name")) {
					String name = args[2];
					sender.sendMessage(plugin.errtag + "History from name has not yet been implemented.");
					return true;
				}
				
				try {
					// Request the name history of the given UUID.
					BufferedReader br = new BufferedReader(new InputStreamReader(
							new URL("https://api.mojang.com/user/profiles/" + uuid + "/names").openStream()));
					String in = "";
					String s;
					// Read the response
					while ((s = br.readLine()) != null)
						in += s;
					
					// Make sure that it isn't empty. If it is, show an error.
					if (in.isEmpty()) {
						sender.sendMessage(plugin.errtag + plugin.translate("uuid.history.invalid", uuid));
						return true;
					}
					
					// Parse the response json.
					JsonArray ja = new JsonParser().parse(in).getAsJsonArray();
					List<String> ss = new ArrayList<String>();
					ss.add(plugin.stag + plugin.translate("uuid.history", uuid));
					// Loop through the name change entries and add the details to the string list.
					for (int i = 0; i < ja.size(); i++) {
						JsonObject jo = ja.get(i).getAsJsonObject();
						// Get the name changed to
						String name = jo.get("name").getAsString();
						// Check if the name is the new name or the original one
						if (jo.has("changedToAt")) {
							// The name is a new one. Get the time it was changed to at.
							String timeReadable = sdf.format(new Date(jo.get("changedToAt").getAsLong()));
							long time = jo.get("changedToAt").getAsLong();
							
							// Add an entry with the time included
							ss.add(plugin.translate("uuid.history.new", name, timeReadable, time));
						} else {
							// The name is the original one.
							// Add an entry without the time changed to at.
							ss.add(plugin.translate("uuid.history.original", name));
						}
					}
					// Send the contents of the array
					sender.sendMessage(ss.toArray(new String[0]));
				} catch (IOException e1) {
					// Something went wrong. Tell it to the user...
					sender.sendMessage(plugin.errtag + plugin.translate("uuid.error", e1.getMessage()));
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
	public void reload() {
		plugin.getCommand("mauuuid").setExecutor(this);
	}
	
	@Override
	public void initialize(Maussentials plugin) {
		this.plugin = plugin;
		this.permission = "maussentials.uuid";
		reload();
	}
	
	@Override
	public void help(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage(plugin.translate("uuid.help.get", label));
		sender.sendMessage(plugin.translate("uuid.help.history", label));
	}
}
