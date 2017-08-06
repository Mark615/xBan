package de.mark615.xban.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.mark615.xban.XBan;

public class EventListener implements Listener
{
	private XBan plugin;

	public EventListener(XBan plugin)
	{
		this.plugin = plugin;
	}

	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
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
	}
	
}
