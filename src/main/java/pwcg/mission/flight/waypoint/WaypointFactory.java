package pwcg.mission.flight.waypoint;

import pwcg.mission.mcu.McuWaypoint;

public class WaypointFactory
{
	public static McuWaypoint createTakeOffWaypointType()
	{
		McuWaypoint waypoint = new McuWaypoint(WaypointType.TAKEOFF_WAYPOINT);
		waypoint.setWpAction(WaypointAction.WP_ACTION_TAKEOFF);
		waypoint.setPriority(WaypointPriority.PRIORITY_MED);
		
		return waypoint;
	}

    public static McuWaypoint createAirStartWaypointType()
    {
        McuWaypoint waypoint = new McuWaypoint(WaypointType.AIR_START_WAYPOINT);
        waypoint.setWpAction(WaypointAction.WP_ACTION_START);
        waypoint.setPriority(WaypointPriority.PRIORITY_LOW);            
        
        return waypoint;
    }

	public static McuWaypoint createIngressWaypointType()
	{
		McuWaypoint waypoint = new McuWaypoint(WaypointType.INGRESS_WAYPOINT);
		waypoint.setWpAction(WaypointAction.WP_ACTION_INGRESS);
		waypoint.setPriority(WaypointPriority.PRIORITY_LOW);			
		
		return waypoint;
	}

    public static McuWaypoint createEgressWaypointType()
    {
        McuWaypoint waypoint = new McuWaypoint(WaypointType.EGRESS_WAYPOINT);
        waypoint.setWpAction(WaypointAction.WP_ACTION_EGRESS);
        waypoint.setPriority(WaypointPriority.PRIORITY_LOW);            
        
        return waypoint;
    }

	public static McuWaypoint createPatrolWaypointType()
	{
		McuWaypoint waypoint = new McuWaypoint(WaypointType.PATROL_WAYPOINT);
		waypoint.setWpAction(WaypointAction.WP_ACTION_PATROL);
		waypoint.setPriority(WaypointPriority.PRIORITY_LOW);			
		
		return waypoint;
	}

	public static McuWaypoint createTargetApproachWaypointType()
	{
		McuWaypoint waypoint = new McuWaypoint(WaypointType.TARGET_APPROACH_WAYPOINT);
		waypoint.setWpAction(WaypointAction.WP_ACTION_TARGET_APPROACH);
		waypoint.setPriority(WaypointPriority.PRIORITY_MED);			
		return waypoint;
	}

	public static McuWaypoint createTargetFinalWaypointType()
	{
		McuWaypoint waypoint = new McuWaypoint(WaypointType.TARGET_FINAL_WAYPOINT);
		waypoint.setWpAction(WaypointAction.WP_ACTION_TARGET_FINAL);
		waypoint.setPriority(WaypointPriority.PRIORITY_MED);			
		return waypoint;
	}

	public static McuWaypoint createTargetEgressWaypointType()
	{
		McuWaypoint waypoint = new McuWaypoint(WaypointType.TARGET_EGRESS_WAYPOINT);
		waypoint.setWpAction(WaypointAction.WP_ACTION_TARGET_EGRESS);
		waypoint.setPriority(WaypointPriority.PRIORITY_MED);			
		return waypoint;
	}

	public static McuWaypoint createMoveToWaypointType()
	{
		McuWaypoint waypoint = new McuWaypoint(WaypointType.MOVE_TO_WAYPOINT);
		waypoint.setWpAction(WaypointAction.WP_ACTION_MOVE_TO);
		waypoint.setPriority(WaypointPriority.PRIORITY_MED);			
		return waypoint;
	}

    public static McuWaypoint createFreeHuntWaypointType()
    {
        McuWaypoint waypoint = new McuWaypoint(WaypointType.HUNT_WAYPOINT);
        waypoint.setWpAction(WaypointAction.WP_ACTION_ATTACK);
        waypoint.setPriority(WaypointPriority.PRIORITY_MED);                    
        return waypoint;
    }

    public static McuWaypoint createObjectiveWaypointType()
    {
        McuWaypoint waypoint = new McuWaypoint(WaypointType.OBJECTIVE_WAYPOINT);
        waypoint.setWpAction(WaypointAction.WP_ACTION_ATTACK);
        waypoint.setPriority(WaypointPriority.PRIORITY_LOW);            
        return waypoint;
    }
 
	public static McuWaypoint createDefinedWaypointType(WaypointType waypointType, WaypointAction waypointAction)
	{
		McuWaypoint waypoint = new McuWaypoint(waypointType);
		waypoint.setWpAction(waypointAction);
		waypoint.setPriority(WaypointPriority.PRIORITY_LOW);			
		
		return waypoint;
	}
}
