package pwcg.mission.platoon;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

import pwcg.campaign.Campaign;
import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.MissionBeginUnit;
import pwcg.mission.ground.builder.ArmoredPlatoonResponsiveRoute;
import pwcg.mission.mcu.McuWaypoint;

public class PlatoonWaypoints
{
    private MissionBeginUnit missionBeginUnit;
    private List<McuWaypoint> waypoints = new ArrayList<>();
    private List<ArmoredPlatoonResponsiveRoute> responsiveRoutes = new ArrayList<>();

    public void write(BufferedWriter writer) throws PWCGException
    {
        missionBeginUnit.write(writer);
        for(McuWaypoint waypoint : waypoints)
        {
            waypoint.write(writer);
        }

        for(ArmoredPlatoonResponsiveRoute responsiveRoutes : responsiveRoutes)
        {
            responsiveRoutes.write(writer);
        }
    }

    public void setWaypoints(Campaign campaign, Coordinate basePosition, int leadTankIndex, List<McuWaypoint> waypoints) throws PWCGException
    {
        this.missionBeginUnit = new MissionBeginUnit(basePosition);
        missionBeginUnit.linkToMissionBegin(waypoints.get(0).getIndex());

        this.waypoints = waypoints;
        for(McuWaypoint waypoint : waypoints)
        {
            waypoint.setObject(leadTankIndex);
        }
    }

    public void updateWaypointsFromBriefing(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        removeUnwantedWaypoints(briefingMapPoints);
        modifyWaypointsFromBriefing(briefingMapPoints);
        addBriefingMapPoints(briefingMapPoints);
    }

    private void removeUnwantedWaypoints(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        PlatoonWaypointsRemover waypointsRemover = new PlatoonWaypointsRemover(waypoints);
        waypoints = waypointsRemover.removeUnwantedWaypoints(briefingMapPoints);
    }

    private void modifyWaypointsFromBriefing(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        PlatoonWaypointModifier waypointModifier = new PlatoonWaypointModifier(waypoints);
        waypoints = waypointModifier.modifyWaypointsFromBriefing(briefingMapPoints);
    }

    private void addBriefingMapPoints(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        PlatoonWaypointsAdder waypointsAdder = new PlatoonWaypointsAdder(waypoints);
        waypoints = waypointsAdder.addWaypointFromBriefing(briefingMapPoints);
    }


    public List<McuWaypoint> getWaypoints()
    {
        return waypoints;
    }

    public void addResponsiveRoute(int leadTankIndex, ArmoredPlatoonResponsiveRoute platoonResponsiveRoute)
    {
        platoonResponsiveRoute.setWaypointObject(leadTankIndex);
        responsiveRoutes.add(platoonResponsiveRoute);
    }

    public List<ArmoredPlatoonResponsiveRoute> getResponsiveRoutes()
    {
        return responsiveRoutes;
    }
}
