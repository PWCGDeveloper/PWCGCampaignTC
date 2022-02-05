package pwcg.gui.rofmap.brief.builder;

import pwcg.core.location.Coordinate;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.mission.mcu.McuWaypoint;

public class BriefingMapPointFactory
{
    public static BriefingMapPoint startToMapPoint(Coordinate start)
    {
        BriefingMapPoint briefingMapPoint = new BriefingMapPoint(McuWaypoint.NO_WAYPOINT_ID);

        briefingMapPoint.setDesc("Start");
        briefingMapPoint.setPosition(start.copy());
        briefingMapPoint.setCruisingSpeed(0);
        briefingMapPoint.setDistanceToNextPoint(0);
        briefingMapPoint.setIsEditable(true);
        briefingMapPoint.setIsTarget(false);
        briefingMapPoint.setIsWaypoint(false);

        return briefingMapPoint;
    }

    public static BriefingMapPoint waypointToMapPoint(McuWaypoint waypoint)
    {
        BriefingMapPoint briefingMapPoint = new BriefingMapPoint(waypoint.getWaypointID());

        briefingMapPoint.setDesc(waypoint.getWpAction().getAction());
        briefingMapPoint.setPosition(waypoint.getPosition());
        briefingMapPoint.setCruisingSpeed(waypoint.getSpeed());
        briefingMapPoint.setDistanceToNextPoint(0);
        briefingMapPoint.setIsEditable(waypoint.getWpAction().isEditable());
        briefingMapPoint.setIsTarget(waypoint.isTargetWaypoint());
        briefingMapPoint.setIsWaypoint(true);

        return briefingMapPoint;
    }
}
