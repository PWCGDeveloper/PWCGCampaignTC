package pwcg.mission.platoon;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.mcu.McuWaypoint;

public class PlatoonWaypointsAdder
{
    private List<McuWaypoint> waypoints = new ArrayList<>();


    public PlatoonWaypointsAdder(List<McuWaypoint> waypoints)
    {
        this.waypoints = waypoints;
    }

    public List<McuWaypoint> addWaypointFromBriefing(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        BriefingMapPoint previousMapPointFromBriefing = null;
        for (BriefingMapPoint briefingMapPoint : briefingMapPoints)
        {
            if (previousMapPointFromBriefing != null)
            {
                McuWaypoint waypoint = PlatoonWaypointsUtils.findWaypoint(waypoints, briefingMapPoint.getWaypointID());
                if (waypoint == null)
                {
                    long waypointIdForAddedWP = addWaypoint(briefingMapPoint, previousMapPointFromBriefing.getWaypointID());
                    briefingMapPoint.setWaypointID(waypointIdForAddedWP);
                }
            }
            previousMapPointFromBriefing = briefingMapPoint;
        }
        return waypoints;
    }

    private long addWaypoint(BriefingMapPoint waypointFromBriefing, long waypointIdBefore) throws PWCGException
    {
        McuWaypoint newWaypoint = null;
        McuWaypoint waypointBefore = PlatoonWaypointsUtils.findWaypoint(waypoints, waypointIdBefore);
        if(waypointBefore != null)
        {
            newWaypoint = waypointBefore.copy();

            Coordinate newPosition = waypointFromBriefing.getPosition();
            newPosition.setYPos(0.0);
            newWaypoint.setSpeed(waypointFromBriefing.getCruisingSpeed());
            newWaypoint.setPosition(newPosition);
            int indexToInsertAfter = PlatoonWaypointsUtils.getWaypointIndex(waypoints, waypointIdBefore);
            waypoints.add(indexToInsertAfter+1, newWaypoint);


        }
        else
        {
            newWaypoint = waypoints.get(0).copy();
            Coordinate newPosition = waypointFromBriefing.getPosition();
            newPosition.setYPos(0.0);
            newWaypoint.setSpeed(waypointFromBriefing.getCruisingSpeed());
            newWaypoint.setPosition(newPosition);
            waypoints.add(0, newWaypoint);
        }

        return newWaypoint.getWaypointID();
    }
}
