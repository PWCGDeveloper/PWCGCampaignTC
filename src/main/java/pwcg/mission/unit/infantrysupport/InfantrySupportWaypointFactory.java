package pwcg.mission.unit.infantrysupport;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.unit.IPlayerUnit;

public class InfantrySupportWaypointFactory
{
    private IPlayerUnit unit;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public InfantrySupportWaypointFactory(IPlayerUnit unit) throws PWCGException
    {
        this.unit = unit;
    }

    public IMissionPointSet createWaypoints() throws PWCGException
    {        
        List<McuWaypoint> waypoints = createTargetWaypoints();
        missionPointSet.addWaypoints(waypoints);

        return missionPointSet;
    }
    
    private List<McuWaypoint> createTargetWaypoints() throws PWCGException  
    {        
        Coordinate objectivePosition = getObjectivePosition();
        McuWaypoint objectiveWaypoint = createWP(objectivePosition);
        
        List<McuWaypoint> targetWaypoints = new ArrayList<>();
        targetWaypoints.add(objectiveWaypoint);
        return targetWaypoints;
    }

	private Coordinate getObjectivePosition()
    {
	    if(unit.getUnitInformation().getObjective().isDefending())
	    {
	        unit.getUnitInformation().getObjective().getStartPosition();
	    }
	    else
	    {
	        unit.getUnitInformation().getObjective().getEndPosition();
	    }
        return null;
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
