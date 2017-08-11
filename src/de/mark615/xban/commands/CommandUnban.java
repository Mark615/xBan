package de.mark615.xban.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import de.mark615.xban.XBan;
import de.mark615.xban.commands.XCommand.XCommandReturnType;
import de.mark615.xban.object.Ban;
import de.mark615.xban.object.XUtil;

public class CommandUnban extends XCommand
{
	private final XBan plugin;
	
	CommandUnban(XBan plugin)
	{
		super("unban","");
		this.plugin = plugin;
	}
	
	@Override
	public void fillSubCommands(List<XSubCommand> subcommands)
	{
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void showHelp(CommandSender sender)
	{
		sender.sendMessage(ChatColor.GREEN + XBan.PLUGIN_NAME + ChatColor.GRAY + " - " + ChatColor.YELLOW + XUtil.getMessage("command.description"));
		if(matchPermission(sender, "xban.unban")) sender.sendMessage(ChatColor.GREEN + "/unban <player> <banid>" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.unban.description"));
		
	}
	@Override
	protected XCommandReturnType run(CommandSender sender, Command command, String s, String[] args)
	{
		if(matchPermission(sender, "xban.unban"))
		{
			if(args.length != 2)
			{
				XUtil.sendCommandUsage(sender, "use: /xban <help> " + ChatColor.YELLOW + "- for help");
				return XCommandReturnType.NONE;
			}
			for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers())
			{
				if(player.getName() == args[0])
				{
					for(Ban ban : plugin.getBanManager().getPlayerBans(player.getUniqueId()))
					{
						try 
						{
							if(ban.getId() == Integer.parseInt(args[1]) && ban.getTarget() == player.getUniqueId())
							{
								if(plugin.getBanManager().removeBan(ban.getId()))
								{
									XUtil.sendFileMessage(sender, "command.unban.success", ChatColor.GREEN);
									return XCommandReturnType.SUCCESS;
								}
								else
								{
									return XCommandReturnType.NONE;
								}
							}
						}
						catch (NumberFormatException e) 
						{
							XUtil.sendCommandUsage(sender, "use: /xban <help> " + ChatColor.YELLOW + "- for help");
							return XCommandReturnType.NONE;
						}
					}
					XUtil.sendFileMessage(sender, "command.unban.message.nobans", ChatColor.RED);
					return XCommandReturnType.NOPLAYERMATCH;
				}
			}
			XUtil.sendFileMessage(sender, "command.player-not-found", ChatColor.RED);
			return XCommandReturnType.NOPLAYERMATCH;
		}
		XUtil.sendFileMessage(sender, "command.nopermission", ChatColor.RED);
		return XCommandReturnType.NOPERMISSION;
	}
}
