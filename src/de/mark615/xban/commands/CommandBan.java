package de.mark615.xban.commands;

import java.util.List;

import org.bukkit.ChatColor;
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
		//TODO beispiele
		subcommands.add(new XSubCommand("accept", "a"));
		subcommands.add(new XSubCommand("decline", "r"));
		subcommands.add(new XSubCommand("show", "s"));
	}
	

	@Override
	protected void showHelp(CommandSender p)
	{
		p.sendMessage(ChatColor.GREEN + XBan.PLUGIN_NAME + ChatColor.GRAY + " - " + ChatColor.YELLOW + XUtil.getMessage("command.description"));
		p.sendMessage(ChatColor.GREEN + "/xagb accept" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.xagb.accept.description"));
		p.sendMessage(ChatColor.GREEN + "/xagb decline" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.xagb.decline.description"));
		p.sendMessage(ChatColor.GREEN + "/xagb show" + ChatColor.YELLOW + " - " + XUtil.getMessage("command.xagb.show.description"));
	}

	@Override
	public XCommandReturnType run(CommandSender sender, Command command, String s, String[] args)
	{
		if (!this.isSubCommand(args[0]))
		{
			XUtil.sendCommandUsage(sender, "use: /ban <help/?> " + ChatColor.YELLOW + "- for help");
			return XCommandReturnType.NONE;
		}
		
		if (!(sender instanceof Player))
		{
			XUtil.sendFileMessage(sender, "command.no-consol-command");
			return XCommandReturnType.NEEDTOBEPLAYER;
		}

		Player target = (Player)sender;
		if (matchesSubCommand("accept", args[0]))
		{
			if (matchPermission(sender, "xban.ban.ban"))
			{
				
			}
			else
			{
				
			}
			
			return XCommandReturnType.SUCCESS;
		}
		
		if (matchesSubCommand("decline", args[0]))
		{
			return XCommandReturnType.SUCCESS;
		}
		
		return XCommandReturnType.NOCOMMAND;
	}

}
