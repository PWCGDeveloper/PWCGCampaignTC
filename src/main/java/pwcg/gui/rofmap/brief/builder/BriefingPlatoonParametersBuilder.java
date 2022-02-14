package pwcg.gui.rofmap.brief.builder;

import pwcg.core.exception.PWCGException;
import pwcg.core.location.Coordinate;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.rofmap.brief.model.BriefingPlatoonParameters;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.platoon.ITankPlatoon;

public class BriefingPlatoonParametersBuilder
{
	private ITankPlatoon playerPlatoon;
	private BriefingPlatoonParameters briefingPlatoonParameters;

	public BriefingPlatoonParametersBuilder (ITankPlatoon playerPlatoon)
	{
        this.playerPlatoon = playerPlatoon;
        briefingPlatoonParameters = new BriefingPlatoonParameters();
	}
	
	public BriefingPlatoonParameters buildBriefParametersContext() throws PWCGException
	{
		setWaypoints();		
		return briefingPlatoonParameters;
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
        briefingPlatoonParameters.addBriefingMapMapPoints(briefingMapPoint);
    }

    private void addPlayerMapWaypoint(McuWaypoint prevWaypoint, McuWaypoint waypoint) throws PWCGException
    {
        BriefingMapPoint briefingMapPoint = BriefingMapPointFactory.waypointToMapPoint(waypoint);
        briefingPlatoonParameters.addBriefingMapMapPoints(briefingMapPoint);
    }
}
