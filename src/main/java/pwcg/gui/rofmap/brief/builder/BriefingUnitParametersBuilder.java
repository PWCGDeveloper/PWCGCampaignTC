package pwcg.gui.rofmap.brief.builder;

import pwcg.core.exception.PWCGException;
import pwcg.gui.rofmap.brief.model.BriefingMapPoint;
import pwcg.gui.rofmap.brief.model.BriefingUnitParameters;
import pwcg.mission.mcu.McuWaypoint;
import pwcg.mission.unit.ITankUnit;

public class BriefingUnitParametersBuilder
{
	private ITankUnit playerUnit;
	private BriefingUnitParameters briefingFlightParameters;

	public BriefingUnitParametersBuilder (ITankUnit playerUnit)
	{
        this.playerUnit = playerUnit;
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
		for (McuWaypoint waypoint :  playerUnit.getWaypoints())
		{				
		     addPlayerFlightWaypoint(prevWaypoint, waypoint);
		     prevWaypoint = waypoint;
		}
	}

	private void addPlayerFlightWaypoint(McuWaypoint prevWaypoint, McuWaypoint waypoint) throws PWCGException
	{
        BriefingMapPoint briefingMapPoint = BriefingMapPointFactory.waypointToMapPoint(waypoint);
	    briefingFlightParameters.addBriefingMapMapPoints(briefingMapPoint);
	}
}
