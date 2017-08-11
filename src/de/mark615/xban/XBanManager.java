package de.mark615.xban;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.mark615.xapi.object.XUtil;
import de.mark615.xban.database.XDatabase;
import de.mark615.xban.object.Ban;
import de.mark615.xban.object.Mute;
import de.mark615.xban.object.XPlayerSubject;

public class XBanManager
{
	private XDatabase database = null;
	private XBan plugin = null;
	private HashMap<UUID, XPlayerSubject> players;
	private UUID serverUUID;
	
	public XBanManager (XBan plugin)
	{
		serverUUID = UUID.fromString("00000000-1111-2222-3333-4444-555555555555");
		this.database = new XDatabase();
		this.plugin = plugin;
		this.players = new HashMap<>();
	}
	
	public XPlayerSubject getXPlayerSubject(UUID uuid)
	{
		return players.get(uuid);
	}
	
	public boolean addBan(Ban ban)
	{
		try
		{
			database.addPlayerBan(ban);
		}catch(SQLException e) {
			XUtil.severe("Database Error; Unable to add player ban");
			XUtil.severe(e.getMessage());
			return false;
		}
		return true;
	}
	
	public List<Ban> getPlayerBans(UUID uuid)
	{
		List<Ban> bans = new ArrayList<>();
		
		try {
			bans = database.getPlayerBans(uuid);
		} catch (SQLException e) {
			XUtil.severe("Database Error; Unable to retrieve player bans");
			XUtil.severe(e.getMessage());
		}
		return bans;
	}
	
	public boolean removeBan(int id) 
	{
		try {
			database.removePlayerBan(id);
		} catch (SQLException e) {
			XUtil.severe("Database Error; Unable to remove player ban");
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
	
	public boolean addMute(Mute mute)
	{
		try
		{
			database.addPlayerMute(mute);
		}catch(SQLException e) {
			XUtil.severe("Database Error; Unable to add player mute");
			XUtil.severe(e.getMessage());
			return false;
		}
		return true;
	}
	
	public List<Mute> getPlayerMutes(UUID uuid)
	{
		List<Mute> mutes = new ArrayList<>();
		
		try {
			mutes = database.getPlayerMutes(uuid);
		} catch (SQLException e) {
			XUtil.severe("Database Error; Unable to retrieve player mutes");
			XUtil.severe(e.getMessage());
		}
		return mutes;
	}
	
	public boolean removeMute(int id) 
	{
		try {
			database.removePlayerMute(id);
		} catch (SQLException e) {
			XUtil.severe("Database Error; Unable to remove player mute");
			XUtil.severe(e.getMessage());
			return false;
		}
		return true;
	}
	
	public int getMuteEnd(UUID uuid)
	{
		try {
			return database.getMuteEnd(uuid);
		} catch (SQLException e) {
			XUtil.severe("Database error");
			XUtil.severe(e.getMessage());
		}
		return 0;
	}

	public UUID getServerUUID() {
		return serverUUID;
	}
}
