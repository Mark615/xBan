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
import de.mark615.xban.object.Mute;
import de.mark615.xban.object.XUtil;

public class CommandUnmute extends XCommand
{
	private final XBan plugin;
	
	CommandUnmute(XBan plugin)
	{
		super("unmute","");
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
		if(matchPermission(sender, "xban.unmute")) sender.sendMessage(ChatColor.GREEN + "/unmute <player> <muteid>" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.unmute.description"));
		
	}
	@Override
	protected XCommandReturnType run(CommandSender sender, Command command, String s, String[] args)
	{
		if(matchPermission(sender, "xban.unmute"))
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
					for(Mute mute: plugin.getBanManager().getPlayerMutes(player.getUniqueId()))
					{
						try 
						{
							if(mute.getId() == Integer.parseInt(args[1]) && mute.getTarget() == player.getUniqueId())
							{
								if(plugin.getBanManager().removeMute(mute.getId()))
								{
									XUtil.sendFileMessage(sender, "command.unmute.success", ChatColor.GREEN);
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
					XUtil.sendFileMessage(sender, "command.unmute.message.nomutes", ChatColor.RED);
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
