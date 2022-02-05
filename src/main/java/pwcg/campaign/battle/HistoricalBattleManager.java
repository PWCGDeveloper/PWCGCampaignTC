package pwcg.campaign.battle;

import java.util.Date;

import pwcg.campaign.context.FrontMapIdentifier;
import pwcg.campaign.io.json.BattleIOJson;
import pwcg.core.exception.PWCGException;
import pwcg.core.utils.DateUtils;
import pwcg.core.utils.PWCGLogger;

public class HistoricalBattleManager
{
	private Battles battles = new Battles();
    private FrontMapIdentifier map;

	public HistoricalBattleManager(FrontMapIdentifier map)
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

    public HistoricalBattle getBattleForCampaign(FrontMapIdentifier mapId, Date date) 
    {     
        try
        {
            for (HistoricalBattle battle : battles.getBattles())
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

    public boolean isHistoricalBattleActive(FrontMapIdentifier mapId, Date date) 
    {     
        try
        {
            if(getBattleForCampaign(mapId, date) != null)
            {
                return true;
            }
        }
        catch (Throwable t)
        {
           PWCGLogger.logException(t);
        }
        
        return false;
    }

    private boolean isBattleOnRightMap(HistoricalBattle battle, FrontMapIdentifier mapId)
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

	private boolean isBattleAtRightTime(Date date, HistoricalBattle battle) throws PWCGException
    {
	    if (DateUtils.isDateInRange(date, battle.getStartDate(), battle.getStopDate()))
        {
        	return true;
        }
            
        return false;
    }
}
