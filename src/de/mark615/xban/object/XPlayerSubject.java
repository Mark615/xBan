package de.mark615.xban.object;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class XPlayerSubject
{
	private UUID uuid;
	
	public XPlayerSubject(UUID uuid)
	{
		this.uuid = uuid;
	}
	
	public UUID getUUID()
	{
		return uuid;
	}
	
	public Player getPlayer()
	{
		return Bukkit.getPlayer(uuid);
	}
}
