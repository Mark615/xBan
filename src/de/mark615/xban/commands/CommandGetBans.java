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

public class CommandGetBans extends XCommand
{

	private final XBan plugin;

	public CommandGetBans(XBan plugin)
	{
		super("getbans", "");
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
		if(matchPermission(sender, "xban.getbans")) sender.sendMessage(ChatColor.GREEN + "/getbans <player>" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.getbans.description"));
		
	}
	
	@Override
	protected XCommandReturnType run(CommandSender sender, Command command, String s, String[] args)
	{
		if(matchPermission(sender, "xban.getbans"))
		{
			if(args.length != 1)
			{
				XUtil.sendCommandUsage(sender, "use: /xban <help> " + ChatColor.YELLOW + "- for help");
				return XCommandReturnType.NONE;
			}
			for (OfflinePlayer player : Bukkit.getServer().getOfflinePlayers())
			{
				if(player.getName() == args[0])
				{
					List<Ban> bans = plugin.getBanManager().getPlayerBans(player.getUniqueId());
					if(bans.isEmpty())
					{						
						XUtil.sendFileMessage(sender, "command.unban.message.nobans", ChatColor.RED);
						return XCommandReturnType.NOPLAYERMATCH;
					}
					XUtil.sendFileMessage(sender, ChatColor.YELLOW + "command.getbans.message.player" + " " + player.getName() + ": ");
					for(Ban ban: bans)
					{
						sender.sendMessage(compileMessage(ban));						
					}
					return XCommandReturnType.SUCCESS;
				}
			}
			XUtil.sendFileMessage(sender, "command.player-not-found", ChatColor.RED);
			return XCommandReturnType.NOPLAYERMATCH;
		}
		XUtil.sendFileMessage(sender, "command.nopermission", ChatColor.RED);
		return XCommandReturnType.NOPERMISSION;
	}
	
	private String compileMessage(Ban ban)
	{
		 return (ChatColor.RED + "Id: " + ChatColor.GREEN + ban.getId() +
				 ChatColor.RED + ", Location: " + ChatColor.GREEN + ban.getBanLocation() +
				 ChatColor.RED + ", Remaining Time: " + ChatColor.GREEN + ((ban.getBanEnd() == 0) ? ("permanent") : ban.getRemainingBanTime()) + 
				 ChatColor.RED + ", Reason: " + ChatColor.GREEN + ban.getReason());
	}


}
