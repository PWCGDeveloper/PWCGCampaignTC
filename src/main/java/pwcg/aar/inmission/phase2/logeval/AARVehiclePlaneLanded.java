package pwcg.aar.inmission.phase2.logeval;

import java.util.Map;

import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType3;
import pwcg.core.logfiles.event.IAType6;

public class AARVehiclePlaneLanded 
{
    private LogEventData logEventData;
    
    public AARVehiclePlaneLanded (LogEventData logEventData)
    {
        this.logEventData = logEventData;
    }

    public void buildLandedLocations(Map <String, LogTank> planeAiEntities)
    {
        notePlaneCrashedLocation(planeAiEntities);
        notePlaneLandedLocation(planeAiEntities);
    }

    private void notePlaneCrashedLocation(Map <String, LogTank> planeAiEntities)
    {
        for (IAType3 atype3 : logEventData.getDestroyedEvents())
        {
            for (LogTank planeEntity: planeAiEntities.values())
            {
                if (planeEntity.getId().equals(atype3.getVictim()))
                {
                    planeEntity.setLandAt(atype3.getLocation());
                }
            }
        }
    }

    private void notePlaneLandedLocation(Map <String, LogTank> planeAiEntities)
    {
        for (IAType6 atype6 : logEventData.getLandingEvents())
        {
            for (LogTank planeEntity: planeAiEntities.values())
            {
                if (planeEntity.getId().equals(atype6.getPid()))
                {
                    planeEntity.setLandAt(atype6.getLocation());
                }
            }
        }
    }

}

