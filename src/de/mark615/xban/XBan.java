package de.mark615.xban;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.mark615.xapi.XApi;
import de.mark615.xapi.versioncheck.VersionCheck;
import de.mark615.xapi.versioncheck.VersionCheck.XType;
import de.mark615.xban.commands.CommandBan;
import de.mark615.xban.commands.XCommand;
import de.mark615.xban.events.EventListener;
import de.mark615.xban.object.XUtil;

public class XBan extends JavaPlugin
{
	public static final int BUILD = 6;
	public static final String PLUGIN_NAME = "[xBan] ";
	public static final String PLUGIN_NAME_SHORT = "[xBan] ";
	
	private static XBan instance = null;

	private XApiConnector xapiconn = null;
	private SettingManager settings = null;
	private XBanManager manager = null;
	private EventListener events = null;

	private Map<String, XCommand> commands = null;
	
	public void onEnable()
	{
		instance = this;
		this.commands = new HashMap<>();

		settings = SettingManager.getInstance();
		settings.setup(this);
		
		this.manager = new XBanManager(this);
		
		registerEvents();
		registerCommands();
		
		setupXApi();
		if (xapiconn != null)
		{
			XUtil.info("connected with xApi");
		}
		
		XUtil.onEnable();
		XUtil.updateCheck(this);
		XUtil.info("Enabled Build " + BUILD);
	}

	@Override
	public void onDisable()
	{
		XUtil.onDisable();
		
		//TODO ondisable
		
		settings.saveConfig();
	}

	public static XBan getInstance()
	{
		return instance;
	}
	
	private void registerEvents()
	{
		events = new EventListener(this);
		Bukkit.getServer().getPluginManager().registerEvents(events, this);
	}
	
	private void registerCommands()
	{
		commands.put("ban", new CommandBan(this));
		//add other commands
		
	}

	private boolean setupXApi() 
	{
		XApi xapi = (XApi)getServer().getPluginManager().getPlugin("xApi");
    	if(xapi == null)
    		return false;
    	
    	try
    	{
	    	if (xapi.checkVersion(XType.xBan, BUILD))
	    	{
	        	xapiconn = new XApiConnector(xapi, this);
	        	xapi.registerXBan(xapiconn);
	    	}
	    	else
	    	{
	    		XUtil.severe("Can't hook to xApi!"); 
	    		if (VersionCheck.isXPluginHigherXApi(XType.xBan, BUILD))
	    		{
		    		XUtil.warning("Please update your xApi!");
		    		XUtil.warning("Trying to hook to xApi. Have an eye into console for errors with xApi!");

		        	xapiconn = new XApiConnector(xapi, this);
		        	xapi.registerXBan(xapiconn);
	    		}
	    		else
	    		{
		    		XUtil.severe("Please update your " + PLUGIN_NAME + " for hooking.");
	    		}
	    	}
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		XUtil.severe("An error accurred during connection to xApi!");
    	}
    	
    	return xapiconn != null;
	}

	
	@Override
	public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args)
	{
		XCommand xCommand = commands.get(command.getLabel());

		if (xCommand == null)
			return false;
		
		if (commandSender instanceof Player)
		{
			if (!((Player) commandSender).hasPermission(xCommand.getPermission()) || !xCommand.runCommand((Player) commandSender, command, s, args))
			{
				commandSender.sendMessage(ChatColor.RED + XUtil.getMessage("command.nopermission"));
				return true;
			}
		}
		else
		{
			if (!xCommand.runCommand(commandSender, command, s, args))
			{
				commandSender.sendMessage(ChatColor.RED + XUtil.getMessage("command.nopermission"));
			}
		}
		return true;
	}

	public SettingManager getSettingManager()
	{
		return this.settings;
	}
	
	public XApiConnector getAPI()
	{
		return this.xapiconn;
	}
	
	public boolean hasAPI()
	{
		return this.xapiconn != null;
	}
	
	public XBanManager getBanManager()
	{
		return manager;
	}
}
