package de.mark615.xban;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import de.mark615.xban.object.XUtil;

public class SettingManager
{
    static SettingManager instance = new SettingManager();
   
    public static SettingManager getInstance()
    {
    	return instance;
    }
    
    FileConfiguration config;
    File cFile;
    
    FileConfiguration message;
    File mFile;
    
    private int dataID;
   
    @SuppressWarnings("deprecation")
	public void setup(Plugin p)
    {
    	if (!p.getDataFolder().exists())
    		p.getDataFolder().mkdir();
    	
    	cFile = new File(p.getDataFolder(), "config.yml");
    	if(!cFile.exists())
    		p.saveResource("config.yml", true);

		//Store it
		config = YamlConfiguration.loadConfiguration(cFile);
		config.options().copyDefaults(true);
		
		//load default config
		InputStream defConfigStream = p.getResource("config.yml");
		YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
		config.setDefaults(defConfig);
		saveConfig();
        

		//Load messages
        mFile = new File(p.getDataFolder(), "messages.yml");
        if(!mFile.exists())
			p.saveResource("messages.yml", true);
		
		//Store it
		message = YamlConfiguration.loadConfiguration(mFile);
		message.options().copyDefaults(true);
		
		//Load default messages
		InputStream defMessageStream = p.getResource("messages.yml");
		YamlConfiguration defMessages = YamlConfiguration.loadConfiguration(defMessageStream);
		message.setDefaults(defMessages);
		try
		{
			message.save(mFile);
		}
		catch (IOException e)
		{
			XUtil.severe("Could not save message.yml!");
		}
    }
    
   
//---------Configuration section
    
    public FileConfiguration getConfig()
    {
        return config;
    }
   
    public void saveConfig()
    {
        try {
            config.save(cFile);
        }
        catch (IOException e) {
        	XUtil.severe("Could not save config.yml!");
        }
    }
   
    public void reloadConfig()
    {
    	config = YamlConfiguration.loadConfiguration(cFile);
    }
    
    public boolean hasCheckVersion()
    {
    	return config.getBoolean("updatecheck", true);
    }
    
    
    
    public void setAPIKey(UUID uuid)
    {
    	config.set("apikey", uuid.toString());
    }
    
    public UUID getAPIKey()
    {
    	return config.getString("apikey", null) == null ? null : UUID.fromString(config.getString("apikey"));
    }
    
    public void setDataID(int dataID)
    {
    	this.dataID = dataID;
    }
    
    public int getDataID()
    {
    	return dataID;
    }
    

//---------Message section
    
    public FileConfiguration getMessage()
    {
        return message;
    }
   
    public void reloadMessage()
    {
    	message = YamlConfiguration.loadConfiguration(mFile);
    }
}
