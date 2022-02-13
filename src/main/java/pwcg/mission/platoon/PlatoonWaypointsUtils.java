package pwcg.mission.platoon;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.mission.mcu.McuWaypoint;

public class PlatoonWaypointsUtils
{

    public static int getWaypointIndex(List<McuWaypoint> waypoints, long waypointId) throws PWCGException
    {
        int index = 0;
        for (McuWaypoint waypoint : waypoints)
        {
            if (waypoint.getWaypointID() == waypointId)
            {
                return index;
            }
            ++index;
        }
        throw new PWCGException("Waypoint not found in waypoint set " + waypointId);
    }

    public static McuWaypoint findWaypoint(List<McuWaypoint> waypoints, long waypointIndex) throws PWCGException
    {
        for (McuWaypoint waypoint : waypoints)
        {
            if (waypoint.getWaypointID() == waypointIndex)
            {
                return waypoint;
            }
        }
        return null;
    }
}
