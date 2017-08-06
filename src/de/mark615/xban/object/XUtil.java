package de.mark615.xban.object;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import de.mark615.xban.SettingManager;
import de.mark615.xban.XBan;
import de.mark615.xban.object.Updater.UpdateResult;
import de.mark615.xban.object.Updater.UpdateType;

public class XUtil
{
	private static boolean jsonMessage = false;
	
	public static void info(String info)
	{
		Bukkit.getLogger().info(XBan.PLUGIN_NAME + info);
	}

	public static void warning(String severe)
	{
		Bukkit.getLogger().info(XBan.PLUGIN_NAME + "[WARNING] " + severe);
	}
	
	public static void severe(String severe)
	{
		Bukkit.getLogger().severe(XBan.PLUGIN_NAME + severe);
	}
	
	public static void severe(String severe, Exception e)
	{
		severe(severe);
		e.printStackTrace();
	}
	
	public static void debug(Exception e)
	{
		e.printStackTrace();
	}
	
	public static String replaceColorCodes(String message)
	{
		return ChatColor.translateAlternateColorCodes('&', message);
	}
	
	public static String getMessage(String file)
	{
		String raw = SettingManager.getInstance().getMessage().getString(file);
		if (raw == null)
		{
			raw = file + " (not found in messages.yml)";
		}
		raw = replaceColorCodes(raw);
		return raw;
	}
	
	private static void sendMessage(CommandSender sender, String message, boolean prefix)
	{
		message = replaceColorCodes(message);
		
		for (String line : message.split("%ln%"))
		{
			if (!prefix)
				sender.sendMessage(line);
			else
				sender.sendMessage(XBan.PLUGIN_NAME_SHORT + line);
		}
	}
	
	private static void sendMessage(CommandSender sender, String message)
	{
		sendMessage(sender, message, false);
	}
	
	public static void sendFileMessage(CommandSender s, String file, ChatColor color)
	{
		String message = getMessage(file);
		if (s instanceof Player)
			message = color + message;
		
		sendMessage(s, message, true);
	}

	public static void sendFileMessage(CommandSender s, String msg)
	{
		sendMessage(s, getMessage(msg));
	}

	public static void sendFileMessage(CommandSender s, String msg, boolean prefix)
	{
		sendMessage(s, getMessage(msg), prefix);
	}
	
	public static void sendCommandUsage(CommandSender s, String usage)
	{
		if (s instanceof Player)
			usage = ChatColor.RED + usage;
		
		sendMessage(s, usage, true);
	}
	
	public static void sendCommandInfo(CommandSender s, String info)
	{
		if (s instanceof Player)
			info = ChatColor.GREEN + info;
		
		sendMessage(s, info, true);
	}
	
	public static void sendCommandHelp(CommandSender s, String help)
	{
		if (s instanceof Player)
			help = ChatColor.YELLOW + help;
		
		sendMessage(s, help, true);
	}
	
	public static void sendCommandError(CommandSender s, String error)
	{
		if (s instanceof Player)
			error = ChatColor.RED + error;
		
		sendMessage(s, error, true);
	}
	
	public static void sendMessage(Player p, String info)
	{
		sendMessage(p, info, false);
	}
	
	public static String toHash(String pw)
	{
		String hashtext = null;
		byte[] bytesOfMessage;
		try {
			bytesOfMessage = pw.getBytes("UTF-8");
	
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] thedigest = md.digest(bytesOfMessage);
			
			BigInteger number = new BigInteger(1, thedigest);
			hashtext = number.toString(16);
			
			while (hashtext.length() < 32)
			{
				hashtext = "0" + hashtext;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return hashtext;
	}

	
	
	public static void updateCheck(final JavaPlugin plugin)
	{
		Bukkit.getServer().getScheduler().runTaskTimer(plugin, new Runnable()
		{
			@Override
			public void run()
			{
				if (SettingManager.getInstance().hasCheckVersion())
				{
					try
					{
						Updater updater = new Updater(plugin, 267923, plugin.getDataFolder(), UpdateType.NO_DOWNLOAD, true);
						if (updater.getResult() == UpdateResult.UPDATE_AVAILABLE) {
						    XUtil.info("New version available! " + updater.getLatestName());
						}
					}
					catch(Exception e)
					{
						XUtil.severe("Can't check version at Bukkit.com");
					}
				}
			}
		}, 20, 6 * 60 * 60 * 20);
	}	
	
	
	
	public static void onEnable()
	{
		if (!jsonMessage)
			return;
		
		Bukkit.getServer().getScheduler().runTaskAsynchronously(XBan.getInstance(), new Runnable()
		{
			@Override
			public void run()
			{
				if (onStart())
				{
					try
					{
						String value = sendGet("setmode?uuid=" + SettingManager.getInstance().getAPIKey().toString() + "&type=XBan&mode=on&build=" + XBan.BUILD);
						JsonElement parser = new JsonParser().parse(value);
						JsonObject json = parser.getAsJsonObject();
						if (json.has("dataid"))
						{
							SettingManager.getInstance().setDataID(json.get("dataid").getAsInt());
						}
					}
					catch(Exception e)
					{
						severe("Can't generate onEnable webrequest");
						debug(e);
					}
				}				
			}
		});
		
	}
	
	private static boolean onStart()
	{
		try
		{
			String url = "startup?servername=" + Bukkit.getServerName() + "";
			if (SettingManager.getInstance().getAPIKey() != null)
			{
				url = url + "&uuid=" + SettingManager.getInstance().getAPIKey().toString();
			}
			String value = sendGet(url);
			if (value != null && value.length() != 0)
			{
				JsonElement parser = new JsonParser().parse(value);
				JsonObject json = parser.getAsJsonObject();
				if (json.has("error"))
				{
					severe("JSON error: " + json.get("error").getAsString());
					if (json.has("action") && json.get("action").getAsString().equalsIgnoreCase("dropUUID"))
					{
						if (UUID.fromString(json.get("uuid").getAsString()).equals(SettingManager.getInstance().getAPIKey()))
						{
							SettingManager.getInstance().setAPIKey(null);
							return onStart();
						}
					}
				}
				else
				if (json.has("uuid"))
				{
					SettingManager.getInstance().setAPIKey(UUID.fromString(json.get("uuid").getAsString()));
					SettingManager.getInstance().saveConfig();
				}
				return true;
			}
			return false;
		}
		catch(Exception e)
		{
			severe("Can't generate onEnable webrequest");
			debug(e);
			return false;
		}
	}
	
	public static void onDisable()
	{
		if (!jsonMessage)
			return;
		
		try
		{
			sendGet("setmode?uuid=" + SettingManager.getInstance().getAPIKey().toString() + "&dataid=" + SettingManager.getInstance().getDataID() + "&" + 
					"type=XBan&mode=off&build=" + XBan.BUILD);
		}
		catch(Exception e)
		{
			severe("Can't generate onDisable webrequest");
			debug(e);
		}
	}
	
	// HTTP GET request
	private static String sendGet(String message) throws Exception {

		String url = "http://134.255.217.210:8080/";
		//String url = "http://localhost:8080/";

		URL obj = new URL(url + message);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request header
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		
		//reponse
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}
	
	public enum PassMatch
	{
		OK,
		ERROR,
		DIGIT,
		SPECIAL,
		UPPERLOWER,
		LENGTH
	}
}
