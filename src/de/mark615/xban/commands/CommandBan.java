package de.mark615.xban.commands;

import java.awt.Color;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.mark615.xban.XBan;
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
//		//TODO beispiele kann weg
//		subcommands.add(new XSubCommand("accept", "a"));
//		subcommands.add(new XSubCommand("decline", "r"));
//		subcommands.add(new XSubCommand("show", "s"));
	}
	

	@Override
	protected void showHelp(CommandSender p)
	{
		//show ban help
		p.sendMessage(ChatColor.GREEN + XBan.PLUGIN_NAME + ChatColor.GRAY + " - " + ChatColor.YELLOW + XUtil.getMessage("command.description"));
		if(matchPermission(p, "xban.ban")) p.sendMessage(ChatColor.GREEN + "/ban <player> <server|this|[world]> <reason> <x:dx:hx:m>" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.ban.description"));
//		p.sendMessage(ChatColor.GREEN + XBan.PLUGIN_NAME + ChatColor.GRAY + " - " + ChatColor.YELLOW + XUtil.getMessage("command.description"));
//		p.sendMessage(ChatColor.GREEN + "/xagb accept" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.xagb.accept.description"));
//		p.sendMessage(ChatColor.GREEN + "/xagb decline" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.xagb.decline.description"));
//		p.sendMessage(ChatColor.GREEN + "/xagb show" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.xagb.show.description"));
	}

	@Override
	public XCommandReturnType run(CommandSender sender, Command command, String s, String[] args)
	{
		
		if(matchPermission(sender, "xban.ban")) {
			
			List<String> worldNames = null;
			for(World world : Bukkit.getServer().getWorlds())
			{
				worldNames.add(world.getName());
			}
			//  https://regex101.com/r/ICwEH4/1  help for regex
			if(args.length<3 || args.length>4 || (!worldNames.contains(args[1]) && (args[1] != "server" && args[1] != "this")) || (args.length == 4) ? matchesTimeFormat(args[3]) : false)
			{
				XUtil.sendCommandUsage(sender, "use: /xban <help> " + ChatColor.YELLOW + "- for help");
				return XCommandReturnType.NONE;
			}
			
			for (Player player : Bukkit.getServer().getOnlinePlayers())
			{
				if(player.getPlayerListName() == args[0])
				{

					//default to serverban if sender !instanceof Player and args[1] == "this"
					if(args.length == 4)
					{
						plugin.getBanManager().banPlayer(player.getUniqueId(),
								(sender instanceof Player) ? ((args[1] == "this") ? ((Player)sender).getWorld().getName() : args[1]) : "server",
								args[2], 
								getBanEnd(args[3]));
						
					}else
					{
						plugin.getBanManager().banPlayer(player.getUniqueId(),
								(sender instanceof Player) ? ((args[1] == "this") ? ((Player)sender).getWorld().getName() : args[1]) : "server",
								args[2]);
					}
					
					//kick player if player in banned area
					
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
		return (!input.matches("[0-9]+:d([0-9]|0[0-9]|1[0-9]|2[0-3]):h[0-5]?[0-9]:m") && 	// x:dx:hx:m
				!input.matches("[0-9]+:d") && 												// x:d
				!input.matches("[0-9]+:d([0-9]|0[0-9]|1[0-9]|2[0-3]):h") && 				// x:dx:h
				!input.matches("([0-9]|0[0-9]|1[0-9]|2[0-3]):h") && 						// x:h
				!input.matches("([0-9]|0[0-9]|1[0-9]|2[0-3]):h[0-5]?[0-9]:m") && 			// x:hx:m
				!input.matches("[0-5]?[0-9]:m"));											// x:m 
	}

}
