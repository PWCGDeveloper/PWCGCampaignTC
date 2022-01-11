package pwcg.aar.ui.events;

import java.util.HashMap;
import java.util.Map;

import pwcg.aar.data.AAREquipmentLosses;
import pwcg.aar.inmission.phase2.logeval.missionresultentity.LogTank;
import pwcg.aar.ui.events.model.PlaneStatusEvent;
import pwcg.campaign.Campaign;
import pwcg.campaign.tank.TankStatus;
import pwcg.core.exception.PWCGException;

public class PlaneStatusEventGenerator
{    
    private Campaign campaign;
	private Map<Integer, PlaneStatusEvent> planeStatusEvents = new HashMap<>();

    public PlaneStatusEventGenerator (Campaign campaign)
    {
        this.campaign = campaign;
    }

    public Map<Integer, PlaneStatusEvent> createPlaneLossEvents(AAREquipmentLosses equipmentLossesInMission) throws PWCGException
    {
        for (LogTank lostPlane : equipmentLossesInMission.getPlanesDestroyed().values())
        {
            PlaneStatusEvent equippedPlaneLostEvent = makePlaneLostEvent(lostPlane);
            planeStatusEvents.put(lostPlane.getTankSerialNumber(), equippedPlaneLostEvent);
        }
        
        for (LogTank lostPlane : equipmentLossesInMission.getPlanesDestroyed().values())
        {
            PlaneStatusEvent equippedPlaneLostEvent = makePlaneLostEvent(lostPlane);
            planeStatusEvents.put(lostPlane.getTankSerialNumber(), equippedPlaneLostEvent);
        }
        
        return planeStatusEvents;
    }

    private PlaneStatusEvent makePlaneLostEvent(LogTank lostPlane) throws PWCGException
    {
        boolean isNewsworthy = true;
        PlaneStatusEvent planeStatusEvent = new PlaneStatusEvent(campaign, lostPlane, TankStatus.STATUS_DESTROYED, isNewsworthy);
        return planeStatusEvent;
    }
}
