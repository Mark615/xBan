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

public class CommandGetMutes extends XCommand
{

	private final XBan plugin;

	public CommandGetMutes(XBan plugin)
	{
		super("getmutes", "");
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
		if(matchPermission(sender, "xban.getmutes")) sender.sendMessage(ChatColor.GREEN + "/getmutes <player>" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.getmutes.description"));
		
	}
	
	@Override
	protected XCommandReturnType run(CommandSender sender, Command command, String s, String[] args)
	{
		if(matchPermission(sender, "xban.getmutes"))
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
					List<Mute> mutes = plugin.getBanManager().getPlayerMutes(player.getUniqueId());
					if(mutes.isEmpty())
					{						
						XUtil.sendFileMessage(sender, "command.unmute.message.nomutes", ChatColor.RED);
						return XCommandReturnType.NOPLAYERMATCH;
					}
					XUtil.sendFileMessage(sender, ChatColor.YELLOW + "command.getmutes.message.player" + " " + player.getName() + ": ");
					for(Mute mute: mutes)
					{
						sender.sendMessage(compileMessage(mute));						
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
	
	private String compileMessage(Mute mute)
	{
		 return (ChatColor.RED + "Id: " + ChatColor.GREEN + mute.getId() +
				 ChatColor.RED + ", Location: " + ChatColor.GREEN + mute.getMuteLocation() +
				 ChatColor.RED + ", Remaining Time: " + ChatColor.GREEN + ((mute.getMuteEnd() == 0) ? ("permanent") : mute.getRemainingMuteTime()) + 
				 ChatColor.RED + ", Reason: " + ChatColor.GREEN + mute.getReason());
	}


}
