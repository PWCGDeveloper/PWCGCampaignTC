package pwcg.mission.flight.patrol;

import java.util.ArrayList;
import java.util.List;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.core.utils.RandomNumberGenerator;
import pwcg.mission.flight.IFlight;
import pwcg.mission.flight.waypoint.WaypointFactory;
import pwcg.mission.flight.waypoint.WaypointType;
import pwcg.mission.flight.waypoint.end.EgressWaypointGenerator;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.flight.waypoint.missionpoint.MissionPointRouteSet;
import pwcg.mission.flight.waypoint.patterns.PathAlongFront;
import pwcg.mission.flight.waypoint.patterns.PathAlongFrontData;
import pwcg.mission.flight.waypoint.patterns.PathAlongFrontDataBuilder;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.product.bos.config.TCProductSpecificConfiguration;

public class PatrolFrontWaypointFactory
{
    private IFlight flight;
    private MissionPointRouteSet missionPointSet = new MissionPointRouteSet();

    public PatrolFrontWaypointFactory(IFlight flight) throws PWCGException
    {
        this.flight = flight;
    }

    public IMissionPointSet createWaypoints(McuWaypoint ingressWaypoint) throws PWCGException
    {
        missionPointSet.addWaypoint(ingressWaypoint);
        
        List<McuWaypoint> waypoints = createTargetWaypoints(ingressWaypoint.getPosition());
        missionPointSet.addWaypoints(waypoints);

        McuWaypoint egressWaypoint = EgressWaypointGenerator.createEgressWaypoint(flight, ingressWaypoint.getPosition());
        missionPointSet.addWaypoint(egressWaypoint);

        return missionPointSet;
    }
    
    private List<McuWaypoint> createTargetWaypoints(Coordinate startPosition) throws PWCGException  
    {        
        PathAlongFrontDataBuilder pathAlongFrontDataBuilder = new PathAlongFrontDataBuilder(flight);
        PathAlongFrontData pathAlongFrontData = pathAlongFrontDataBuilder.buildPathAlongFrontData(startPosition, calculateDepthOfPenetration(), calculatePatrolDistance());
        
        PathAlongFront pathAlongFront = new PathAlongFront();
        List<Coordinate> patrolCoordinates = pathAlongFront.createPathAlongFront(pathAlongFrontData);
 
        List<McuWaypoint> targetWaypoints = new ArrayList<>();
        for (Coordinate patrolCoordinate : patrolCoordinates)
        {
            McuWaypoint waypoint = createWP(patrolCoordinate.copy());
            waypoint.setTargetWaypoint(true);
            waypoint.setName(WaypointType.PATROL_WAYPOINT.getName());
            targetWaypoints.add(waypoint);
        }
        return targetWaypoints;
    }

    private int calculatePatrolDistance() throws PWCGException
    {
        int patrolDistance = 100000;
        return patrolDistance;
    }

    private int calculateDepthOfPenetration()
    {
        TCProductSpecificConfiguration productSpecific =new TCProductSpecificConfiguration();
        int depthOfPenetrationMax = productSpecific.getMaxDepthOfPenetrationPatrol();
        int depthOfPenetration = RandomNumberGenerator.getRandom(depthOfPenetrationMax);
        depthOfPenetration -= 1000;
        return depthOfPenetration;
    }

	private McuWaypoint createWP(Coordinate patrolCoord) throws PWCGException 
	{
        patrolCoord.setYPos(flight.getFlightInformation().getAltitude());

		McuWaypoint wp = WaypointFactory.createPatrolWaypointType();
		wp.setTriggerArea(McuWaypoint.COMBAT_AREA);
		wp.setSpeed(flight.getFlightCruisingSpeed());			
		wp.setPosition(patrolCoord);
		return wp;
	}
}
