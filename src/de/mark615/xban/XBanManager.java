package de.mark615.xban;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.mark615.xapi.object.XUtil;
import de.mark615.xban.object.XDatabase;
import de.mark615.xban.object.XPlayerSubject;

public class XBanManager
{
	private XDatabase database = null;
	private XBan plugin = null;
	private HashMap<UUID, XPlayerSubject> players;
	
	public XBanManager (XBan plugin)
	{
		this.database = new XDatabase();
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
	
	public boolean banPlayer(UUID uuid, String banLocation)
	{
		try
		{
			database.registerPlayerBan(uuid, banLocation);
		}catch(SQLException e) {
			XUtil.severe("Unable to register player ban; Database error");
			XUtil.severe(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean banPlayer(UUID uuid, String banLocation, long banEnd)
	{
		try
		{
			database.registerPlayerBan(uuid, banLocation, banEnd);
		}catch(SQLException e) {
			XUtil.severe("Unable to register player ban; Database error");
			XUtil.severe(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean banPlayer(UUID uuid, String banLocation, String banReason, long banEnd)
	{
		try
		{
			database.registerPlayerBan(uuid, banLocation, banEnd, banReason);
		}catch(SQLException e) {
			XUtil.severe("Unable to register player ban; Database error");
			XUtil.severe(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean banPlayer(UUID uuid, String banLocation, String banReason)
	{
		try
		{
			database.registerPlayerBan(uuid, banLocation, banReason);
		}catch(SQLException e) {
			XUtil.severe("Unable to register player ban; Database error");
			XUtil.severe(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean unbanPlayer(UUID uuid) 
	{
		try {
			database.unregisterPlayerBan(uuid);
		} catch (SQLException e) {
			XUtil.severe("Unable to remove player ban; Database error");
			XUtil.severe(e.getMessage());
			return false;
		}
		return true;
	}
	
	public int getBanEnd(UUID uuid)
	{
		try {
			return database.getBanEnd(uuid);
		} catch (SQLException e) {
			XUtil.severe("Database error");
			XUtil.severe(e.getMessage());
		}
		return 0;
	}
}
