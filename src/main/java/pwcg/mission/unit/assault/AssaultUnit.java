package pwcg.mission.unit.assault;

import pwcg.core.exception.PWCGException;
import pwcg.mission.flight.waypoint.missionpoint.IMissionPointSet;
import pwcg.mission.unit.PlayerUnit;
import pwcg.mission.unit.UnitInformation;

public class AssaultUnit extends PlayerUnit
{
    public AssaultUnit(UnitInformation unitInformation)
    {
        super(unitInformation);
    }
    
    @Override
    protected void createWaypoints() throws PWCGException
    {
        AssaultWaypointFactory missionWaypointFactory = new AssaultWaypointFactory(this);
        IMissionPointSet missionWaypoints = missionWaypointFactory.createWaypoints();
        this.getWaypointPackage().addMissionPointSet(missionWaypoints);
    }
}
