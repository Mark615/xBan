package de.mark615.xban.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.mark615.xban.object.Ban;
import de.mark615.xban.object.Mute;
import de.mark615.xban.object.XUtil;

public class XDatabase
{
	private Connection con;
	private Statement stmt;
	
	public XDatabase()
	{
		this.con = null;
		this.stmt = null;
	}
	
	public boolean isValid()
	{
	    try {
	    	Class.forName("org.sqlite.JDBC");
	    	con = DriverManager.getConnection("jdbc:sqlite:plugins/xBan/xban.db");
	    } catch ( Exception e )
	    {
	    	XUtil.severe("Can't open database");
	    	XUtil.severe(e.getMessage());
	    	return false;
	    }
	    
	    try
	    {
	    	loadDatabase();
	    }
	    catch (SQLException e)
	    {
	    	XUtil.severe("Can't load database");
	    	XUtil.severe(e.getMessage());
	    }
	    return true;
	}
	
	private void loadDatabase() throws SQLException
	{
		//user database 
		stmt = con.createStatement();
		stmt.execute("CREATE TABLE IF NOT EXISTS xban (id INTEGER PRIMARY KEY, target TEXT not null, sender TEXT not null, banStart INTEGER not null, banEnd INTEGER not null, banLocation TEXT not null, banReason TEXT not null)");
		stmt.close();
		stmt = con.createStatement();
		stmt.execute("CREATE TABLE IF NOT EXISTS xmute (id INTEGER PRIMARY KEY, target TEXT not null, sender TEXT not null, muteStart INTEGER not null, muteEnd INTEGER not null, muteLocation TEXT not null, muteReason TEXT not null)");
		stmt.close();
	}
	
	public void addPlayerMute(Mute mute) throws SQLException
	{
		stmt = con.createStatement();
		stmt.execute("INSERT INTO xmute(target, moderator, muteLocation, muteStart, muteEnd, muteReason) " + "values ('" + mute.getTarget() + "', '" + mute.getSender() + "', '" + mute.getMuteStart() + "', '" + mute.getMuteEnd() + "', " + mute.getMuteLocation() + "', '" + mute.getReason() + "'");
		stmt.close();
	}
	
	public List<Mute> getPlayerMutes(UUID uuid) throws SQLException
	{
		stmt = con.createStatement();
		List<Mute> muteList = new ArrayList<>();
		ResultSet result = stmt.executeQuery("SELECT * FROM xban WHERE uuid='" + uuid + "'");
		if(result.next())
		{
			muteList.add(new Mute(result.getInt("id"), UUID.fromString(result.getString("target")), UUID.fromString(result.getString("sender")), (long) result.getInt("muteStart"), (long) result.getInt("muteEnd"), result.getString("muteLocation"), result.getString("muteReason")));
			
		}
		return muteList;
	}
	
	public void removePlayerMute(int id) throws SQLException
	{
		stmt = con.createStatement();
		stmt.execute("DELETE FROM xmute WHERE id='" + id + "'");
		stmt.close();
	}
	
	public int getMuteEnd(UUID uuid) throws SQLException
	{
		stmt = con.createStatement();
		ResultSet result = stmt.executeQuery("SELECT muteEnd FROM xmute WHERE uuid='" + uuid + "'");
		if(result.next())
		{
			return result.getInt("muteEnd");
		}
		return 0;
	}
	public String getMuteReason(UUID uuid) throws SQLException
	{
		stmt = con.createStatement();
		ResultSet result = stmt.executeQuery("SELECT muteReason FROM xmute WHERE uuid='" + uuid + "'");
		if(result.next())
		{
			return result.getString("muteReason");
		}
		return null;
	}
	
	public void addPlayerBan(Ban ban) throws SQLException
	{
		stmt = con.createStatement();
		stmt.execute("INSERT INTO xban(target, moderator, banLocation, banStart, banEnd, banReason) " + "values ('" + ban.getTarget() + "', '" + ban.getSender() + "', '" + ban.getBanStart() + "', '" + ban.getBanEnd() + "', " + ban.getBanLocation() + "', '" + ban.getReason() + "'");
		stmt.close();
	}
	
	public List<Ban> getPlayerBans(UUID uuid) throws SQLException
	{
		stmt = con.createStatement();
		List<Ban> banList = new ArrayList<>();
		ResultSet result = stmt.executeQuery("SELECT * FROM xban WHERE uuid='" + uuid + "'");
		if(result.next())
		{
			banList.add(new Ban(result.getInt("id"), UUID.fromString(result.getString("target")), UUID.fromString(result.getString("sender")), (long) result.getInt("banStart"), (long) result.getInt("banEnd"), result.getString("banLocation"), result.getString("banReason")));
			
		}
		return banList;
	}
	
	public void removePlayerBan(int id) throws SQLException
	{
		stmt = con.createStatement();
		stmt.execute("DELETE FROM xban WHERE id='" + id + "'");
		stmt.close();
	}
	
	public int getBanEnd(UUID uuid) throws SQLException
	{
		stmt = con.createStatement();
		ResultSet result = stmt.executeQuery("SELECT banEnd FROM xban WHERE uuid='" + uuid + "'");
		if(result.next())
		{
			return result.getInt("banEnd");
		}
		return 0;
	}
	public String getBanReason(UUID uuid) throws SQLException
	{
		stmt = con.createStatement();
		ResultSet result = stmt.executeQuery("SELECT banReason FROM xban WHERE uuid='" + uuid + "'");
		if(result.next())
		{
			return result.getString("banReason");
		}
		return null;
	}
}