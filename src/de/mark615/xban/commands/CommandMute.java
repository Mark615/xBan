package de.mark615.xban.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mark615.xban.XBan;
import de.mark615.xban.object.Ban;
import de.mark615.xban.object.Mute;
import de.mark615.xban.object.XUtil;

public class CommandMute extends XCommand
{
	private final XBan plugin;

	public CommandMute(XBan plugin)
	{
		super("mute", "");
		this.plugin = plugin;
	}

	@Override
	public void fillSubCommands(List<XSubCommand> subcommands)
	{
		
	}
	

	@Override
	protected void showHelp(CommandSender p)
	{
		p.sendMessage(ChatColor.GREEN + XBan.PLUGIN_NAME + ChatColor.GRAY + " - " + ChatColor.YELLOW + XUtil.getMessage("command.description"));
		if(matchPermission(p, "xban.mute")) p.sendMessage(ChatColor.GREEN + "/mute <player> <server|this|[world]> <reason> [<x:dx:hx:m>]" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.mute.description"));
	}

	@Override
	public XCommandReturnType run(CommandSender sender, Command command, String s, String[] args)
	{
		
		if(matchPermission(sender, "xban.mute")) {
			
			List<String> worldNames = new ArrayList<>();
			for(World world : Bukkit.getServer().getWorlds())
			{
				worldNames.add(world.getName());
			}
			
			if(args.length<3 || args.length>4 || (!worldNames.contains(args[1]) && (args[1] != "server" && args[1] != "this")) || (args.length == 4) ? matchesTimeFormat(args[3]) : false)
			{
				XUtil.sendCommandUsage(sender, "use: /xban <help> " + ChatColor.YELLOW + "- for help");
				return XCommandReturnType.NONE;
			}
			
			for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers())
			{
				if(player.getName() == args[0])
				{

					//default to servermute if sender !instanceof Player and args[1] == "this"
					if(args.length == 4)
					{
						Mute mute = new Mute(
								player.getUniqueId(), 
								(sender instanceof Player) ? ((Player)sender).getUniqueId() : plugin.getBanManager().getServerUUID(), 
								System.currentTimeMillis()/1000L, 
								getMuteEnd(args[3]), 
								getLocation(args[1],sender), 
								args[2]);
						
						if(!plugin.getBanManager().addMute(mute))
						{
							return XCommandReturnType.NONE;
						}
						
					}else
					{
						Mute mute = new Mute(
								player.getUniqueId(), 
								(sender instanceof Player) ? ((Player)sender).getUniqueId() : plugin.getBanManager().getServerUUID(), 
								System.currentTimeMillis()/1000L, 
								0, 
								getLocation(args[1],sender), 
								args[2]);
						
						if(!plugin.getBanManager().addMute(mute))
						{
							return XCommandReturnType.NONE;
						}
					}
					
					XUtil.sendFileMessage(sender, "command.mute.success", ChatColor.GREEN);
					return XCommandReturnType.SUCCESS;					
				}
			}
			XUtil.sendFileMessage(sender, "command.player-not-found", ChatColor.RED);
			return XCommandReturnType.NOPLAYERMATCH;
			
		}
		XUtil.sendFileMessage(sender, "command.nopermission", ChatColor.RED);
		return XCommandReturnType.NOPERMISSION;
		
	}
	
	private long getMuteEnd(String input)
	{
		int days = 0, hours = 0, minutes = 0;
		long seconds = 0;
		String temp = "";
		for(int i = 0; i<input.length(); i++)
		{
			if(i != 'd' && i != 'h' && i != 'm' && i != ':')
			{
				temp += i;
			}else if(i != 'd')
			{
				days = Integer.parseInt(temp);
				temp = "";
			}else if(i != 'h')
			{
				hours = Integer.parseInt(temp);
				temp = "";
			}else if(i != 'm')
			{
				minutes = Integer.parseInt(temp);
				temp = "";
			}
		}
		seconds = (days * 24 * 60 * 60) + (hours * 60 * 60) + (minutes * 60);
		
		return System.currentTimeMillis()/1000L + seconds;
	}
	
	private String getLocation(String input, CommandSender sender)
	{
		if(sender instanceof Player || input != "this")
		{
			if(input == "this")
			{
				return ((Player)sender).getWorld().getName();
			}
			else
			{
				return input;
			}
		}
		else
		{
			return "server";
		}
	}
	
	private boolean matchesTimeFormat(String input)
	{
		//  https://regex101.com/r/ICwEH4/1  help for regex
		return (!input.matches("[0-9]+:d([0-9]|0[0-9]|1[0-9]|2[0-3]):h[0-5]?[0-9]:m") && 	// x:dx:hx:m
				!input.matches("[0-9]+:d") && 												// x:d
				!input.matches("[0-9]+:d([0-9]|0[0-9]|1[0-9]|2[0-3]):h") && 				// x:dx:h
				!input.matches("([0-9]|0[0-9]|1[0-9]|2[0-3]):h") && 						// x:h
				!input.matches("([0-9]|0[0-9]|1[0-9]|2[0-3]):h[0-5]?[0-9]:m") && 			// x:hx:m
				!input.matches("[0-5]?[0-9]:m"));											// x:m 
	}

}
