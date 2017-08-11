package de.mark615.xban.events;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

import de.mark615.xban.XBan;
import de.mark615.xban.object.Ban;
import de.mark615.xban.object.Mute;
import de.mark615.xban.object.XUtil;

public class EventListener implements Listener
{
	private XBan plugin;

	public EventListener(XBan plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLogin(final PlayerLoginEvent e)
	{
        final UUID player = e.getPlayer().getUniqueId();
        List<Ban> bans = plugin.getBanManager().getPlayerBans(player);
        if(!bans.isEmpty())
        {
        	for(Ban ban : bans)
        	{
        		if(ban.getBanEnd() != 0 && ban.getBanEnd()<System.currentTimeMillis()/1000)
        		{
        			plugin.getBanManager().removeBan(ban.getId());
        		}else {
	        		if(ban.getBanLocation() == "server")
	        		{
	        			if(ban.getBanEnd() == 0)
	        			{
		        			e.disallow(Result.KICK_BANNED, "command.ban.message.perm" + ": " + ban.getBanLocation() + ". " + "command.ban.message.reason" + ": " + ban.getReason());
	        			}else {
	        				e.disallow(Result.KICK_BANNED, "command.ban.message.time" + ": " + ban.getBanLocation() + ". " + "command.ban.message.expires" + ": " + ban.getRemainingBanTime() + ". " + "command.ban.message.reason" + ": " + ban.getReason());
	        			}
	        		}
        		}
        	}
        }
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		//Maybe kick player if he joins in banned area
		final Player p = e.getPlayer();
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		final Player p = e.getPlayer();
	}
	
	@EventHandler
	public void onPlayerChatEvent(AsyncPlayerChatEvent e)
	{
		final Player p = e.getPlayer();
		UUID player = e.getPlayer().getUniqueId();
        List<Mute> mutes = plugin.getBanManager().getPlayerMutes(player);
        if(!mutes.isEmpty())
        {
        	for(Mute mute : mutes)
        	{
        		if(mute.getMuteEnd() != 0 && mute.getMuteEnd()<System.currentTimeMillis()/1000)
        		{
        			plugin.getBanManager().removeMute(mute.getId());
        		}else {
	        		if(mute.getMuteLocation() == "server" || p.getWorld().getName() == mute.getMuteLocation())
	        		{
	        			e.setCancelled(true);
	        			if(mute.getMuteEnd() == 0)
	        			{
	        				XUtil.sendMessage(p, "command.mute.message.perm" + ": " + mute.getMuteLocation() + ". " + "command.mute.message.reason" + ": " + mute.getReason());
	        			}else {
	        				XUtil.sendMessage(p,  "command.mute.message.time" + ": " + mute.getMuteLocation() + ". " + "command.mute.message.expires" + ": " + mute.getRemainingMuteTime() + ". " + "command.mute.message.reason" + ": " + mute.getReason());
	        			}
	        		}
        		}
        	}
        }
	}	
}
