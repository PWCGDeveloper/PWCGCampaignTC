package pwcg.gui.rofmap.brief.update;

import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.campaign.context.PWCGContext;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.rofmap.brief.model.BriefingData;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.rofmap.brief.model.BriefingUnit;
import pwcg.mission.Mission;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.unit.ITankUnit;

public class BriefingUnitUpdater
{

    public static void finalizeMission(BriefingData briefingContext) throws PWCGException
    {
        Mission mission = briefingContext.getMission();
        if (!mission.getFinalizer().isFinalized())
        {
            pushEditsToMission(briefingContext);
            mission.finalizeMission();
            mission.write();

            Campaign campaign = PWCGContext.getInstance().getCampaign();
            campaign.setCurrentMission(mission);
        }
    }

    public static void pushEditsToMission(BriefingData briefingData) throws PWCGException 
    {
        Mission mission = briefingData.getMission();
        if (!mission.getFinalizer().isFinalized())
        {
            pushUnitParametersToMission(briefingData);
            pushCrewAndPayloadToMission(briefingData);
            pushFuelToMission(briefingData);
            
        }
    }

    private static void pushUnitParametersToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        mission.getMissionOptions().getMissionTime().setMissionTime(briefingData.getMissionTime());

        for (BriefingUnit briefingUnit : briefingData.getBriefingUnits())
        {
            ITankUnit playerUnit = mission.getUnits().getPlayerUnitForCompany(briefingUnit.getCompanyId());
            List<McuWaypoint> waypoints = briefingPointToWaypoint(briefingUnit.getBriefingUnitParameters().getBriefingMapMapPoints());
            playerUnit.setWaypoints(waypoints);
        }
    }


    private static List<McuWaypoint> briefingPointToWaypoint(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        List<McuWaypoint> waypoints = new ArrayList<>();
        
        for (BriefingMapPoint briefingMapPoint : briefingMapPoints)
        {
            McuWaypoint newWaypoint = makeWaypointFromBriefing(briefingMapPoint);
            waypoints.add(newWaypoint);
        }
        
        return waypoints;
    }

    private static McuWaypoint makeWaypointFromBriefing(BriefingMapPoint waypointFromBriefing) throws PWCGException
    {
        McuWaypoint newWaypoint = new McuWaypoint(WaypointType.OBJECTIVE_WAYPOINT);
        
        Coordinate newPosition = waypointFromBriefing.getPosition();
        newWaypoint.setSpeed(waypointFromBriefing.getCruisingSpeed());
        newWaypoint.setPosition(newPosition);
        return newWaypoint;
    }

    private static void pushCrewAndPayloadToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        for (BriefingUnit briefingUnit : briefingData.getBriefingUnits())
        {
            ITankUnit playerUnit = mission.getUnits().getPlayerUnitForCompany(briefingUnit.getCompanyId());
            BriefingCrewTankUpdater crewePlaneUpdater = new BriefingCrewTankUpdater(mission.getCampaign(), playerUnit);
            crewePlaneUpdater.updatePlayerTanks(briefingUnit.getBriefingAssignmentData().getCrews());
        }
    }
    

    private static void pushFuelToMission(BriefingData briefingData) throws PWCGException
    {
        Mission mission = briefingData.getMission();
        mission.getMissionOptions().getMissionTime().setMissionTime(briefingData.getMissionTime());

        for (BriefingUnit briefingUnit : briefingData.getBriefingUnits())
        {
            ITankUnit playerUnit = mission.getUnits().getPlayerUnitForCompany(briefingUnit.getCompanyId());
            playerUnit.getUnitTanks().setFuelForUnit(briefingUnit.getSelectedFuel());
        }
    }

}
