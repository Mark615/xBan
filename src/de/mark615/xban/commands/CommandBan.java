package de.mark615.xban.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mark615.xban.XBan;
import de.mark615.xban.object.Ban;
import de.mark615.xban.object.XUtil;

public class CommandBan extends XCommand
{
	private final XBan plugin;

	public CommandBan(XBan plugin)
	{
		super("ban", "");
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
		if(matchPermission(p, "xban.ban")) p.sendMessage(ChatColor.GREEN + "/ban <player> <server|this|[world]> <reason> [<x:dx:hx:m>]" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.ban.description"));
	}

	@Override
	public XCommandReturnType run(CommandSender sender, Command command, String s, String[] args)
	{
		
		if(matchPermission(sender, "xban.ban")) {
			
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
					//default to serverban if sender !instanceof Player and args[1] == "this"
					if(args.length == 4)
					{
						Ban ban = new Ban(
									player.getUniqueId(), 
									(sender instanceof Player) ? ((Player)sender).getUniqueId() : plugin.getBanManager().getServerUUID(), 
									System.currentTimeMillis()/1000L, 
									getBanEnd(args[3]), 
									getLocation(args[1],sender), 
									args[2]);
						if(!plugin.getBanManager().addBan(ban))
						{
							return XCommandReturnType.NONE;
						}
						
					}else
					{
						Ban ban = new Ban(
								player.getUniqueId(), 
								(sender instanceof Player) ? ((Player)sender).getUniqueId() : plugin.getBanManager().getServerUUID(), 
								System.currentTimeMillis()/1000L, 
								0, 
								getLocation(args[1],sender), 
								args[2]);
						if(!plugin.getBanManager().addBan(ban))
						{
							return XCommandReturnType.NONE;
						}
						
					}
					if(player.isOnline())
					{
						Player onlinePlayer = Bukkit.getPlayer(player.getUniqueId());
						if(onlinePlayer.getWorld().getName() == args[1] || args[1] == "server" ||(sender instanceof Player) ? (onlinePlayer.getWorld().getName() == ((Player)sender).getWorld().getName()) : true)
						{
							//Kick Player
							if(args.length == 4) 
							{
								onlinePlayer.kickPlayer("command.ban.message.time" + ": " + getLocation(args[1],sender)  + ". " + 
														"command.ban.message.expires" + ": " + getRemainingBanTime(getBanEnd(args[3])) + ". " + 
														"command.ban.message.reason" + ": " + args[2]);			
							}else {
								onlinePlayer.kickPlayer("command.ban.message.perm" + ": " + getLocation(args[1],sender) + ". " + 
														"command.ban.message.reason" + ": " + args[2]);			
							}
						}
					}
					XUtil.sendFileMessage(sender, "command.ban.success", ChatColor.GREEN);
					return XCommandReturnType.SUCCESS;					
				}
			}
			XUtil.sendFileMessage(sender, "command.player-not-found", ChatColor.RED);
			return XCommandReturnType.NOPLAYERMATCH;
			
		}
		XUtil.sendFileMessage(sender, "command.nopermission", ChatColor.RED);
		return XCommandReturnType.NOPERMISSION;
		
	}
	
	private long getBanEnd(String input)
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
	
	private String getRemainingBanTime(long seconds)
	{
		seconds -= System.currentTimeMillis()/1000;
		int day = (int) TimeUnit.SECONDS.toDays(seconds);
		long hour = TimeUnit.SECONDS.toHours(seconds) - (day * 24);
		long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
		long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

		return day + "d " + hour + "h " + minute + "m " + second + "s";
	}
}
