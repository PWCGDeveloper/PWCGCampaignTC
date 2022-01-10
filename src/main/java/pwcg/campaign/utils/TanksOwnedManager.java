package pwcg.campaign.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import pwcg.campaign.context.PWCGDirectoryUserManager;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class TanksOwnedManager 
{

	static private ArrayList<String> tanksOwnedList = new ArrayList<String>();
	
	static private TanksOwnedManager instance = null;
	
	private TanksOwnedManager ()
	{
	}
	
	public static TanksOwnedManager getInstance()
	{
		if (instance == null)
		{
			instance = new TanksOwnedManager();
			instance.read();
		}
		
		return instance;
	}
	
	public void write() throws PWCGException 
	{
		try
        {
            String userfilename = PWCGDirectoryUserManager.getInstance().getPwcgUserConfigDir() + "TanksOwned.config"; 
            File userFile = new File(userfilename);		
            BufferedWriter writer = new BufferedWriter(new FileWriter(userFile));
            
            for(int i = 0; i < tanksOwnedList.size(); ++i)
            {
            	String tank = tanksOwnedList.get(i);
            	writer.write(tank);
            	writer.newLine();
            }
            
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            PWCGLogger.logException(e);
            throw new PWCGException(e.getMessage());
        }
	}
	
	public void read() 
	{
		try
		{
			String tanksOwnedFilename = PWCGDirectoryUserManager.getInstance().getPwcgUserConfigDir() + "TanksOwned.config"; 
			File tanksOwnedFile = new File(tanksOwnedFilename);
			
			BufferedReader reader = new BufferedReader(new FileReader(tanksOwnedFile));
			String line = "";
			
			while ((line = reader.readLine()) != null) 
			{
				line = line.trim();
	
				if (line != null && line.length() > 0)
				{
					tanksOwnedList.add(line);
				}
			}
			
			reader.close();
		}
		catch (Exception e)
		{
			PWCGLogger.log(LogLevel.DEBUG, "Planes owned file not found");
		}
	}
	
	public boolean isTankOwned(String tankName)
	{		
	for(int i = 0; i < tanksOwnedList.size(); ++i)
		{
			String tank = tanksOwnedList.get(i);
			if (tankName.equalsIgnoreCase(tank))
			{
				return true;
			}
		}
		
		return false;
	}

	public boolean hasTanksOwned()
	{
		if (tanksOwnedList.size() > 0)
		{
			return true;
		}

		return false;
	}

	public void clear()
	{
		tanksOwnedList.clear();
	}

	public void setTankOwned(String tankName)
	{
		tanksOwnedList.add(tankName);
	}
}
