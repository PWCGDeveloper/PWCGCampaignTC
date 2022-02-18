package pwcg.mission.platoon;

import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.mcu.McuWaypoint;

public class PlatoonStartPositionModifier
{
    private List<McuWaypoint> waypoints;

    public PlatoonStartPositionModifier(List<McuWaypoint> waypoints) throws PWCGException
    {
        this.waypoints = waypoints;
    }

    public List<McuWaypoint> modifyWaypointsFromBriefing(List<BriefingMapPoint> briefingMapPoints) throws PWCGException
    {
        for (BriefingMapPoint briefingMapPoint : briefingMapPoints)
        {
            McuWaypoint waypoint = PlatoonWaypointsUtils.findWaypoint(waypoints, briefingMapPoint.getWaypointID());
            if(waypoint != null)
            {
                updateWaypointFromBriefing(briefingMapPoint, waypoint);
            }
        }
        return waypoints;
    }

    private void updateWaypointFromBriefing(BriefingMapPoint briefingMapPoint, McuWaypoint waypoint) throws PWCGException
    {
        Coordinate waypointPosition = briefingMapPoint.getPosition();
        waypointPosition.setYPos(0.0);
        waypoint.setSpeed(briefingMapPoint.getCruisingSpeed());
        waypoint.setPosition(waypointPosition);
    }
}
