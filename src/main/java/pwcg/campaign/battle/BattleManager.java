package pwcg.campaign.battle;

import java.util.Date;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.io.json.BattleIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;

public class BattleManager
{
	private Battles battles = new Battles();
    private FrontMapIdentifier map;

	public BattleManager(FrontMapIdentifier map)
	{
	    this.map = map;
	}
	
	public void initialize()
	{
        try
        {
            battles = BattleIOJson.readJson(map.getMapName());
        }
        catch (Exception e)
        {
            PWCGLogger.logException(e);
        }
	}

    public Battle getBattleForCampaign(FrontMapIdentifier mapId, Coordinate position, Date date) 
    {     
        try
        {
            for (Battle battle : battles.getBattles())
            {
                if (isBattleAtRightTime(date, battle))
                {
	                if (isBattleOnRightMap(battle, mapId))
	                {
                        return battle;
                    }
                }
            }
        }
        catch (Throwable t)
        {
           PWCGLogger.logException(t);
        }
        
        return null;
    }

    private boolean isBattleOnRightMap(Battle battle, FrontMapIdentifier mapId)
	{
    	if (mapId == null)
    	{
    		return false;
    	}
    	
    	if (battle.getMap() == mapId)
    	{
    		return true;
    	}
    	
		return false;
	}

	private boolean isBattleAtRightTime(Date date, Battle battle) throws PWCGException
    {
	    if (DateUtils.isDateInRange(date, battle.getStartDate(), battle.getStopDate()))
        {
        	return true;
        }
            
        return false;
    }
}
