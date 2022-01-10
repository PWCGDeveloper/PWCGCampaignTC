package pwcg.mission.unit.assault;

import pwcg.core.exception.PWCGException;
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
        AssaultWaypointFactory waypointFactory = new AssaultWaypointFactory(this);
        waypoints = waypointFactory.createWaypoints();
    }
}
