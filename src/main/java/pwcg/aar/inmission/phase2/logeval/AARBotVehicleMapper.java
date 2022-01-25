package pwcg.aar.inmission.phase2.logeval;

import java.util.List;
import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType12;
import pwcg.core.utils.PWCGLogger;
import pwcg.core.utils.PWCGLogger.LogLevel;

public class AARBotVehicleMapper 
{
    private LogEventData logEventData;
    private Map <String, LogTank> tankAiEntities;
    
    public AARBotVehicleMapper (LogEventData logEventData)
    {
        this.logEventData = logEventData;
    }

    public void mapBotsToCrews(Map <String, LogTank> tankAiEntities) throws PWCGException
    {
        this.tankAiEntities = tankAiEntities;        
        mapBotsToCrewPosition(logEventData.getBots());
    }
    
    private void mapBotsToCrewPosition(List<IAType12> botList) throws PWCGException
    {
        for (IAType12 atype12Bot : botList)
        {
            String tankId = logEventData.getPlaneIdByBot(atype12Bot);

            LogTank tankResult = getMissionResultTankById(tankId);
            if (tankResult != null)
            {
                if (atype12Bot.getType().contains("BotCrewMember"))
                {
                    mapBotCrewMember(atype12Bot, tankResult);
                }
            }
        }
    }

    private void mapBotCrewMember(IAType12 atype12, LogTank tankResult) throws PWCGException
    {
        PWCGLogger.log(LogLevel.DEBUG, "Add CrewMember bot for : " + tankResult.getLogCrewMember().getSerialNumber() + "   " + atype12.getId());
        tankResult.mapBotToCrew(atype12.getId());
    }
    
    private LogTank getMissionResultTankById(String id)
    {
        for (LogTank tankResult : tankAiEntities.values())
        {
            if (tankResult.isWithTank(id))
            {
                return tankResult;
            }
        }

        return null;
    }

}

