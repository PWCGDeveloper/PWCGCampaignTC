package pwcg.gui.rofmap.brief.builder;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.rofmap.brief.model.BriefingUnitParameters;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingUnitParametersBuilder
{
	private ITankPlatoon playerPlatoon;
	private BriefingUnitParameters briefingFlightParameters;

	public BriefingUnitParametersBuilder (ITankPlatoon playerPlatoon)
	{
        this.playerPlatoon = playerPlatoon;
        briefingFlightParameters = new BriefingUnitParameters();
	}
	
	public BriefingUnitParameters buildBriefParametersContext() throws PWCGException
	{
		setWaypoints();		
		return briefingFlightParameters;
	}
	
	private void setWaypoints() throws PWCGException
	{
		McuWaypoint prevWaypoint = null;
		addPlayerMapStart(playerPlatoon.getLeadVehicle().getPosition().copy());
		for (McuWaypoint waypoint :  playerPlatoon.getWaypoints())
		{				
		     addPlayerMapWaypoint(prevWaypoint, waypoint);
		     prevWaypoint = waypoint;
		}
	}

    private void addPlayerMapStart(Coordinate start) throws PWCGException
    {
        BriefingMapPoint briefingMapPoint = BriefingMapPointFactory.startToMapPoint(start);
        briefingFlightParameters.addBriefingMapMapPoints(briefingMapPoint);
    }

    private void addPlayerMapWaypoint(McuWaypoint prevWaypoint, McuWaypoint waypoint) throws PWCGException
    {
        BriefingMapPoint briefingMapPoint = BriefingMapPointFactory.waypointToMapPoint(waypoint);
        briefingFlightParameters.addBriefingMapMapPoints(briefingMapPoint);
    }
}
