package de.mark615.xban.object;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

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
		stmt.execute("CREATE TABLE IF NOT EXISTS xban (id INTEGER PRIMARY KEY, uuid TEXT not null, banLocation TEXT not null, banEnd INTEGER, banReason TEXT)");
		stmt.close();
	}
	
	public void registerPlayerBan(UUID uuid, String banLocation) throws SQLException
	{
		stmt = con.createStatement();
		stmt.execute("INSERT INTO xban(uuid, banLocation) " + "values ('" + uuid + "', '" + banLocation + "'");
		stmt.close();
	}
	
	public void registerPlayerBan(UUID uuid, String banLocation, long banEnd) throws SQLException
	{
		stmt = con.createStatement();
		stmt.execute("INSERT INTO xban(uuid, banLocation, banEnd) " + "values ('" + uuid + "', '" + banLocation + "', " + banEnd);
		stmt.close();
	}
	
	public void registerPlayerBan(UUID uuid, String banLocation, long banEnd, String banReason) throws SQLException
	{
		stmt = con.createStatement();
		stmt.execute("INSERT INTO xban(uuid, banLocation, banEnd) " + "values ('" + uuid + "', '" + banLocation + "', " + banEnd + ", '" + banReason + "'");
		stmt.close();
	}
	
	public void registerPlayerBan(UUID uuid, String banLocation, String banReason) throws SQLException
	{
		stmt = con.createStatement();
		stmt.execute("INSERT INTO xban(uuid, banLocation, banEnd, banReason) " + "values ('" + uuid + "', '" + banLocation + "', " + 0 + ", '" + banReason + "'");
		stmt.close();
	}
	
	public void unregisterPlayerBan(UUID uuid) throws SQLException
	{
		stmt = con.createStatement();
		stmt.execute("DELETE FROM xban WHERE uuid='" + uuid + "'");
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