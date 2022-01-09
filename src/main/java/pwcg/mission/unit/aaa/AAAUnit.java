package pwcg.mission.unit.aaa;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.unit.PlayerUnit;
import pwcg.mission.unit.UnitInformation;

public class AAAUnit extends PlayerUnit
{
    public AAAUnit(UnitInformation unitInformation)
    {
        super(unitInformation);
    }
    
    @Override
    protected void createWaypoints() throws PWCGException
    {
        AAAWaypointFactory missionWaypointFactory = new AAAWaypointFactory(this);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints();
        this.getWaypointPackage().addMissionPointSet(missionWaypoints);
    }
}
