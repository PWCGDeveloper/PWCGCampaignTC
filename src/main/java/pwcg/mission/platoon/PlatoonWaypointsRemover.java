package pwcg.mission.platoon;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.mcu.McuWaypoint;

public class PlatoonWaypointsRemover
{
    private List<McuWaypoint> waypoints;

    public PlatoonWaypointsRemover(List<McuWaypoint> waypoints) throws PWCGException
    {
        this.waypoints = waypoints;
    }

    public List<McuWaypoint> removeUnwantedWaypoints(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        for (McuWaypoint unwantedWaypoint : getUnwantedWaypoints(briefingMapPoints))
        {
            int indexToRemove = PlatoonWaypointsUtils.getWaypointIndex(waypoints, unwantedWaypoint.getWaypointID());
            waypoints.remove(indexToRemove);
        }
        return waypoints;
    }

    private List<McuWaypoint> getUnwantedWaypoints(List<BriefingMapPoint> waypointsToKeep) throws PWCGException
    {
        List<McuWaypoint> unwantedWaypoints = new ArrayList<>();
        for (McuWaypoint waypoint : waypoints)
        {
            boolean keepWaypoint = false;
            for (BriefingMapPoint waypointToKeep : waypointsToKeep)
            {
                if (waypoint.getWaypointID() == waypointToKeep.getWaypointID())
                {
                    keepWaypoint = true;
                }
            }

            if (!keepWaypoint)
            {
                unwantedWaypoints.add(waypoint);
            }
        }
        return unwantedWaypoints;
    }
}
