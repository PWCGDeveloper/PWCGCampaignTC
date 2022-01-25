package pwcg.aar.inmission.phase2.logeval.equipmentstatus;

import pwcg.aar.inmission.phase2.logeval.AARVehicleBuilder;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;
import pwcg.core.logfiles.LogEventData;
import pwcg.core.logfiles.event.IAType3;

public class AAREquipmentStatusEvaluator
{
    public static void determineFateOfPlanesInMission (AARVehicleBuilder aarVehicleBuilder, LogEventData logEventData) throws PWCGException 
    {        
        for (LogTank logTank : aarVehicleBuilder.getLogTanks().values())
        {
            IAType3 destroyedEventForTank = logEventData.getDestroyedEvent(logTank.getId());
            if (destroyedEventForTank != null)
            {
                logTank.setTankStatus(TankStatus.STATUS_DESTROYED);
            }
        }
    }
}
