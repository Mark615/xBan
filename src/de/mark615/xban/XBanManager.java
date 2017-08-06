package de.mark615.xban;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import de.mark615.xban.object.XPlayerSubject;

public class XBanManager
{
	private XBan plugin;
	private HashMap<UUID, XPlayerSubject> players;
	
	public XBanManager (XBan plugin)
	{
		this.plugin = plugin;
		this.players = new HashMap<>();
	}
	
	public XPlayerSubject getXPlayerSubject(UUID uuid)
	{
		return players.get(uuid);
	}
	
	public void registerPlayer(Player p)
	{
		
	}
	
	public void unregisterPlayer(UUID uuid)
	{
		
	}
}
