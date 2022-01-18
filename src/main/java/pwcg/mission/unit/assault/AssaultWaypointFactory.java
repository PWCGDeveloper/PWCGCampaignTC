package pwcg.mission.unit.assault;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.unit.ITankPlatoon;

public class AssaultWaypointFactory
{
    private ITankPlatoon unit;

    public AssaultWaypointFactory(ITankPlatoon unit) throws PWCGException
    {
        this.unit = unit;
    }

    public List<McuWaypoint> createWaypoints() throws PWCGException
    {        
        List<McuWaypoint> waypoints = createTargetWaypoints();
        return waypoints;
    }
    
    private List<McuWaypoint> createTargetWaypoints() throws PWCGException  
    {        
        Coordinate objectivePosition = unit.getUnitInformation().getObjective().getEndPosition();
        McuWaypoint objectiveWaypoint = createWP(objectivePosition);
        
        List<McuWaypoint> targetWaypoints = new ArrayList<>();
        targetWaypoints.add(objectiveWaypoint);
        return targetWaypoints;
    }

	private McuWaypoint createWP(Coordinate objectivePosition) throws PWCGException 
	{
        objectivePosition.setYPos(0.0);

		McuWaypoint wp = WaypointFactory.createTargetFinalWaypointType();
		wp.setTriggerArea(McuWaypoint.COMBAT_AREA);
		wp.setSpeed(unit.getLeadVehicle().getCruisingSpeed());			
		wp.setPosition(objectivePosition);
		return wp;
	}
}
