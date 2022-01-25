package pwcg.aar.ui.events;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.ui.events.model.TankStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;

public class TankStatusEventGenerator
{    
    private Campaign campaign;
	private Map<Integer, TankStatusEvent> tankStatusEvents = new HashMap<>();

    public TankStatusEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Map<Integer, TankStatusEvent> createTankLossEvents(AAREquipmentLosses equipmentLossesInMission) throws PWCGException
    {
        for (LogTank lostTank : equipmentLossesInMission.getTanksDestroyed().values())
        {
            TankStatusEvent equippedTankLostEvent = makeTankLostEvent(lostTank);
            tankStatusEvents.put(lostTank.getTankSerialNumber(), equippedTankLostEvent);
        }
        
        return tankStatusEvents;
    }

    private TankStatusEvent makeTankLostEvent(LogTank lostTank) throws PWCGException
    {
        boolean isNewsworthy = true;
        TankStatusEvent tankStatusEvent = new TankStatusEvent(campaign, lostTank, TankStatus.STATUS_DESTROYED, isNewsworthy);
        return tankStatusEvent;
    }
}
