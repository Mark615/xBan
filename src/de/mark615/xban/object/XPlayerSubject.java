package de.mark615.xban.object;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class XPlayerSubject
{
	private UUID uuid;
	private int dbid;
	
	public XPlayerSubject(int dbid, UUID uuid)
	{
		this.uuid = uuid;
		this.dbid = dbid;
	}
	
	public UUID getUUID()
	{
		return uuid;
	}
	
	public Player getPlayer()
	{
		return Bukkit.getPlayer(uuid);
	}

	public int getDbid() {
		return dbid;
	}
}
